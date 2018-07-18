package org.xframium.exception;

public class BubbledFailureException extends XFramiumException
{


    private static final long serialVersionUID = 5590632064136801352L;
    private Throwable rootException;

    public BubbledFailureException(Throwable rootException )
    {
        super( ExceptionType.SCRIPT );
        this.rootException = rootException;
    }

    
    
    @Override
    public String getMessage()
    {
        return rootException.getMessage();
    }
    
    public Throwable getException()
    {
    	return rootException;
    }

}
