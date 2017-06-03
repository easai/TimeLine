import java.awt.*;
import java.io.*;

public class EventItem
{
    int from=0;
    int to=0;
    String description;
    String caption;
    Color color;

    EventItem()
    {
    }

    EventItem(int from, int to, String caption, String description, Color color)
    {
	this.from=from;
	this.to=to;
	this.description=description;
	this.caption=caption;
	this.color=color;
    }

    EventItem(int from, String caption)
    {
	this.from=from;
	this.caption=caption;
    }

    public void writeEvent(PrintWriter writer)
    {
	writer.println("<event>");
	writer.println("<from>"+from+"</from>");
	writer.println("<to>"+to+"</to>");
	writer.println("<description>"+description+"</description>");
	writer.println("<caption>"+caption+"</caption>");
	writer.println("<color>"+color+"</color>");
	writer.println("</event>");
    }    
}
