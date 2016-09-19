package org.xframium.editor.http;

import java.awt.Desktop;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.editor.http.handler.spi.AnalyzePageData;
import org.xframium.editor.http.handler.spi.OpenEditor;
import org.xframium.editor.http.handler.spi.OpenFile;
import org.xframium.editor.http.handler.spi.OpenSuite;
import org.xframium.editor.http.handler.spi.SupportedBYTypes;
import org.xframium.editor.http.handler.spi.SupportedSteps;
import org.xframium.editor.initialization.Initializer;
import com.sun.net.httpserver.HttpServer;



@SuppressWarnings("restriction")
public class RESTServer 
{

	private HttpServer httpServer;

    private Log log = LogFactory.getLog(RESTServer.class);
    
    public static void main(String[] args) 
    {
        try
        {
            new RESTServer().startUp( InetAddress.getLocalHost().getHostAddress(), 8123 );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
	}
    
    public RESTServer() {
		Initializer.instance().initialize();
	}
    
    public void startUp( String ipAddress, int portNumber )
    {
        try
        {
            if ( httpServer != null )
                httpServer.stop( 0 );
            
            httpServer = HttpServer.create( new InetSocketAddress( ipAddress, portNumber ), 1000 );
            httpServer.createContext( "/steps/supported", new SupportedSteps() );
            httpServer.createContext( "/or/supported", new SupportedBYTypes() );
            httpServer.createContext( "/pageData/open", new AnalyzePageData() );
            httpServer.createContext( "/suite/open", new OpenSuite() );
            httpServer.createContext( "/editor", new OpenEditor() );
            httpServer.createContext( "/js", new OpenFile() );
            httpServer.createContext( "/css", new OpenFile() );
            httpServer.start();
            
            Thread.sleep( 2000 );
            
            Desktop.getDesktop().browse( new URI( "http://" + ipAddress + ":" + portNumber + "/editor") );

        }
        catch ( Exception e )
        {
            log.error( "Error starting DEBUGGER", e );
        }
    }

    public void shutDown()
    {
        httpServer.stop( 0 );
    }
}
