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

import org.openqa.selenium.WebDriver;
import org.xframium.gesture.AbstractPressGesture;
import org.xframium.integrations.common.PercentagePoint;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;

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
	    String executionId = getExecutionId( webDriver );
        String deviceName = getDeviceName( webDriver );
		
		
		if ( webElement != null )
        {
            if ( webElement.getLocation() != null && webElement.getSize() != null && webElement.getSize().getWidth() > 0 && webElement.getSize().getHeight() > 0 )
            {
                int x = (int) ( ( getPressPosition().getX() / 100.0 ) * (double) webElement.getSize().getWidth() + webElement.getLocation().getX() );
                int y = (int) ( ( getPressPosition().getY() / 100.0 ) * (double) webElement.getSize().getHeight() + webElement.getLocation().getY() );
                
                PercentagePoint pressPosition = new PercentagePoint( x, y, false );
                
                for ( int i=0; i<getPressCount(); i++ )
                {
                
                    PerfectoMobile.instance().gestures().tapAt( executionId, deviceName, pressPosition );
                    try
                    {
                        Thread.sleep( 500 );
                    }
                    catch(Exception e )
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                log.warn( "A relative elements was specified however no size could be determined" );
                return false;
            }
        }
		else
		{
		    PercentagePoint pressPosition = new PercentagePoint( getPressPosition().getX(), getPressPosition().getY(), true );
		    for ( int i=0; i<getPressCount(); i++ )
            {
            
                PerfectoMobile.instance().gestures().tapAt( executionId, deviceName, pressPosition );
                try
                {
                    Thread.sleep( 500 );
                }
                catch(Exception e )
                {
                    e.printStackTrace();
                }
            }
		}
		
		
		
		return true;
	}

}
