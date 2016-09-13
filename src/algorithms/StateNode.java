package algorithms;

import agents.Car;
import algorithms.Search.SEARCH_MODE;
import graph.Edge;
import graph.MyNode;

public class StateNode {
	private StateNode parent;
	private Edge edge;
	private MyNode node;
	private Car car;
	private boolean gasNeeded;
	private boolean sleepNeeded;
	private double g, h;

	public StateNode(MyNode node, StateNode parent, Edge edge, Car car,
			SEARCH_MODE useHeuristics, double distRatio, double priceRatio) {
		this.parent = parent;
		this.node = node;
		this.edge = edge;
		this.gasNeeded = false;
		this.sleepNeeded = false;
		this.car = car;
		double fuelLiters = 0.0;

		if (this.parent != null) {
			if (toppingUpFuelTank()) {
				// calc before reset... to use in the cost
				fuelLiters = this.parent.car.getTankFuel()
						- ((this.parent.car.getDrivingRange() * this.parent.car
								.getTankFuel()) / this.car.getMaxDrivingRange());
				this.parent.gasNeeded = true;
				this.parent.car.resetDrivingRange();
			}

			if (sleepOver()) {
				if (this.parent.getNode().getHotelPrice() > 0.0) {
					this.parent.sleepNeeded = true;
					this.parent.car.resetDrivenMinutesOnDay();
				}
			}

			if (this.edge != null) {
				this.car = new Car(this.parent.getCar());
				this.car.subtractDrivingRange(this.edge.getDistance());
				this.car.addTotalDrivenMinutes(this.edge.getMinutes());
			}
		}

		double dist = 0.0;
		double price = 0.0;
		if (this.edge != null) {
			dist = edge.getDistance();
			price = edge.getPrice();
		}

		if (this.parent == null) {
			this.g = 0.0;
		} else {
			this.g = this.parent.getPathCost()
					+ ((dist * distRatio) + (price * priceRatio));
			if (this.parent.sleepNeeded) {
				this.g += (this.parent.getNode().getHotelPrice() * priceRatio);
			}

			if (this.parent.gasNeeded) {
				// System.out.println("litters needed: " + fuelLiters + " XD ");
				this.g += ((fuelLiters * this.parent.car.getFuelPrice()) * priceRatio);
			}
		}

		MyNode goalNode = car.getDestiny();

		double distanceToGoal;
		double drivingRangeNeeded;
		double currentTankFuelLiters;
		double neededLitersToGoal;

		switch (useHeuristics) {
		case A_STAR_BASIC:
			distanceToGoal = Search.straightLineDistance(node.getLatitude(),
					node.getLongitude(), goalNode.getLatitude(),
					goalNode.getLongitude());
			drivingRangeNeeded = distanceToGoal - this.car.getDrivingRange();
			currentTankFuelLiters = (this.car.getDrivingRange() * this.car
					.getTankFuel()) / this.car.getMaxDrivingRange();
			neededLitersToGoal = (currentTankFuelLiters * drivingRangeNeeded)
					/ car.getDrivingRange();
			if (drivingRangeNeeded < 0) {
				neededLitersToGoal = 0;
			}
			this.h = distanceToGoal * distRatio + neededLitersToGoal
					* priceRatio;
			break;
		case A_STAR_ADVANCED:
			distanceToGoal = Search.straightLineDistance(node.getLatitude(),
					node.getLongitude(), goalNode.getLatitude(),
					goalNode.getLongitude());
			drivingRangeNeeded = distanceToGoal - this.car.getDrivingRange();
			currentTankFuelLiters = (this.car.getDrivingRange() * this.car
					.getTankFuel()) / this.car.getMaxDrivingRange();
			neededLitersToGoal = (currentTankFuelLiters * drivingRangeNeeded)
					/ car.getDrivingRange();
			if (drivingRangeNeeded < 0) {
				neededLitersToGoal = 0;
			}
			this.h = 1.25 * distanceToGoal * distRatio + neededLitersToGoal
					* priceRatio;
			break;
		default:

			break;
		}
	}

	public StateNode(StateNode stateNode) {
		this.parent = stateNode.parent;
		this.edge = stateNode.edge;
		this.node = stateNode.node;
		this.car = new Car(stateNode.getCar());
		this.gasNeeded = stateNode.gasNeeded;
		this.sleepNeeded = stateNode.sleepNeeded;
		this.g = stateNode.g;
		this.h = stateNode.h;
	}

	public boolean toppingUpFuelTank() {
		if (this.edge == null)
			return false;
		double newDrivingRange = this.car.getDrivingRange()
				- this.edge.getDistance();
		return (newDrivingRange <= 0);
	}

	public boolean sleepOver() {
		if (this.edge == null)
			return false;
		double newDrivingTime = this.car.getDrivenMinutes()
				+ this.edge.getMinutes();
		return (newDrivingTime >= this.car.getDailyMaxDrivingTime());
	}

	public StateNode getParent() {
		return parent;
	}

	public void setParent(StateNode parent) {
		this.parent = parent;
	}

	public MyNode getNode() {
		return this.node;
	}

	public Edge getEdge() {
		return edge;
	}

	public double getEvaluation() {
		return (this.g + this.h);
	}

	public double getPathCost() {
		return this.g;
	}

	public double getCostToGoal() {
		return this.h;
	}

	public int compareEvaluation(StateNode obj) {
		double eval1 = this.getEvaluation();
		double eval2 = obj.getEvaluation();
		if (eval1 > eval2) {
			return 1;
		} else {
			if (eval1 < eval2) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	public int compareCostToReach(StateNode obj) {
		double eval1 = this.getPathCost();
		double eval2 = obj.getPathCost();

		if (eval1 > eval2) {
			return 1;
		} else if (eval1 < eval2) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj) {
			return true;
		}

		if (this.getClass() != obj.getClass())
			return false;

		StateNode pqEvalNode = (StateNode) obj;

		boolean match = false;

		// dirty trick
		// public boolean contains(Object o)
		// Returns true if this queue contains the specified element.
		// More formally, returns true if and only if this queue contains
		// at least one element e such that o.equals(e).
		if (this.node.getId() == pqEvalNode.getNode().getId()) {
			if (pqEvalNode.getPathCost() > this.getPathCost()) {
				pqEvalNode.parent = this.parent;
				pqEvalNode.edge = this.edge;
				pqEvalNode.g = this.g;
				pqEvalNode.h = this.h;
				pqEvalNode.node = this.node;
				pqEvalNode.car = this.car;
			}
			match = true;
		}

		return match;
	}

	@Override
	public int hashCode() {
		int hash = this.node.getId() * 31;
		return hash;
	}

	@Override
	public String toString() {
		return "id: "
				+ this.node.getId()
				+ "\nparent = "
				+ (this.parent == null ? "null" : (this.parent.getNode()
						.getId() + "")) + "\ng = " + this.g + "\nh = " + this.h;
	}

	public boolean isValid() {
		return car.isDistanceDrivable(edge.getDistance())
				&& car.isTimeDrivable(edge.getMinutes());
	}

	public Car getCar() {
		return car;
	}

	public boolean isGasNeeded() {
		return gasNeeded;
	}

	public boolean isSleepNeeded() {
		return sleepNeeded;
	}
}
