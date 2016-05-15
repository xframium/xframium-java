package com.xframium.gesture.factory.spi.appium;

import org.openqa.selenium.WebDriver;
import com.xframium.gesture.AbstractKeyPressGesture;
import com.xframium.spi.driver.NativeDriverProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyPressGesture.
 */
public class KeyPressGesture extends AbstractKeyPressGesture
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
		
		if ( appiumDriver == null )
			return false;
		
		if ( appiumDriver instanceof AndroidDriver )
			( (AndroidDriver) appiumDriver ).pressKeyCode( getKeyCode(), getMetaState() );
		else
		{
			log.error( "Key Press is not supported for " + appiumDriver.getClass().getName() );
			return false;
		}
			
		
		
		
		return true;
	}

}
