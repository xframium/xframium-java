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

import java.util.Map;
import java.util.Iterator;

import org.openqa.selenium.WebDriver;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.utility.SQLUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSSQL.
 */
public class KWSSQL extends AbstractKeyWordStep
{
    private static final String[] JDBC = new String[] { "jdbc.username", "jdbc.password", "jdbc.url", "jdbc.driverClassName" };

    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
    {
        boolean rtn = true;
        
        if ( pageObject == null )
            throw new IllegalStateException( "Page Object was not defined" );

        //
        // OK, this step needs at least one parameter ans the first is the query to execute.  If there are
        // any more, they are values to compare the query results to.  If all the test parameters match
        // the step passes
        //

        String query = null;
        int paramCount = getParameterList().size();
        
        if ( paramCount < 1 )
        {
            throw new IllegalStateException( "SQL requires at least one parameter" );
        }
        else
        {
            Object obj = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap );
            if ( obj instanceof String )
            {
                query = (String) obj;
            }
            else
            {
                throw new IllegalStateException( "SQL requires a parameter of type String" );
            }
        }

        try
        {
            Map[] resultsArr = SQLUtil.getRow( KeyWordDriver.instance().getConfigProperties().getProperty( JDBC[0] ),
                                               KeyWordDriver.instance().getConfigProperties().getProperty( JDBC[1] ),
                                               KeyWordDriver.instance().getConfigProperties().getProperty( JDBC[2] ),
                                               KeyWordDriver.instance().getConfigProperties().getProperty( JDBC[3] ),
                                               query,
                                               null );
            Map results = resultsArr[0];

            //
            // The Map returned is keyed by the selected column alias AND
            // the integer column alias.  So, we'll ignore keys of type
            // Integer when setting contest data
            //
            
            Iterator keys = results.keySet().iterator();
            if ( getContext() != null && !getContext().isEmpty() )
            {
                while( keys.hasNext() )
                {
                    Object key = keys.next();
                    Object value = results.get( key );
    
                    if ( key instanceof Integer )
                    {
                        continue;
                    }
                    
                    if ( log.isDebugEnabled() )
                        log.debug( "Setting Context Data to [" + value + "] for [" + key + "]" );
                    
                    contextMap.put( getContext() + "_" + key, value );
                }
            }

            for( int i = 1; (( i < paramCount ) && ( rtn )); ++i )
            {
                Object paramValue = getParameterValue( getParameterList().get( i ), contextMap, dataMap );
                Object queryValue = results.get( i );

                rtn = ((( paramValue == null ) && ( queryValue == null )) ||
                       (( paramValue != null ) && ( paramValue.equals( queryValue ))));
            }
        }
        catch( Throwable e )
        {
            throw new IllegalStateException( "SQL execution failed with: ", e );
        }
		
        return rtn;
    }
	
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
     */
    public boolean isRecordable()
    {
        return false;
    }

}
