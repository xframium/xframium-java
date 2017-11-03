package org.xframium.device.keepAlive;

import org.xframium.device.factory.DeviceWebDriver;

public class ContextKeepAlive extends AbstractDeviceKeepAlive
{

    @Override
    protected boolean _keepAlive( DeviceWebDriver webDriver )
    {
        try
        {
            webDriver.getContext();
            return true;
        }
        catch( Exception e )
        {
            log.info( "Error exedcuting keep alive", e );
            return false;
        }
        
    }

}
