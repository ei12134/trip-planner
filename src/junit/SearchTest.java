package junit;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.junit.Test;

import agents.Car;
import algorithms.Itinerary;
import algorithms.Search;
import algorithms.Search.SEARCH_MODE;
import graph.MyGraph;
import graph.MyNode;
import graph.MyResultGraph;
import utils.Constants;

public class SearchTest {

	@Test
	public void testStraightLineDistance() {
		assertEquals(Search.straightLineDistance(38.898556, -77.037852,
				38.897147, -77.043934), 0.549, 0.001);

		assertEquals(Search.straightLineDistance(41.0961655, -8.7248637,
				39.4078969, -0.4315509), 728.321, 0.001);
	}

	@Test
	public void testSearch() {
		MyGraph map = null;
		try {
			map = loadMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//
		MyNode start = map.findNode(3);

		LinkedList<MyNode> poiList = new LinkedList<MyNode>();
		poiList.add(map.findNode(6));
		poiList.add(map.findNode(50));
		poiList.add(map.findNode(70));
		poiList.add(map.findNode(87));

		MyNode dest = map.findNode(4);

		poiList.addFirst(start);
		poiList.addLast(dest);

		Car car_uni = new Car(start, 50, 5, 1);
		car_uni.setDestiny(dest);
		Itinerary i_uni = new Itinerary(map, car_uni, poiList,
				SEARCH_MODE.UNIFORM_COST, 1.0, 0.0);

		Car car_bas = new Car(start, 50, 5, 1);
		car_bas.setDestiny(dest);
		Itinerary i_bas = new Itinerary(map, car_bas, poiList,
				SEARCH_MODE.A_STAR_BASIC, 1.0, 0.0);

		assertEquals(true, i_uni.equals(i_bas));

		Car car_ad = new Car(start, 50, 5, 1);
		car_ad.setDestiny(dest);
		Itinerary i_ad = new Itinerary(map, car_ad, poiList,
				SEARCH_MODE.A_STAR_ADVANCED, 1.0, 0.0);

		assertEquals(true, i_uni.equals(i_ad));
	}

	@Test
	public void testManualSearch() {
		MyGraph map = null;
		try {
			map = loadMap();
		} catch (IOException e) {
			e.printStackTrace();
		}

		MyNode start = map.findNode(1);

		LinkedList<MyNode> poiList = new LinkedList<MyNode>();

		MyNode dest = map.findNode(11);

		poiList.addFirst(start);
		poiList.addLast(dest);

		Car car_uni = new Car(start, 50, 5, 1);
		car_uni.setDestiny(dest);
		Itinerary i_uni = new Itinerary(map, car_uni, poiList,
				SEARCH_MODE.UNIFORM_COST, 1.0, 0.0);

		MyResultGraph mg = new MyResultGraph();
		mg.addNode(map.findNode(1));
		mg.addNode(map.findNode(3));
		mg.addNode(map.findNode(4));
		mg.addNode(map.findNode(11));

		LinkedList<MyNode> it = new LinkedList<MyNode>();
		it.add(map.findNode(1));
		it.add(map.findNode(3));
		it.add(map.findNode(4));
		it.add(map.findNode(11));
		mg.setItinerary(it);

		LinkedList<MyNode> gas = new LinkedList<MyNode>();
		mg.setGas(gas);

		LinkedList<MyNode> hotels = new LinkedList<MyNode>();
		mg.setGas(hotels);
		i_uni.run();

		assertEquals(true, mg.equals(i_uni.getResult()));
	}

	public MyGraph loadMap() throws IOException {
		File mapFile = new File("res/map.csv");

		MyGraph map = new MyGraph();
		FileInputStream fstream = new FileInputStream(mapFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String readMode = Constants.UNDEFINED;
		String fileLine;

		while ((fileLine = br.readLine()) != null) {

			// Check non empty line
			if (fileLine.length() > 0) {

				// Settings type to read
				if (fileLine.equals(Constants.NODES)) {
					readMode = Constants.NODES;
					continue;
				} else if (fileLine.equals(Constants.EDGES)) {
					readMode = Constants.EDGES;
					continue;
				}

				switch (readMode) {

				case Constants.NODES:
					String[] nodeValues = fileLine
							.split(Constants.SETTINGS_SPLITTER);
					if (nodeValues.length != 6) {
						break;
					}

					MyNode node = new MyNode(Integer.parseInt(nodeValues[0]),
							Double.parseDouble(nodeValues[1]),
							Double.parseDouble(nodeValues[2]),
							Double.parseDouble(nodeValues[3]),
							Boolean.parseBoolean(nodeValues[4]), nodeValues[5]);
					map.addNode(node);
					break;

				case Constants.EDGES:

					String[] edgeValues = fileLine
							.split(Constants.SETTINGS_SPLITTER);
					if (edgeValues.length != 5) {
						break;
					}

					MyNode source = map.findNode(Integer
							.parseInt(edgeValues[0]));
					MyNode destiny = map.findNode(Integer
							.parseInt(edgeValues[1]));

					if (source == null || destiny == null) {
						throw new IOException();
					}

					double distance = Double.parseDouble(edgeValues[2]);
					double price = Double.parseDouble(edgeValues[3]);
					int minutes = Integer.parseInt(edgeValues[4]);
					// undirected graph
					map.addEdge(source, destiny, distance, price, minutes);
					map.addEdge(destiny, source, distance, price, minutes);
					break;
				default:
					readMode = Constants.UNDEFINED;
					break;
				}
			}
		}

		// Close the input stream
		br.close();
		return map;
	}
}
