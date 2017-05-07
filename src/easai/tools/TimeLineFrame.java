package easai.tools;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class TimeLineFrame extends JFrame {
	TimeLine timeLine = new TimeLine();

	public void init() {
		timeLine.init();
		timeLine.menu.setMenu(timeLine, (ActionListener) timeLine, timeLine.comp, timeLine.menu.locale);
		add(timeLine);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		setSize(700, 800);
		setTitle("TimeLine");
		setVisible(true);
	}
}
