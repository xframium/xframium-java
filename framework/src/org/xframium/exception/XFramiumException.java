package org.xframium.exception;

public abstract class XFramiumException extends RuntimeException
{
    public enum ExceptionType
    {
        APPLICATION,
        CONFIGURATION,
        SCRIPT,
        CLOUD;
    }
    
    private ExceptionType eType;
    
    public ExceptionType getType()
    {
        return eType;
    }
    
    public XFramiumException( ExceptionType eType )
    {
        this.eType = eType;
    }
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 2616149259768993726L;

}
