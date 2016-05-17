/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package com.xframium.page.data.provider;

import java.util.*;
import com.xframium.page.BY;
import com.xframium.page.ElementDescriptor;
import com.xframium.page.data.DefaultPageData;
import com.xframium.page.data.PageData;
import com.xframium.page.element.Element;
import com.xframium.page.element.ElementFactory;
import com.xframium.utility.SQLUtil;


/**
 * The Class SQLDataProvider.
 */
public class SQLPageDataProvider extends AbstractPageDataProvider
{
    //
    // class data
    //

    private static final String DEF_QUERY =
        "SELECT DT.NAME, DT.LOCK_RECORDS, \n" +
        "       PG.NAME, PG.ACTIVE, \n" +
        "       AT.NAME, AT.VALUE \n" +
        "FROM PERFECTO_PAGE_DATA_TYPE DT \n" +
        "     INNER JOIN PERFECTO_PAGE_DATA PG ON PG.TYPE_NAME = DT.NAME \n" +
        "     INNER JOIN PERFECTO_PAGE_DATA_ATTRS ON AT.TYPE_NAME = DT.NAME AND AT.RECORD_NAME = PG.NAME";

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
     * Instantiates a new SQL data provider.
     *
     * @param username the user name
     * @param password the password
     * @param url the JDBC URL
     * @param driver the driver class name
     */
    public SQLPageDataProvider( String username, String password, String url, String driver )
    {
        this.username = username;
        this.password = password;
        this.url = url;
        this.driver = driver;
        this.query = DEF_QUERY;
    }

    /**
     * Instantiates a new SQL data provider.
     *
     * @param username the user name
     * @param password the password
     * @param url the JDBC URL
     * @param driver the driver class name
     */
    public SQLPageDataProvider( String username, String password, String url, String driver, String query )
    {
        this( username, password, url, driver );

        this.query = (( query != null ) ? query : DEF_QUERY );
    }

    //
    // override AbstractPageDataProvider
    //
    
    public void readPageData()
    {
        try
        {
            Object[][] data = SQLUtil.getResults( username, password, url, driver, query, null );
            HashSet existingRecordTypes = new HashSet();
            HashMap existingRecordsByName = new HashMap();

            for( int i = 0; i < data.length; ++i )
            {
                String record_type_name = (String) data[i][0];
                boolean lockRecords = "Y".equals( (String) data[i][1] );

                if ( !existingRecordTypes.contains( record_type_name ))
                {
                    addRecordType( record_type_name, lockRecords );
                    existingRecordTypes.add( record_type_name );
                }

                String record_name = (String) data[i][2];
                boolean active = "Y".equals( (String) data[i][3] );

                DefaultPageData currentRecord = (DefaultPageData) existingRecordsByName.get( record_name );
                if( currentRecord == null )
                {
                    currentRecord = new DefaultPageData( record_type_name, record_name, active );
                }

                String currentName = (String) data[i][4];
                String currentValue = (String) data[i][5];

                if ( currentValue.startsWith( PageData.TREE_MARKER ) && currentValue.endsWith( PageData.TREE_MARKER ) )
                {
                    //
                    // This is a reference to another page data table
                    //
                    currentRecord.addPageData( currentName );
                    currentRecord.addValue( currentName + PageData.DEF, currentValue );
                    currentRecord.setContainsChildren( true );
                }
                else
                {
                    currentRecord.addValue( currentName, currentValue );
                }
            }

            Iterator records = existingRecordsByName.values().iterator();
            while( records.hasNext() )
            {
                DefaultPageData currentRecord = (DefaultPageData) records.next();
                    
                addRecord( currentRecord );
            }

        }
        catch (Exception e)
        {
            log.fatal( "Error reading Excel Element File", e );
        }
    }
}
