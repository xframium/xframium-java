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
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc

/**
 * The Class KWSSet.
 */
public class KWSVisual extends AbstractKeyWordStep
{
    public KWSVisual()
    {
        kwName = "Visual";
        kwDescription = "Allows the script to perform a Visual operation";
        kwHelp = "https://www.xframium.org/keyword.html#kw-visual";
        orMapping = false;
        category = "Visual";
    }
    
	public enum VisualType 
	{
		SET,
		BUTTON,
		FIND,
		SWIPE,
		TAP,
		DRAG,
		ROTATE		
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC )
	{
		if ( pageObject == null )
			throw new IllegalStateException( "There was no Page Object defined" );

		if ( getParameterList().size() < 1 )
			throw new IllegalArgumentException( "You must provide 1 parameter to setValue" );

		String switchType = getName();
		String command = "";

		switch ( VisualType.valueOf( switchType ) )
		{
			case SET:
				command = "mobile:edit-text:set";
				break;
			case BUTTON:
				command = "mobile:button-text:click";
				break;
			case FIND:
				command = "mobile:text:find";
				break;
			case SWIPE:
				command = "mobile:touch:swipe";
				break;
			case TAP:
				command = "mobile:touch:tap";
				break;
			case DRAG:
				// specify 4 coordinates in comma delimited list. Ex:
				// <parameter type="STATIC" value="location=97%,80%,97%,95%"/>
				command = "mobile:touch:drag";
				break;
			case ROTATE:
				command = "mobile:device:rotate";
				break;			
		}
		Map<String, Object> params = new HashMap<>();
		for (int i = 0; i < getParameterList().size(); i++) {
			String currentParamStr = getParameterValue( getParameterList().get( i ), contextMap, dataMap ) + "";
			int splitIndex = currentParamStr.indexOf("=");
			if (currentParamStr.length() < 3 || splitIndex < 1) continue;
			String key = currentParamStr.substring(0, splitIndex);
			String value = currentParamStr.substring(splitIndex + 1);
			params.put(key, value);
		}

		try
		{
			log.info( "Running command: " + command + ", with parameters: " + params);
			String result = ((RemoteWebDriver) ((DeviceWebDriver) webDriver).getWebDriver()).executeScript(command, params) + "";

			log.info( "Command Result: " + result);
			return !"false".equals(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

}
