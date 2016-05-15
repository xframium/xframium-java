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

        this.query = query;
        
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
