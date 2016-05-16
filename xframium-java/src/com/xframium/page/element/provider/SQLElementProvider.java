package com.xframium.page.element.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import com.xframium.page.BY;
import com.xframium.page.ElementDescriptor;
import com.xframium.page.PageManager;
import com.xframium.page.element.Element;
import com.xframium.page.element.ElementFactory;
import com.xframium.page.element.provider.xsd.Import;
import com.xframium.page.element.provider.xsd.ObjectFactory;
import com.xframium.page.element.provider.xsd.Page;
import com.xframium.page.element.provider.xsd.RegistryRoot;
import com.xframium.page.element.provider.xsd.Site;
import com.xframium.utility.SQLUtil;

/**
 * The Class SQLElementProvider.
 */
public class SQLElementProvider
    extends AbstractElementProvider
{
    //
    // class data
    //

    private static final String DEF_QUERY =
        "SELECT PS.NAME, \n" +
        "       PP.NAME, \n" +
        "       PE.NAME, PE.DESCRIPTOR, PE.VALUE, PE.CONTEXT_NAME \n" +
        "FROM PERFECTO_SITES PS \n" +
        "     INNER JOIN PERFECTO_PAGES PP ON PP.SITE_NAME = PS.NAME \n" +
        "     INNER JOIN PERFECTO_ELEMENTS PE ON PE.SITE_NAME = PP.SILTE_NAME AND PE.PAGE_NAME = PP.NAME \n" +
        "ORDER BY PS.NAME, PP.NAME";

    //
    // instance data
    //

    /** The element map. */
    private Map<String,Element> elementMap = new HashMap<String,Element>(20);

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
     * Instantiates a new SQL element provider.
     *
     * @param username the user name
     * @param password the password
     * @param url the JDBC URL
     * @param driver the driver class name
     */
    public SQLElementProvider( String username, String password, String url, String driver )
    {
        this.username = username;
        this.password = password;
        this.url = url;
        this.driver = driver;
        this.query = DEF_QUERY;
        
        readElements();
    }
	
    /**
     * Instantiates a new SQL element provider.
     *
     * @param username the user name
     * @param password the password
     * @param url the JDBC URL
     * @param driver the driver class name
     */
    public SQLElementProvider( String username, String password, String url, String driver, String query )
    {
        this( username, password, url, driver );
        this.query = (( query != null ) ? query : DEF_QUERY);
                
        readElements();
    }
	
    /**
     * Read elements.
     */
    private void readElements()
    {
        try
        {
            Object[][] data = SQLUtil.getResults( username, password, url, driver, query, null );

            for( int i = 0; i < data.length; ++i )
            {
                String siteName = (String) data[i][0];
                String pageName = (String) data[i][1];
                String eltName = (String) data[i][2];
                String eltDesc = (String) data[i][3];
                String eltVal = (String) data[i][4];
                String contextName = (String) data[i][5];
                

                ElementDescriptor elementDescriptor = new ElementDescriptor( siteName,
                                                                             pageName,
                                                                             eltName );
                
                Element currentElement = ElementFactory.instance().createElement( BY.valueOf( eltDesc ),
                                                                                  eltVal.replace( "$$", ","),
                                                                                  eltName,
                                                                                  pageName,
                                                                                  contextName );
            
                if ( log.isDebugEnabled() )
                    log.debug( "Adding Excel Element using [" + elementDescriptor.toString() + "] as [" + currentElement );
                
                elementMap.put(elementDescriptor.toString(), currentElement );
            }
        }
        catch (Exception e)
        {
            log.fatal( "Error reading Excel Element File", e );
        }

    }
		
    @Override
    protected Element _getElement( ElementDescriptor elementDescriptor )
    {
        return elementMap.get(  elementDescriptor.toString() );
    }	
}
