package org.xframium.exception;

public class DeviceException extends XFramiumException
{
    /**
     * 
     */
    private static final long serialVersionUID = -8470149870082036015L;
    private String message;

    public DeviceException( String message )
    {
        super( ExceptionType.CLOUD );
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
