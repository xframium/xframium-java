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
package com.xframium.content.provider;

import java.util.*;
import com.xframium.content.ContentData;
import com.xframium.content.ContentManager;
import com.xframium.content.DefaultContentData;
import com.xframium.utility.SQLUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLContentProvider.
 */
public class SQLContentProvider extends AbstractContentProvider
{
    //
    // class data
    //

    private static final String DEF_QUERY =
        "SELECT KEY_NAME, VALUE \n" +
        "FROM PERFECTO_CONTENT \n" +
        "ORDER BY KEY_NAME, OFFSET";

    private static final String[] STR_ARR = new String[0];

    //
    // instance data
    //
	
    /** The username. */
    private String username;
	
    /** The password. */
    private String password;
	
    /** The JDBC URL. */
    private String url;
	
    /** The driver class name. */
    private String driver;
	
    /** The query. */
    private String query;
	
	
    /**
     * Instantiates a new SQL contect provider.
     *
     * @param username the user name
     * @param password the password
     * @param url the JDBC URL
     * @param driver the driver class name
     */
    public SQLContentProvider( String username, String password, String url, String driver )
    {
        this.username = username;
        this.password = password;
        this.url = url;
        this.driver = driver;
        this.query = DEF_QUERY;
    }
	
     /**
     * Instantiates a new SQL contect provider.
     *
     * @param username the user name
     * @param password the password
     * @param url the JDBC URL
     * @param driver the driver class name
     */
    public SQLContentProvider( String username, String password, String url, String driver, String query )
    {
        this( username, password, url, driver );

        this.query = (( query != null ) ? query : DEF_QUERY );;
    }
	
    /* (non-Javadoc)
     * @see com.perfectoMobile.content.provider.ContentProvider#readContent()
     */
    public void readContent()
    {
        try
        {
            Object[][] data = SQLUtil.getResults( username, password, url, driver, query, null );

            HashMap contentMap = new HashMap();

            for( int i = 0; i < data.length; ++i )
            {
                String key = (String) data[i][0];
                String value = (String) data[i][1];

                List valset = (List) contentMap.get( key );
                if ( valset == null )
                {
                    valset = new ArrayList();
                    contentMap.put( key, valset );
                }
                valset.add( value );
            }

            Iterator keys = contentMap.keySet().iterator();
            while( keys.hasNext() )
            {
                String key = (String) keys.next();
                List valset = (List) contentMap.get( key );

                ContentData contentData = new DefaultContentData( key, (String[]) valset.toArray(STR_ARR) );
				
                ContentManager.instance().addContentData( contentData );
            }
        }
        catch( Exception e )
        {
            log.fatal( "Error reading content: ", e );
        }
    }
	
}
