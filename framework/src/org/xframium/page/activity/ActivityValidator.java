package org.xframium.page.activity;

public class ActivityValidator
{
    private String elementName;
    private String validation;
    public String getElementName()
    {
        return elementName;
    }
    public void setElementName( String elementName )
    {
        this.elementName = elementName;
    }
    public String getValidation()
    {
        return validation;
    }
    public void setValidation( String validation )
    {
        this.validation = validation;
    }
    public ActivityValidator( String elementName, String validation )
    {
        super();
        this.elementName = elementName;
        this.validation = validation;
    }
    
    
}
