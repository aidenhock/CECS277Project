package FileManager;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class GUI extends JFrame {
	JPanel panel, topPanel, toolbar;
	JMenuBar menu, status;
	JDesktopPane desktop;
	File[] paths;
	JComboBox combo;
	FileFrame ff;
	boolean details = true;

	// Creates file items and listeners and adds to file
	JMenuItem rename = new JMenuItem("Rename");
	JMenuItem copy = new JMenuItem("Copy");
	JMenuItem delete = new JMenuItem("Delete");
	JMenuItem run = new JMenuItem("Run");
	JMenuItem exit = new JMenuItem("Exit");
	final JPopupMenu popupMenu = new JPopupMenu("Options");


	private void createPopupMenu(JFrame frame){
		popupMenu.add(rename);
		popupMenu.add(copy);
		popupMenu.add(delete);
		popupMenu.add(run);
		popupMenu.add(exit);
		rename.addActionListener(new MultiActionListener());
		copy.addActionListener(new MultiActionListener());
		delete.addActionListener(new DeleteActionListener());
		run.addActionListener(new RunActionListener());
		exit.addActionListener(new ExitActionListener());
		rename.getAccessibleContext().setAccessibleDescription("Rename");
		copy.getAccessibleContext().setAccessibleDescription("Copy");
		delete.getAccessibleContext().setAccessibleDescription("Delete");
		run.getAccessibleContext().setAccessibleDescription("Run");
		exit.getAccessibleContext().setAccessibleDescription("Exit");

	}


	public GUI() {
		panel = new JPanel();
		topPanel = new JPanel();
		menu = new JMenuBar();
		toolbar = new JPanel();
		desktop = new JDesktopPane();
		status = new JMenuBar();
		createPopupMenu(this);

	}

	public void go() {
		this.setTitle("CECS 277 File Manger");
		panel.setLayout(new BorderLayout());
		topPanel.setLayout(new BorderLayout());

		buildMenu();
		buildToolBar();
		buildStatusBar();
		topPanel.add(desktop, BorderLayout.CENTER);
		ff = new FileFrame(paths[0]);
		desktop.add(ff);
		ff.fileP.list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (SwingUtilities.isRightMouseButton(e)){
					popupMenu.show(e.getComponent(), x, y);
				}

			}
		});


		panel.add(topPanel, BorderLayout.CENTER);
		this.add(panel);
		this.setSize(1300, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void revalidateAndPaint() {
		this.revalidate();
		this.repaint();
	}
	private void buildToolBar() {
		paths = File.listRoots();
		combo = new JComboBox(paths);
		JButton simple = new JButton("Simple");
		JButton details = new JButton("Details");

		combo.addItemListener(new ComboActionListener());
		simple.addActionListener(new SDActionListener());
		details.addActionListener(new SDActionListener());

		toolbar.add(combo);
		toolbar.add(simple);
		toolbar.add(details);

		topPanel.add(toolbar, BorderLayout.NORTH);
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
		status.removeAll();
		File f = (File) combo.getSelectedItem();
		JLabel size = new JLabel("Current Drive: " + f
				+ "     Free Space: " + f.getFreeSpace()/(1024*1024*1024)
				+ "GB     Used Space: " + (f.getTotalSpace() - f.getFreeSpace())/(1024*1024*1024)
				+ "GB     Total Space: " + f.getTotalSpace()/(1024*1024*1024) + "GB");
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
			FileFrame ff = (FileFrame) desktop.getSelectedFrame();
			File fromFile = (File) ff.fileP.list.getSelectedValue();
			dlg.setFromField(fromFile.getName());
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) ff.dirPanel.dirTree.getLastSelectedPathComponent();
			FileNode currentDirectoryNode = (FileNode) node.getUserObject();
			File currentDirectory = (File) currentDirectoryNode.getFile();
			dlg.setCurrentDirectory("Current Directory: " + currentDirectory);
			dlg.setVisible(true);

			//if (e.getActionCommand().equals("Rename")){
			//	title = "Renaming";
			//	Scanner scanner = new Scanner(System.in);
			//	System.out.println("Enter New Name");
			//	String input = scanner.nextLine();
			//	fromFile.renameTo(new File(input));
			//	System.out.println("Renamed.");

			//} else if (e.getActionCommand().equals("Copy")) {
			//	title = "Copying";
			//	Scanner scanner = new Scanner(System.in);
			//	System.out.println("Enter path you'd like to copy to");
			//	String input = scanner.nextLine();
			//	Path desPath = Paths.get(input);
			//	try {
			//		Files.copy(currentDirectoryNode.getFile().toPath(), desPath);
			//	} catch (IOException ioException) {
			//		ioException.printStackTrace();
			//	}
			//}
		}
	}

	//works but should be implemented so opens file on double click in FilePanel class
	//..
	//add actionMouseListener for double click on file nodes?
	private class RunActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			FileFrame ff = (FileFrame) desktop.getSelectedFrame();
			File fromFile = (File) ff.fileP.list.getSelectedValue();
			executeFile(fromFile.getPath());
		}
	}

	//needs front end integration so popup up window confirms deletion..
	private class DeleteActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Scanner scanner = new Scanner(System.in);
			FileFrame ff = (FileFrame) desktop.getSelectedFrame();
			File fromFile = (File) ff.fileP.list.getSelectedValue();
			System.out.println("You are deleting "+fromFile.getName()+" continue?");
			String input = scanner.nextLine();
			if (input == "Yes" || input == "yes"){
				fromFile.delete();
			}
			else{
				dispose();
			}
		}
	}

	//works
	public void executeFile(String filePath) {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(new File(filePath));
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	private class ComboActionListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				File f = (File) combo.getSelectedItem();
				buildStatusBar();
				topPanel.revalidate();
				topPanel.repaint();

				FileFrame ff2 = new FileFrame(f);
				desktop.add(ff2);

				ff2.moveToFront();
				revalidateAndPaint();

				//declaring new MListener, but it is not recognized
				//by refresh..
				ff2.fileP.list.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						int x = e.getX();
						int y = e.getY();
						if (SwingUtilities.isRightMouseButton(e)){
							popupMenu.show(e.getComponent(), x, y);
						}

					}
				});
			}


		}
	}

	//simple/detailed change file information
	private class SDActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Simple")) {
				System.out.println("Simple");
				ff.fileP.details = false;
				ff.fileP.revalidate();
				ff.fileP.repaint();
			} else if (e.getActionCommand().equals("Details")) {
				System.out.println("Details");
				ff.fileP.details = true;
				ff.fileP.revalidate();
				ff.fileP.repaint();
			}
		}
	}
}
