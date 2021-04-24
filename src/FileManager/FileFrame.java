package FileManager;

import javax.swing.*;

public class FileFrame extends JInternalFrame {

    JSplitPane split;

    public FileFrame() {
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new DirectoryPanel(), new FilePanel());
        this.setTitle("C:");

        this.getContentPane().add(split);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setClosable(true);
        this.setSize(1000, 600);
        this.setVisible(true);
    }

    public static class FileFrame extends JInternalFrame {

        JSplitPane split;

        public FileFrame() {
            split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new DirectoryPanel(), new FilePanel());
            this.setTitle("C:");

            this.getContentPane().add(split);
            this.setMaximizable(true);
            this.setIconifiable(true);
            this.setClosable(true);
            this.setSize(1000, 600);
            this.setVisible(true);
        }

    }
}
