package org.xframium.exception;

public class ScriptConfigurationException extends XFramiumException
{
    /**
     * 
     */
    private static final long serialVersionUID = -8470149870082036015L;
    private String message;

    public ScriptConfigurationException( String message )
    {
        super( ExceptionType.CONFIGURATION );
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
