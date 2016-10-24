package org.xframium.exception;

public class FilteredException extends XFramiumException
{


    private static final long serialVersionUID = 5590632064136801352L;
    private String message;

    public FilteredException( String message )
    {
        super( ExceptionType.FILTERED );
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

}
