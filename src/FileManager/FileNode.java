package FileManager;

import java.io.File;

public class FileNode {
    File file;
    String name;

    public FileNode(String n) {
        file = new File(n);
    }

    public FileNode(String n, File f) {
        name = n;
        file = f;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        if (file.getName().equals("")) {
            return file.getPath();
        }
        return file.getName();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }
}
