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
package org.xframium.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLUtil
{
    //
    // Class Data
    //

    private static final Map<String, String>[] EMPTY_MAP_ARRAY = new Map[0];

    //
    // Implementation
    //
    /**
     * This method establishes the DB connection, executes the SQL statement and
     * returns an array of Object
     * 
     * @param username
     * @param password
     * @param url
     * @param driver
     * @param query
     * @param params
     * @return object
     * @throws Exception
     */
    public static Object[][] getResults( String username, String password, String url, String driver, String query, String[] params ) throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            conn = getConnection( username, password, url, driver );

            pstmt = conn.prepareStatement( query );

            if ( params != null )
            {
                int offset = 1;
                for ( String param : params )
                    pstmt.setString( offset++, param );
            }

            rs = pstmt.executeQuery();

            return consume( rs );
        }
        finally
        {
            try
            {
                rs.close();
            }
            catch ( Throwable e )
            {
            }

            try
            {
                pstmt.close();
            }
            catch ( Throwable e )
            {
            }

            try
            {
                conn.close();
            }
            catch ( Throwable e )
            {
            }
        }
    }

    /**
     * This method establishes the DB connection, executes a query and returns
     * results in a map
     * 
     * @param username
     * @param password
     * @param url
     * @param driver
     * @param query
     * @param params
     * @return Map
     * @throws Exception
     */
    public static Map<String, String>[] getRow( String username, String password, String url, String driver, String query, String[] params ) throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            conn = getConnection( username, password, url, driver );

            pstmt = conn.prepareStatement( query );

            if ( params != null )
            {
                int offset = 1;
                for ( String param : params )
                {
                    pstmt.setString( offset++, param );
                }
            }

            rs = pstmt.executeQuery();

            return consume2( rs );
        }
        finally
        {
            try
            {
                rs.close();
            }
            catch ( Throwable e )
            {
            }

            try
            {
                pstmt.close();
            }
            catch ( Throwable e )
            {
            }

            try
            {
                conn.close();
            }
            catch ( Throwable e )
            {
            }
        }
    }

    /**
     * This method establishes the DB connection, executes the SQL statement and
     * returns the modified row count
     * 
     * @param username
     * @param password
     * @param url
     * @param driver
     * @param statement
     * @param params
     * @return Map
     * @throws Exception
     */
    public static int execute( String username, String password, String url, String driver, String statement, String[] params ) throws Exception
    {
        int rtn = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnection( username, password, url, driver );

            pstmt = conn.prepareStatement( statement );

            if ( params != null )
            {
                int offset = 1;
                for ( String param : params )
                {
                    pstmt.setString( offset++, param );
                }
            }

            rtn = pstmt.executeUpdate();

            conn.commit();
        }
        finally
        {
            try
            {
                pstmt.close();
            }
            catch ( Throwable e )
            {
            }

            try
            {
                conn.close();
            }
            catch ( Throwable e )
            {
            }
        }

        return rtn;
    }

    //
    // Helpers
    //

    /**
     * Gets the DB connection
     * 
     * @param username
     * @param password
     * @param url
     * @param driver
     * @return Connection
     * @throws Exception
     */
    private static Connection getConnection( String username, String password, String url, String driver ) throws Exception
    {
        Connection conn = null;

        Class.forName( driver );

        conn = DriverManager.getConnection( url, username, password );

        return conn;
    }

    /**
     * This method converts the result set to a two dimensional array Object
     * 
     * @param rs
     *            - ResultSet
     * @return Object[][]
     * @throws Exception
     */
    private static Object[][] consume( ResultSet rs ) throws Exception
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        ArrayList results = new ArrayList();
        int colCount = rsmd.getColumnCount();

        while ( rs.next() )
        {
            Object[] row = new Object[colCount];

            for ( int i = 1; i <= colCount; ++i )
            {
                row[i - 1] = rs.getObject( i );
            }

            results.add( row );
        }

        return toOutArray( results, colCount );
    }

    /**
     * This method converts result set to an array of Map
     * 
     * @param rs
     *            - ResultSet
     * @return Map[]
     * @throws Exception
     */
    private static Map<String, String>[] consume2( ResultSet rs ) throws Exception
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        List<Map<String, String>> results = new ArrayList<Map<String, String>>( 10 );
        int colCount = rsmd.getColumnCount();

        while ( rs.next() )
        {
            Map<String, String> row = new HashMap<String, String>();

            for ( int i = 1; i <= colCount; ++i )
            {
                String val = String.valueOf( rs.getObject( i ) );

                row.put( rsmd.getColumnName( i ), val );
                row.put( i + "", val );
            }

            results.add( row );
        }

        return toOutArray2( results );
    }

    /**
     * Converts List of Array to a two dimensional object
     * 
     * @param listOfArray
     * @param colCount
     * @return Object[][]
     */
    private static Object[][] toOutArray( List<String[]> listOfArray, int colCount )
    {
        int length = listOfArray.size();
        Object[][] rtn = new Object[length][colCount];

        for ( int i = 0; i < rtn.length; ++i )
        {
            rtn[i] = (Object[]) listOfArray.get( i );
        }

        return rtn;
    }

    /**
     * Converts List of Maps to a Map array
     * 
     * @param listOfMaps
     * @return Map[]
     */
    private static Map<String, String>[] toOutArray2( List<Map<String, String>> listOfMaps )
    {
        return (Map<String, String>[]) listOfMaps.toArray( EMPTY_MAP_ARRAY );
    }
}
