package org.xframium.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.exception.XFramiumException;
import org.xframium.exception.XFramiumException.ExceptionType;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.spi.Device;

public class ExecutionContextTest
{
    public enum TestStatus
    {
        PASSED,
        FAILED,
        SKIPPED;
    }
    
    
    private ExceptionType exceptionType;
    private KeyWordTest test;
    private CloudDescriptor cloudDescriptor;
    private Device device;
    private Date startTime = new Date( System.currentTimeMillis() );
    private Date endTime;
    private List<ExecutionContextStep> stepList = new ArrayList<ExecutionContextStep>( 10 ); 
    private ExecutionContextStep currentStep;
    private Throwable testException; 
    private TestStatus testStatus;
    private String sessionId;
    private Map<String,String> executionParameters = new HashMap<String,String>( 20 );
    private Map<String,PageData> dataMap = null;
    private Map<String,Page> pageMap = null;
    private Map<String,Object> contextMap = null;
    private String message;
    private String folderName;
    private Map<String,String> sPMap = new HashMap<String,String>( 10 );
    
    public Map<String,Object> toMap()
    {
        Map<String,Object> asMap = new HashMap<String,Object>( 20 );
        
        asMap.put( "exceptionType", exceptionType );
        asMap.put( "name", test.getName() );
        asMap.put( "startTime", startTime );
        asMap.put( "endTime", endTime );
        asMap.put( "testStatus", testStatus );
        asMap.put( "passed", getStepCount( StepStatus.SUCCESS ) );
        asMap.put( "failed", getStepCount( StepStatus.FAILURE ) );
        asMap.put( "ignored", getStepCount( StepStatus.FAILURE_IGNORED ) );
        asMap.put( "total", getStepCount( null ) );
        asMap.put( "cloud", cloudDescriptor );
        asMap.put( "device", device );
        asMap.put( "sessionId", sessionId );
        asMap.put( "folderName", folderName );
        return asMap;
    }
    
    public void popupateSystemProperties()
    {
        sPMap.clear();
        Properties sP = System.getProperties();
        for ( Object key : sP.keySet() )
        {
            sPMap.put( (String) key, sP.getProperty( (String)key ) ); 
        }
    }

    public String getFolderName()
    {
        return folderName;
    }

    public void setFolderName( String folderName )
    {
        this.folderName = folderName;
    }

    public void addExecutionParameter( String name, String value )
    {
        executionParameters.put( name,  value );
    }
    
    public ExecutionContextStep getStep()
    {
        return currentStep;
    }
    
    public Map<String, PageData> getDataMap()
    {
        return dataMap;
    }



    public void setDataMap( Map<String, PageData> dataMap )
    {
        this.dataMap = dataMap;
    }



    public Map<String, Page> getPageMap()
    {
        return pageMap;
    }



    public void setPageMap( Map<String, Page> pageMap )
    {
        this.pageMap = pageMap;
    }



    public Map<String, Object> getContextMap()
    {
        return contextMap;
    }



    public void setContextMap( Map<String, Object> contextMap )
    {
        this.contextMap = contextMap;
    }



    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId( String sessionId )
    {
        this.sessionId = sessionId;
    }

    public Device getDevice()
    {
        return device;
    }

    public void setDevice( Device device )
    {
        this.device = device;
    }

    public Throwable getTestException()
    {
        return testException;
    }

    public void setTestException( Throwable testException )
    {
        this.testException = testException;
    }

    public TestStatus getTestStatus()
    {
        return testStatus;
    }

    public void setTestStatus( TestStatus testStatus )
    {
        this.testStatus = testStatus;
    }

    public CloudDescriptor getCloud()
    {
        return cloudDescriptor;
    }

    public void setCloud( CloudDescriptor cloudDescriptor )
    {
        this.cloudDescriptor = cloudDescriptor;
    }

    public KeyWordTest getTest()
    {
        return test;
    }

    public void setTest( KeyWordTest test )
    {
        this.test = test;
    }
    
    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime( long startTime )
    {
        this.startTime = new Date( startTime );
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime( long endTime )
    {
        this.endTime = new Date( endTime );
    }

    public List<ExecutionContextStep> getStepList()
    {
        return stepList;
    }

    public void setStepList( List<ExecutionContextStep> stepList )
    {
        this.stepList = stepList;
    }

    public void startStep( KeyWordStep step, Map<String, Object> contextMap, Map<String, PageData> dataMap )
    {
        if ( currentStep == null )
        {
            currentStep = new ExecutionContextStep( null );
            currentStep.setStep( step );
            stepList.add( currentStep );
        }
        else
        {
            ExecutionContextStep subStep = new ExecutionContextStep( currentStep );
            subStep.setStep( step );
            currentStep.getStepList().add( subStep );
            currentStep = subStep;
        }
        
        for ( KeyWordParameter p : step.getParameterList() )
        {
            currentStep.addParameterValue( step.getParameterValue( p, contextMap, dataMap ) + "" );
        }
    }
    
    public String getMessage()
    {
        return message;
    }



    public void setMessage( String message )
    {
        this.message = message;
    }

    public int getStepCount( StepStatus stepStatus )
    {
        int stepCount = 0;
        
        for ( ExecutionContextStep s : stepList )
            stepCount += s.getStepCount( stepStatus );
        
        return stepCount;
    }

    public void completeStep( StepStatus stepStatus, Throwable throwable )
    {
        currentStep.setEndTime( System.currentTimeMillis() );
        currentStep.setStepStatus( stepStatus );
        currentStep.setThrowable( throwable );
        if ( currentStep.getParentStep() != null )
            currentStep = currentStep.getParentStep();
        else
            currentStep = null;
    }
    
    public void completeTest( TestStatus testStatus, Throwable testException )
    {
        if ( stepList == null || stepList.size() == 0 )
        {
            startStep( new SyntheticStep( test.getName(), "TEST" ), contextMap, dataMap );
            completeStep( StepStatus.FAILURE, testException );
        }
        
        
        endTime = new Date( System.currentTimeMillis() );
        this.testStatus = testStatus;
        if ( testException != null )
            message = testException.getMessage();
        this.testException = testException;
        if ( testStatus.equals( TestStatus.FAILED ) )
            exceptionType = _getExceptionType();
        
    }
    
    public boolean getStatus()
    {
        for ( ExecutionContextStep eS : stepList )
        {
            if ( !eS.getStatus() )
                return false;
        }
        
        return true;
    }
    
    public ExceptionType getExceptionType()
    {
        return exceptionType;
    }
    
    private ExceptionType _getExceptionType()
    {
        for ( ExecutionContextStep eS : stepList )
        {
            if ( !eS.getStatus() )
                return eS.getExceptionType();
        }
        
        if ( testException != null && testException instanceof XFramiumException )
            return ( (XFramiumException) testException ).getType();
        
        return null;
    }

    public Map<String, String> getExecutionParameters()
    {
        return executionParameters;
    }
    
    
}
