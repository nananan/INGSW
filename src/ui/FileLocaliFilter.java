
package ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Use with a file chooser to filter out non-xml files.
 */
public class FileLocaliFilter extends FileFilter {
    
    @Override
    public boolean accept(File f) {
        
        if (f.isDirectory()) {
            return true;
        }
        
        String extension = getExtension(f);
        if(extension != null) {
            if(extension.equals("png")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return ".png";
    }
    
    /**
     * Get the lower case extension of a file.
     */  
    private String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
}

