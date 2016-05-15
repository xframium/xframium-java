package com.xframium.gesture.factory.spi.appium;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import com.xframium.gesture.AbstractTwoFingerGesture;
import com.xframium.spi.driver.NativeDriverProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;

// TODO: Auto-generated Javadoc
/**
 * The Class TwoFingerGesture.
 */
public class TwoFingerGesture extends AbstractTwoFingerGesture
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
		
		Point startOne = getActualPoint( getStartOne(), screenDimension );
		Point startTwo = getActualPoint( getStartTwo(), screenDimension );
		Point endOne = getActualPoint( getEndOne(), screenDimension );
		Point endTwo = getActualPoint( getEndTwo(), screenDimension );
		
		MultiTouchAction tAction = new MultiTouchAction( appiumDriver );
		
		TouchAction fingerOne = new TouchAction( appiumDriver );
		fingerOne.press(  startOne.getX(), startOne.getY() ).moveTo( endOne.getX(), endOne.getY() ).release();
		
		TouchAction fingerTwo = new TouchAction( appiumDriver );
		fingerTwo.press(  startTwo.getX(), startTwo.getY() ).moveTo( endTwo.getX(), endTwo.getY() ).release();
		
		tAction.add( fingerOne ).add( fingerTwo ).perform();
		return true;
	}

}
