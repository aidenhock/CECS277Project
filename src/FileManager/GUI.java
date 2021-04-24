package FileManager;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class GUI extends JFrame{
	JPanel panel, topPanel;
	JMenuBar menu, status;
	JDesktopPane desktop;
	
	public GUI() {
		panel = new JPanel();
		topPanel = new JPanel();
		menu = new JMenuBar();
		desktop = new JDesktopPane();
		status = new JMenuBar();
	}

	public void go() {
		this.setTitle("CECS 277 File Manger");
		panel.setLayout(new BorderLayout());
		topPanel.setLayout(new BorderLayout());

		buildMenu();
		buildStatusBar();
		buildToolBar();
		topPanel.add(desktop, BorderLayout.CENTER);
		FileFrame ff = new FileFrame();
		desktop.add(ff);

		panel.add(topPanel, BorderLayout.CENTER);
		this.add(panel);
		this.setSize(1300, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	private void buildToolBar() {

	}

	private void buildMenu() {
		JMenu file, help, window, tree;
		// Sets up menu titles
		file = new JMenu("File");
		help = new JMenu("Help");
		window = new JMenu("Window");
		tree = new JMenu("Tree");

		// Creates file items and listeners and adds to file
		JMenuItem rename = new JMenuItem("Rename");
		JMenuItem copy = new JMenuItem("Copy");
		JMenuItem delete = new JMenuItem("Delete");
		JMenuItem run = new JMenuItem("Run");
		JMenuItem exit = new JMenuItem("Exit");
		rename.addActionListener(new MultiActionListener());
		copy.addActionListener(new MultiActionListener());
		delete.addActionListener(new DeleteActionListener());
		run.addActionListener(new RunActionListener());
		exit.addActionListener(new ExitActionListener());
		file.add(rename);
		file.add(copy);
		file.add(delete);
		file.add(run);
		file.add(exit);

		// Creates tree items and listeners and adds to tree
		JMenuItem expand = new JMenuItem("Expand Branch");
		JMenuItem collapse = new JMenuItem("Collapse Branch");
		tree.add(expand);
		tree.add(collapse);

		// Creates window items and listeners and adds to window
		JMenuItem newW = new JMenuItem("New");
		JMenuItem cascade = new JMenuItem("Cascade");
		window.add(newW);
		window.add(cascade);

		// Creates help items and listeners and adds to help
		JMenuItem helpH = new JMenuItem("Help");
		JMenuItem about = new JMenuItem("About");
		help.add(helpH);
		help.add(about);

		menu.add(file);
		menu.add(tree);
		menu.add(window);
		menu.add(help);
		panel.add(menu, BorderLayout.NORTH);
	}

	public void buildStatusBar() {
		JLabel size = new JLabel("Size in GB:");
		status.add(size);
		topPanel.add(status, BorderLayout.SOUTH);
	}


	private class ExitActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}


	private class MultiActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String title = "";
			if (e.getActionCommand().equals("Rename")){
				title = "Renaming";
			} else if (e.getActionCommand().equals("Copy")) {
				title = "Copying";
			}
			Dialog dlg = new Dialog(title);
			dlg.setVisible(true);
		}
	}

	private class RunActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

		}
	}

	private class DeleteActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

		}
	}

	public void executeFile(String filePath) {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(new File(filePath));
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}
}
