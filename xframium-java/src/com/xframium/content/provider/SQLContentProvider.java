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
