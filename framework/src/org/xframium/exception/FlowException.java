package org.xframium.exception;

public class FlowException extends XFramiumException
{


    private static final long serialVersionUID = 5590632064136801352L;
    private boolean success;

    public FlowException( boolean success )
    {
        super( ExceptionType.SCRIPT );
        this.setSuccess( success );
    }

    
    
    @Override
    public String getMessage()
    {
        return "The test was " + (isSuccess() ? "PASSED" : "FAILED") + " and foreced to exit via the FLOW keyword";
    }



    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess( boolean success )
    {
        this.success = success;
    }

}
