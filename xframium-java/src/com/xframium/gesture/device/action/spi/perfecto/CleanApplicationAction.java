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
package com.xframium.gesture.device.action.spi.perfecto;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.xframium.application.ApplicationDescriptor;
import com.xframium.application.ApplicationRegistry;
import com.xframium.gesture.device.action.AbstractDefaultAction;
import com.xframium.gesture.device.action.DeviceAction;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import com.xframium.integrations.perfectoMobile.rest.bean.Handset;
import com.xframium.utility.BrowserCacheLogic;

/**
 * The Class CleanApplicationAction.
 */
public class CleanApplicationAction extends AbstractDefaultAction implements DeviceAction
{

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.gesture.device.action.AbstractDefaultAction#
     * _executeAction(org.openqa.selenium.WebDriver, java.util.List)
     */
    @Override
    public boolean _executeAction( WebDriver webDriver, List<Object> parameterList )
    {
        String executionId = getExecutionId( webDriver );
        String deviceName = getDeviceName( webDriver );

        String applicationName = (String) parameterList.get( 0 );

        ApplicationDescriptor appDesc = ApplicationRegistry.instance().getApplication( applicationName );

        Handset localDevice = PerfectoMobile.instance().devices().getDevice( deviceName );

        if ( appDesc.getUrl() != null && !appDesc.getUrl().isEmpty() )
        {
            //
            // This is a Web URL so clear all cookies
            //

            webDriver.manage().deleteAllCookies();

            //
            // clear the browser cache (IOS only)
            //

            if ( (localDevice.getOs().toLowerCase().equals( "ios" )) && (webDriver instanceof RemoteWebDriver) )
            {
                try
                {
                    BrowserCacheLogic.clearSafariIOSCache( (RemoteWebDriver) webDriver );
                }
                catch ( Throwable e )
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            if ( localDevice.getOs().toLowerCase().equals( "android" ) )
                PerfectoMobile.instance().application().clean( executionId, deviceName, appDesc.getName(), appDesc.getAndroidIdentifier() );
            else
                log.warn( "Could not clean application on " + localDevice.getOs() );
        }
        return true;
    }

}
