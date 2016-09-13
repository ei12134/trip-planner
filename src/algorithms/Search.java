package algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import agents.Car;
import graph.Edge;
import graph.MyNode;
import graph.MyResultGraph;
import utils.Constants;

public class Search {
	public static enum SEARCH_MODE {
		UNIFORM_COST("Uniform-cost search"), A_STAR_BASIC(
				"A* search with basic heuristic function"), A_STAR_ADVANCED(
				"A* search with advanced heuristic function");

		private final String outputString;

		private SEARCH_MODE(String value) {
			outputString = value;
		}

		@Override
		public String toString() {
			return outputString;
		}
	}

	protected Car car;
	protected MyResultGraph result;

	protected SEARCH_MODE searchMode;
	protected double priceRatio;
	protected double distRatio;
	private int generatedNodesCounter;

	public Search(Car car, SEARCH_MODE searchMode, double distRatio,
			double priceRatio) {
		this.generatedNodesCounter = 0;
		this.setCar(car);
		this.setResult(new MyResultGraph());
		this.searchMode = searchMode;
		this.distRatio = distRatio;
		this.priceRatio = priceRatio;
	}

	public double getPriceRatio() {
		return priceRatio;
	}

	public void setPriceRatio(double priceRatio) {
		this.priceRatio = priceRatio;
	}

	public double getDistRatio() {
		return distRatio;
	}

	public void setDistRatio(double distRatio) {
		this.distRatio = distRatio;
	}

	public static double straightLineDistance(double lat1, double lon1,
			double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.pow((Math.sin(dLat / 2.0)), 2.0)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2))
				* Math.pow((Math.sin(dLon / 2.0)), 2.0);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return Constants.EARTH_RADIUS * c;
	}

	public boolean buildResult(StateNode finalNode) {
		this.result = new MyResultGraph();
		this.car = finalNode.getCar();
		StateNode lastNode = null;
		StateNode nodEval = finalNode;

		do {
			MyNode destNode = nodEval.getNode();

			MyNode sourcNode = new MyNode(destNode);
			if (lastNode != null) {
				Edge edge = lastNode.getEdge();
				if (edge != null) {
					sourcNode.addEdge(lastNode.getNode(), edge.getDistance(),
							edge.getPrice(), edge.getMinutes());
				}
			}
			this.result.addNode(sourcNode);

			if (nodEval.isSleepNeeded()) {
				// System.out.println("sleep needed: "
				// + nodEval.getNode().getDescription());
				this.result.addHotel(nodEval.getNode());
			}
			if (nodEval.isGasNeeded()) {
				this.result.addGasStop(nodEval.getNode());
			}

			lastNode = nodEval;
			nodEval = nodEval.getParent();
		} while (nodEval != null);

		this.result.setCar(car);
		return true;
	}

	public boolean run() {
		Comparator<StateNode> compareNodes = new Comparator<StateNode>() {
			public int compare(StateNode node1, StateNode node2) {
				return node1.compareEvaluation(node2);
			}
		};

		PriorityQueue<StateNode> open = new PriorityQueue<StateNode>(20,
				compareNodes);
		HashMap<Integer, StateNode> closed = new HashMap<Integer, StateNode>();

		StateNode startNode = new StateNode(this.car.getPosition(), null, null,
				new Car(car), this.searchMode, this.distRatio, this.priceRatio);
		open.add(startNode);

		StateNode currentNode;
		StateNode finalNode = new StateNode(this.car.getDestiny(), null, null,
				new Car(car), this.searchMode, this.distRatio, this.priceRatio);

		while (!open.isEmpty()) {
			currentNode = open.poll();
			closed.put(currentNode.getNode().getId(), currentNode);

			if (currentNode.getNode().getId() == finalNode.getNode().getId()) {
				return buildResult(currentNode);
			}
			for (Edge edge : currentNode.getNode().getOutEdges()) {
				StateNode neighbour = new StateNode(edge.getDestiny(),
						new StateNode(currentNode), edge, new Car(
								currentNode.getCar()), this.searchMode,
						this.distRatio, this.priceRatio);

				generatedNodesCounter++;

				if (closed.containsKey(neighbour.getNode().getId())) {
					continue;
				}

				// Replace node if already exists one with higher cost
				if (neighbour.isValid() && !open.contains(neighbour)) {
					open.add(neighbour);
				}
			}
		}
		return false;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public MyResultGraph getResult() {
		return result;
	}

	protected void setResult(MyResultGraph result) {
		this.result = result;
	}

	public int getGeneratedNodesCounter() {
		return generatedNodesCounter;
	}

}
