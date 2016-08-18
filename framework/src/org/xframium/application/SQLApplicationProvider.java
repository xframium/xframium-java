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
package org.xframium.application;

import java.util.HashMap;
import org.openqa.selenium.Platform;
import org.xframium.utility.SQLUtil;

/**
 * The Class SQLApplicationProvider.
 */
public class SQLApplicationProvider extends AbstractApplicationProvider
{

    //
    // class data
    //

    private static final String DEF_QUERY =
        "SELECT NAME, APP_PACKAGE, BUNDLE_ID, URL, IOS_INSTALL, ANDROID_INSTALL \n" +
        "FROM APPLICATIONS";

    private static final String DEF_CAP_QUERY =
        "SELECT APP_NAME, NAME, CLASS, VALUE \n" +
        "FROM APP_DEV_CAPABILITIES";

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

    /** The capability query. */
    private String capQuery;

    /**
     * Instantiates a new SQl application provider.
     *
     * @param resourceName the resource name
     */
    public SQLApplicationProvider( String username, String password, String url, String driver )
    {
        this.username = username;
        this.password = password;
        this.url = url;
        this.driver = driver;
        this.query = DEF_QUERY;
        this.capQuery = DEF_CAP_QUERY;
        
        readData();
    }
	
    /**
     * Instantiates a new CSV application provider.
     *
     * @param resourceName the resource name
     */
    public SQLApplicationProvider( String username, String password, String url, String driver, String query, String capQuery )
    {
        this( username, password, url, driver );
        this.query = (( query != null ) ? query : DEF_QUERY );;
        this.capQuery = (( capQuery != null ) ? capQuery : DEF_CAP_QUERY );
        
        readData();
    }
	
    /* (non-Javadoc)
     * @see com.perfectoMobile.device.application.ApplicationProvider#readData()
     */
    
    private String parseString( String currentValue, String defaultValue, boolean emptyCheck )
    {
        if ( currentValue == null )
            return defaultValue;
        else
        {
            if ( emptyCheck && currentValue.trim().isEmpty() )
                return defaultValue;
            else
                return currentValue;
        }
    }
    
    public void readData()
    {
        ApplicationRegistry.instance().clear();
        HashMap capabilitiesByName = new HashMap();
        

        try
        {
            Object[][] data = SQLUtil.getResults( username, password, url, driver, query, null );

            for( int i = 0; i < data.length; ++i )
            {
                String name = parseString( (String) data[i][0], null, true );
                String pkg = parseString( (String) data[i][1], null, true );
                String bndl = parseString( (String) data[i][2], null, true );
                String url = parseString( (String) data[i][3], null, true );
                String ios_inst = parseString( (String) data[i][4], null, true );
                String and_inst = parseString( (String) data[i][5], null, true );

                HashMap<String,Object> capabilities = new HashMap<String,Object>( 0 );

                capabilitiesByName.put( name, capabilities );

                ApplicationRegistry.instance().addApplicationDescriptor( new ApplicationDescriptor( name,
                                                                                                    name,
                                                                                                    pkg,
                                                                                                    bndl,
                                                                                                    url,
                                                                                                    and_inst,
                                                                                                    ios_inst,
                                                                                                    capabilities ));
            }

            data = SQLUtil.getResults( username, password, url, driver, capQuery, null );

            for( int i = 0; i < data.length; ++i )
            {
                String app_name = (String) data[i][0];
                String name = (String) data[i][1];
                String clazz = (String) data[i][2];
                String value = (String) data[i][3];

                HashMap<String,Object> capabilities = (HashMap<String,Object>) capabilitiesByName.get( app_name );

                if ( capabilities != null )
                {
                    capabilities.put( name, getValue( clazz, value ));
                }
            }       
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
        }
    }

    //
    // Helpers
    //

    private Object getValue( String clazz, String value )
    {
        Object rtn = null;

        switch ( clazz )
        {
            case "BOOLEAN":
                rtn = Boolean.parseBoolean( value );
                break;
                
            case "OBJECT":
                rtn = value;
                break;
                
            case "STRING":
                rtn = value;
                break;
                
            case "PLATFORM":
                rtn = ((value != null) ? Platform.valueOf( value.toUpperCase() ) : null );
                break;
        }

        return rtn;
    }
}
