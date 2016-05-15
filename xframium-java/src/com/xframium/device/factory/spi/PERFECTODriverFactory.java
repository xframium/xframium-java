/*
 * 
 */
package com.xframium.device.factory.spi;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.perfectomobile.selenium.MobileDriver;
import com.perfectomobile.selenium.api.IMobileDevice;
import com.perfectomobile.selenium.api.IMobileDriver;
import com.perfectomobile.selenium.options.MobileDeviceFindOptions;
import com.perfectomobile.selenium.options.MobileDeviceOS;
import com.perfectomobile.selenium.options.MobileDeviceOpenOptions;
import com.xframium.application.ApplicationRegistry;
import com.xframium.device.DeviceManager;
import com.xframium.device.artifact.api.PerfectoArtifactProducer;
import com.xframium.device.cloud.CloudRegistry;
import com.xframium.device.factory.AbstractDriverFactory;
import com.xframium.device.factory.DeviceWebDriver;
import com.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating PERFECTODriver objects.
 */
public class PERFECTODriverFactory extends AbstractDriverFactory
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.factory.AbstractDriverFactory#_createDriver(com.perfectoMobile.device.Device)
	 */
	@Override
	protected DeviceWebDriver _createDriver( Device currentDevice )
	{
		DeviceWebDriver webDriver = null;
		try
		{
			MobileDeviceFindOptions options = new MobileDeviceFindOptions();
			if ( currentDevice.getOs() != null && !currentDevice.getOs().isEmpty()  )
				options.setOS( MobileDeviceOS.valueOf( currentDevice.getOs().toUpperCase() ) );
			
			if ( currentDevice.getOsVersion() != null && !currentDevice.getOsVersion().isEmpty()  )
				options.setOSVersion( currentDevice.getOsVersion() );
			
			if ( currentDevice.getManufacturer() != null && !currentDevice.getManufacturer().isEmpty()  )
				options.setManufacturer( currentDevice.getManufacturer() );
				
			if ( currentDevice.getModel() != null && !currentDevice.getModel().isEmpty()  )
				options.setModel( currentDevice.getModel() );

			boolean webMode = false;
			boolean appMode = false;
			if (ApplicationRegistry.instance().getAUT().getUrl() != null && !ApplicationRegistry.instance().getAUT().getUrl().isEmpty() )
			{
				webMode = true;
			}
			else
			{
				if ( ApplicationRegistry.instance().getAUT().getAndroidIdentifier() != null && !ApplicationRegistry.instance().getAUT().getAndroidIdentifier().isEmpty() )
					appMode = true;
				else if ( ApplicationRegistry.instance().getAUT().getAppleIdentifier()!= null && !ApplicationRegistry.instance().getAUT().getAppleIdentifier().isEmpty() )
					appMode = true;
			}
			
			
			if ( appMode == false && webMode == false )
				throw new IllegalArgumentException( "You must provide and application or website to test" );
			
			IMobileDriver mobileDriver = new MobileDriver( CloudRegistry.instance().getCloud().getHostName(), CloudRegistry.instance().getCloud().getUserName(), CloudRegistry.instance().getCloud().getPassword() );
			IMobileDevice mobileDevice = mobileDriver.findDevice( options );
			if ( mobileDevice == null )
				throw new IllegalArgumentException( "Could not locate device" );
			
			mobileDevice.open();
			mobileDevice.home();
			
			if ( appMode )
				webDriver = new PerfectoWebDriver(mobileDevice, null, ApplicationRegistry.instance().getAUT().getName(), DeviceManager.instance().isCachingEnabled() );
			else if ( webMode )
				webDriver = new PerfectoWebDriver(mobileDevice, ApplicationRegistry.instance().getAUT().getUrl(), null, DeviceManager.instance().isCachingEnabled() );
			
			
			webDriver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );

			webDriver.setExecutionId( mobileDriver.getExecutionId() );
			webDriver.setReportKey( mobileDriver.getReportKey() );
			webDriver.setDeviceName( mobileDevice.getDeviceId() );
			webDriver.setArtifactProducer( new PerfectoArtifactProducer() );
			String interruptString = ApplicationRegistry.instance().getAUT().getCapabilities().get( "deviceInterrupts" )  != null ? (String)ApplicationRegistry.instance().getAUT().getCapabilities().get( "deviceInterrupts" ) : DeviceManager.instance().getDeviceInterrupts();
            webDriver.setDeviceInterrupts( getDeviceInterrupts( interruptString, webDriver.getExecutionId(), webDriver.getDeviceName() ) );

			//
			// If there was a URL then we are in Web Mode
			//
			if ( !appMode && webMode )
				webDriver.get( ApplicationRegistry.instance().getAUT().getUrl() );

			return webDriver;
		}
		catch (Throwable e)
		{
			log.fatal( "Could not connect to Cloud instance for " + currentDevice, e );
			if (webDriver != null)
			{
				try
				{
					webDriver.close();
				}
				catch (Exception e2)
				{
				}
				try
				{
					webDriver.quit();
				}
				catch (Exception e2)
				{
				}
			}
			return null;
		}
	}
}
