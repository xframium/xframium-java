/*
 * 
 */
package com.xframium.device;

import org.openqa.selenium.WebDriver;
import com.xframium.device.factory.DeviceWebDriver;
import com.xframium.spi.Device;

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
	public WebDriver getWebDriver()
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
		return device.toShortString();  
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
