package com.xframium.gesture.factory.spi.appium;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import com.xframium.gesture.AbstractSwipeGesture;
import com.xframium.spi.driver.NativeDriverProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

// TODO: Auto-generated Javadoc
/**
 * The Class SwipeGesture.
 */
public class SwipeGesture extends AbstractSwipeGesture
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
		
		Point actualStart = getActualPoint( getSwipeStart(), screenDimension );
		Point actualEnd = getActualPoint( getSwipeEnd(), screenDimension );
		
		TouchAction swipeAction = new TouchAction( appiumDriver );
		swipeAction.press( actualStart.getX(), actualStart.getY() ).moveTo( actualEnd.getX(), actualEnd.getY() ).release().perform();
		
		return true;
	}

}
