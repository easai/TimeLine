package com.github.easai.edu.timeline;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class AddEvent extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField from = new JTextField(5);
	JTextField to = new JTextField(5);
	JTextArea description = new JTextArea(10, 20);
	JTextField caption = new JTextField(30);
	JButton ok = new JButton("Ok");
	JButton cancel = new JButton("Cancel");
	EventList events;
	DefaultTableModel model;
	TimeLine timeLine = null;
	TimeLineMenu menu = new TimeLineMenu();

	AddEvent(TimeLine timeLine) {
		this.timeLine = timeLine;
		this.events = timeLine.eventList;
		this.model = timeLine.model;
		init();
	}

	public void init() {
		Container pane = getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		// pane.setLayout(new FlowLayout());
		JPanel year = new JPanel();
		year.setLayout(new FlowLayout());
		year.add(new JLabel("Year: "));
		year.add(from);
		pane.add(year);
		// pane.add(new JLabel("To: "));
		// pane.add(to);
		JPanel event = new JPanel();
		event.add(new JLabel("Event: "));
		event.add(caption);
		pane.add(event);
		// pane.add(new JLabel("Description: "));
		// pane.add(description);
		JPanel control = new JPanel();
		control.setLayout(new FlowLayout());
		control.add(ok);
		control.add(cancel);
		pane.add(control);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		setTitle("Add Event");
		setVisible(true);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == ok) {
			int fromValue = Integer.parseInt(from.getText());
			// int toValue=Integer.parseInt(to.getText());
			String captionValue = caption.getText();
			// String descriptionValue=description.getText();
			EventItem item = new EventItem(fromValue, 0, captionValue, "", Color.blue);
			int i = events.addEvent(item);
			model.insertRow(i, new Object[] { fromValue, captionValue });

			if (timeLine != null)
				timeLine.repaint();
		}
		dispose();
	}
}