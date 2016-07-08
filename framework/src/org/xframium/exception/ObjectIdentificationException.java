package org.xframium.exception;

import org.openqa.selenium.By;
import org.xframium.page.BY;

public class ObjectIdentificationException extends XFramiumException
{
    /**
     * 
     */
    private static final long serialVersionUID = 479258953143046488L;
    private BY xBy;
    private By by;
    
    public ObjectIdentificationException( BY xBy, By by )
    {
        super( ExceptionType.CONFIGURATION );
        this.xBy = xBy;
        this.by = by;
    }
    public BY getxBy()
    {
        return xBy;
    }
    public By getBy()
    {
        return by;
    }
    
    @Override
    public String toString()
    {
        return "Could not locate " + by.toString();
    }
    
    @Override
    public String getMessage()
    {
        return "Could not locate " + by.toString();
    }
    
}
