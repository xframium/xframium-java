package com.xframium.gesture.factory.spi.perfecto;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.xframium.gesture.AbstractPressGesture;
import com.xframium.spi.driver.NativeDriverProvider;

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
		RemoteWebDriver remoteDriver = null;
		
		if ( webDriver instanceof RemoteWebDriver )
			remoteDriver = (RemoteWebDriver) webDriver;
		else if ( webDriver instanceof NativeDriverProvider )
		{
			NativeDriverProvider nativeProvider = (NativeDriverProvider) webDriver;
			if ( nativeProvider.getNativeDriver() instanceof RemoteWebDriver )
				remoteDriver = (RemoteWebDriver) nativeProvider.getNativeDriver();
			else
				throw new IllegalArgumentException( "Unsupported Driver Type " + webDriver );
		}
		
		Dimension screenDimension = remoteDriver.manage().window().getSize();
		
		Point pressPosition = getActualPoint( getPressPosition(), screenDimension );
		
		TouchActions swipeAction = new TouchActions( remoteDriver );
		swipeAction.down(  pressPosition.getX(), pressPosition.getY() ).up(pressPosition.getX(), pressPosition.getY()).perform();
		
		return true;
	}

}
