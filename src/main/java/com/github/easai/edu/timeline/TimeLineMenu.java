package com.github.easai.edu.timeline;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TimeLineMenu {
	Locale locale = new Locale("en", "US");
	JMenuItem mi;
	JMenuBar mb = new JMenuBar();
	JMenu m[];
	String menus[] = { "File", "Edit", "View", "Tools", "Help" };
	String menuitems[][] = { { "Open", "Save", "SaveAs", "Quit" }, { "Add", "Delete", "EditTitle" },
			{ "Enlarge", "Shrink", "TextSizeIncrease", "TextSizeDecrease", "Reload", "Refresh" }, { "Options" },
			{ "Help", "AboutTimeLine" } };

	final static int nFileOpen = 0;
	final static int nFileSave = 1;
	final static int nFileSaveAs = 2;
	final static int nFileQuit = 3;
	final static int nEditAdd = 4;
	final static int nEditDelete = 5;
	final static int nEditEditTitle = 6;
	final static int nViewEnlarge = 7;
	final static int nViewShrink = 8;
	final static int nViewTextSizeIncrease = 9;
	final static int nViewTextSizeDecrease = 10;
	final static int nViewReload = 11;
	final static int nViewRefresh = 12;
	final static int nToolsOptions = 13;
	final static int nHelpHelp = 14;
	final static int nHelpAboutTimeLine = 15;

	int mi_num[][] = { { nFileOpen, nFileSave, nFileSaveAs, nFileQuit }, { nEditAdd, nEditDelete, nEditEditTitle },
			{ nViewEnlarge, nViewShrink, nViewTextSizeIncrease, nViewTextSizeDecrease, nViewReload, nViewRefresh },
			{ nToolsOptions }, { nHelpHelp, nHelpAboutTimeLine } };

	public void setMenu(JFrame frame, ActionListener l, Hashtable<JMenuItem, Integer> comp, Locale locale) {
		this.locale = locale;
		setMenu(l, comp);
		frame.setJMenuBar(mb);
	}

	public void setMenu(JApplet ap, ActionListener l, Hashtable<JMenuItem, Integer> comp, Locale locale) {
		this.locale = locale;
		setMenu(l, comp);
		ap.setJMenuBar(mb);
	}

	public void setMenu(ActionListener l, Hashtable<JMenuItem, Integer> comp) {
		// setMnemonic(new MenuShortcut(KeyEvent.VK_A))
		m = new JMenu[menus.length];
		ResourceBundle menuStrings = ResourceBundle.getBundle("TimeLineMenu", locale);
		ResourceBundle menuItemStrings = ResourceBundle.getBundle("TimeLineMenuItem", locale);

		for (int i = 0; i < menus.length; i++) {
			m[i] = new JMenu(menuStrings.getString(menus[i]));
			if (i != menus.length - 1) {
				mb.add(m[i]);
			}
			for (int j = 0; j < menuitems[i].length; j++) {
				m[i].add(mi = new JMenuItem(menuItemStrings.getString(menuitems[i][j])));
				comp.put(mi, new Integer(mi_num[i][j]));
				mi.addActionListener(l);
				// if ( // disabled menuitems
				// mi_num[i][j] == nRun ||
				// ((ap != null)
				// && (mi_num[i][j] == nOpenURL
				// || mi_num[i][j] == nSave)))

				// mi.setEnabled (false);
				// else if (mi_num[i][j] == nQuit)
				// mi.setShortcut(new
				// MenuShortcut(KeyEvent.VK_Q|KeyEvent.CTRL_MASK));
			}
		}
		mb.add(Box.createHorizontalGlue());
		mb.add(m[menus.length - 1]);

	}
}
