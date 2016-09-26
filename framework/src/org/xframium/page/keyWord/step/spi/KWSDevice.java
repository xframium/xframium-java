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
import org.openqa.selenium.WebDriver;
import org.xframium.gesture.device.action.DeviceAction;
import org.xframium.gesture.device.action.DeviceAction.ActionType;
import org.xframium.gesture.device.action.DeviceActionManager;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;



// TODO: Auto-generated Javadoc
/**
 * The Class KWSDevice.
 */
public class KWSDevice extends AbstractKeyWordStep
{
    public KWSDevice()
    {
        kwName = "Device Action";
        kwDescription = "Allows the script to perform device specific actions";
        kwHelp = "https://www.xframium.org/keyword.html#kw-device";
        orMapping = false;
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
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
		long startTime = System.currentTimeMillis();
		if ( log.isDebugEnabled() )
			log.debug( "Executing Device Action " + getName() + " using " + getParameterList() );
		
		DeviceAction deviceAction = DeviceActionManager.instance().getAction( ActionType.valueOf( getName().toUpperCase() ) );
		
		List<Object> actionParameters = new ArrayList<Object>(10);
		
		for ( KeyWordParameter param : getParameterList() )
		{
			actionParameters.add( getParameterValue( param, contextMap, dataMap ) );
		}
		
		boolean returnValue = deviceAction.executeAction( webDriver, actionParameters );
		
		

		return returnValue;
	}

}
