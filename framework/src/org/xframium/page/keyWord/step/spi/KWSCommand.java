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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;
import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc

/**
 * The Class KWSSet.
 */
public class KWSCommand extends AbstractKeyWordStep
{
    public KWSCommand()
    {
        kwName = "Command";
        kwDescription = "Executes an arbitrary named command with parameters";
        kwHelp = "https://www.xframium.org/keyword.html#kw-command";
        orMapping = false;
        category = "Utility";
        featureId = 14;
    }
   

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
	{
		String commandName = getName();
		
		
		Map<String, Object> params = new HashMap<>();
		
		KeyWordParameter useCommand = getParameter( "commandName" );
		if ( useCommand != null )
		    commandName = getParameterValue( useCommand, contextMap, dataMap, executionContext.getxFID() );
		
		for (int i = 0; i < getParameterList().size(); i++) 
		{
			String currentName = getParameterList().get( i ).getName();
			if ( currentName != null && currentName.equals( "commandName" ) )
			    continue;
			String currentValue = getParameterValue( getParameterList().get( i ), contextMap, dataMap, executionContext.getxFID() ) + "";

			params.put(currentName, currentValue);
		}

		try
		{
			log.info( "Running command: " + commandName + ", with parameters: " + params);
			String result = ((RemoteWebDriver) ((DeviceWebDriver) webDriver).getWebDriver()).executeScript(commandName, params) + "";

			log.info( "Command Result: " + result);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

}
