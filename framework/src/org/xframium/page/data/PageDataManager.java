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
package org.xframium.page.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.data.provider.XMLPageDataProvider;
import org.xframium.page.keyWord.KeyWordDriver;

// TODO: Auto-generated Javadoc
/**
 * The singleton containing all loaded page data records.
 */
public class PageDataManager
{
    
	/** The singleton. */
	private static Map<String,PageDataManager> singleton = new HashMap<String,PageDataManager>(5);

    /**
     * Instance.
     *
     * @return the page data manager
     */
    public static PageDataManager instance( String xFID )
    {
        if ( singleton.containsKey( xFID ) )
            return singleton.get( xFID );
        else
        {
            singleton.put( xFID, new PageDataManager() );
            return singleton.get( xFID );
        }
    }

    /**
     * Instantiates a new page data manager.
     */
    private PageDataManager() {}
    
    /** The data provider. */
    private PageDataProvider dataProvider;
    
    
    
    /**
     * Sets the page data provider.
     *
     * @param dataProvider the new page data provider
     */
    public void setPageDataProvider( PageDataProvider dataProvider )
    {
    	this.dataProvider = dataProvider;
    	if ( dataProvider != null )
    	    dataProvider.readPageData();
    }
    
    
    
    public PageDataProvider getDataProvider()
    {
        return dataProvider;
    }

    /**
     * Gets the records.
     *
     * @param recordType the record type
     * @return the records
     */
    public PageData[] getRecords( String recordType )
    {
    	return dataProvider.getRecords( recordType );
    }
    
    /**
     * Gets the record i ds.
     *
     * @param recordType the record type
     * @return the record i ds
     */
    public String[] getRecordIDs( String recordType )
    {
    	PageData[] records = getRecords( recordType );
    	String[] ids = new String[ records.length ];
    	
    	for ( int i=0; i<records.length; i++ )
    		ids[ i ] = records[ i ].getName();
    	
    	return ids;
    }
    
    /**
     * Gets the page data.
     *
     * @param recordType the record type
     * @return the page data
     */
    public PageData getPageData( String recordType )
    {
    	return dataProvider.getRecord( recordType );
    }
    
    /**
     * Gets the page data.
     *
     * @param recordType the record type
     * @param recordId the record id
     * @return the page data
     */
    public PageData getPageData( String recordType, String recordId )
    {
    	return dataProvider.getRecord( recordType, recordId );
    }
    
    /**
     * Put page data.
     *
     * @param pageData the page data
     */
    public void putPageData( PageData pageData )
    {
    	dataProvider.putRecord( pageData );
    }

}
