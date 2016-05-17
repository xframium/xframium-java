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
package org.xframium.gesture.factory.spi.perfecto;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.gesture.AbstractPressGesture;
import org.xframium.spi.driver.NativeDriverProvider;

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
