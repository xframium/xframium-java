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
package org.xframium.gesture.device.action.spi.perfecto;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.gesture.device.action.AbstractDefaultAction;
import org.xframium.gesture.device.action.DeviceAction;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.bean.Handset;
import org.xframium.utility.BrowserCacheLogic;

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

        ApplicationDescriptor appDesc = ApplicationRegistry.instance( ( (DeviceWebDriver) webDriver).getxFID() ).getApplication( applicationName );

        Handset localDevice = PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).devices().getDevice( deviceName );

        if ( appDesc == null || (appDesc.getUrl() != null && !appDesc.getUrl().isEmpty()) )
        {
            //
            // This is a Web URL so clear all cookies
            //

            try {
                webDriver.manage().deleteAllCookies();
            } catch (Exception e) {
                log.error("Failed to delete all Cookies.");
            }

            //
            // clear the browser cache (IOS only)
            //

            if ( (localDevice != null && localDevice.getOs().toLowerCase().equals( "ios" )) && (webDriver instanceof DeviceWebDriver) )
            {
                try
                {
                    BrowserCacheLogic.clearSafariIOSCache( (RemoteWebDriver) ((DeviceWebDriver) webDriver).getWebDriver() );
                }
                catch ( Throwable e )
                {
                    e.printStackTrace();
                }
            }
            else if ( (localDevice != null && localDevice.getOs().toLowerCase().equals( "android" )) && (webDriver instanceof DeviceWebDriver) )
            {
                try
                {
                    BrowserCacheLogic.clearChromeAndroidCache( (RemoteWebDriver) ((DeviceWebDriver) webDriver).getWebDriver() );
                }
                catch ( Throwable e )
                {
                    e.printStackTrace();
                }
            }
            
            if ( appDesc != null )
            {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                webDriver.get(appDesc.getUrl());
            }
        }
        else
        {
            if ( localDevice.getOs().toLowerCase().equals( "android" ) )
                PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).application().clean( executionId, deviceName, appDesc.getName(), appDesc.getAndroidIdentifier() );
            else if ( localDevice.getOs().toLowerCase().equals( "ios" ) )
            {
                PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).application().uninstall( executionId, deviceName, appDesc.getName(), appDesc.getAndroidIdentifier() );
                PerfectoMobile.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).application().install( executionId, deviceName, appDesc.getIosInstallation(), "instrument" );
            }
            else
                log.warn( "Could not clean application on " + localDevice.getOs() );
        }
        return true;
    }

}
