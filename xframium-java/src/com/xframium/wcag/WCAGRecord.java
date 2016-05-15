package com.xframium.wcag;

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
