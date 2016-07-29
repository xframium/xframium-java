package org.xframium.exception;

public class ScriptException extends XFramiumException
{


    private static final long serialVersionUID = 5590632064136801352L;
    private String message;

    public ScriptException( String message )
    {
        super( ExceptionType.SCRIPT );
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
