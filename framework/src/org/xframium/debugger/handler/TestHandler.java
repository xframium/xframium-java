package org.xframium.debugger.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xframium.debugger.DebugManager;
import org.xframium.debugger.StepContainer;
import org.xframium.debugger.TestContainer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings ( "restriction")
public class TestHandler implements HttpHandler
{
    @Override
    public void handle( HttpExchange t ) throws IOException
    {
        Map<String, String> parameterMap = queryToMap( t.getRequestURI().getQuery() );
        StringBuilder stringBuilder = new StringBuilder();

        TestContainer tC = DebugManager.instance().getActiveTests().get( parameterMap.get( "executionId" ) );

        if ( tC == null )
        {
            tC = DebugManager.instance().getCompletedTests().get( parameterMap.get( "executionId" ) );
        }

        if ( parameterMap.get( "ajax" ) != null )
        {
            if ( parameterMap.get( "newSteps" ) != null )
            {
                List<StepContainer> newList = new ArrayList<StepContainer>();
                
                for ( int i=tC.getStepsSent(); i<tC.getSteps().size(); i++ )
                    newList.add( tC.getSteps().get( i ) );
                
                tC.setStepsSent( tC.getStepsSent() + newList.size() );
                stringBuilder.append( new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), newList, 0 ) ) );
            }
            else
            {
                tC.setStepsSent( tC.getSteps().size() );
                stringBuilder.append( new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), tC, 0 ) ) );
            }
        }
        else
        {
            InputStream inputStream = null;
            try
            {
                int bytesRead = 0;
                byte[] buffer = new byte[ 512 ];
                inputStream = DebugManager.class.getResourceAsStream( "debugger.html" );
                
                while ( ( bytesRead = inputStream.read( buffer ) ) > 0 )
                {
                    stringBuilder.append( new String (buffer, 0, bytesRead ) );
                }
                
                String replaced = stringBuilder.toString().replace( "_EXE_ID_", parameterMap.get( "executionId" ) );
                stringBuilder = new StringBuilder();
                stringBuilder.append( replaced );
                
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
            finally
            {
                try { inputStream.close(); } catch( Exception e ){}
            }
        }
        
        t.sendResponseHeaders( 200, stringBuilder.length() );
        OutputStream os = t.getResponseBody();
        os.write( stringBuilder.toString().getBytes() );
        os.close();

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
