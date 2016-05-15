package com.xframium.gesture.device.action.spi.perfecto;

import java.util.List;
import org.openqa.selenium.WebDriver;
import com.xframium.gesture.device.action.AbstractDefaultAction;
import com.xframium.gesture.device.action.DeviceAction;
import com.xframium.spi.driver.NativeDriverProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class HideKeyboardAction.
 */
public class SendKeysAction extends AbstractDefaultAction implements DeviceAction
{


	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.AbstractDefaultAction#_executeAction(org.openqa.selenium.WebDriver, java.util.List)
	 */
	@Override
	public boolean _executeAction( WebDriver webDriver, List<Object> parameterList )
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
		
		appiumDriver.getKeyboard().sendKeys( (String) parameterList.get( 0 ) ); 

		
		return true;
	}

}
