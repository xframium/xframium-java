package org.xframium.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xframium.exception.XFramiumException;
import org.xframium.exception.XFramiumException.ExceptionType;
import org.xframium.page.StepStatus;
import org.xframium.page.keyWord.KeyWordStep;

public class ExecutionContextStep
{
    private KeyWordStep step;
    private Date startTime = new Date(System.currentTimeMillis());
    private Date endTime;
    private List<ExecutionContextStep> stepList = new ArrayList<ExecutionContextStep>( 10 );
    private transient ExecutionContextStep parentStep;
    private StepStatus stepStatus;
    private Throwable throwable;
    private String message;
    private Map<String,String> executionParameter = new HashMap<String,String>( 10 );
    private List<String> parameterList = new ArrayList<String>( 10 );

    public String toString()
    {
        return step.getName();
    }
    
    public void addParameterValue( String value )
    {
        parameterList.add( value );
    }
    
    public int getStepCount( StepStatus stepStatus )
    {
        int stepCount = 0;
        if ( stepStatus == null )
            stepCount++;
        else
        {
            if ( this.stepStatus == stepStatus )
                stepCount++;
        }
        
        for ( ExecutionContextStep s : stepList )
            stepCount += s.getStepCount( stepStatus );
        
        return stepCount;
    }
    
    public void addExecutionParameter( String name, String value )
    {
        executionParameter.put( name,  value );
    }
    
    public Map<String, String> getExecutionParameter()
    {
        return executionParameter;
    }

    public StepStatus getStepStatus()
    {
        return stepStatus;
    }

    public void setStepStatus( StepStatus stepStatus )
    {
        this.stepStatus = stepStatus;
    }

    public Throwable getThrowable()
    {
        return throwable;
    }
    
    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public boolean getStatus()
    {
        if ( stepStatus != null && stepStatus.equals( StepStatus.FAILURE ) )
            return false;
            
        for ( ExecutionContextStep eS : stepList )
        {
            if ( !eS.getStatus() )
                return false;
        }
        
        return true;
    }
    
    public ExceptionType getExceptionType()
    {
        if ( stepStatus != null && stepStatus.equals( StepStatus.FAILURE ) )
        {
            if ( throwable != null && throwable instanceof XFramiumException )
            {
                return ( (XFramiumException) throwable ).getType();
            }
        }
            
        for ( ExecutionContextStep eS : stepList )
        {
            if ( !eS.getStatus() )
            {
                return eS.getExceptionType();
            }
        }
        
        return null;
    }

    public void setThrowable( Throwable throwable )
    {
        if ( throwable != null )
            message = throwable.getMessage();
        this.throwable = throwable;
    }

    public ExecutionContextStep( ExecutionContextStep parentStep )
    {
        this.parentStep = parentStep;
    }

    public ExecutionContextStep getParentStep()
    {
        return parentStep;
    }

    public void setParentStep( ExecutionContextStep parentStep )
    {
        this.parentStep = parentStep;
    }

    public KeyWordStep getStep()
    {
        return step;
    }

    public void setStep( KeyWordStep step )
    {
        this.step = step;
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

}
