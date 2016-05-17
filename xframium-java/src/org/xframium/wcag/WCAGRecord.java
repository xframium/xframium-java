/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
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
package org.xframium.wcag;

public class WCAGRecord
{
    public enum WCAGType
    {
        ColorVerification,
        ContrastVerification;
    }
    
    private String pageName;
    private String elementName;
    private WCAGType type;
    private long timeStamp;
    private long duration;
    private boolean status;
    private String imageLocation;
    private String expectedValue;
    private String actualValue;
    private String failureMessage;
    
    
    public WCAGRecord( String pageName, String elementName, WCAGType type, long timeStamp, long duration, boolean status, String imageLocation, String expectedValue, String actualValue, String failureMessage )
    {
        this.pageName = pageName;
        this.elementName = elementName;
        this.type = type;
        this.timeStamp = timeStamp;
        this.duration = duration;
        this.status = status;
        this.imageLocation = imageLocation;
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
        this.failureMessage = failureMessage;
    }
    public String getPageName()
    {
        return pageName;
    }
    public void setPageName( String pageName )
    {
        this.pageName = pageName;
    }
    public String getElementName()
    {
        return elementName;
    }
    public void setElementName( String elementName )
    {
        this.elementName = elementName;
    }
    public WCAGType getType()
    {
        return type;
    }
    public void setType( WCAGType type )
    {
        this.type = type;
    }
    public long getTimeStamp()
    {
        return timeStamp;
    }
    public void setTimeStamp( long timeStamp )
    {
        this.timeStamp = timeStamp;
    }
    public long getDuration()
    {
        return duration;
    }
    public void setDuration( long duration )
    {
        this.duration = duration;
    }
    public boolean getStatus()
    {
        return status;
    }
    public void setStatus( boolean status )
    {
        this.status = status;
    }
    public String getImageLocation()
    {
        return imageLocation;
    }
    public void setImageLocation( String imageLocation )
    {
        this.imageLocation = imageLocation;
    }
    public String getExpectedValue()
    {
        return expectedValue;
    }
    public void setExpectedValue( String expectedValue )
    {
        this.expectedValue = expectedValue;
    }
    public String getActualValue()
    {
        return actualValue;
    }
    public void setActualValue( String actualValue )
    {
        this.actualValue = actualValue;
    }
    public String getFailureMessage()
    {
        return failureMessage;
    }
    public void setFailureMessage( String failureMessage )
    {
        this.failureMessage = failureMessage;
    }
    
    
}
