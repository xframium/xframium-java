package org.xframium.console.http.handler.spi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import org.xframium.console.http.ExecutionConsole;
import org.xframium.console.http.handler.ECHandler;
import org.xframium.container.DriverContainer;
import org.xframium.container.SuiteContainer;
import org.xframium.driver.ConfigurationReader;
import org.xframium.driver.TXTConfigurationReader;
import org.xframium.driver.XMLConfigurationReader;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.element.provider.ElementProvider;
import com.sun.net.httpserver.HttpExchange;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings("restriction")
public class OpenSuite extends ECHandler
{
    private Preferences configPreferences = Preferences.userNodeForPackage( ExecutionConsole.class );
    
    @Override
	protected byte[] _handle(HttpExchange httpExchange) 
	{	
	    try
	    {
	        Map<String,String> queryMap = queryToMap( httpExchange.getRequestURI().getQuery() );
	        
    		String fileName = queryMap.get( "suiteName" );
    		
    		
    		Map <String,Object> returnMap = new HashMap<String,Object>( 10 );
    		
    		
    		ConfigurationReader cR = null;

    		if ( fileName.toLowerCase().endsWith( ".xml" ) )
    		    cR = new XMLConfigurationReader();
    		else if ( fileName.toLowerCase().endsWith( ".txt" ) )
    		    cR = new TXTConfigurationReader();
    		
    		cR.readFile( new File( fileName ) );
    		
    		if ( !fileName.equals( configPreferences.get( "recentOne", null ) ) && !fileName.equals( configPreferences.get( "recentTwo", null ) ) && !fileName.equals( configPreferences.get( "recentThree", null ) ) )
    		{
    		    if ( configPreferences.get( "recentTwo", null ) != null )
                    configPreferences.put( "recentThree", configPreferences.get( "recentTwo", null ) );
                if ( configPreferences.get( "recentOne", null ) != null )
                    configPreferences.put( "recentTwo", configPreferences.get( "recentOne", null ) );
                
                configPreferences.put( "recentOne", fileName );
        		}

    		PageDataProvider pdp = cR.configureData();
    		if ( pdp != null )
    		    pdp.readPageData();
    		returnMap.put( "eC", pdp );    
    		SuiteContainer sC = cR.configureTestCases( pdp, false );
    		returnMap.put( "sC", sC );
    		DriverContainer gC = cR.configureDriver( null );
            cR.configureArtifacts( gC );
            returnMap.put( "gC", gC );
    		returnMap.put( "cC", cR.configureCloud( gC.isSecureCloud() ) );
    		ElementProvider eP = cR.configurePageManagement( sC );
    		returnMap.put( "pC", eP );
    		returnMap.put( "aC", cR.configureApplication() );
    		returnMap.put( "fC", cR.configureFavorites() );
    		cR.configureContent();
    		
    		try
    		{
    		    returnMap.put( "dC", cR.configureDevice() );
    		}
    		catch( Exception e )
    		{
    		    e.printStackTrace();
    		}

    		return SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), returnMap, 0 );
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	        return e.getMessage().getBytes();
	    }
	}
}
