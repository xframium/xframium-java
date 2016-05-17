/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.gesture.factory.spi.appium;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.xframium.gesture.AbstractTwoFingerGesture;
import org.xframium.spi.driver.NativeDriverProvider;
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
