package org.xframium.exception;

public class ObjectConfigurationException extends XFramiumException
{
    /**
     * 
     */
    private static final long serialVersionUID = -8470149870082036015L;
    private String pageName;
    private String elementName;
    
    public ObjectConfigurationException( String pageName, String elementName )
    {
        super( ExceptionType.SCRIPT );
        this.pageName = pageName;
        this.elementName = elementName;
    }

    public String getPageName()
    {
        return pageName;
    }

    public String getElementName()
    {
        return elementName;
    }
    
    @Override
    public String toString()
    {
        if ( elementName != null )
            return "Could not locate Descriptor for " + pageName + "." + elementName;
        else
            return "Could not locate Descriptor for " + pageName;
    }
    
    @Override
    public String getMessage()
    {
        if ( elementName != null )
            return "Could not locate Descriptor for " + pageName + "." + elementName;
        else
            return "Could not locate Descriptor for " + pageName;
    }
    
    
    
}
