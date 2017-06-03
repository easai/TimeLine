package com.github.easai.edu.timeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EventList {
	String title = "";
	ArrayList<EventItem> eventList = new ArrayList<EventItem>();

	public int addEvent(EventItem item) {
		int nEvents = eventList.size();
		int i = 0;
		if (nEvents > 0) {
			while (eventList.get(i).from < item.from && ++i < nEvents)
				;
			if (i < nEvents) {
				eventList.add(i, item);
			} else
				eventList.add(item);
		} else {
			i = eventList.size();
			eventList.add(item);
		}
		return i;
	}

	public void deleteEvent(int index) {
		eventList.remove(index);
	}

	public int getMax() {
		int maxYear = -1;
		if (eventList.size() > 0) {
			EventItem item;
			maxYear = eventList.get(0).from;
			for (int i = 0; i < eventList.size(); i++) {
				item = eventList.get(i);
				if (maxYear < item.from)
					maxYear = item.from;
			}
		}
		return maxYear;
	}

	public int getMin() {
		int minYear = -1;
		if (eventList.size() > 0) {
			EventItem item;
			minYear = eventList.get(0).from;
			for (int i = 0; i < eventList.size(); i++) {
				item = eventList.get(i);
				if (item.from < minYear)
					minYear = item.from;
			}
		}
		return minYear;
	}

	public void parse(File xmlFile) {
		try {
			if (xmlFile.exists()) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(xmlFile);
				parse(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parse(String uri) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			URL url = new URL(uri);
			Document document = builder.parse(url.openConnection().getInputStream());
			parse(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parse(Document document) {

		try {
			Node root = document.getDocumentElement();
			NodeList catalog = root.getChildNodes();
			for (int i = 0; i < catalog.getLength(); i++) {
				Node imageFile = catalog.item(i);
				if (imageFile.getNodeName().equals("title")) {
					title = imageFile.getTextContent();
				} else {
					NodeList list = imageFile.getChildNodes();
					Node node = null;
					EventItem item = new EventItem();
					for (int j = 0; j < list.getLength(); j++) {
						node = list.item(j);
						if (node.getNodeName().equals("from"))
							item.from = Integer.parseInt(node.getTextContent());
						else if (node.getNodeName().equals("to"))
							item.to = Integer.parseInt(node.getTextContent());
						else if (node.getNodeName().equals("description"))
							item.description = node.getTextContent();
						else if (node.getNodeName().equals("caption"))
							item.caption = node.getTextContent();
						// else if(node.getNodeName().equals("color"))
						// item.color=node.getTextContent();
					}
					if (item.caption != null) {
						addEvent(item);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeEvents(File file) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		PrintWriter writer = null;
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, "UTF8");
			writer = new PrintWriter(osw);
			writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			writer.println("<?xml-stylesheet type=\"text/xsl\" href=\"style.xsl\"?>");
			writer.println("<catalog>");
			writer.println("<title>" + title + "</title>");
			EventItem item;
			for (int i = 0; i < eventList.size(); i++) {
				item = eventList.get(i);
				if (item.caption != null)
					item.writeEvent(writer);
			}

			writer.println("</catalog>");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
				else if (osw != null)
					osw.close();
				else if (fos != null)
					fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
