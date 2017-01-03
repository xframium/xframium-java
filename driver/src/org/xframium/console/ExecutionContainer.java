package org.xframium.console;

import org.xframium.reporting.ExecutionContextTest.TestStatus;
import org.xframium.spi.Device;

public class ExecutionContainer
{
    private String sessionId;
    private String testName;
    private String fullTestName;
    private Device device;
    private int stepCount;
    private TestStatus testStatus;
    private String folderName;
    private long startTime;
    private long stopTime;
    private String rootFolder;
    
    
    
    public String getRootFolder()
    {
        return rootFolder;
    }

    public void setRootFolder( String rootFolder )
    {
        this.rootFolder = rootFolder;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public void setStartTime( long startTime )
    {
        this.startTime = startTime;
    }

    public long getStopTime()
    {
        return stopTime;
    }

    public void setStopTime( long stopTime )
    {
        this.stopTime = stopTime;
    }

    public String getFolderName()
    {
        return folderName;
    }

    public void setFolderName( String folderName )
    {
        this.folderName = folderName;
    }

    public ExecutionContainer( String sessionId )
    {
        this.sessionId = sessionId;
    }
    
    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId( String sessionId )
    {
        this.sessionId = sessionId;
    }

    public String getTestName()
    {
        return testName;
    }
    public void setTestName( String testName )
    {
        this.testName = testName;
    }
    public String getFullTestName()
    {
        return fullTestName;
    }
    public void setFullTestName( String fullTestName )
    {
        this.fullTestName = fullTestName;
    }
    public Device getDevice()
    {
        return device;
    }
    public void setDevice( Device device )
    {
        this.device = device;
    }
    public int getStepCount()
    {
        return stepCount;
    }
    public void setStepCount( int stepCount )
    {
        this.stepCount = stepCount;
    }
    public TestStatus getTestStatus()
    {
        return testStatus;
    }
    public void setTestStatus( TestStatus testStatus )
    {
        this.testStatus = testStatus;
    }
    
    
}
