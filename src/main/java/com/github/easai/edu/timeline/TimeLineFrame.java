package com.github.easai.edu.timeline;

import java.util.Locale;

import javax.swing.JFrame;

public class TimeLineFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TimeLine timeLine = new TimeLine();

	public void init() {
		timeLine.init();
		timeLine.menu.setMenu(this, timeLine, Locale.US);
		add(timeLine);

		//setSize(700, 800);
		pack();
		setTitle("TimeLine");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
