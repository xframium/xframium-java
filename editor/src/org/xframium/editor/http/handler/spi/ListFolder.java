package org.xframium.editor.http.handler.spi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xframium.driver.container.FileContainer;
import org.xframium.editor.http.handler.XOLHandler;
import com.sun.net.httpserver.HttpExchange;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings("restriction")
public class ListFolder extends XOLHandler {

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
    	            currentFile = new File( folderName, folderDelta );
    	    }
    	    
    	    Map<String,Object> fileContainer = new HashMap<String,Object>( 10 );
    	    
    	    if ( currentFile.isFile() )
    	        fileContainer.put( "fileName", currentFile.getAbsolutePath() );
    	    else
    	    {
    	        File[] fileList = currentFile.getAbsoluteFile().listFiles();
    	        List<FileContainer> fullList = new ArrayList<FileContainer>( 20 );
    	        if ( fullList != null )
    	        {
        	        for ( File file : fileList )
        	            fullList.add( new FileContainer( file ) );
        	        fileContainer.put( "folderList", fullList );
    	        }
    	    }
    	    
    	    fileContainer.put( "folderName", currentFile.getAbsolutePath() );
    
    	    
    	    
    		return SerializationManager.instance().toByteArray(fileContainer);
	    }
        catch( Exception e )
	    {
            e.printStackTrace();
            return e.getMessage().getBytes();
	    }
	}
}
