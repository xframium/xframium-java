package org.xframium.device.cloud.action;

import org.xframium.device.SimpleDevice;
import org.xframium.device.artifact.ArtifactProducer;
import org.xframium.device.artifact.api.PerfectoArtifactProducer;
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
    
    @Override
    public String getExecutionId( DeviceWebDriver webDriver )
    {
        return webDriver.getCapabilities().getCapability( "executionId" ) + "";
    }
    
    @Override
    public ArtifactProducer getArtifactProducer()
    {
        return new PerfectoArtifactProducer();
    }
    
    @Override
    public void disableLogging( DeviceWebDriver webDriver )
    {
        PerfectoMobile.instance().device().startDebug( webDriver.getExecutionId(), webDriver.getDeviceName() );
        
    }
    
    @Override
    public void enabledLogging( DeviceWebDriver webDriver )
    {
        PerfectoMobile.instance().device().stopDebug( webDriver.getExecutionId(), webDriver.getDeviceName() );
        
    }
}
