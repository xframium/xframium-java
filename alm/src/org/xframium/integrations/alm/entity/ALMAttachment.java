/*******************************************************************************
 * xFramium
 *
 * Copyright 2017 by Moreland Labs LTD (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
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
