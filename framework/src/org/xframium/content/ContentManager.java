/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.content;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.content.provider.ContentProvider;

// TODO: Auto-generated Javadoc
/**
 * The singleton containing all loaded page data records.
 */
public class ContentManager
{
	
    /** The Constant OB. */
    private static final String OB = "{";
	
    /** The Constant CB. */
    private static final String CB = "}";
	
    /** The Constant PS. */
    private static final String PS = "#";
	
    /** The content pattern. */
    private static Pattern CONTENT_PATTERN = Pattern.compile( "(\\S*)\\{?(#)?(\\w*)?\\}?" );

    /** The content key holder. */
    private static ThreadLocal<String> CONTENT_KEY_HOLDER = new ThreadLocal<String>();
	
    /** The singleton. */
    private static ContentManager singleton = new ContentManager();
	
    /** The content map. */
    private Map<String,ContentData> contentMap = new HashMap<String,ContentData>( 100 );
	
    /** The matrix positions. */
    private Map<String,Integer> matrixPositions = new HashMap<String,Integer> ();
	
    /** The matrix data. */
    private Map<Integer,String> matrixData = new HashMap<Integer,String> ();
	
    /** The log. */
    private Log log = LogFactory.getLog( ContentManager.class );
	
    /**
     * Instance.
     *
     * @return the page data manager
     */
    public static ContentManager instance()
    {
        return singleton;
    }

    /**
     * Instantiates a new content manager.
     */
    private ContentManager() {}
    
    /** The content provider. */
    private ContentProvider contentProvider;
    
    /**
     * Sets the page data provider.
     *
     * @param contentProvider the new content provider
     */
    public void setContentProvider( ContentProvider contentProvider )
    {
    	this.contentProvider = contentProvider;
    	contentProvider.readContent();
    }
    
    /**
     * Gets the content.
     *
     * @param contentName the content name
     * @return the content
     */
    public ContentData getContent( String contentName )
    {
    	return contentMap.get( contentName );
    }
    
    /**
     * Gets the content value.  This will parse the key to extract the Content Data element
     * and then using the key extension it will get the actual value.  The key is formatted 
     * using the key name along with wavy brackets indicating the position or language if specified
     *
     * @param contentKey the content key
     * @return the content value
     */
    public String getContentValue( String contentKey )
    {
    	
    	if ( log.isInfoEnabled() )
    		log.info( "Extracting Content for [" + contentKey + "]" );
    	String contentName = null;
    	String keyName = null;
    	int indexNumber = -1;
    	
    	if ( contentKey.indexOf( OB ) > 0 )
    	{
    		contentName = contentKey.substring( 0, contentKey.indexOf( OB ) );
    		
    		if ( contentKey.indexOf( PS ) > 0 )
    			indexNumber = Integer.parseInt( contentKey.substring( contentKey.indexOf( PS ) + 1, contentKey.length() - 1 ) );
    		else
    			keyName = contentKey.substring( contentKey.indexOf( OB ) + 1,contentKey.length() - 1 );
    	}
    	else
    		contentName = contentKey;
    	    	
    	ContentData contentData = contentMap.get( contentName );

        if (( keyName == null ) &&
            ( CONTENT_KEY_HOLDER.get() != null ) &&
            ( CONTENT_KEY_HOLDER.get().length() > 0 ))
        {
            keyName = CONTENT_KEY_HOLDER.get();
        }
    	
    	if ( contentData != null )
    	{
    		if ( keyName != null )
    			return contentData.getValue( keyName );
    		
    		if ( indexNumber >= 0 )
    			return contentData.getValue( indexNumber );
    		
    		return contentData.getValue();
    	}
    	else
    	{
    	    log.error( "No content could be found for [" + contentName + "]" );
    	}
    	
    	return null;
    }
    
    
    /**
     * Sets the matrix data.
     *
     * @param matrixEntries the new matrix data
     */
    public void setMatrixData( String[] matrixEntries )
    {
    	for ( int i=0; i<matrixEntries.length; i++ )
    	{
    		matrixPositions.put( matrixEntries[ i ], i );
    		matrixData.put( i, matrixEntries[ i ] );
    	}
    }
    
    /**
     * Gets the matrix value.
     *
     * @param matrixPosition the matrix position
     * @return the matrix value
     */
    public String getMatrixValue( int matrixPosition )
    {
    	return matrixData.get( matrixPosition );
    }
    
    /**
     * Gets the matrix position.
     *
     * @param matrixValue the matrix value
     * @return the matrix position
     */
    public Integer getMatrixPosition (String matrixValue )
    {
    	return matrixPositions.get( matrixValue );
    }
    
    /**
     * Adds the content data.
     *
     * @param contentData the content data
     */
    public void addContentData( ContentData contentData )
    {
    	if ( contentData != null && contentData.getName() != null && contentData.getValue() != null )
    	{
    		if ( log.isDebugEnabled() )
    			log.debug( "Adding content for [" + contentData.getName() + "] as [" + contentData + "]" );
    		
    		contentMap.put( contentData.getName(), contentData );
    	}
    }

    /**
     * Set the current content key.
     *
     * @param contentKey the content key
     */
    public void setCurrentContentKey( String key )
    {
        CONTENT_KEY_HOLDER.set( key );
    }

    /**
     * Get the current content key.
     */
    public String getCurrentContentKey()
    {
        return CONTENT_KEY_HOLDER.get();
    }
}
