package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import agents.Car;
import algorithms.Itinerary;
import algorithms.Search;
import algorithms.Search.SEARCH_MODE;
import graph.Edge;
import graph.MyGraph;
import graph.MyNode;
import utils.Constants;

public class TripPlanner extends JFrame {

	private static final long serialVersionUID = -1L;
	private JPanel contentPane;
	private JTextField textFuelTank;
	private JTextField textFieldConsumption;
	private MyGraph graph;
	private JList<?> listPoints;
	private JComboBox<?> comboBoxStartingPoint;
	private JComboBox<?> comboBoxDestination;
	private JTextField textFieldFuelPrice;
	private JTextField textField_PriceSlider;
	private JTextField textField_distanceSlider;
	private JComboBox<SEARCH_MODE> comboBoxSearchMode;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TripPlanner() {
		// code to make Jframe como no sistema operativo em que corre(native
		// look)
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.graph = this.loadMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ----------------------------------------------------------
		setResizable(false);
		setTitle("Trip Planner");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 108, 614, 2);
		contentPane.add(separator);

		JLabel lblCarSetting = new JLabel("Car settings");
		lblCarSetting.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCarSetting.setBounds(10, 0, 326, 44);
		contentPane.add(lblCarSetting);

		JLabel lblFuel_Tank = new JLabel("Fuel tank capacity (L):");
		lblFuel_Tank.setBounds(18, 46, 170, 14);
		contentPane.add(lblFuel_Tank);
		textFuelTank = new JTextField();
		textFuelTank.setText("50");
		textFuelTank.setColumns(10);
		textFuelTank.setBounds(18, 65, 140, 20);
		textFuelTank.setHorizontalAlignment(JTextField.CENTER);
		textFuelTank.setToolTipText("Maximum capacity of the car's fuel tank in litres");
		contentPane.add(textFuelTank);

		JLabel lblFuelConsumption = new JLabel("Fuel consumption (L/100km):");
		lblFuelConsumption.setBounds(240, 46, 200, 14);
		contentPane.add(lblFuelConsumption);
		textFieldConsumption = new JTextField();
		textFieldConsumption.setText("5");
		textFieldConsumption.setBounds(240, 65, 140, 20);
		textFieldConsumption.setHorizontalAlignment(JTextField.CENTER);
		textFieldConsumption.setToolTipText("Average fuel consumption of the car in litres for each 100 kilometers");
		contentPane.add(textFieldConsumption);
		textFieldConsumption.setColumns(10);

		JLabel lblFuelPrice = new JLabel("Fuel price (\u20AC/L):");
		lblFuelPrice.setBounds(480, 46, 181, 14);
		contentPane.add(lblFuelPrice);
		textFieldFuelPrice = new JTextField();
		textFieldFuelPrice.setText("1");
		textFieldFuelPrice.setColumns(10);
		textFieldFuelPrice.setBounds(480, 65, 140, 20);
		textFieldFuelPrice.setHorizontalAlignment(JTextField.CENTER);
		textFieldFuelPrice.setToolTipText("Fuel price in euros for each litre");
		contentPane.add(textFieldFuelPrice);

		JLabel lbnTripSettings = new JLabel("Trip settings");
		lbnTripSettings.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbnTripSettings.setBounds(10, 108, 326, 44);
		contentPane.add(lbnTripSettings);

		JLabel lblStart = new JLabel("Starting point:");
		lblStart.setBounds(81, 160, 140, 18);
		contentPane.add(lblStart);

		comboBoxStartingPoint = new JComboBox(this.graph.getNodes().toArray());
		comboBoxStartingPoint.setBounds(18, 185, 203, 20);
		contentPane.add(comboBoxStartingPoint);

		comboBoxDestination = new JComboBox(this.graph.getNodes().toArray());
		comboBoxDestination.setBounds(417, 185, 203, 20);
		contentPane.add(comboBoxDestination);

		JLabel lblDestination = new JLabel("Destination:");
		lblDestination.setBounds(484, 160, 140, 18);
		contentPane.add(lblDestination);

		JLabel lblPointsOfInterest = new JLabel("Points of interest:");
		lblPointsOfInterest.setBounds(18, 234, 131, 14);
		contentPane.add(lblPointsOfInterest);

		// selection of poi
		listPoints = new JList(this.graph.getNodes().toArray());
		listPoints.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane JU1 = new JScrollPane(listPoints);
		JU1.setBounds(18, 259, 200, 202);
		listPoints.setToolTipText("Select points of interest (CTRL or SHIFT + mouse click for multiple selection)");
		contentPane.add(JU1);

		JButton btnMapViewer = new JButton("Show map");
		btnMapViewer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayGraph(graph, "Show map"); //
			}
		});
		btnMapViewer.setBounds(240, 429, 113, 32);
		contentPane.add(btnMapViewer);

		JButton btnSelectPOI = new JButton("Plan trip");
		btnSelectPOI.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				Object[] selectedPOI = listPoints.getSelectedValues();
				MyNode start = (MyNode) comboBoxStartingPoint.getSelectedItem();
				MyNode dest = (MyNode) comboBoxDestination.getSelectedItem();
				try {
					double tankFuel = Double.parseDouble(textFuelTank.getText());
					double fuelEconomy = Double.parseDouble(textFieldConsumption.getText());
					double fuelPrice = Double.parseDouble(textFieldFuelPrice.getText());

					if (start != null && dest != null) {
						LinkedList<MyNode> poiList = new LinkedList<MyNode>();
						for (int i = 0; i < selectedPOI.length; i++)
							poiList.add((MyNode) selectedPOI[i]);

						if (start == dest && poiList.size() == 0) {
							JOptionPane.showMessageDialog(TripPlanner.this, "You must select at least a POI when the destination is equal to the starting point.", "Trip settings error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							Car car = new Car(start, tankFuel, fuelEconomy, fuelPrice);

							poiList.addFirst(start);
							poiList.addLast(dest);

							car.setDestiny(dest);

							// *******************************************************
							graph.setPointsOfInterest(poiList);

							double priceRatio = Double.parseDouble(textField_PriceSlider.getText());
							double distRatio = Double.parseDouble(textField_distanceSlider.getText());

							SEARCH_MODE heuristicType = (SEARCH_MODE) comboBoxSearchMode.getSelectedItem();
							Itinerary i = new Itinerary(graph, car, poiList, heuristicType, distRatio, priceRatio);
							boolean hasOptimalSolution = i.run();

							if (!hasOptimalSolution) {
								JOptionPane.showMessageDialog(TripPlanner.this, "No optimal solution found. Recheck trip parameters.", "Plan trip error", JOptionPane.ERROR_MESSAGE);
							} else {
								System.out.println("Depth = " + (i.getResult().getNumNodes() - 1));
								System.out.println("Generated nodes = " + i.getGeneratedNodesCounter());

								MyGraph itineraryResult = i.getResult();

								// printTripSettings(itineraryResult, car);
								displayGraph(itineraryResult, "Itinerary");

								// display info frame;
								displayInfo(i);

								// *******************************************************
							}
						}
					} else {
						JOptionPane.showMessageDialog(TripPlanner.this, "You must select a start and destination point before planning the trip.", "Trip settings error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(TripPlanner.this, "Number Format Exception.", "Number format error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSelectPOI.setToolTipText("Select POI (ctrl or shift + mouse click) to select multiple POI)");
		btnSelectPOI.setBounds(363, 429, 134, 32);
		contentPane.add(btnSelectPOI);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnExit.setBounds(507, 429, 113, 32);
		contentPane.add(btnExit);

		JLabel lblSearchMode = new JLabel("Solution search mode:");
		lblSearchMode.setBounds(240, 234, 200, 14);
		contentPane.add(lblSearchMode);

		final JSlider sliderHeuristics = new JSlider();
		sliderHeuristics.setValue(0);
		sliderHeuristics.setBounds(240, 339, 384, 23);
		sliderHeuristics.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				textField_PriceSlider.setText((sliderHeuristics.getValue() / 100.0) + "");
				textField_distanceSlider.setText(((100.0 - sliderHeuristics.getValue()) / 100.0) + "");
			}
		});
		contentPane.add(sliderHeuristics);

		JLabel lbl_PriceSlider = new JLabel("Price (\u20AC):");
		lbl_PriceSlider.setBounds(240, 373, 120, 14);
		contentPane.add(lbl_PriceSlider);

		JLabel lbl_distanceSlider = new JLabel("Distance (km):");
		lbl_distanceSlider.setBounds(454, 373, 120, 14);
		contentPane.add(lbl_distanceSlider);

		textField_PriceSlider = new JTextField();
		textField_PriceSlider.setText("0.0");
		textField_PriceSlider.setEditable(false);
		textField_PriceSlider.setBounds(240, 398, 165, 20);
		contentPane.add(textField_PriceSlider);
		textField_PriceSlider.setColumns(10);

		textField_distanceSlider = new JTextField();
		textField_distanceSlider.setText("1.0");
		textField_distanceSlider.setEditable(false);
		textField_distanceSlider.setBounds(454, 398, 165, 20);
		contentPane.add(textField_distanceSlider);
		textField_distanceSlider.setColumns(10);

		comboBoxSearchMode = new JComboBox<SEARCH_MODE>(Search.SEARCH_MODE.values());
		comboBoxSearchMode.setBounds(240, 258, 380, 20);
		comboBoxSearchMode.setSelectedItem(Search.SEARCH_MODE.A_STAR_BASIC);
		contentPane.add(comboBoxSearchMode);

		JLabel lblCostRatio = new JLabel("Relative cost coeficient ratio:");
		lblCostRatio.setBounds(240, 314, 250, 14);
		contentPane.add(lblCostRatio);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 221, 614, 2);
		contentPane.add(separator_1);

	}

	private void displayInfo(final Search search) {
		Thread display = new Thread() {
			public void run() {
				try {
					TripInformation frame = new TripInformation(search);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		display.start();
	}

	public static void displayGraph(final MyGraph trip, final String title) {
		Thread display = new Thread() {
			public void run() {
				Graph graph = new MultiGraph(title);

				ArrayList<MyNode> tempArr = trip.getNodes();
				for (MyNode node : tempArr) {
					String id = node.getId() + "";
					graph.addNode(id);
					Node nodeTemp = graph.getNode(id);
					nodeTemp.setAttribute("x", node.getLatitude());
					nodeTemp.setAttribute("y", node.getLongitude() * -1);
					nodeTemp.addAttribute("ui.label", "( " + node.getId() + " )" + " " + node.getDescription());

					if (node.getPointOfInterest()) {
						nodeTemp.addAttribute("ui.class", "poi");
						graph.addAttribute("ui.stylesheet", "node.poi { fill-color: red; }");
					}
				}

				for (MyNode node : tempArr) {
					ArrayList<Edge> Edges = node.getOutEdges();
					for (Edge edge : Edges) {
						String idSource = edge.getSource().getId() + "";
						String idDesteny = edge.getDestiny().getId() + "";

						if (graph.getEdge(idDesteny + idSource) == null && graph.getEdge(idSource + idDesteny) == null) {
							graph.addEdge(idSource + idDesteny, idSource, idDesteny);
						}
					}
				}

				Viewer viewer = graph.display();
				View view = viewer.getDefaultView();
				viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

				Camera camera = view.getCamera();
				camera.setViewRotation(-90);

				// Let the layout work ...
				viewer.disableAutoLayout();
			}
		};
		display.start();
	}

	public static void printTripSettings(MyGraph map, Car car) {

		System.out.println("");
		System.out.println("Map settings");
		System.out.println("\tNodes: " + map.getNumNodes());
		System.out.println("\tEdges: " + map.getNumEdges());
		System.out.println("\tTotal distance: " + map.getTotalDistance(true) + " Km");

		System.out.println("");
		System.out.println("Car settings");
		System.out.println("	Position: " + car.getPosition().getId() + " - " + car.getPosition().getDescription());
		System.out.println("\tFuel: " + car.getTankFuel() + " L");
		System.out.println("\tFuel economy: " + car.getFuelEconomy() + " L/100 Km");

		System.out.println("");
		System.out.println("Trip settings");
		System.out.println("\tDaily maximum driving time: " + car.getMaxDriving() + " minutes");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TripPlanner frame = new TripPlanner();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		Action actionListener = new AbstractAction() {
			private static final long serialVersionUID = -6263573854878451452L;

			public void actionPerformed(ActionEvent actionEvent) {
				dispose();
			}
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);

		return rootPane;
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
					String[] nodeValues = fileLine.split(Constants.SETTINGS_SPLITTER);
					if (nodeValues.length != 6) {
						break;
					}

					MyNode node = new MyNode(Integer.parseInt(nodeValues[0]), Double.parseDouble(nodeValues[1]), Double.parseDouble(nodeValues[2]), Double.parseDouble(nodeValues[3]),
							Boolean.parseBoolean(nodeValues[4]), nodeValues[5]);
					map.addNode(node);
					break;

				case Constants.EDGES:

					String[] edgeValues = fileLine.split(Constants.SETTINGS_SPLITTER);
					if (edgeValues.length != 5) {
						break;
					}

					MyNode source = map.findNode(Integer.parseInt(edgeValues[0]));
					MyNode destiny = map.findNode(Integer.parseInt(edgeValues[1]));

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