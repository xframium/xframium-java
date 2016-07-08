package org.xframium.exception;

public class TestConfigurationException extends XFramiumException
{
    /**
     * 
     */
    private static final long serialVersionUID = -8470149870082036015L;
    private String testName;

    public TestConfigurationException( String testName )
    {
        super( ExceptionType.CONFIGURATION );
        this.testName = testName;
    }
    
    @Override
    public String toString()
    {
        return "Could not locate a test name " + testName;
    }
    
    @Override
    public String getMessage()
    {
        return "Could not locate a test name " + testName;
    }
    
    
}
