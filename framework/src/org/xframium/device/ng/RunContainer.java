package org.xframium.device.ng;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RunContainer
{
    private Log log = LogFactory.getLog( RunContainer.class );
    private Log testFlow = LogFactory.getLog( "testFlow" );
    private int retryCount;
    
    public enum RunStatus
    {
        RUNNING,
        COMPLETED,
        FAILED,
        CHECK,
        RESET,
        RETRY;
    }
    
    private Map<String,RunStatus> runMap = new HashMap<String,RunStatus>( 20 );
    
    
    public void clear()
    {
        runMap.clear();
    }
    
    public synchronized boolean addRun( String runKey, RunStatus runStatus, String reliesOn )
    {
        if (runStatus.equals( RunStatus.RESET ) )
        {
            testFlow.warn( Thread.currentThread().getName() + ":Issued RESET on " + runKey );
            runMap.remove( runKey );
            return true;
        }
        
        RunStatus currentStatus = runMap.get( runKey );
        
        if ( runStatus.equals( RunStatus.CHECK ) )
        {
            testFlow.warn( Thread.currentThread().getName() + ":CHECKING on " + runKey + " - " + (currentStatus == null) );
            return currentStatus == null;
        }
        
        if ( currentStatus == null )
        {
            if ( reliesOn != null )
            {
                RunStatus reliesOnStatus = runMap.get( reliesOn );
                if ( reliesOnStatus != null )
                {
                    if ( reliesOnStatus.equals( RunStatus.COMPLETED ) )
                    {
                        testFlow.warn( Thread.currentThread().getName() + ":ADDING " + runKey  );
                        runMap.put( runKey, runStatus );
                        return true;
                    }
                    else
                    {
                        testFlow.debug( Thread.currentThread().getName() + ":Issued RELYING RESET on " + runKey );
                        runMap.remove( runKey );
                        return false;
                    }
                    
                }
                else
                {
                    testFlow.debug( Thread.currentThread().getName() + ":Issued RELYING RESET on " + runKey );
                    runMap.remove( runKey );
                    return false;
                }
            }
            else
            {
                testFlow.warn( Thread.currentThread().getName() + ":ADDING " + runKey  );
                runMap.put( runKey, runStatus );
                return true;
            }
        }
        
        switch( runStatus )
        {
            case COMPLETED:
                if ( currentStatus.equals( RunStatus.RUNNING ) )
                {
                    testFlow.warn(runStatus + " on " + runKey );
                    runMap.put( runKey, runStatus);
                    return true;
                }
                else
                    return false;
            case FAILED:
                if ( currentStatus.equals( RunStatus.RUNNING ) )
                {

                    testFlow.warn(runStatus + " on " + runKey );
                    runMap.put( runKey, runStatus);
                    return true;
                }
                else
                    return false;
            case RUNNING:
                return false;
                
            default:
                return false;
        }
        
    }
    
}
