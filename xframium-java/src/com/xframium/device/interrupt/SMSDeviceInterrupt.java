package com.xframium.device.interrupt;

import com.xframium.device.factory.DeviceWebDriver;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;

public class SMSDeviceInterrupt extends AbstractDeviceInterrupt
{

    @Override
    public void interruptDevice( DeviceWebDriver webDriver )
    {
        PerfectoMobile.instance().device().sendText( executionId, "Test from SMSDeviceInterrupt", deviceId );
    }

}
