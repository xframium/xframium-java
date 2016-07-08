package org.xframium.exception;

public class DeviceConfigurationException extends XFramiumException
{
    /**
     * 
     */
    private static final long serialVersionUID = -8470149870082036015L;
    private String appIdentifier;

    public DeviceConfigurationException( String appIdentifier )
    {
        super( ExceptionType.SCRIPT );
        this.appIdentifier = appIdentifier;
    }

    @Override
    public String toString()
    {
        return "Could not start the application identified by " + appIdentifier;
    }

    @Override
    public String getMessage()
    {
        return "Could not start the application identified by " + appIdentifier;
    }

}
