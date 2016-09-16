package org.xframium.driver.container;

import java.util.ArrayList;
import java.util.List;
import org.xframium.device.data.DataProvider.DriverType;
import org.xframium.spi.Device;

public class DeviceContainer
{
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
        if ( currentDevice.isActive() )
            activeDevices.add( currentDevice );
        else
            inactiveDevices.add( currentDevice );
    }
    
    
 
}
