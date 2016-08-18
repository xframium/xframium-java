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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.DeviceManager;
import org.xframium.device.SimpleDevice;
import org.xframium.device.data.DataProvider;
import com.perfectomobile.selenium.util.EclipseConnector;

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

    /**
     * Instantiates a new perfecto mobile data provider.
     *
     * @param pmValidator
     *            the pm validator
     * @param driverType
     *            the driver type
     */
    public PerfectoMobilePluginProvider( DriverType driverType )
    {
        this.driverType = driverType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.device.data.DataProvider#readData()
     */
    public void readData()
    {
        try
        {
            EclipseConnector connector = new EclipseConnector();
            String eclipseHost = connector.getHost();
            if ( eclipseHost == null )
            {
                String executionId = connector.getExecutionId();
                
                SimpleDevice device = new SimpleDevice( "Plugin Device", driverType.name() );
                device.addCapability( EclipseConnector.ECLIPSE_EXECUTION_ID, executionId );
                DeviceManager.instance().registerDevice( device );
                
            }
        }
        catch ( Exception e )
        {
            log.fatal( "Could not connect to local device" );
        }
    }
}
