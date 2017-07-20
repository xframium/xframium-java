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
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.ng.TestName;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSAddDevice.
 */
public class KWSAddDevice extends AbstractKeyWordStep
{
    
    
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
    {
        Object name = null;
        Object deviceId = null;
        Object deviceName = null;
        
        if ( getParameterList().size() == 1 ) {
        	deviceName = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() );
        	
        	if ( !( deviceName instanceof String ) )
                throw new ScriptConfigurationException( "Device name must be of type String" );
        	
        	if ( DeviceManager.instance( executionContext.getxFID() ).getDevice(deviceName.toString()) == null )
        		throw new ScriptConfigurationException( "Device Name should be configured in DeviceRegistry with inactive status" );
        		
        	executionContext.getDeviceMap().put( deviceName + "", DeviceManager.instance( executionContext.getxFID() ).getInactiveDevice( deviceName + "", executionContext.getxFID()  ) );
        }
        else if ( getParameterList().size() == 2 )
        {
        	name = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() );
	        
        	if ( !( name instanceof String ) )
        		throw new ScriptConfigurationException( "Device name must be of type String" );
	
        	deviceId = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() );
	        
        	if ( !( deviceId instanceof String ) )
	        	throw new ScriptConfigurationException( "Device id must be of type String" );
        	
        	
        	executionContext.getDeviceMap().put( deviceName + "", DeviceManager.instance( executionContext.getxFID() ).getUnconfiguredDevice( deviceName + "",  executionContext.getxFID()  ) );
        }
	    else
	    {
	    	throw new ScriptConfigurationException( "add device requires either the device name from DeviceRegistry.xml or two string properties (name, deviceId)" );
	    }
        return true;
    }
	
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
     */
    public boolean isRecordable()
    {
        return false;
    }
    
    public KWSAddDevice()
    {
        kwName = "Add device";
        kwDescription = "Allows thte script to add a additional device to the same script to validate device/device actions";
        kwHelp = "https://www.xframium.org/keyword.html#kw-adddevice";
        orMapping = false;
        category="Utility";
    }

}
