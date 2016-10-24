package org.xframium.exception;

public class ObjectConfigurationException extends XFramiumException
{
    /**
     * 
     */
    private static final long serialVersionUID = -8470149870082036015L;
    private String siteName;
    private String pageName;
    private String elementName;
    
    public ObjectConfigurationException( String siteName, String pageName, String elementName )
    {
        super( ExceptionType.CONFIGURATION );
        this.pageName = pageName;
        this.elementName = elementName;
        this.siteName = siteName;
    }

    public String getPageName()
    {
        return pageName;
    }

    public String getElementName()
    {
        return elementName;
    }
    
    
    
    public String getSiteName()
    {
        return siteName;
    }

    @Override
    public String toString()
    {
        if ( elementName != null )
            return "Could not locate Descriptor for " + siteName + "." + pageName + "." + elementName;
        else
            return "Could not locate Descriptor for " + siteName + "." + pageName;
    }
    
    @Override
    public String getMessage()
    {
        if ( elementName != null )
            return "Could not locate Descriptor for " + siteName + "." + pageName + "." + elementName;
        else
            return "Could not locate Descriptor for " + siteName + "." +  pageName;
    }
    
    
    
}
