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
package org.xframium.page.keyWord.step.spi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.DefaultPageData;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.utility.SQLUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSSQL.
 */
public class KWSSQL extends AbstractKeyWordStep
{
    public KWSSQL()
    {
        kwName = "Execute SQL";
        kwDescription = "Allows the script to execute a SQL statement and process the results";
        kwHelp = "https://www.xframium.org/keyword.html#kw-sql";
        orMapping = false;
        category = "Utility";
    }

    private static final String[] JDBC = new String[] { "jdbc.username", "jdbc.password", "jdbc.url", "jdbc.driverClassName" };
    private static final String[] EMPTY = new String[0];

    private String getProperty( String connectionName, String propertyName, WebDriver webDriver )
    {
        String useName = propertyName;
        if ( connectionName != null )
            useName = connectionName + "." + propertyName;
        
        return KeyWordDriver.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).getConfigProperties().getProperty( useName );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com
     * .perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map,
     * java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
    {
        boolean rtn = true;

        String query = null;
        String[] query_params = null;
        
        int paramCount = getParameterList().size();
        
        KeyWordParameter namedConnection = getParameter( "connectionName" );
        String connectionName = namedConnection != null ? getParameterValue( namedConnection, contextMap, dataMap, executionContext.getxFID() ) : null;
        
        if ( paramCount < 1 )
        {
            throw new ScriptConfigurationException( "SQL requires at least one parameter" );
        }
        else
        {

            query = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() );
            ArrayList<String> paramList = new ArrayList<String>();
            if ( getParameterList().size() > 1 )
            {
                
                for ( int i=1; i<getParameterList().size(); i++ )
                {
                    if ( !"connectionName".equals( getParameterList().get( i ).getName() ) )
                        paramList.add( getParameterValue( getParameterList().get( i ), contextMap, dataMap, executionContext.getxFID() ) );
                }
            }
            
            
            query_params = paramList.toArray( new String[ 0 ] );
        }

        try
        {
            log.info( "Executing [" + query + "] against [" + getProperty( connectionName, JDBC[2], webDriver ) + "] as [" + getProperty( connectionName, JDBC[3], webDriver ) + "]" );
            
            //
            // OK, now we need to look at the statement to see if we're reading or writing
            //
            
            if ( query.trim().toUpperCase().startsWith( "SELECT" ))
            {
                
                Map[] resultsArr = SQLUtil.getRow( getProperty( connectionName, JDBC[0], webDriver ), getProperty( connectionName, JDBC[1], webDriver ) , getProperty( connectionName, JDBC[2], webDriver ) , getProperty( connectionName, JDBC[3], webDriver ), query, query_params );
                        
                Map<String,String> results = resultsArr[0];
                
                if (resultsArr.length > 1)
                {
                    //
                    // Populate page data using the context name if more than one record is returned
                    //
	                DefaultPageData pageData = null;
	                Map resultMap = null;
	                String mapKey = null;
	                String mapValue = null;
	                PageDataProvider dp = PageDataManager.instance(sC.getxFID()).getDataProvider();
	                dp.addRecordType(getContext(), false);
	                
	                for (int i=1; i<resultsArr.length; i++) {
	                	resultMap = resultsArr[i];
		                pageData = new DefaultPageData(getContext(), String.valueOf(i), true);
		                dp.addRecord(pageData);
		                
		                Iterator keySet = resultMap.keySet().iterator();
		                
		                while (keySet.hasNext()) {
		                	mapKey = String.valueOf(keySet.next());
		                	mapValue = String.valueOf(resultMap.get(mapKey));
		                	pageData.addValue( mapKey, mapValue );
		                }
	                }
                }

                //
                // The Map returned is keyed by the selected column alias AND
                // the integer column alias.  So, we'll ignore keys of type
                // Integer when setting contest data
                //
            
                if ( getContext() != null && !getContext().isEmpty() )
                {
                    Iterator keys = results.keySet().iterator();
            
                    while( keys.hasNext() )
                    {
                        Object key = keys.next();
                        Object value = results.get( key );
    
                        try
                        {
                            Integer.parseInt( key + "" );
                            continue;
                        }
                        catch( Exception e )
                        {
                            
                        }

                        String context_name = getContext() + "_" + key;
                    
                        if ( log.isInfoEnabled() )
                            log.info( "Setting Context Data to [" + value + "] for [" + context_name + "]" );
                    
                        contextMap.put( context_name, value );
                    }
                }
            }
            else
            {
                //
                // We only capture the update count here
                //
                int row_count = SQLUtil.execute( getProperty( connectionName, JDBC[0], webDriver ), getProperty( connectionName, JDBC[1], webDriver ) , getProperty( connectionName, JDBC[2], webDriver ) , getProperty( connectionName, JDBC[3], webDriver ), query, query_params );

                if ( getContext() != null && !getContext().isEmpty() )
                {
                    String context_name = getContext() + "_row_count";
                    String value = "" + row_count;
                    
                    if ( log.isDebugEnabled() )
                        log.debug( "Setting Context Data to [" + value + "] for [" + context_name + "]" );
                    
                    contextMap.put( context_name, value );
                }
            }
        }
        catch( Throwable e )
        {
            e.printStackTrace();
            throw new ScriptException( "SQL execution failed with: " + e.getMessage() );
        }
		
        return rtn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
     */
    public boolean isRecordable()
    {
        return false;
    }

}
