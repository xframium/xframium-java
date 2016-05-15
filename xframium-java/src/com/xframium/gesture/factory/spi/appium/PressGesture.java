package com.xframium.gesture.factory.spi.appium;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import com.xframium.gesture.AbstractPressGesture;
import com.xframium.spi.driver.NativeDriverProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

// TODO: Auto-generated Javadoc
/**
 * The Class PressGesture.
 */
public class PressGesture extends AbstractPressGesture
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
		Dimension screenDimension = appiumDriver.manage().window().getSize();
		
		Point pressPosition = getActualPoint( getPressPosition(), screenDimension );
		
		TouchAction swipeAction = new TouchAction( appiumDriver );
		swipeAction.press(  pressPosition.getX(), pressPosition.getY() ).waitAction( getPressLength() ).release().perform();
		
		return true;
	}

}
