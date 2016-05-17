/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package com.xframium.device.interrupt;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.xframium.device.factory.DeviceWebDriver;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;

public class CallDeviceInterrupt extends AbstractDeviceInterrupt
{
    private static final String DECLINE = "//UIAButton[@name='Decline']";
    private static final String SLIDE_DECLINE = "//*[resource-id='android:id/statusBarBackground'";
    @Override
    public void interruptDevice( DeviceWebDriver webDriver )
    {
        PerfectoMobile.instance().device().call( executionId, deviceId );
        
        try
        {
            Thread.sleep( 5000 );
            
            if ( getElement( DECLINE, webDriver ) != null )
            {
                getElement( DECLINE, webDriver ).click();
            }
            else
            {
                
            }
        }
        catch( Exception e )
        {
            
        }
    }
    
    private WebElement getElement( String elementLocation, DeviceWebDriver webDriver )
    {
        try
        {
            if ( log.isInfoEnabled() )
                log.info( "Attempting to find Call Element as " + elementLocation );
            return webDriver.findElement( By.xpath( elementLocation ) );
        }
        catch( Exception e )
        {
            return null;
        }
    }

}
