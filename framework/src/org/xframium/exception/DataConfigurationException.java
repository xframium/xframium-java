package org.xframium.exception;

public class DataConfigurationException extends XFramiumException
{
    /**
     * 
     */
    private static final long serialVersionUID = -8470149870082036015L;
    private String pageData;
    private String elementName;
    
    public DataConfigurationException( String pageData, String elementName )
    {
        super( ExceptionType.SCRIPT );
        this.pageData = pageData;
        this.elementName = elementName;
    }

    public String getPageData()
    {
        return pageData;
    }

    public String getElementName()
    {
        return elementName;
    }
    
    @Override
    public String toString()
    {
        if ( elementName != null )
            return "Could not locate Page Data for " + pageData + "." + elementName;
        else
            return "Could not locate Page Data for " + pageData;
    }
    
    @Override
    public String getMessage()
    {
        if ( elementName != null )
            return "Could not locate Page Data for " + pageData + "." + elementName;
        else
            return "Could not locate Page Data for " + pageData;
    }
    
    
    
}
