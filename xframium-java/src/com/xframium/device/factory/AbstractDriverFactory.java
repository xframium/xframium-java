/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
/*
 * 
 */
package com.xframium.device.factory;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.xframium.device.data.DataManager;
import com.xframium.device.interrupt.DeviceInterrupt;
import com.xframium.device.interrupt.DeviceInterruptFactory;
import com.xframium.device.interrupt.DeviceInterrupt.INTERRUPT_TYPE;
import com.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating AbstractDriver objects.
 */
public abstract class AbstractDriverFactory implements DriverFactory
{

	/** The log. */
	protected Log log = LogFactory.getLog( DriverFactory.class );
	
	/**
	 * _create driver.
	 *
	 * @param currentDevice the current device
	 * @return the device web driver
	 */
	protected abstract DeviceWebDriver _createDriver( Device currentDevice );
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.factory.DriverFactory#createDriver(com.perfectoMobile.device.Device)
	 */
	public DeviceWebDriver createDriver( Device currentDevice )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Creating Driver for " + getClass().getSimpleName() );
		
		DeviceWebDriver webDriver = _createDriver( currentDevice ); 
		
		return webDriver;
	}
	
	/**
	 * Capabilities to string.
	 *
	 * @param caps the caps
	 * @return the string
	 */
	protected String capabilitiesToString( Capabilities caps )
	{
		StringBuilder capData = new StringBuilder();
		
		for ( String keyName : caps.asMap().keySet() )
		{
			capData.append( keyName ).append( "=" ).append( caps.getCapability( keyName ) + "\r\n" );
		}
		
		return capData.toString();
	}
	
	protected List<DeviceInterrupt> getDeviceInterrupts( String interruptString, String executionId, String deviceName )
	{
	    if ( interruptString != null && !interruptString.isEmpty() )
	    {
	        String[] interrupts = interruptString.split( "," );
	        List<DeviceInterrupt> interruptList = new ArrayList<DeviceInterrupt>( interrupts.length );
	        
	        for ( String interruptName : interrupts )
	        {
	            interruptList.add( DeviceInterruptFactory.instance().getDeviceInterrupt( INTERRUPT_TYPE.valueOf( interruptName.trim().toUpperCase() ), executionId, deviceName ) );
	        }
	        
	        return interruptList;
	    }
	    
	    return null;
	}
	
	/**
	 * Adds the device capabilities.
	 *
	 * @param currentDevice the current device
	 * @param caps the caps
	 */
	protected void addDeviceCapabilities( Device currentDevice, DesiredCapabilities caps )
	{
		for ( String name : currentDevice.getCapabilities().keySet() )
			caps.setCapability( name, currentDevice.getCapabilities().get( name ) );
		
		
	}

}
