package org.xframium.page.activity;

public class ActivityInitiator
{
    private String elementName;
    private String action;
    public String getElementName()
    {
        return elementName;
    }
    public void setElementName( String elementName )
    {
        this.elementName = elementName;
    }
    public String getAction()
    {
        return action;
    }
    public void setAction( String action )
    {
        this.action = action;
    }
    public ActivityInitiator( String elementName, String action )
    {
        super();
        this.elementName = elementName;
        this.action = action;
    }
    
    
}
