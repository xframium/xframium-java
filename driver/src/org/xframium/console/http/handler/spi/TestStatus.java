package org.xframium.console.http.handler.spi;

import java.util.HashMap;
import java.util.Map;
import org.xframium.console.http.ExecutionConsole;
import org.xframium.console.http.handler.ECHandler;
import com.sun.net.httpserver.HttpExchange;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings("restriction")
public class TestStatus extends ECHandler
{

	@Override
	protected byte[] _handle(HttpExchange httpExchange) 
	{	
	    try
	    {
	        Map<String,Object> returnMap = new HashMap<String,Object>( 10 );
	        returnMap.put( "test", ExecutionConsole.instance().drainSync() );
	        returnMap.put( "status", ExecutionConsole.instance().isTestExecuting() );
    		
	        return SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), returnMap, 0 );
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	        return e.getMessage().getBytes();
	    }
	}
}
