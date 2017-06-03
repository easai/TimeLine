package com.github.easai.edu.timeline;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.github.easai.edu.timeline.TimeLineMenu.MENUITEM;

public class TimeLine extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EventList eventList = new EventList();
	TimeLinePanel panel = new TimeLinePanel(eventList);
	JTable table;
	TimeLineMenu menu = new TimeLineMenu();

	JButton ok = new JButton("Ok");
	JButton add = new JButton("Add Event");
	JButton delete = new JButton("Delete Event");
	JButton enlarge = new JButton("Enlarge (+)");
	JButton shrink = new JButton("Shrink (-)");
	JButton largeFont = new JButton("Font (+)");
	JButton smallFont = new JButton("Font (-)");
	JButton editTitle = new JButton("Edit Title");
	DefaultTableModel model = new DefaultTableModel();
	File xmlFile;
	JScrollPane timeLinePane;
	JScrollPane timeTablePane;

	public void init() {
		eventList.title = "History of Japan";
		eventList.addEvent(new EventItem(592, "\u98db\u9ce5\u3000Asuka"));
		eventList.addEvent(new EventItem(710, "\u5948\u826f\u3000Nara"));
		eventList.addEvent(new EventItem(794, "\u5e73\u5b89\u3000Heian"));
		eventList.addEvent(new EventItem(1192, "\u938c\u5009\u3000Kamakura"));
		eventList.addEvent(new EventItem(1338, "\u5ba4\u753a\u3000Muromachi"));
		eventList.addEvent(new EventItem(1573, "\u5b89\u571f\u6843\u5c71\u3000Azuchi-Momoyama"));
		eventList.addEvent(new EventItem(1600, "\u6c5f\u6238\u3000Edo"));
		eventList.addEvent(new EventItem(1868, "\u660e\u6cbb\u3000Meiji"));
		eventList.addEvent(new EventItem(1912, "\u5927\u6b63\u3000Taisho"));
		eventList.addEvent(new EventItem(1926, "\u662d\u548c\u3000Showa"));
		eventList.addEvent(new EventItem(1989, "\u5e73\u6210\u3000Heisei"));
		loadTable();
	
		table = new JTable();
		table.setModel(model);
	
		panel.setPreferredSize(new Dimension(340, 720));
	
		// pane.setLayout(new FlowLayout());
		timeLinePane = new JScrollPane(panel);
		timeTablePane = new JScrollPane(table);
	
		JPanel control = new JPanel();
		control.add(add);
		control.add(delete);
		control.add(ok);
		JPanel panelSize = new JPanel();
		panelSize.add(enlarge);
		panelSize.add(shrink);
		control.add(panelSize);
		JPanel fontSize = new JPanel();
		fontSize.add(largeFont);
		fontSize.add(smallFont);
		fontSize.add(editTitle);
		control.add(fontSize);
	
		add.addActionListener(this);
		delete.addActionListener(this);
		ok.addActionListener(this);
		enlarge.addActionListener(this);
		shrink.addActionListener(this);
		largeFont.addActionListener(this);
		smallFont.addActionListener(this);
		editTitle.addActionListener(this);
	
		JSplitPane vert = new JSplitPane(JSplitPane.VERTICAL_SPLIT, control, timeTablePane){
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private final int location = 120;
		    {
		        setDividerLocation( location );
		    }
		    @Override
		    public int getDividerLocation() {
		        return location ;
		    }
		    @Override
		    public int getLastDividerLocation() {
		        return location ;
		    }
		};
		control.setPreferredSize(new Dimension(240, 120));
		vert.setDividerLocation(120);
		JSplitPane horiz = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, timeLinePane, vert){
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private final int location = 350;
		    {
		        setDividerLocation( location );
		    }
		    @Override
		    public int getDividerLocation() {
		        return location ;
		    }
		    @Override
		    public int getLastDividerLocation() {
		        return location ;
		    }
		};
		horiz.setDividerLocation(350);
		this.add(horiz);
		
		
		
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == ok) {
			setTable();
		} else if (source == delete) {
			deleteEvent();
		} else if (source == add) {
			addItem();
		} else if (source == enlarge) {
			resizeTimeLine(1.3);
		} else if (source == shrink) {
			resizeTimeLine(1.0 / 1.3);
		} else if (source == largeFont) {
			setFontSize(1.3);
		} else if (source == smallFont) {
			setFontSize(1.0 / 1.3);
		} else if (source == editTitle) {
			editTitle();
		} else {
			MENUITEM n = menu.comp.get(source);
			if (n != null) {
				switch (n) {
				case nFileOpen:
					open();
					break;
				case nFileSave:
					save();
					break;
				case nFileSaveAs:
					saveAs();
					break;
				case nFileQuit:
					quit();
					break;
				case nEditAdd:
					addItem();
					break;
				case nEditDelete:
					deleteEvent();
					break;
				case nEditEditTitle:
					editTitle();
					break;
				case nViewEnlarge:
					resizeTimeLine(1.3);
					break;
				case nViewShrink:
					resizeTimeLine(1.0 / 1.3);
					break;
				case nViewTextSizeIncrease:
					setFontSize(1.3);
					break;
				case nViewTextSizeDecrease:
					setFontSize(1.0 / 1.3);
					break;
				case nViewReload:
					reload();
					break;
				case nViewRefresh:
					repaint();
					break;
				case nToolsOptions:
					break;
				case nHelpHelp:
					break;
				case nHelpAbout:
					aboutTimeLine();
					break;
				}
			}
		}
	}

	public void editTitle() {
		eventList.title = JOptionPane.showInputDialog(this, "Title of the chronicle: ");
		repaint();
	}

	public void setFontSize(double rate) {
		panel.setFontSize(rate);
		Font font = new Font("sansserif", Font.PLAIN, panel.fontSize);
		table.setFont(font);
		table.setRowHeight(panel.fontSize + 3);
		repaint();
	}

	public void resizeTimeLine(double rate) {
		int width = panel.getWidth();
		int height = panel.getHeight();
		panel.setPreferredSize(new Dimension(width, (int) (height * rate)));
		panel.setSize(new Dimension(width, (int) (height * rate)));

		timeLinePane.revalidate();
		repaint();
	}

	public void addItem() {
		new AddEvent(this);
		table.validate();
	}

	public void loadTable() {
		int nItems = eventList.eventList.size();
		Object values[][] = new Object[nItems][2];
		for (int i = 0; i < nItems; i++) {
			EventItem item = eventList.eventList.get(i);
			values[i][0] = item.from;
			values[i][1] = item.caption;
		}
		model.setDataVector(values, new String[] { "Year", "Era" });
	}

	public void setTable() {
		TableCellEditor editor = table.getCellEditor();
		if (editor != null)
			editor.stopCellEditing();
		for (int i = 0; i < eventList.eventList.size(); i++) {
			eventList.eventList.get(i).from = (Integer) table.getValueAt(i, 0);
			eventList.eventList.get(i).caption = (String) table.getValueAt(i, 1);
		}
		repaint();
	}

	public void deleteEvent() {
		int row = table.getSelectedRow();
		if (row >= 0) {
			model.removeRow(row);
			eventList.deleteEvent(row);
			timeLinePane.revalidate();
			repaint();
		}
	}

	public void save() {
		if (xmlFile != null && !xmlFile.equals("")) {
			eventList.writeEvents(xmlFile);
		} else {
			saveAs();
		}
	}

	public void saveAs() {
		try {
			JFileChooser dlg = new JFileChooser();
			dlg.setDialogType(JFileChooser.SAVE_DIALOG);
			int retval = dlg.showDialog(this, "Save");
			if (retval == JFileChooser.APPROVE_OPTION) {
				File file = dlg.getSelectedFile();
				eventList.writeEvents(file);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	public void reload() {
		if (xmlFile != null && xmlFile.equals(""))
			eventList.parse(xmlFile);
	}

	public void open() {
		try {
			JFileChooser dlg = new JFileChooser();
			dlg.setDialogType(JFileChooser.OPEN_DIALOG);
			int retval = dlg.showDialog(this, "Open");
			if (retval == JFileChooser.APPROVE_OPTION) {
				xmlFile = dlg.getSelectedFile();
				eventList.parse(xmlFile);
				loadTable();
				repaint();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	public void aboutTimeLine() {
		JOptionPane.showMessageDialog(this, "TimeLine\n (c) 2005 Erica Asai");
	}

	public void quit() {

	}

	public static void main(String args[]) {
		TimeLineFrame frame = new TimeLineFrame();
		frame.init();
	}

}
