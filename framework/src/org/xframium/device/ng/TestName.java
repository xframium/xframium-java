package org.xframium.device.ng;

import org.xframium.page.data.PageData;
import org.xframium.reporting.ExecutionContextTest;

public class TestName
{
    /** The test name. */
    private String testName;
    private String baseTestName;

    /** The full name. */
    private String fullName;

    /** The persona name. */
    private String personaName;

    private String testContext;

    private String rawName;

    private String contentKey;
    
    private PageData dataDriven;
    
    private int iteration;

    private ExecutionContextTest test;
    

    public PageData getDataDriven()
    {
        return dataDriven;
    }
    
    public TestName clone()
    {
        TestName newTest = new TestName();
        newTest.testName = testName;
        newTest.baseTestName = baseTestName;
        newTest.fullName = fullName;
        newTest.personaName = personaName;
        newTest.testContext = testContext;
        newTest.rawName = rawName;
        newTest.contentKey = contentKey;
        newTest.dataDriven = dataDriven;
        newTest.iteration = iteration;
        newTest.test = test;
        return newTest;
    }

//    public String toString()
//    {
//        return testName;
//    }
    
    
    
    public void setDataDriven( PageData dataDriven )
    {
        this.dataDriven = dataDriven;
        formatTestName();
    }

    public ExecutionContextTest getTest()
    {
        return test;
    }

    public void setTest( ExecutionContextTest test )
    {
        this.test = test;
        test.setTestName( testName );
    }

    public String getRawName()
    {
        return rawName;
    }

    public void setRawName( String rawName )
    {
        this.rawName = rawName;
    }

    
    
    public int getIteration()
    {
        return iteration;
    }

    public void setIteration( int iteration )
    {
        this.iteration = iteration;
        formatTestName();
    }

    /**
     * Gets the full name.
     *
     * @return the full name
     */
    public String getFullName()
    {
        return fullName;
    }

    /**
     * Sets the full name.
     *
     * @param fullName
     *            the new full name
     */
    public void setFullName( String fullName )
    {
        this.fullName = fullName;
    }

    /**
     * Gets the persona name.
     *
     * @return the persona name
     */
    public String getPersonaName()
    {
        return personaName;
    }

    /**
     * Sets the persona name.
     *
     * @param personaName
     *            the new persona name
     */
    public void setPersonaName( String personaName )
    {
        this.personaName = personaName;
        formatTestName();
    }

    /**
     * Gets the content key.
     *
     * @return the content key
     */
    public String getContentKey()
    {
        return contentKey;
    }

    /**
     * Sets the content key.
     *
     * @param contentKey
     *            the new content key
     */
    public void setContentKey( String contentKey )
    {
        this.contentKey = contentKey;
        formatTestName();
    }

    public String getTestContext()
    {
        return testContext;
    }

    public void setTestContext( String testContext )
    {
        this.testContext = testContext;
        formatTestName();
    }

    /**
     * Instantiates a new test name.
     */
    public TestName()
    {
    }

    /**
     * Instantiates a new test name.
     *
     * @param testName
     *            the test name
     */
    public TestName( String testName )
    {
        this.testName = testName;
        baseTestName = testName;
        rawName = testName;
        formatTestName();

    }

    /**
     * Gets the test name.
     *
     * @return the test name
     */
    public String getTestName()
    {
        return testName;
    }

    public void setTestName( String testName )
    {
        this.testName = testName;
        rawName = testName;
        baseTestName = testName;
        formatTestName();
    }

    private void formatTestName()
    {
        if ( testName == null )
            return;
        String useTest = baseTestName;

        

        if ( contentKey != null && !contentKey.isEmpty() )
            useTest = useTest + " {" + contentKey + "}";

        if ( testContext != null && !testContext.isEmpty() )
            useTest = useTest + " [" + testContext + "]";
        
        if ( dataDriven != null  )
            useTest = useTest + " (" + dataDriven.getName() + ")";
        
        if ( iteration> 0 )
            useTest = useTest + " #" + iteration;
        
        if ( personaName != null && !personaName.isEmpty() )
            useTest = useTest + " using " + personaName;

        this.testName = useTest;
        
        if ( test != null )
            test.setTestName( testName );
    }

    public String getKeyName()
    {
        return testName + (personaName != null && !personaName.isEmpty() ? "." + personaName : "");
    }
}
