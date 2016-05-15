package com.xframium.gesture.factory.spi.appium;

import org.openqa.selenium.WebDriver;
import com.xframium.gesture.AbstractRotateGesture;
import com.xframium.spi.driver.NativeDriverProvider;
import io.appium.java_client.AppiumDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class RotateGesture.
 */
public class RotateGesture extends AbstractRotateGesture
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.AbstractGesture#_executeGesture(org.openqa.selenium.WebDriver)
	 */
	@Override
	protected boolean _executeGesture( WebDriver webDriver )
	{
		AppiumDriver appiumDriver = null;

		if ( webDriver instanceof AppiumDriver )
			appiumDriver = (AppiumDriver) webDriver;
		else if ( webDriver instanceof NativeDriverProvider )
		{
			NativeDriverProvider nativeProvider = (NativeDriverProvider) webDriver;
			if ( nativeProvider.getNativeDriver() instanceof AppiumDriver )
				appiumDriver = (AppiumDriver) nativeProvider.getNativeDriver();
			else
				throw new IllegalArgumentException( "Unsupported Driver Type " + webDriver );
		}
		
		if ( log.isInfoEnabled() )
			log.info( "Rotating " + getOrientation() );
		
		
		appiumDriver.rotate( getOrientation() );
		return true;
	}

}
