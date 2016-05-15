package com.xframium.device.interrupt;

import com.xframium.device.interrupt.DeviceInterrupt.INTERRUPT_TYPE;

public class DeviceInterruptFactory
{
    /** The singleton. */
    private static DeviceInterruptFactory singleton = new DeviceInterruptFactory();

    /**
     * Instance.
     *
     * @return the device manager
     */
    public static DeviceInterruptFactory instance()
    {
        return singleton;
    }

    /**
     * Instantiates a new device manager.
     */
    private DeviceInterruptFactory()
    {

    }

    public DeviceInterrupt getDeviceInterrupt( INTERRUPT_TYPE interruptType, String executionId, String deviceName )
    {
        DeviceInterrupt di = null;
        switch ( interruptType )
        {
            case CALL:
                di = new CallDeviceInterrupt();
                break;
                
            case SMS:
                di = new SMSDeviceInterrupt();
                break;
                
            default:
                di = new NOOPDeviceInterrupt();
                break;  
        }
        
        di.setDeviceId( deviceName );
        di.setExecutionId( executionId );
        return di;
    }
}
