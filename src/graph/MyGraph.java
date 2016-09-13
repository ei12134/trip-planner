package graph;

import java.util.ArrayList;
import java.util.LinkedList;

public class MyGraph {
	protected ArrayList<MyNode> nodes;

	public MyGraph() {
		this.nodes = new ArrayList<MyNode>();
	}

	public ArrayList<MyNode> getNodes() {
		return nodes;
	}

	public MyNode findNode(int nodeId) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getId() == nodeId) {
				return nodes.get(i);
			}
		}
		return null;
	}

	public boolean addNode(MyNode node) {
		if (nodes.contains(node))
			return false;
		nodes.add(node);
		return true;
	}

	/*
	 * public boolean addNodeBegin(MyNode node) { if (nodes.contains(node))
	 * return false; nodes.add(0, node); return true; }
	 */

	public boolean removeNode(MyNode node) {// need to send the same node memory
											// position as the one sent to
											// because in removeEdgeTo is going
											// to decrement the indegree value
											// (in this case is stupid because
											// we are going to remove the
											// destiny node from the graph)
		if (nodes.remove(node)) {
			for (int i = 0; i < nodes.size(); i++) {
				nodes.get(i).removeEdgeTo(node);
			}
			return true;
		}
		return false;
	}

	public boolean addEdge(MyNode source, MyNode destiny, double distance, double price, int minutes) {
		if (nodes.contains(source) && nodes.contains(destiny)) {
			source.addEdge(destiny, distance, price, minutes);
			return true;
		}
		return false;
	}

	public boolean removeEdge(MyNode source, MyNode destiny) {// Important to
																// send
																// the destiny
																// memory
																// position
																// of the graph
																// to
																// correctly
																// decrement the
																// indegree
																// value
																// from the node
		if (nodes.contains(source) && nodes.contains(destiny)) {
			return source.removeEdgeTo(destiny);
		}
		return false;
	}

	public int getNumNodes() {
		return nodes.size();
	}

	public int getNumEdges() {
		int count = 0;

		for (int i = 0; i < nodes.size(); i++) {
			count += nodes.get(i).getOutEdges().size();
		}

		return count;
	}

	public double getTotalDistance(boolean directed) {
		int distance = 0;

		for (int i = 0; i < nodes.size(); i++) {
			ArrayList<Edge> edges = nodes.get(i).getOutEdges();

			for (int z = 0; z < edges.size(); z++) {
				distance += edges.get(z).getDistance();
			}
		}

		if (directed)
			return distance / 2.0;
		else
			return distance;
	}

	public double getTotalTime(boolean directed) {
		int time = 0;

		for (int i = 0; i < nodes.size(); i++) {
			ArrayList<Edge> edges = nodes.get(i).getOutEdges();
			for (int z = 0; z < edges.size(); z++) {
				time += edges.get(z).getMinutes();
			}
		}

		if (directed)
			return time / 2.0;
		else
			return time;
	}

	public void setPointsOfInterest(LinkedList<MyNode> poiList) {
		for (MyNode node : nodes) {
			node.setPOI(false);
		}

		for (MyNode node : poiList) {
			node.setPOI(true);
		}
	}

	public int compareTotalDistance(MyGraph obj) {
		double d1 = this.getTotalDistance(false);
		double d2 = obj.getTotalDistance(false);
		if (d1 > d2) {
			return 1;
		} else {
			if (d1 < d2) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
