package org.xframium.device.keepAlive;

import org.openqa.selenium.OutputType;
import org.xframium.device.factory.DeviceWebDriver;

public class SnapshotKeepAlive extends AbstractDeviceKeepAlive
{

    @Override
    protected boolean _keepAlive( DeviceWebDriver webDriver )
    {
        try
        {
            webDriver.getScreenshotAs( OutputType.BYTES );
            return true;
        }
        catch( Exception e )
        {
            log.info( "Error exedcuting keep alive", e );
            return false;
        }
        
    }

}
