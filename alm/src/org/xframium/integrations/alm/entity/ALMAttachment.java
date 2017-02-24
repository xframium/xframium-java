package org.xframium.integrations.alm.entity;

import java.io.File;

public class ALMAttachment
{
    private File fileName;
    private byte[] fileData;
    private String contentType;
    private String description;

    public ALMAttachment( File fileName, byte[] fileData, String contentType, String description )
    {
        super();
        this.fileName = fileName;
        this.fileData = fileData;
        this.contentType = contentType;
        this.description = description;
    }
    public File getFileName()
    {
        return fileName;
    }
    public void setFileName( File fileName )
    {
        this.fileName = fileName;
    }
    public byte[] getFileData()
    {
        return fileData;
    }
    public void setFileData( byte[] fileData )
    {
        this.fileData = fileData;
    }
    public String getContentType()
    {
        return contentType;
    }
    public void setContentType( String contentType )
    {
        this.contentType = contentType;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription( String description )
    {
        this.description = description;
    }
    
    
    
}
