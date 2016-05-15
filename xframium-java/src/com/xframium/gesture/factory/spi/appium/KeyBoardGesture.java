package com.xframium.gesture.factory.spi.appium;

import org.openqa.selenium.WebDriver;
import com.xframium.gesture.AbstractKeyBoardGesture;
import com.xframium.spi.driver.NativeDriverProvider;
import io.appium.java_client.AppiumDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyPressGesture.
 */
public class KeyBoardGesture extends AbstractKeyBoardGesture
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
		
		if ( !isShown() )
			appiumDriver.hideKeyboard();
		
		return true;
	}

}
