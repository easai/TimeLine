package com.github.easai.edu.timeline;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

public class TimeLinePanel extends JPanel implements Scrollable, MouseMotionListener {
	Events events = null;
	boolean debug = true;
	private int maxUnitIncrement = 1;
	int fontSize = -1;

	TimeLinePanel(Events events) {
		this.events = events;
		setAutoscrolls(true);
		addMouseMotionListener(this);
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
		scrollRectToVisible(r);
	}

	public void setFontSize(double rate) {
		fontSize = (int) (fontSize * rate);
		repaint();
	}

	public void paint(Graphics g) {
		Font font = g.getFont();
		if (fontSize == -1)
			fontSize = font.getSize();
		if (font.getSize() != fontSize)
			font = font.deriveFont(fontSize);
		font = new Font("sansserif", Font.PLAIN, fontSize);
		g.setFont(font);

		FontMetrics fm = getFontMetrics(font);

		Rectangle2D titleRect = fm.getStringBounds(events.title, g);
		int titleHeight = (int) titleRect.getHeight();

		int height = getHeight();
		int width = getWidth();
		int leftMargin = 100;
		int topMargin = 30 + titleHeight;
		int bottomMargin = 20;
		int scaleWidth = 10;

		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.black);

		g.drawString(events.title, 10, titleHeight);

		g.drawLine(leftMargin, topMargin - scaleWidth, leftMargin, height - bottomMargin + scaleWidth);
		int minYear = events.getMin();
		int range = events.getMax() - minYear;

		if (range > 0) {
			double scale = (double) (height - bottomMargin - topMargin) / range;
			for (int i = 0; i < events.events.size(); i++) {
				EventItem item = events.events.get(i);
				int y = topMargin + (int) ((item.from - minYear) * scale);
				g.drawLine(leftMargin - scaleWidth, y, leftMargin + scaleWidth, y);
				if (item != null && item.caption != null && !item.caption.equals("")) {
					Rectangle2D rect = fm.getStringBounds(item.caption, g);
					y += rect.getHeight() / 2;
					g.drawString(item.caption, leftMargin + scaleWidth * 2, y);
					rect = fm.getStringBounds("" + item.from, g);
					g.drawString("" + item.from, (int) (leftMargin - rect.getWidth() - scaleWidth * 2), y);
				}
			}
		} else if (events.events != null && events.events.size() > 0) {
			EventItem item = events.events.get(0);
			int y = (height - bottomMargin - topMargin) / 2 + topMargin;
			g.drawLine(leftMargin - scaleWidth, y, leftMargin + scaleWidth, y);
			Rectangle2D rect = fm.getStringBounds(item.caption, g);
			y += (rect.OUT_BOTTOM - rect.OUT_TOP) / 2;
			g.drawString(item.caption, leftMargin + scaleWidth * 2, y);
			g.drawString("" + item.from, leftMargin - scaleWidth * 2, y);
		}
	}

	public int getScrollableUnitIncrement(Rectangle rect, int orientation, int direction) {
		int currentPosition = 0;
		if (orientation == SwingConstants.HORIZONTAL) {
			currentPosition = rect.x;
		} else {
			currentPosition = rect.y;
		}
		if (direction < 0) {
			int newPosition = currentPosition - (currentPosition / maxUnitIncrement) * maxUnitIncrement;
			return (newPosition == 0) ? maxUnitIncrement : newPosition;
		} else {
			return ((currentPosition / maxUnitIncrement) + 1) * maxUnitIncrement - currentPosition;
		}
	}

	public int getScrollableBlockIncrement(Rectangle rect, int orientation, int direction) {
		if (orientation == SwingConstants.HORIZONTAL)
			return rect.width - maxUnitIncrement;
		else
			return rect.height - maxUnitIncrement;
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
}
