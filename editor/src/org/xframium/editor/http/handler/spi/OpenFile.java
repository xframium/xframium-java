package org.xframium.editor.http.handler.spi;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.xframium.editor.http.handler.XOLHandler;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class OpenFile extends XOLHandler
{

	@Override
	protected byte[] _handle(HttpExchange httpExchange) 
	{	
	    try
	    {
    		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    		
    		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream( "org/xframium/editor/html" + httpExchange.getRequestURI().toString() );
    		
    		byte[] buffer = new byte[512];
    		int bytesRead = 0;
    		
    		while ( (bytesRead = inputStream.read( buffer ) ) > 0 )
    		    outputStream.write( buffer, 0, bytesRead );
    		
    		inputStream.close();
    		
    		return outputStream.toByteArray();
    		
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	        return e.getMessage().getBytes();
	    }
	}
}
