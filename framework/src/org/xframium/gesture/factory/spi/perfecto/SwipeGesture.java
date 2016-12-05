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

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.gesture.AbstractSwipeGesture;
import org.xframium.integrations.common.PercentagePoint;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;

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
		
		String executionId = getExecutionId( webDriver );
		String deviceName = getDeviceName( webDriver );
		if ( executionId != null && deviceName != null )
		{
			
			if ( webElement != null )
			{
				if ( webElement.getLocation() != null && webElement.getSize() != null && webElement.getSize().getWidth() > 0 && webElement.getSize().getHeight() > 0 )
				{
					int x = (int)( ( getSwipeStart().getX() / 100.0 ) * (double) webElement.getSize().getWidth() + webElement.getLocation().getX() );
					int y = (int) ( ( getSwipeStart().getY() / 100.0 ) * (double) webElement.getSize().getHeight() + webElement.getLocation().getY() );
					Point swipeStart = new Point( x, y );
					
					x = (int) ( ( getSwipeEnd().getX() / 100.0 ) * (double) webElement.getSize().getWidth() + webElement.getLocation().getX() );
					y = (int) ( ( getSwipeEnd().getY() / 100.0 ) * (double) webElement.getSize().getHeight() + webElement.getLocation().getY() );
					Point swipeEnd = new Point( x, y );
					
					Map<String, Object> params = new HashMap<>();
					params.put( "start", new PercentagePoint( swipeStart.getX(), swipeStart.getY(), false ).toString() );
					params.put( "end", new PercentagePoint( swipeEnd.getX(), swipeEnd.getY(), false ).toString() );
					params.put( "duration", "1" );
					String result = ((RemoteWebDriver) ((DeviceWebDriver) webDriver).getWebDriver()).executeScript("mobile:touch:swipe", params) + "";
					//PerfectoMobile.instance().gestures().swipe( executionId, deviceName, new PercentagePoint( swipeStart.getX(), swipeStart.getY(), false ), new PercentagePoint( swipeEnd.getX(), swipeEnd.getY(), false ), 3 );
					return true;
				}
				else
				{
					log.warn( "A relative elements was specified however no size could be determined" );
					return false;
				}
			}

			Map<String, Object> params = new HashMap<>();
			params.put( "start", new PercentagePoint( getSwipeStart().getX(), getSwipeStart().getY() ).toString() );
			params.put( "end", new PercentagePoint( getSwipeEnd().getX(), getSwipeEnd().getY() ).toString() );
			params.put( "duration", "1" );
			String result = ((RemoteWebDriver) ((DeviceWebDriver) webDriver).getWebDriver()).executeScript("mobile:touch:swipe", params) + "";
			
			//PerfectoMobile.instance().gestures().swipe( executionId, deviceName, new PercentagePoint( getSwipeStart().getX(), getSwipeStart().getY() ), new PercentagePoint( getSwipeEnd().getX(), getSwipeEnd().getY() ), 3 );
			return true;
		}
		else
			return false;
	}

}
