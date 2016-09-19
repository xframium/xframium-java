package org.xframium.editor.http.handler.spi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.xframium.driver.ConfigurationReader;
import org.xframium.driver.TXTConfigurationReader;
import org.xframium.driver.XMLConfigurationReader;
import org.xframium.editor.http.handler.XOLHandler;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.keyWord.provider.SuiteContainer;
import com.sun.net.httpserver.HttpExchange;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings("restriction")
public class OpenSuite extends XOLHandler
{

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
    		PageDataProvider pdp = cR.configureData();
    		pdp.readPageData();
    		returnMap.put( "eC", pdp );    
    		SuiteContainer sC = cR.configureTestCases( pdp, false );
    		returnMap.put( "sC", sC );
    		returnMap.put( "cC", cR.configureCloud() );
    		ElementProvider eP = cR.configurePageManagement( sC );
    		returnMap.put( "pC", eP );
    		try
    		{
    		    returnMap.put( "dC", cR.configureDevice() );
    		}
    		catch( Exception e )
    		{
    		    e.printStackTrace();
    		}
    		        
    	
    		
    	    
    		return SerializationManager.instance().toByteArray( returnMap );
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	        return e.getMessage().getBytes();
	    }
	}
}
