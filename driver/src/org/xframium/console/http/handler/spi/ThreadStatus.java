package org.xframium.console.http.handler.spi;

import java.util.ArrayList;
import java.util.List;
import org.xframium.console.http.handler.ECHandler;
import org.xframium.container.ThreadContainer;
import com.sun.net.httpserver.HttpExchange;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings("restriction")
public class ThreadStatus extends ECHandler
{

	@Override
	protected byte[] _handle(HttpExchange httpExchange) 
	{	
	    try
	    {
	        return SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), getThreads(), 0 );
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	        return e.getMessage().getBytes();
	    }
	}
	
	public List<ThreadContainer> getThreads()
    {
        List<ThreadContainer> tList = new ArrayList<ThreadContainer>( 10 );
        ThreadGroup rootGroup = Thread.currentThread( ).getThreadGroup( );
        ThreadGroup parentGroup;
        while ( ( parentGroup = rootGroup.getParent() ) != null ) 
            rootGroup = parentGroup;
        
        Thread[] threadList = new Thread[200];
        rootGroup.enumerate( threadList, true );
        
        for ( Thread t : threadList )
        {
            if ( t == null )
                break;
            
            if ( t.getName().startsWith( "xF-" ) )
                tList.add(  new ThreadContainer( t ) );
        }
        
        return tList;
        
    }
	
}
