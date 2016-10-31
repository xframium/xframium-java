package org.xframium.debugger.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.xframium.debugger.DebugManager;
import org.xframium.debugger.TestContainer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings ( "restriction")
public class SnapshotHandler implements HttpHandler
{
    @Override
    public void handle( HttpExchange t ) throws IOException
    {
        Map<String, String> parameterMap = queryToMap( t.getRequestURI().getQuery() );
        TestContainer tC = DebugManager.instance().getActiveTests().get( parameterMap.get( "executionId" ) );

        String fileName = parameterMap.get( "fileName" );

        byte[] screenShot = null;

        if ( fileName != null && !fileName.isEmpty() )
        {
            File inputFile = new File( fileName );
            if ( inputFile.exists() )
            {
                FileInputStream is = null;
                try
                {
                    is = new FileInputStream( inputFile );
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[512];
                    int bytesRead = 0;

                    while ( (bytesRead = is.read( buffer )) > 0 )
                    {
                        bos.write( buffer, 0, bytesRead );
                    }
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {
                        is.close();
                    }
                    catch ( Exception e )
                    {
                    }
                }
            }
        }

        if ( screenShot == null )
        {
            if ( tC.getWebDriver() instanceof TakesScreenshot )
            {

                try
                {
                    screenShot = ((TakesScreenshot) tC.getWebDriver()).getScreenshotAs( OutputType.BYTES );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }

            }
        }

        try
        {
            screenShot = Base64.encodeBase64( screenShot );
            t.sendResponseHeaders( 200, screenShot.length );
            OutputStream os = t.getResponseBody();
            os.write( screenShot );
            os.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public Map<String, String> queryToMap( String query )
    {
        Map<String, String> result = new HashMap<String, String>();
        for ( String param : query.split( "&" ) )
        {
            String pair[] = param.split( "=" );
            if ( pair.length > 1 )
            {
                result.put( pair[0], pair[1] );
            }
            else
            {
                result.put( pair[0], "" );
            }
        }
        return result;
    }
}
