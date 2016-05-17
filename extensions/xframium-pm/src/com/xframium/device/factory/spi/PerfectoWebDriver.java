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
package com.xframium.device.factory.spi;

import org.openqa.selenium.WebDriver;
import com.perfectomobile.selenium.api.IMobileDevice;
import com.perfectomobile.selenium.nativeapp.MobileNativeApplication;
import com.xframium.application.ApplicationRegistry;
import com.xframium.device.DeviceManager;
import com.xframium.device.factory.DeviceWebDriver;
import com.xframium.spi.driver.VisualDriverProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class PerfectoWebDriver.
 */
public class PerfectoWebDriver extends DeviceWebDriver implements VisualDriverProvider
{
	
	/** The mobile device. */
	private IMobileDevice mobileDevice;
	
	/**
	 * Instantiates a new perfecto web driver.
	 *
	 * @param mobileDevice the mobile device
	 * @param browserUrl the browser url
	 * @param applicationName the application name
	 * @param cachingEnabled the caching enabled
	 */
	public PerfectoWebDriver( IMobileDevice mobileDevice, String browserUrl, String applicationName, boolean cachingEnabled )
	{
		super( browserUrl == null ? mobileDevice.getNativeDriver( applicationName ) : mobileDevice.getDOMDriver(), DeviceManager.instance().isCachingEnabled(), null );
		if ( browserUrl != null )
			getWebDriver().get( ApplicationRegistry.instance().getAUT().getUrl() );
		else
			( (MobileNativeApplication) getWebDriver() ).open();
		
		this.mobileDevice = mobileDevice;
		
	}
	
	/**
	 * Gets the mobile device.
	 *
	 * @return the mobile device
	 */
	public IMobileDevice getMobileDevice()
	{
		return mobileDevice;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.factory.DeviceWebDriver#close()
	 */
	@Override
	public void close()
	{
		super.close();
		mobileDevice.close();
	}
	
	
	/* (non-Javadoc)
	 * @see com.morelandLabs.spi.driver.VisualDriverProvider#getVisualDriver()
	 */
	public WebDriver getVisualDriver()
	{
		return mobileDevice.getVisualDriver();
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.factory.DeviceWebDriver#quit()
	 */
	@Override
	public void quit()
	{
		
		super.quit();
	}
	
	
}
