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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.GherkinContainer;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;


public class KWSGherkin extends AbstractKeyWordStep
{
    public KWSGherkin()
    {
        kwName = "Gherkin Call";
        kwDescription = "Allows the script to call a predefined Gherkin step";
        kwHelp = "https://www.xframium.org/keyword.html#kw-call";
        orMapping = false;
        category = "Flow Control";
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext ) throws Exception
	{
		boolean executed = false;
		for ( GherkinContainer g : KeyWordDriver.instance(executionContext.getxFID()).getMethodMap().values() )
		{
			Matcher m = g.getRegex().matcher( getName() );
			if ( m.matches() )
			{
				List<Object> parameterArray = new ArrayList<Object>( 10 );;
				Object methodClass = g.getMethod().getDeclaringClass().newInstance();
				for ( int i=1; i<m.groupCount() + 1; i++ )
					parameterArray.add( m.group( i ) );
				
				for ( int i=0; i<g.getMethod().getParameterTypes().length; i++ )
				{
					Class c = g.getMethod().getParameterTypes()[ i ];
					if ( c.isAssignableFrom( WebDriver.class ) )
					{
						parameterArray.add( i, webDriver );
					}
				}
				
				boolean reportingElement = ( (DeviceWebDriver) webDriver ).isReportingElement();
				( (DeviceWebDriver) webDriver ).setReportingElement( true );
				g.getMethod().invoke( methodClass, parameterArray.toArray() );
				executed = true;
				( (DeviceWebDriver) webDriver ).setReportingElement(reportingElement);
				break;
			}
		}

		return executed;
	}
	
	public boolean isRecordable()
    {
        return false;
    }

}

