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
/*
 * 
 */
package org.xframium.device;

import org.openqa.selenium.WebDriver;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class ConnectedDevice.
 */
public class ConnectedDevice
{
	
	/** The web driver. */
	private DeviceWebDriver webDriver;
	
	/** The device. */
	private Device device;
	
	/** The persona. */
	private String persona;
	
	private boolean success;
	
	public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess( boolean success )
    {
        this.success = success;
    }

    /**
	 * Instantiates a new connected device.
	 *
	 * @param webDriver the web driver
	 * @param device the device
	 * @param persona the persona
	 */
	public ConnectedDevice( DeviceWebDriver webDriver, Device device, String persona )
	{
		super();
		this.webDriver = webDriver;
		this.device = device;
		this.persona = persona;
	}

	/**
	 * Gets the persona.
	 *
	 * @return the persona
	 */
	public String getPersona() {
		return persona;
	}

	/**
	 * Sets the persona.
	 *
	 * @param persona the new persona
	 */
	public void setPersona(String persona) {
		this.persona = persona;
	}

	/**
	 * Instantiates a new connected device.
	 */
	public ConnectedDevice()
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Gets the web driver.
	 *
	 * @return the web driver
	 */
	public DeviceWebDriver getWebDriver()
	{
		return webDriver;
	}
	
	/**
	 * Sets the web driver.
	 *
	 * @param webDriver the new web driver
	 */
	public void setWebDriver( DeviceWebDriver webDriver )
	{
		this.webDriver = webDriver;
	}
	
	/**
	 * Gets the device.
	 *
	 * @return the device
	 */
	public Device getDevice()
	{
		return device;
	}
	
	public Device getPopulatedDevice()
    {
	    return webDriver.getPopulatedDevice();
    }
	
	/**
	 * Sets the device.
	 *
	 * @param device the new device
	 */
	public void setDevice( Device device )
	{
		this.device = device;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
	    if ( webDriver != null && webDriver.getPopulatedDevice() != null )
	        return webDriver.getPopulatedDevice().getEnvironment();
	    else if ( device != null )
	        return device.getEnvironment();
	    else
	        return "Unknown Device";
	}
	
	public String getExecutionId()
	{
	    return webDriver.getExecutionId();
	}
	
	public String getDeviceName()
	{
	    return webDriver.getDeviceName();
	}
}
