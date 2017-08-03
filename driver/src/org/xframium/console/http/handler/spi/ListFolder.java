package org.xframium.console.http.handler.spi;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import org.xframium.console.http.ExecutionConsole;
import org.xframium.console.http.handler.ECHandler;
import org.xframium.container.FileContainer;
import com.sun.net.httpserver.HttpExchange;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings("restriction")
public class ListFolder extends ECHandler {

    private Preferences configPreferences = Preferences.userNodeForPackage( ExecutionConsole.class );

    
	@Override
	protected byte[] _handle(HttpExchange httpExchange) 
	{
	    try
	    {
    	    Map<String,String> queryMap = queryToMap( httpExchange.getRequestURI().getQuery() );
    	    
    	    String folderName = queryMap.get( "folderName" );
    	    String folderDelta = queryMap.get( "folderDelta" );
    	    File currentFile = new File( "" );
    	    
    	    if ( queryMap.get( "folderName" ) != null )
    	    {
    	        if ( folderDelta.equals( ".." ) )
    	            currentFile = new File( folderName ).getParentFile();
    	        else
    	        {
    	            if ( folderName.isEmpty() )
    	                currentFile = new File( folderDelta );
    	            else
    	                currentFile = new File( folderName, folderDelta );
    	        }
    	    }
    	    
    	    Map<String,Object> fileContainer = new HashMap<String,Object>( 10 );
    	    
    	    System.out.println( currentFile.getAbsolutePath() );
    	    if ( currentFile.isFile() )
    	        fileContainer.put( "fileName", currentFile.getAbsolutePath() );
    	    else
    	    {
    	        
    	        File[] fileList = new File( new URI( "file:/" + currentFile.getAbsolutePath().replaceAll( " ", "%20" ).replaceAll( "\\\\", "/" ) ) ).listFiles();
    	        List<FileContainer> fullList = new ArrayList<FileContainer>( 20 );
    	        if ( fullList != null )
    	        {
        	        for ( File file : fileList )
        	            fullList.add( new FileContainer( file ) );
        	        fileContainer.put( "folderList", fullList );
    	        }
    	    }
    	    
    	    List<String> recentList = new ArrayList<String>( 3 );
    	    if ( configPreferences.get( "recentOne", null ) != null )
    	        recentList.add( configPreferences.get( "recentOne", null ) );
    	    if ( configPreferences.get( "recentTwo", null ) != null )
                recentList.add( configPreferences.get( "recentTwo", null ) );
    	    if ( configPreferences.get( "recentThree", null ) != null )
                recentList.add( configPreferences.get( "recentThree", null ) );
    	    
    	    fileContainer.put( "folderName", currentFile.getAbsolutePath() );
    	    fileContainer.put( "recent", recentList );
    
    	    return SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), fileContainer, 0 );
	    }
        catch( Exception e )
	    {
            e.printStackTrace();
            return e.getMessage().getBytes();
	    }
	}
}
