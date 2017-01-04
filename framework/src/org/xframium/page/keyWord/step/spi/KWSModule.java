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

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;


public class KWSModule extends KWSCall2
{
    public KWSModule()
    {
        kwName = "Execute a Module";
        kwDescription = "Allows the execution of an xFramium module.  Modules are used when creating module based tests";
        kwHelp = "https://www.xframium.org/keyword.html#kw-module";
        orMapping = false;
        category = "Flow Control";
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext ) throws Exception
	{
		Map<String,String> localContextMap = new HashMap<String,String>( 20 );
		boolean returnValue = false;
		
		String functionName = getName();
		
		if ( log.isDebugEnabled() )
			log.debug( "Execution Module " + getName() );
		
		try
		{
			
			if ( getParameterList() != null && !getParameterList().isEmpty() )
			{
				for ( KeyWordParameter param : getParameterList() )
				{
					if ( param.getName() != null && !param.getName().isEmpty() )
					{
						if ( param.getName().equals( "OVERRIDE") )
						{
							//
							// Override is used for dataMap parameter mappings
							//
							String dataProvider = getParameterValue( param, contextMap, dataMap ) + "";
							String[] dpArray = dataProvider.split( "=" );
							if ( dpArray.length == 2 )
							{
								PageData pageData = dataMap.get( dpArray[1] );
								if ( pageData != null )
									dataMap.put( dpArray[0], pageData );
							}
						}
						else if ( param.getName().equals( "FUNCTION_NAME") )
						{
						    functionName = getParameterValue( param, contextMap, dataMap ) + "";
						}
						else
						{
							//
							// These are locally used context variables
							//
							localContextMap.put( param.getName(), param.getName() );
							contextMap.put( param.getName(), getParameterValue( param, contextMap, dataMap ) + "" );
						}
					}
				}
			}
			
			if ( sC != null )
				returnValue = sC.getTest( functionName ).executeTest(webDriver, contextMap, dataMap, pageMap, sC, executionContext);
			else
				returnValue = KeyWordDriver.instance().executionFunction( functionName, webDriver, dataMap, pageMap, contextMap, sC, executionContext );
		}
		finally
		{
			for ( String name : localContextMap.keySet() )
				contextMap.remove( name );
		}
		
		return returnValue;
	}
	
	public boolean isRecordable()
    {
        return false;
    }

}
