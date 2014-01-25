import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.swing.table.*;

class AddEvent extends JDialog implements ActionListener
{
    JTextField from=new JTextField(5);
    JTextField to=new JTextField(5);
    JTextArea description=new JTextArea(10,20);
    JTextField caption=new JTextField(30);
    JButton ok=new JButton("Ok");
    JButton cancel=new JButton("Cancel");
    Events events;
    DefaultTableModel model;
    TimeLine timeLine=null;
    
    AddEvent(TimeLine timeLine)
    {
	this.timeLine=timeLine;
	this.events=timeLine.events;
	this.model=timeLine.model;
	init();
    }

    public void init()
    {	
	Container pane=getContentPane();
	pane.setLayout(new BoxLayout(pane,BoxLayout.PAGE_AXIS));
	//pane.setLayout(new FlowLayout());
	JPanel year=new JPanel();
	year.setLayout(new FlowLayout());
	year.add(new JLabel("Year: "));
	year.add(from);
	pane.add(year);
	//	pane.add(new JLabel("To: "));
	//	pane.add(to);
	JPanel event=new JPanel();
	event.add(new JLabel("Event: "));
	event.add(caption);
	pane.add(event);
	//	pane.add(new JLabel("Description: "));
	//	pane.add(description);
	JPanel control=new JPanel();
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

    public void actionPerformed(ActionEvent e)
    {
	Object source=e.getSource();
	if(source==ok)
	    {
		int fromValue=Integer.parseInt(from.getText());
		//		int toValue=Integer.parseInt(to.getText());
		String captionValue=caption.getText();
		//		String descriptionValue=description.getText();
		EventItem item=new EventItem(fromValue,0,captionValue,"",Color.blue);
		int i=events.addEvent(item);
		model.insertRow(i,new Object[]{fromValue,captionValue});

		if(timeLine!=null)
		    timeLine.repaint();
	    }
	dispose();
    }
}

public class TimeLine extends JApplet implements ActionListener
{
    Events events=new Events();
    TimeLinePanel panel=new TimeLinePanel(events);

    //(load-file "E://elisp//java.el")
    //(java-jmenu-update "TimeLine" '("File""Edit""View""Tools""Help") '(("Open""Save""Save As""Quit")("Add""Delete""Edit Title")("Enlarge""Shrink""Text Size Increase""Text Size Decrease""Reload""Refresh")("Options")("Help""About TimeLine")))

    Hashtable comp= new Hashtable();	
    JTable table;
    TimeLineMenu menu=new TimeLineMenu();

    JButton ok=new JButton("Ok");
    JButton add=new JButton("Add Event");
    JButton delete=new JButton("Delete Event");
    JButton enlarge=new JButton("Enlarge (+)");
    JButton shrink=new JButton("Shrink (-)");
    JButton largeFont=new JButton("Font (+)");
    JButton smallFont=new JButton("Font (-)");
    JButton editTitle=new JButton("Edit Title");
    DefaultTableModel model = new DefaultTableModel();
    File xmlFile;
    JScrollPane timeLinePane;
    JScrollPane timeTablePane;

    public void actionPerformed (ActionEvent e)
    {
	Object source=e.getSource();
	if(source==ok)
	    {
		setTable();
	    }
	else if(source==delete)
	    {
		deleteEvent();
	    }
	else if(source==add)
	    {
		addItem();		
	    }
	else if(source==enlarge)
	    {
		resizeTimeLine(1.3);
	    }
	else if(source==shrink)
	    {
		resizeTimeLine(1.0/1.3);
	    }
	else if(source==largeFont)
	    {
		setFontSize(1.3);
	    }
	else if(source==smallFont)
	    {
		setFontSize(1.0/1.3);
	    }
	else if(source==editTitle)
	    {
		editTitle();
	    }
	else
	    {
		int num=((Integer)comp.get(source)).intValue();
		switch(num)
		    {
		    case TimeLineMenu.nFileOpen: open(); break;
		    case TimeLineMenu.nFileSave: save(); break;
		    case TimeLineMenu.nFileSaveAs: saveAs(); break;
		    case TimeLineMenu.nFileQuit: quit(); break;
		    case TimeLineMenu.nEditAdd: addItem(); break;
		    case TimeLineMenu.nEditDelete: deleteEvent(); break;
		    case TimeLineMenu.nViewReload: reload(); break;
		    case TimeLineMenu.nViewRefresh: repaint(); break;
		    case TimeLineMenu.nToolsOptions: break;
		    case TimeLineMenu.nHelpHelp: break;
		    case TimeLineMenu.nHelpAboutTimeLine: aboutTimeLine(); break;
		    case TimeLineMenu.nViewEnlarge: resizeTimeLine(1.3); break;
		    case TimeLineMenu.nViewShrink: resizeTimeLine(1.0/1.3); break;
		    case TimeLineMenu.nViewTextSizeIncrease: setFontSize(1.3); break;
		    case TimeLineMenu.nViewTextSizeDecrease: setFontSize(1.0/1.3); break;
		    case TimeLineMenu.nEditEditTitle: editTitle(); break;
		    }
	    }
    }

    public void editTitle()
    {
	events.title=JOptionPane.showInputDialog(this,"Title of the chronicle: ");
	repaint();
    }

    public void setFontSize(double rate)
    {
	panel.setFontSize(rate);
	Font font=new Font("sansserif",Font.PLAIN,panel.fontSize);
	table.setFont(font);
	table.setRowHeight(panel.fontSize+3);
	repaint();
    }
    
    public void resizeTimeLine(double rate)
    {
	int width=panel.getWidth();
	int height=panel.getHeight();
	panel.setPreferredSize(new Dimension(width,(int)(height*rate)));
	panel.setSize(new Dimension(width,(int)(height*rate)));
	
	timeLinePane.revalidate();
	repaint();
    }

    public void aboutTimeLine()
    {
	JOptionPane.showMessageDialog(this,"TimeLine\n (c) 2005 Erica Asai");
    }

    public void addItem()
    {
	new AddEvent(this);
	table.validate();
    }

    public void loadTable()
    {
	int nItems=events.events.size();
	Object values[][]=new Object[nItems][2];
	for(int i=0;i<nItems;i++)
	    {
		EventItem item=events.events.get(i);
		values[i][0]=item.from;
		values[i][1]=item.caption;
	    }
	model.setDataVector(values, new String[]{"Year","Era"});
    }
    
    public void init()
    {
	//	events.parse("http://easai.00freehost.com/rekishi");
	//events.parse("file:rekishi");
	//	loadTable();

	events.title="History of Japan";
	events.addEvent(new EventItem(592,"\u98db\u9ce5\u3000Asuka"));
	events.addEvent(new EventItem(710,"\u5948\u826f\u3000Nara"));
	events.addEvent(new EventItem(794,"\u5e73\u5b89\u3000Heian"));
	events.addEvent(new EventItem(1192,"\u938c\u5009\u3000Kamakura"));
	events.addEvent(new EventItem(1338,"\u5ba4\u753a\u3000Muromachi"));
	events.addEvent(new EventItem(1573,"\u5b89\u571f\u6843\u5c71\u3000Azuchi-Momoyama"));
	events.addEvent(new EventItem(1600,"\u6c5f\u6238\u3000Edo"));
	events.addEvent(new EventItem(1868,"\u660e\u6cbb\u3000Meiji"));
	events.addEvent(new EventItem(1912,"\u5927\u6b63\u3000Taisho"));
	events.addEvent(new EventItem(1926,"\u662d\u548c\u3000Showa"));
	events.addEvent(new EventItem(1989,"\u5e73\u6210\u3000Heisei"));
	loadTable();

	table=new JTable();
	table.setModel(model);

	Container pane=getContentPane();

	panel.setPreferredSize(new Dimension(340,720));

	//pane.setLayout(new FlowLayout());
	 timeLinePane=new JScrollPane(panel);
	 timeTablePane=new JScrollPane(table);

	JPanel control=new JPanel();	
	control.add(add);
	control.add(delete);
	control.add(ok);
	JPanel panelSize=new JPanel();
	panelSize.add(enlarge);
	panelSize.add(shrink);
	control.add(panelSize);
	JPanel fontSize=new JPanel();
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

	JSplitPane toolBar=new JSplitPane(JSplitPane.VERTICAL_SPLIT,control,timeTablePane);
	control.setPreferredSize(new Dimension(240,120));
	toolBar.setDividerLocation(120);
	JSplitPane splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,timeLinePane,toolBar);
	splitPane.setDividerLocation(350);
	pane.add(splitPane);

	//	menu.setMenu(this, (ActionListener)this, comp, menu.locale);t
    }

    public void setTable()
    {
	TableCellEditor editor=table.getCellEditor();
	if(editor!=null)
	    editor.stopCellEditing();
	for(int i=0;i<events.events.size();i++)
	    {
		events.events.get(i).from=(Integer)table.getValueAt(i,0);
		events.events.get(i).caption=(String)table.getValueAt(i,1);
	    }	
	repaint();
    }
    
    public void deleteEvent()
    {
	int row=table.getSelectedRow();
	if(row>=0)
	    {
		model.removeRow(row);
		events.deleteEvent(row);
		timeLinePane.revalidate();
		repaint();
	    }
    }

    public void save()
    {
	if(xmlFile!=null && !xmlFile.equals(""))
	    {
		events.writeEvents(xmlFile);
	    }
	else
	    {
		saveAs();
	    }
    }

    public void saveAs()
    {
	try
	    {
		JFileChooser dlg=new JFileChooser();
		dlg.setDialogType(JFileChooser.SAVE_DIALOG);
		int retval=dlg.showDialog(this,"Save");
		if(retval==JFileChooser.APPROVE_OPTION)
		    {
			File file=dlg.getSelectedFile();	
			events.writeEvents(file);
		    }
	    }
	catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}	
    }

    public void reload()
    {
	if(xmlFile!=null && xmlFile.equals(""))
	    events.parse(xmlFile);
    }

    public void open()
    {
	try
	    {
		JFileChooser dlg=new JFileChooser();
		dlg.setDialogType(JFileChooser.OPEN_DIALOG);
		int retval=dlg.showDialog(this,"Open");
		if(retval==JFileChooser.APPROVE_OPTION)
		    {
			xmlFile=dlg.getSelectedFile();	
			events.parse(xmlFile);
			loadTable();
			repaint();
		    }
	    }
	catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage());}	
    }

    public void quit()
    {

    }
}
