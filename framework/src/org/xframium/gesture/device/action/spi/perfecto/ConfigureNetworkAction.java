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
package org.xframium.gesture.device.action.spi.perfecto;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.gesture.device.action.AbstractDefaultAction;
import org.xframium.gesture.device.action.DeviceAction;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.bean.Execution;
import org.xframium.integrations.perfectoMobile.rest.bean.Handset;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigureNetworkAction.
 */
public class ConfigureNetworkAction extends AbstractDefaultAction implements DeviceAction
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.AbstractDefaultAction#_executeAction(org.openqa.selenium.WebDriver, java.util.List)
	 */
	@Override
	public boolean _executeAction( WebDriver webDriver, List<Object> parameterList )
	{
		String executionId = getExecutionId( webDriver );
		String deviceName = getDeviceName( webDriver );
		
		Handset localDevice = PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).devices().getDevice( deviceName );
		
		if ( localDevice.getOs().toLowerCase().equals( "ios" ) )
			throw new ScriptConfigurationException( "Configure network option is not supported for Perfecto IOS platform...");
		
		
		if (parameterList.size()<2){
			throw new ScriptConfigurationException( "Please supply atleast 2 parameters. Usage param-1, wifi=true param-2 airplane=true../n For Android OS version greater than 4.4");
		}
				
		String wifiMode = "", dataMode = "", airplaneMode = "";		
		for (int i = 0; i < parameterList.size(); i++) 
		{
			String currentParamStr = (String) parameterList.get( i );
			int splitIndex = currentParamStr.indexOf("=");
			if (currentParamStr.length() < 3 || splitIndex < 1) continue;
			String key = currentParamStr.substring(0, splitIndex);
			String value = currentParamStr.substring(splitIndex + 1);			
			
			
			if (key.equalsIgnoreCase("wifi")){								
				wifiMode = value.equalsIgnoreCase("true")?"enabled":"disabled";
			}
			
			if (key.equalsIgnoreCase("data")){								
				dataMode = value.equalsIgnoreCase("true")?"enabled":"disabled";
			}
			
			if (key.equalsIgnoreCase("airplane")){								
				airplaneMode = value.equalsIgnoreCase("true")?"enabled":"disabled";
			}
		}
		
		if (wifiMode.equals("") || airplaneMode.equals("")){
			throw new ScriptConfigurationException( "Make sure wifi and airplane modes are supplied as parameters with value true or false \n Usage param-1, wifi=true param-2 airplane=true");
		}
		
		String execStatus = "";
		Execution confNetExe = null;
		if (dataMode.equals("")){			
			// For Android OS version greater than 4.4, device data cannot be set 
			confNetExe = PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).windTunnel().configureNetwork(executionId, deviceName, wifiMode,airplaneMode);
			execStatus = confNetExe.getStatus();
		}else{
			// For Android OS version 2.3 to 4.4 you need to supply wifi, data and airplane mode
			confNetExe = PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).windTunnel().configureNetwork(executionId, deviceName, wifiMode,dataMode,airplaneMode);
			execStatus = confNetExe.getStatus();
		}	
		
		if(execStatus.indexOf("Error")>=0 || execStatus.indexOf("failed")>=0){
			throw new ScriptException ("Failed to configure the network " + confNetExe.toString());
		}
		
		return true;
	}

}
