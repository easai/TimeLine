import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.swing.table.*;

public class TimeLineFrame extends JFrame
{
    TimeLine timeLine=new TimeLine();

    public void init()
    {
	timeLine.init();	
	timeLine.menu.setMenu(timeLine, (ActionListener)timeLine, timeLine.comp, timeLine.menu.locale);
	add(timeLine);
	addWindowListener(new WindowAdapter(){
		public void windowClosing( WindowEvent e ) { dispose(); }
	    });	

	setSize(700,800);
	setTitle("TimeLine");
	setVisible(true);
    }

    public static void main(String args[])
    {
	TimeLineFrame frame=new TimeLineFrame();
	frame.init();
    }
}
