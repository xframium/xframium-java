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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.gesture.device.action.AbstractDefaultAction;
import org.xframium.gesture.device.action.DeviceAction;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.bean.Execution;
import org.xframium.integrations.perfectoMobile.rest.bean.Handset;
import org.xframium.integrations.perfectoMobile.rest.services.WindTunnel.Network;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigureNetworkAction.
 */
public class ConfigureNetworkAction extends AbstractDefaultAction implements DeviceAction
{
	
	/** The application map. */
	private Map<String,String> networkMap;
	
	private String returnValue;
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.AbstractDefaultAction#_executeAction(org.openqa.selenium.WebDriver, java.util.List)
	 */
	@Override
	public boolean _executeAction( WebDriver webDriver, List<Object> parameterList )
	{
		String executionId = getExecutionId( webDriver );
		String deviceName = getDeviceName( webDriver );
		
		Handset localDevice = PerfectoMobile.instance().devices().getDevice( deviceName );
		if ( localDevice.getOs().toLowerCase().equals( "ios" ) )
			throw new ScriptConfigurationException( "Configure network option is not supported for Perfecto IOS platform...");
		
		// Get the network setting for the Device
		Execution getNetExec = PerfectoMobile.instance().windTunnel().getNetworkSetting(executionId, deviceName, Network.wifi);
		
		// Will return {airplanemode=false, wifi=true, data=false}
		returnValue = getNetExec.getReturnValue();
		
		String deviceWifiMode = getNetworkOption("wifi");
		String newwifiMode;
		if ( parameterList.size() > 0 ){
			String expWifiMode = (String) parameterList.get( 0 );
			newwifiMode = expWifiMode.equalsIgnoreCase("true")?"enabled":"disabled";						
		}else{
			newwifiMode = deviceWifiMode.equalsIgnoreCase("true")?"enabled":"disabled";
		}
		
		String deviceDataMode = getNetworkOption("data");
		String newdataMode;
		if ( parameterList.size() > 1 ){
			String expDataMode = (String) parameterList.get( 1 );
			newdataMode = expDataMode.equalsIgnoreCase("true")?"enabled":"disabled";						
		}else{
			newdataMode = deviceDataMode.equalsIgnoreCase("true")?"enabled":"disabled";
		}
		
		String deviceAirplaneMode = getNetworkOption("airplanemode");
		String newAirplaneMode;
		if ( parameterList.size() > 2 ){
			String expAirplaneDataMode = (String) parameterList.get( 2 );
			newAirplaneMode = expAirplaneDataMode.equalsIgnoreCase("true")?"enabled":"disabled";						
		}else{
			newAirplaneMode = deviceAirplaneMode.equalsIgnoreCase("true")?"enabled":"disabled";
		}	
		
		
		// Configure the network settings (WIFI, DATA or AIRPLANEMODE) through the below service call
		Execution confNetExe = PerfectoMobile.instance().windTunnel().configureNetwork(executionId, deviceName, newwifiMode,newdataMode,newAirplaneMode);
		String execStatus = confNetExe.getStatus();
		
		if(execStatus.indexOf("Error")>=0 || execStatus.indexOf("failed")>=0){
			throw new ScriptException ("Failed to configure the network " + confNetExe.toString());
		}
		
		return true;
	}
	
	
	
	/**
	 * Parses the applications.
	 */
	private void parseNetworksOptions()
	{
		networkMap = new HashMap<String,String>( 20 );
		
		String workingValue = returnValue;
		workingValue = workingValue.replace( '{', ' ' ).replace( '}', ' ' ).trim();
		String[] allOptions = workingValue.split( "," );
		
		for ( String network : allOptions )
		{
			String[] networkMode = network.trim().split( "=" );			
			networkMap.put( networkMode[0].trim().toLowerCase(), networkMode[1].trim().toLowerCase() );
		}
	}
	
	/**
	 * Gets the application.
	 *
	 * @param applicationName the application name
	 * @return the application
	 */
	private String getNetworkOption(String networkOption )
	{
		if ( networkMap == null )
			parseNetworksOptions();
		
		return networkMap.get( networkOption.toLowerCase() );
	}

}
