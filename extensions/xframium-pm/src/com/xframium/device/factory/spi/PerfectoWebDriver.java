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
