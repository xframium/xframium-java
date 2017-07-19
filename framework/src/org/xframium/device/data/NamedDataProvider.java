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
/*
 * 
 */
package org.xframium.device.data;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.DeviceManager;
import org.xframium.device.SimpleDevice;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.bean.Handset;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class CSVDataProvider.
 */
public class NamedDataProvider implements DataProvider
{

    /** The log. */
    private Log log = LogFactory.getLog( NamedDataProvider.class );

    /** The named resources. */
    private String[] namedResources = null;

    /** The driver type. */
    private DriverType driverType;

    /**
     * Instantiates a new named data provider.
     *
     * @param resourceName
     *            A comma separated list of resource names
     * @param driverType
     *            the driver type
     */
    public NamedDataProvider( String resourceName, DriverType driverType )
    {
        namedResources = resourceName.split( "," );
        this.driverType = driverType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.device.data.DataProvider#readData()
     */
    public List<Device> readData()
    {
        List<Device> deviceList = new ArrayList<Device>( 10 );
        for ( String device : namedResources )
        {
            deviceList.add( lookupDeviceById( device, driverType ) );
        }

        return deviceList;
    }

    public static Device lookupDeviceById( String device, DriverType driverType )
    {
        if ( device.equals( "FIREFOX" ) )
        {
            return new SimpleDevice( device, "Mozilla", "Windows", "Windows", null, "firefox", null, 1, "WEB", true, device );
        }
        else if ( device.equals( "CHROME" ) )
        {
            return new SimpleDevice( device, "Google", "Windows", "Windows", null, "Chrome", null, 1, "WEB", true, device );
        }
        else if ( device.equals( "INTERNET EXPLORER" ) )
        {
            return new SimpleDevice( device, "Microsoft", "Windows", "Windows", null, "internet explorer", null, 1, "WEB", true, device );
        }

        Handset handset = PerfectoMobile.instance().devices().getDevice( device );

        String driverName = "";
        switch ( driverType )
        {
            case APPIUM:
                if ( handset.getOs().equals( "iOS" ) )
                    driverName = "IOS";
                else if ( handset.getOs().equals( "Android" ) )
                    driverName = "ANDROID";
                else
                    throw new IllegalArgumentException( "Appium is not supported on the following OS " + handset.getOs() );
                break;

            case PERFECTO:
                driverName = "PERFECTO";
                break;

            case WEB:
                driverName = "WEB";
                break;
        }

        return new SimpleDevice( device, handset.getManufacturer(), handset.getModel(), handset.getOs(), handset.getOsVersion(), null, null, 1, driverName, true, device );
    }

}
