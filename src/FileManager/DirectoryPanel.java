package FileManager;

import javax.swing.*;

public class DirectoryPanel extends JPanel {
    private JScrollPane scrollPane = new JScrollPane();
    private JTree dirTree = new JTree();

    public DirectoryPanel(){
        scrollPane.setViewportView(this.dirTree);
        add(scrollPane);
    }
}

