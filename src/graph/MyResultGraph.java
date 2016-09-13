package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import agents.Car;

public class MyResultGraph extends MyGraph {
	// only use in the result to get informations
	private LinkedList<MyNode> hotels;
	private LinkedList<MyNode> gas;
	protected LinkedList<MyNode> itinerary;
	private Car car;

	public MyResultGraph() {
		super();
		this.hotels = new LinkedList<MyNode>();
		this.gas = new LinkedList<MyNode>();
		this.itinerary = new LinkedList<MyNode>();
	}

	public LinkedList<MyNode> getItinerary() {
		return itinerary;
	}

	public void setItinerary(LinkedList<MyNode> itinerary) {
		this.itinerary = itinerary;
	}

	public void mergeGraph(MyResultGraph g) {
		ArrayList<MyNode> gNodes = g.getNodes();
		ArrayList<Edge> edges = new ArrayList<Edge>();

		for (int i = 0; i < gNodes.size(); i++) {
			if (!this.addNode(gNodes.get(i))) {
				edges = gNodes.get(i).getOutEdges();
				MyNode n = findNode(gNodes.get(i).getId());

				for (Edge e : edges) {
					this.addEdge(n, e.getDestiny(), e.getDistance(),
							e.getPrice(), e.getMinutes());
				}
			}
		}
		ArrayList<MyNode> arr = g.getNodes();
		for (int i = arr.size() - 1; i >= 0; i--) {
			this.itinerary.add(arr.get(i));
		}

		for (int i = g.getHotels().size() - 1; i >= 0; i--) {
			this.hotels.add(g.getHotels().get(i));
		}

		for (int i = g.getGas().size() - 1; i >= 0; i--) {
			this.gas.add(g.getGas().get(i));
		}

		this.car = g.car;
	}

	public LinkedList<MyNode> getHotels() {
		return hotels;
	}

	public void setHotels(LinkedList<MyNode> hotels) {
		this.hotels = hotels;
	}

	public LinkedList<MyNode> getGas() {
		return gas;
	}

	public void setGas(LinkedList<MyNode> gas) {
		this.gas = gas;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = new Car(car);
	}

	public void addHotel(MyNode node) {
		this.hotels.add(node);
	}

	public void addGasStop(MyNode node) {
		this.gas.add(node);
	}

	public double getHotelsPrice() {
		double total = 0.0;
		for (MyNode myNode : this.hotels)
			total += myNode.getHotelPrice();
		return total;
	}

	public double getTripSpendMoneyOnGas() {
		double distance = this.getTotalDistance(false);
		double pricePer100km = this.car.getFuelEconomy()
				* this.car.getFuelPrice();
		return (distance * pricePer100km) / 100.0;
	}

	@Override
	// only to use in junit!!!!
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (this.getClass() != obj.getClass())
			return false;

		MyResultGraph it = (MyResultGraph) obj;

		if (this.nodes.size() != it.nodes.size())
			return false;

		for (MyNode myNode : this.nodes)
			if (!it.nodes.contains(myNode))
				return false;

		if (this.gas.size() != it.gas.size())
			return false;

		for (MyNode myNode : gas)
			if (!it.gas.contains(myNode))
				return false;

		if (this.hotels.size() != it.hotels.size())
			return false;

		for (MyNode myNode : hotels)
			if (!it.hotels.contains(myNode))
				return false;

		if (this.itinerary.size() != it.itinerary.size())
			return false;

		for (MyNode myNode : itinerary)
			if (!it.itinerary.contains(myNode))
				return false;

		return true;
	}
}
