package FileManager;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author Dr. Hoffman
 */

public class FilePanel extends JPanel {
    private FileFrame ff;
    JList list = new JList();
    DefaultListModel model = new DefaultListModel();
    JScrollPane scroll = new JScrollPane();
    boolean details = true;

    public void setFileFFrame(FileFrame fileFrame){
        ff = fileFrame;
    }

    public FilePanel() {
        this.setDropTarget(new MyDropTarget());
        list.setDragEnabled(true);
        Map<Object, Icon> icons = new HashMap<Object, Icon>();
        icons.put("file", MetalIconFactory.getTreeLeafIcon());
        icons.put("folder", MetalIconFactory.getTreeFolderIcon());
        list.setCellRenderer(new IconListRenderer(icons));

        scroll.setViewportView(this.list);
        list.setModel(model);
        scroll.setPreferredSize(new Dimension(650, 550));
        add(scroll);
    }

    public void addJList(JList jlist) {
        this.removeAll();
        list = jlist;
        if (list.getModel().getSize() > 0) {
            scroll.setViewportView(this.list);
            scroll.setPreferredSize(new Dimension(650, 550));
            add(scroll);
        } else {
            add(jlist);
        }

        Map<Object, Icon> icons = new HashMap<Object, Icon>();
        icons.put("file", MetalIconFactory.getTreeLeafIcon());
        icons.put("folder", MetalIconFactory.getTreeFolderIcon());
        list.setCellRenderer(new IconListRenderer(icons));

        revalidate();
        repaint();
    }

    public void setDetails(boolean details) {
        this.details = details;
    }
    /*************************************************************************
     * class MyDropTarget handles the dropping of files onto its owner
     * (whatever JList it is assigned to). As written, it can process two
     * types: strings and files (String, File). The String type is necessary
     * to process internal source drops from another FilePanel object. The
     * File type is necessary to process drops from external sources such
     * as Windows Explorer or IOS.
     *
     * Note: no code is provided that actually copies files to the target
     * directory. Also, you may need to adjust this code if your list model
     * is not the default model. JList assumes a toString operation is
     * defined for whatever class is used.
     */
    class MyDropTarget extends DropTarget {
        /**************************************************************************
         *
         * @param evt the event that caused this drop operation to be invoked
         */
        public void drop(DropTargetDropEvent evt){

            //call tree through our local variable
            JTree tree = ff.getDirPanel().getDirTree();
            //get destination drop "last selected path"
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if(node == null)
                return;
            FileNode nodeInfo = (FileNode) node.getUserObject();
            //drop file path
            String destination = nodeInfo.getFile().getAbsolutePath() + File.separator;

            try {
                //copy
                evt.acceptDrop(DnDConstants.ACTION_COPY);
                //storage to hold the drop data for processing
                List result = new ArrayList();
                //what is being dropped? First, Strings are processed

                //drop and copy strings
                if(evt.getTransferable().isDataFlavorSupported(DataFlavor.stringFlavor)){
                    String temp = (String)evt.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    //String events are concatenated if more than one list item
                    //selected in the source. The strings are separated by
                    //newline characters. Use split to break the string into
                    //individual file names and store in String[]
                    String[] next = temp.split("\\n");
                    //add the strings to the listmodel
                    for(int i=0; i<next.length;i++) {
                        model.addElement(next[i]);
                        FileNode src = new FileNode(this.toString());
                        destination = destination + src.getFile().getName();
                        Path desPath = Paths.get(destination);
                        Files.copy(src.file.toPath(), desPath);
                        repaint();
                        revalidate();
                    }
                    // ^I'm not quite sure how copying strings works or why we would in the file manager
                    // but i did same as files
                }

                //drop and copy files
                else{ //then if not String, Files are assumed
                    result =(List)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    //process the input
                    for(Object o : result){
                        System.out.println(o.toString());
                        model.addElement(o.toString());
                        FileNode src = new FileNode(o.toString());
                        //new destination
                        destination = destination + src.getFile().getName();
                        //convert to path
                        Path desPath = Paths.get(destination);
                        //copy over to destination pathway
                        Files.copy(src.file.toPath(), desPath);

                        //file doesnt refresh after i copy over i have to manually click off and back on, so
                        //i dont know how to fix it so it immediately uploads after copying over
                        revalidate();
                        repaint();
                    }
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }

    private class IconListRenderer extends DefaultListCellRenderer {
        private Map<Object, Icon> icons = null;

        public IconListRenderer(Map<Object, Icon> icons) {
            this.icons = icons;
        }
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            File f = (File) value;
            String s = "";
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            DecimalFormat dformat = new DecimalFormat("#,###");
            if (details && !f.isDirectory()) {
                s = f.getName() + "     " + formatter.format(f.lastModified()) + "     " + dformat.format(f.length());
            } else {
                s = f.getName();
            }
            value = s;
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String iconName = "file";
            if (f.isDirectory()) {
                iconName = "folder";
            }
            Icon icon = icons.get(iconName);
            label.setIcon(icon);
            return label;
        }
    }
}
