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
