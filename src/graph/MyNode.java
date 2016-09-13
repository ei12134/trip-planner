package graph;

import java.util.ArrayList;

public class MyNode {

	private int id;
	private double latitude;
	private double longitude;
	private double hotelPrice;
	private boolean pointOfInterest;
	private boolean gasStation;
	private String description;
	private ArrayList<Edge> outEdges;
	private boolean processing;
	private boolean visited;
	private int indegree; // incident edges

	public MyNode(int id, double latitude, double longitude, double hotelPrice,
			boolean gasStation, String description) {
		this.setId(id);
		this.latitude = latitude;
		this.longitude = longitude;
		this.hotelPrice = hotelPrice;
		this.pointOfInterest = false;
		this.gasStation = gasStation;
		this.description = description;
		this.processing = false;
		this.indegree = 0;
		this.outEdges = new ArrayList<Edge>();
	}

	// clones node without edges
	public MyNode(MyNode node) {
		if (node != null) {
			this.setId(node.id);
			this.latitude = node.latitude;
			this.longitude = node.longitude;
			this.hotelPrice = node.hotelPrice;
			this.pointOfInterest = node.pointOfInterest;
			this.gasStation = node.gasStation;
			this.description = node.description;
			this.processing = node.processing;
			this.indegree = 0;
			this.outEdges = new ArrayList<Edge>();
		}
	}

	public ArrayList<Edge> cloneList(ArrayList<Edge> list) {
		ArrayList<Edge> clone = new ArrayList<Edge>(list.size());
		for (Edge item : list)
			clone.add(new Edge(item));
		return clone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public int getIndegree() {
		return indegree;
	}

	public double getHotelPrice() {
		return hotelPrice;
	}

	public boolean hasGasStation() {
		return gasStation;
	}

	public void setHotelPrice(double hotelPrice) {
		this.hotelPrice = hotelPrice;
	}

	public boolean getPointOfInterest() {
		return pointOfInterest;
	}

	public void setGasStation(boolean gasStation) {
		this.gasStation = gasStation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<Edge> getOutEdges() {
		return outEdges;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean getVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public void setPOI(boolean value) {
		this.pointOfInterest = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (this.getClass() != obj.getClass())
			return false;

		MyNode node = (MyNode) obj;

		return this.id == node.id;
	}

	// we need to decrement the destiny indegree value outside (if we don't send
	// the same node memory position)
	public boolean removeEdgeTo(MyNode node) {
		for (int i = 0; i < outEdges.size(); i++) {
			if (outEdges.get(i).getDestiny().equals(node)) {
				node.indegree--;
				outEdges.remove(i);
				return true;
			}
		}
		return false;
	}

	public void addEdge(MyNode destiny, double distance, double price,
			int minutes) {
		destiny.indegree++;
		Edge edge = new Edge(this, destiny, distance, price, minutes);
		this.outEdges.add(edge);
	}

	public String toString() {
		return this.description;
	}
}
