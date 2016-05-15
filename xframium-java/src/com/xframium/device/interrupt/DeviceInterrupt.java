package com.xframium.device.interrupt;

import com.xframium.device.factory.DeviceWebDriver;

public interface DeviceInterrupt
{
    public enum INTERRUPT_TYPE
    {
        NOOP,
        CALL,
        SMS;
    }
    
    public void interruptDevice( DeviceWebDriver webDriver );
    
    public void setExecutionId( String executionId );
    public void setDeviceId( String deviceId );
}
