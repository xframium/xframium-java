package com.xframium.device.interrupt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractDeviceInterrupt implements DeviceInterrupt
{
    protected String executionId;
    protected String deviceId;
    /** The log. */
    protected Log log = LogFactory.getLog( DeviceInterrupt.class );

    @Override
    public void setExecutionId( String executionId )
    {
        this.executionId = executionId;
        
    }

    @Override
    public void setDeviceId( String deviceId )
    {
        this.deviceId = deviceId;
    }

}
