package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import algorithms.Search;

public class TripInformation extends JFrame {
	private static final long serialVersionUID = 1L;
	private final Search search;
	private JList<?> listPoints;
	private JTextField textFieldCost;
	private JTextField textField_Days;
	private JTextField txtStart;
	private JTextField txtDest;
	private JList<?> listHot;
	private JList<?> listGas;
	private JTextField textField_spendHot;
	private JTextField textField_SpentGas;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TripInformation(Search search) {
		this.search = search;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setResizable(false);
		setTitle("Trip Information");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(640, 480);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel lblItinerary = new JLabel("Itinerary:");
		lblItinerary.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItinerary.setBounds(62, 5, 163, 20);
		getContentPane().add(lblItinerary);

		listPoints = new JList(new MyListModel(this.search));
		listPoints
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane JU1 = new JScrollPane(listPoints);
		JU1.setBounds(10, 26, 177, 294);
		getContentPane().add(JU1);

		JLabel lblPrice = new JLabel("Total distance drived (km):");
		lblPrice.setBounds(447, 344, 200, 14);
		getContentPane().add(lblPrice);

		textFieldCost = new JTextField();
		textFieldCost.setEditable(false);
		textFieldCost.setBounds(447, 369, 177, 20);
		textFieldCost.setColumns(10);
		textFieldCost.setText(search.getResult().getTotalDistance(false) + "");
		getContentPane().add(textFieldCost);

		JLabel lblDays = new JLabel("Time spent driving (hours):");
		lblDays.setBounds(447, 397, 200, 20);
		getContentPane().add(lblDays);

		textField_Days = new JTextField();
		textField_Days.setText("0.0");
		textField_Days.setEditable(false);
		textField_Days.setBounds(447, 421, 177, 20);
		textField_Days.setColumns(10);
		textField_Days.setText(search.getResult().getTotalTime(false) / 60.0
				+ "");

		getContentPane().add(textField_Days);

		JLabel lblHotels = new JLabel("Hotels:");
		lblHotels.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblHotels.setBounds(282, 5, 177, 20);
		getContentPane().add(lblHotels);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 331, 614, 2);
		getContentPane().add(separator);

		JLabel lblStation = new JLabel("Gas stations:");
		lblStation.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblStation.setBounds(484, 5, 177, 20);
		getContentPane().add(lblStation);

		JLabel lblStart = new JLabel("Start:");
		lblStart.setBounds(10, 344, 177, 14);
		getContentPane().add(lblStart);

		JLabel lblDestination = new JLabel("Destination:");
		lblDestination.setBounds(10, 400, 177, 14);
		getContentPane().add(lblDestination);

		txtStart = new JTextField();
		txtStart.setEditable(false);
		txtStart.setColumns(10);
		txtStart.setBounds(10, 369, 177, 20);
		getContentPane().add(txtStart);

		txtDest = new JTextField();
		txtDest.setEditable(false);
		txtDest.setColumns(10);
		txtDest.setBounds(10, 421, 177, 20);
		getContentPane().add(txtDest);

		listHot = new JList(
				new MyListModel(this.search.getResult().getHotels()));
		listHot.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane JU2 = new JScrollPane(listHot);
		JU2.setBounds(224, 26, 185, 294);
		getContentPane().add(JU2);

		listGas = new JList(new MyListModel(this.search.getResult().getGas()));
		listGas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane JU3 = new JScrollPane(listGas);
		JU3.setBounds(447, 26, 177, 294);
		getContentPane().add(JU3);

		textField_spendHot = new JTextField();
		textField_spendHot.setEditable(false);
		textField_spendHot.setBounds(224, 369, 185, 20);
		textField_spendHot.setColumns(10);
		textField_spendHot.setText(this.search.getResult().getHotelsPrice()
				+ "");
		getContentPane().add(textField_spendHot);

		JLabel lblNewLabel = new JLabel("Spent on hotels (\u20AC):");
		lblNewLabel.setBounds(224, 344, 176, 14);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Spent in fuel (\u20AC):");
		lblNewLabel_1.setBounds(224, 400, 177, 14);
		getContentPane().add(lblNewLabel_1);

		textField_SpentGas = new JTextField();
		textField_SpentGas.setText("0.0");
		textField_SpentGas.setEditable(false);
		textField_SpentGas.setBounds(224, 421, 185, 20);
		textField_SpentGas.setColumns(10);
		textField_SpentGas.setText(search.getResult().getTripSpendMoneyOnGas()
				+ "");
		getContentPane().add(textField_SpentGas);

		if (search.getResult().getNodes().size() > 0) {
			txtStart.setText(search.getResult().getItinerary().getFirst()
					.getDescription());
			txtDest.setText(search.getResult().getItinerary().getLast()
					.getDescription());
		}

		// System.out.println(search.getResult().getTotalDistance(false));
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
		InputMap inputMap = rootPane
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);

		return rootPane;
	}
}
