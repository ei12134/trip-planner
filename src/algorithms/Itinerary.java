package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

import agents.Car;
import graph.MyGraph;
import graph.MyNode;
import graph.MyResultGraph;
import utils.MyThread;

public class Itinerary extends Search {

	private MyGraph map;
	private LinkedList<MyNode> unvisitedPOI;
	private final MyNode destiny;
	private int generatedNodesCounter;

	protected LinkedList<MyNode> gasStations;
	protected LinkedList<MyNode> hotels;

	public Itinerary(MyGraph map, Car car, LinkedList<MyNode> poiList, SEARCH_MODE heuristics, double distRatio, double priceRatio) {
		super(car, heuristics, distRatio, priceRatio);
		setMap(map);
		this.generatedNodesCounter = 0;
		this.result = new MyResultGraph();
		this.unvisitedPOI = new LinkedList<MyNode>(poiList);
		unvisitedPOI.removeFirst();
		destiny = unvisitedPOI.peekLast();
		unvisitedPOI.removeLast();
	}

	@Override
	public boolean run() {
		Comparator<MyResultGraph> compareGraphs = new Comparator<MyResultGraph>() {
			public int compare(MyResultGraph g1, MyResultGraph g2) {
				return g1.compareTotalDistance(g2);
			}
		};

		PriorityQueue<MyResultGraph> open = new PriorityQueue<MyResultGraph>(20, compareGraphs);
		HashMap<Integer, MyResultGraph> closed = new HashMap<Integer, MyResultGraph>();
		car.resetDrivenMinutesOnDay();
		car.resetDrivingRange();

		ArrayList<MyThread> threads = new ArrayList<MyThread>();

		for (int i = 0; i < this.unvisitedPOI.size(); i++) {
			car.setDestiny(unvisitedPOI.get(i));

			MyThread th = new MyThread(new Search(new Car(car), searchMode, distRatio, priceRatio));
			th.start();
			threads.add(th);
		}
		for (MyThread thread : threads) {
			Search search;
			try {
				search = thread.waitStop();
				this.generatedNodesCounter += thread.search.getGeneratedNodesCounter();
				open.add(search.getResult());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		threads.clear();

		while (!open.isEmpty() && unvisitedPOI.size() > 0) {
			MyResultGraph subItinerary = open.poll();
			if (subItinerary.getCar() == null) {
				return false;
			}
			car = new Car(subItinerary.getCar());
			car.setPosition(map.findNode(subItinerary.getNodes().get(0).getId()));
			closed.put(car.getPosition().getId(), subItinerary);

			this.result.mergeGraph(subItinerary);

			open.clear();

			for (MyNode poi : unvisitedPOI) {
				if (closed.containsKey(poi.getId())) {
					continue;
				}

				car.setDestiny(poi);
				MyThread th = new MyThread(new Search(new Car(car), searchMode, distRatio, priceRatio));
				th.start();
				threads.add(th);
			}

			for (MyThread thread : threads) {
				Search search;
				try {
					search = thread.waitStop();
					this.generatedNodesCounter += thread.search.getGeneratedNodesCounter();
					open.add(search.getResult());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			threads.clear();
		}

		// go to the final destiny
		if (!car.getPosition().equals(destiny)) {
			car.setDestiny(destiny);
			Search search = new Search(new Car(car), this.searchMode, this.distRatio, this.priceRatio);
			search.run();
			this.generatedNodesCounter += search.getGeneratedNodesCounter();
			this.result.mergeGraph(search.getResult());

			if (search.getResult().getNodes().size() > 0) {
				car.setPosition(map.findNode(search.getResult().getNodes().get(0).getId()));
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public int getGeneratedNodesCounter() {
		return generatedNodesCounter;
	}

	public MyGraph getMap() {
		return map;
	}

	public void setMap(MyGraph map) {
		this.map = map;
	}
	
	@Override // only to use in junit!!!! (because we only compare results)
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (this.getClass() != obj.getClass())
			return false;

		Itinerary it = (Itinerary) obj;
		
		return this.result.equals(it.result);
	}
}
