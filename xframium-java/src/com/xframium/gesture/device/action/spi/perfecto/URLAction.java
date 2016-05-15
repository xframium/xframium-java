package com.xframium.gesture.device.action.spi.perfecto;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.xframium.gesture.device.action.AbstractDefaultAction;
import com.xframium.gesture.device.action.DeviceAction;
import com.xframium.spi.driver.NativeDriverProvider;
import io.appium.java_client.AppiumDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class URLAction.
 */
public class URLAction extends AbstractDefaultAction implements DeviceAction
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.device.action.AbstractDefaultAction#_executeAction(org.openqa.selenium.WebDriver, java.util.List)
	 */
	@Override
	public boolean _executeAction( WebDriver webDriver, List<Object> parameterList )
	{
		webDriver.navigate().to( parameterList.get( 0 ) + "" );
		return true;
	}

}
