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
package org.xframium.device.data.perfectoMobile;

import com.perfectomobile.intellij.connector.ConnectorConfiguration;
import com.perfectomobile.intellij.connector.impl.client.ClientSideLocalFileSystemConnector;
import com.perfectomobile.intellij.connector.impl.client.ProcessOutputLogAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.DeviceManager;
import org.xframium.device.SimpleDevice;
import org.xframium.device.data.DataProvider;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.bean.Handset;
import org.xframium.spi.Device;
import com.perfectomobile.selenium.util.EclipseConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class PerfectoMobileDataProvider.
 */
public class PerfectoMobilePluginProvider implements DataProvider
{

    /** The log. */
    private Log log = LogFactory.getLog( DataProvider.class );

    /** The driver type. */
    private DriverType driverType;
    private String deviceId;
    private String pluginType;

    /**
     * Instantiates a new perfecto mobile data provider.
     *
     * @param pmValidator
     *            the pm validator
     * @param driverType
     *            the driver type
     */
    public PerfectoMobilePluginProvider( String deviceId, DriverType driverType, String pluginType )
    {
        this.driverType = driverType;
        this.deviceId = deviceId;
        this.pluginType = pluginType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.device.data.DataProvider#readData()
     */
    public List<Device> readData()
    {
        List<Device> deviceList = new ArrayList<Device>( 10 );
        try
        {
            SimpleDevice device = (SimpleDevice)lookupDeviceById( deviceId, driverType );
            String executionId = null;
            if ( PluginType.INTELLIJ.toString().equals( pluginType ) )
            {
                ClientSideLocalFileSystemConnector intellijConnector = new ClientSideLocalFileSystemConnector( new ProcessOutputLogAdapter( System.err, System.out, System.out, System.out ) );
                ConnectorConfiguration connectorConfiguration = intellijConnector.getConnectorConfiguration();
                if ( connectorConfiguration != null && connectorConfiguration.getHost() != null )
                {
                    executionId = connectorConfiguration.getExecutionId();
                }
            }
            else
            {
                EclipseConnector connector;
                try
                {
                    connector = new EclipseConnector();
                    if ( connector.getHost() != null )
                    {
                        executionId = connector.getExecutionId();
                    }
                } catch ( IOException e )
                {
                    log.error( "Eclipse Connector Plugin socket not found" );
                }
            }

            if (executionId != null)
            {
                device.addCapability( EclipseConnector.ECLIPSE_EXECUTION_ID, executionId );
            }
            deviceList.add( device );
            return deviceList;
        }
        catch ( Exception e )
        {
            log.fatal( "Could not connect to local device" );
            return null;
        }
    }
    
    private Device lookupDeviceById( String device, DriverType driverType )
    {
        
        Handset handset = PerfectoMobile.instance().devices().getDevice( device );
            
        String driverName = "";
        switch( driverType )
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
            
        return new SimpleDevice( device,
                                 handset.getManufacturer(),
                                 handset.getModel(),
                                 handset.getOs(),
                                 handset.getOsVersion(),
                                 null,
                                 null,
                                 1,
                                 driverName,
                                 true,
                                 device );
    }
}
