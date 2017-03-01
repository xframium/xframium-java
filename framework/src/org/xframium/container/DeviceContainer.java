package org.xframium.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.data.XMLDataProvider;
import org.xframium.device.data.DataProvider.DriverType;
import org.xframium.spi.Device;

public class DeviceContainer
{
    private Log log = LogFactory.getLog( DeviceContainer.class );
    private DriverType dType; 
    public DriverType getdType()
    {
        return dType;
    }
    public void setdType( DriverType dType )
    {
        this.dType = dType;
    }

    private List<Device> activeDevices = new ArrayList<Device>( 10 );
    private List<Device> inactiveDevices = new ArrayList<Device>( 10 );
    private List<Device> deviceList = new ArrayList<Device>( 10 );
    private Map<String,Device> deviceMap = new HashMap<String,Device>( 10 );
    
    public List<Device> getActiveDevices()
    {
        return activeDevices;
    }
    public void setActiveDevices( List<Device> activeDevices )
    {
        this.activeDevices = activeDevices;
    }
    public List<Device> getInactiveDevices()
    {
        return inactiveDevices;
    }
    public void setInactiveDevices( List<Device> inactiveDevices )
    {
        this.inactiveDevices = inactiveDevices;
    }
    
    public void addDevice( Device currentDevice )
    {
        if ( deviceMap.containsKey( currentDevice.getKey() ) )
        {
            log.warn( "Ignoring duplicate device " + currentDevice.getKey() );
            return;
        }
        
        if ( currentDevice.isActive() )
            activeDevices.add( currentDevice );
        else
            inactiveDevices.add( currentDevice );
        
        deviceList.add( currentDevice );
        deviceMap.put( currentDevice.getKey(), currentDevice );
    }
    
    
 
}
