package org.xframium.device.ng;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RunContainer
{
    private Log log = LogFactory.getLog( RunContainer.class );
    
    public enum RunStatus
    {
        RUNNING,
        COMPLETED,
        FAILED,
        CHECK,
        RESET;
    }
    
    private Map<String,RunStatus> runMap = new HashMap<String,RunStatus>( 20 );
    
    public void clear()
    {
        runMap.clear();
    }
    
    public synchronized boolean addRun( String runKey, RunStatus runStatus )
    {
        if (runStatus.equals( RunStatus.RESET ) )
        {
            //if( log.isInfoEnabled() )
                log.warn( Thread.currentThread().getName() + ":Issued RESET on " + runKey );
            runMap.remove( runKey );
            return true;
        }
        
        RunStatus currentStatus = runMap.get( runKey );
        
        if ( runStatus.equals( RunStatus.CHECK ) )
        {
            //if( log.isInfoEnabled() )
                log.warn( Thread.currentThread().getName() + ":CHECKING on " + runKey + " - " + (currentStatus == null) );
            return currentStatus == null;
        }
        
        if ( currentStatus == null )
        {
            log.warn( Thread.currentThread().getName() + ":ADDING " + runKey  );
            runMap.put( runKey, runStatus );
            return true;
        }
        
        switch( runStatus )
        {
            case COMPLETED:
                if ( currentStatus.equals( RunStatus.RUNNING ) )
                {
                    //if( log.isInfoEnabled() )
                        log.warn(runStatus + " on " + runKey );
                    runMap.put( runKey, runStatus);
                    return true;
                }
                else
                    return false;
            case FAILED:
                if ( currentStatus.equals( RunStatus.RUNNING ) )
                {
                    //if( log.isInfoEnabled() )
                        log.warn(runStatus + " on " + runKey );
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
