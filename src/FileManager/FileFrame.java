package FileManager;

import javax.swing.*;
import java.io.File;

public class FileFrame extends JInternalFrame {

    JSplitPane split;
    FilePanel fileP = new FilePanel();
    DirectoryPanel dirPanel;

    public FileFrame(File rootDirectory) {
        dirPanel = new DirectoryPanel(fileP, rootDirectory);
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dirPanel, fileP);
        this.setTitle(rootDirectory.getPath());

        this.getContentPane().add(split);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setClosable(true);
        this.setSize(1000, 600);
        this.setVisible(true);
    }

}
