package graph;

public class Edge {

	private MyNode source;
	private MyNode destiny;
	private double distance;
	private double price;
	private int minutes;

	public Edge(MyNode source, MyNode destiny, double distance, double price,
			int minutes) {
		this.setSource(source);
		this.setDestiny(destiny);
		this.distance = distance;
		this.price = price;
		this.minutes = minutes;
	}

	public Edge(Edge edge) {// we are not really cloning the source and destiny
							// Nodes because it can be helpful to have the sabe
							// memory instace in the variable
		if (edge != null) {
			this.distance = edge.distance;
			this.source = edge.source;
			this.destiny = edge.destiny;
			this.price = edge.price;
			this.minutes = edge.minutes;
		}
	}

	public boolean lessThan(Edge edge) {
		if (edge != null)
			return this.distance < edge.distance;
		return false;
	}

	public boolean greaterThan(Edge edge) {
		if (edge != null)
			return this.distance > edge.distance;
		return false;
	}

	public MyNode getSource() {
		return source;
	}

	public void setSource(MyNode source) {
		this.source = source;
	}

	public MyNode getDestiny() {
		return destiny;
	}

	public void setDestiny(MyNode destiny) {
		this.destiny = destiny;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (this.getClass() != obj.getClass())
			return false;

		Edge edge = (Edge) obj;

		return this.distance == edge.distance
				&& this.source.equals(edge.source)
				&& this.destiny.equals(edge.destiny);
	}
}
