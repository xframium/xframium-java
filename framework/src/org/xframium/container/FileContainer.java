package org.xframium.container;

import java.io.File;

public class FileContainer
{
    private String fullPath;
    private String leafName;
    private boolean file;
    private boolean folder;
    private int fileCount = 0;
    
    public FileContainer( File currentFile )
    {
        fullPath = currentFile.getAbsolutePath();
        leafName = currentFile.getName();
        file = currentFile.isFile();
        folder = currentFile.isDirectory();
        
        if ( currentFile.isDirectory() && currentFile.listFiles() != null )
        {
            fileCount = currentFile.listFiles().length;
        }
    }

    public String getFullPath()
    {
        return fullPath;
    }

    public void setFullPath( String fullPath )
    {
        this.fullPath = fullPath;
    }

    public String getLeafName()
    {
        return leafName;
    }

    public void setLeafName( String leafName )
    {
        this.leafName = leafName;
    }

    public boolean isFile()
    {
        return file;
    }

    public void setFile( boolean file )
    {
        this.file = file;
    }

    public boolean isFolder()
    {
        return folder;
    }

    public void setFolder( boolean folder )
    {
        this.folder = folder;
    }
    
    
    
    
}
