package org.xframium.device.cloud.action;

import org.xframium.device.SimpleDevice;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.bean.Handset;
import org.xframium.spi.Device;

public class PERFECTOCloudActionProvider extends AbstractCloudActionProvider
{
    @Override
    public boolean startApp( String executionId, String deviceId, String appName, String appIdentifier )
    {
        PerfectoMobile.instance().application().open( executionId, deviceId, appName, appIdentifier );
        return true;
    }

    @Override
    public boolean popuplateDevice( DeviceWebDriver webDriver, String deviceId, Device device )
    {
        try
        {
            Handset handSet = PerfectoMobile.instance().devices().getDevice( deviceId );

            device.setOs( handSet.getOs() );
            if ( device.getOs().toLowerCase().equals( "android" ) || device.getOs().toLowerCase().equals( "ios" ) )
                device.setModel( handSet.getModel() );
            
            device.setOsVersion( handSet.getOsVersion() );
            device.setResolution( handSet.getResolution() );
            device.setManufacturer( handSet.getManufacturer() );
            
            ((SimpleDevice) device).setDeviceName( deviceId );
            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }
}
