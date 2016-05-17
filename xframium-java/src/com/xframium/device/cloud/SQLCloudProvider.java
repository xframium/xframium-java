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
package com.xframium.device.cloud;

import com.xframium.utility.SQLUtil;

/**
 * The Class SQLCloudProvider.
 */
public class SQLCloudProvider extends AbstractCloudProvider
{
    //
    // class data
    //

    private static final String DEF_QUERY =
        "SELECT NAME, USER_NAME, PASSWORD, HOST_NAME, \n" +
        "       PROXY_HOST, PROXY_PORT, DESCRIPTION, GRID_INSTANCE \n" +
        "FROM PERFECTO_CLOUDS";

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
     * Instantiates a new SQL cloud provider.
     *
     * 
     */
    public SQLCloudProvider( String username, String password, String url, String driver  )
    {
        this.username = username;
        this.password = password;
        this.url = url;
        this.driver = driver;
        this.query = DEF_QUERY;
        
        readData();
    }

    /**
     * Instantiates a new SQL application provider.
     *
     * @param resourceName            the resource name
     * @param tabName the tab name
     */
    public SQLCloudProvider( String username, String password, String url, String driver, String query )
    {
        this( username, password, url, driver );

        this.query = (( query != null ) ? query : DEF_QUERY );
        
        readData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.device.application.ApplicationProvider#readData()
     */
    public void readData()
    {
        CloudRegistry.instance().clear();

        try
        {
            Object[][] data = SQLUtil.getResults( username, password, url, driver, query, null );

            for( int i = 0; i < data.length; ++i )
            {
                CloudRegistry.instance().addCloudDescriptor( new CloudDescriptor( (String) data[i][0],    // name
                                                                                  (String) data[i][1],    // user nemr
                                                                                  (String) data[i][2],    // password
                                                                                  (String) data[i][3],    // host name
                                                                                  (String) data[i][4],    // proxy host 
                                                                                  (String) data[i][5],    // proxy post
                                                                                  (String) data[i][6],    // description
                                                                                  (String) data[i][7] )); // grid instance
            }
        }
        catch (Exception e)
        {
            log.fatal( "Error reading Excel Element File", e );
        }
    }

}
