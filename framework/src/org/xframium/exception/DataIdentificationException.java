package org.xframium.exception;

public class DataIdentificationException extends XFramiumException
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1478754807884058923L;
    private String value;
    
    public DataIdentificationException( String value )
    {
        super( ExceptionType.APPLICATION );
    }
    
    @Override
    public String toString()
    {
        return value;
    }
    
    @Override
    public String getMessage()
    {
        return value;
    }
    
}
