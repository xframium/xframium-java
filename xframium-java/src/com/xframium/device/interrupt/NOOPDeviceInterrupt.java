package com.xframium.device.interrupt;

import com.xframium.device.factory.DeviceWebDriver;

public class NOOPDeviceInterrupt extends AbstractDeviceInterrupt
{

    @Override
    public void interruptDevice( DeviceWebDriver webDriver )
    {
        System.out.println( "NOOP" );
        try
        {
            Thread.sleep( 5000 );
        }
        catch( Exception e )
        {
            
        }
    }

}
