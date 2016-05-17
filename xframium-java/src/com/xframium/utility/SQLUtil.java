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
package com.xframium.utility;

import java.sql.*;
import java.util.*;

public class SQLUtil
{
    public static Object[][] getResults( String username,
                                         String password,
                                         String url,
                                         String driver,
                                         String query,
                                         String[] params )
        throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            conn = getConnection( username,
                                  password,
                                  url,
                                  driver );

            pstmt = conn.prepareStatement( query );

            if ( params != null )
            {
                int offset = 1;
                for( String param : params )
                {
                    pstmt.setString( offset++, param );
                }
            }

            rs = pstmt.executeQuery();

            return consume( rs );
        }
        finally
        {
            try { rs.close(); }
            catch( Throwable e ) {}

            try { pstmt.close(); }
            catch( Throwable e ) {}
            
            try { conn.close(); }
            catch( Throwable e ) {}
        }

    }

    //
    // Helpers
    //

    private static Connection getConnection( String username,
                                             String password,
                                             String url,
                                             String driver )
        throws Exception
    {
        Connection conn = null;

        Class.forName( driver );

        conn = DriverManager.getConnection( url,
                                            username,
                                            password );

        return conn;
    }

    private static Object[][] consume( ResultSet rs )
        throws Exception
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        ArrayList results = new ArrayList();
        int colCount = rsmd.getColumnCount();

        while( rs.next() )
        {
            Object[] row = new Object[ colCount ];

            for( int i = 1; i <= colCount; ++i )
            {
                row[ i - 1 ] = rs.getObject( i );
            }

            results.add( row );
        }

        return (Object[][]) results.toArray();
    }
}
