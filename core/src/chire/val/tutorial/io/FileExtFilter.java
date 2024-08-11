package chire.val.tutorial.io;

import java.io.File;
import java.io.FilenameFilter;

public class FileExtFilter implements FilenameFilter {
    private final String ext;
    public FileExtFilter(String ext){
        this.ext=ext;
    }

    public boolean accept(File dir, String name){
        return name.endsWith(ext);
    }
}
