package org.xframium.container;

import java.util.ArrayList;
import java.util.List;

public class ThreadContainer
{
    private String description;
    private String threadState;
    private int priority;
    private List<String> stackTrace = new ArrayList<String>( 10 );
    
    public ThreadContainer( Thread currentThread )
    {
        description = currentThread.getName().substring( 3 );
        threadState = currentThread.getState().name();
        priority = currentThread.getPriority();
        
        StackTraceElement[] s = currentThread.getStackTrace();
        for ( StackTraceElement e : s )
            stackTrace.add( e.toString() );
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getThreadState()
    {
        return threadState;
    }

    public void setThreadState( String threadState )
    {
        this.threadState = threadState;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority( int priority )
    {
        this.priority = priority;
    }

    public List<String> getStackTrace()
    {
        return stackTrace;
    }

    public void setStackTrace( List<String> stackTrace )
    {
        this.stackTrace = stackTrace;
    }
}
