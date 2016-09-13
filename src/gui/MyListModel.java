package gui;

import java.util.LinkedList;

import javax.swing.DefaultListModel;

import algorithms.Search;

import graph.MyNode;

public class MyListModel extends DefaultListModel<String> {
	private static final long serialVersionUID = 1L;

	public MyListModel(Search search) {
		super();
		LinkedList<MyNode> itenerary = search.getResult().getItinerary();
		String msg = "";
		int number = 1;
		for (int i = 0; i < itenerary.size() - 1; i++) {
			if (itenerary.get(i + 1).equals(itenerary.get(i)))
				itenerary.remove(i--);
			else {
				msg = (number++) + " - " + itenerary.get(i).toString();
				this.addElement(msg);
			}

		}
		if (!itenerary.isEmpty()) {
			msg = (number++) + " - " + itenerary.getLast().toString();
			this.addElement(msg);
		}
	}

	public MyListModel(LinkedList<MyNode> nodes) {
		super();
		for (int i = 0; i < nodes.size(); i++)
			this.addElement((i + 1) + " - " + nodes.get(i).toString());
	}
}
