package org.xframium.device.cloud.action;

import org.xframium.spi.Device;

public interface CloudActionProvider
{
    public boolean startApp( String executionId, String deviceId, String appName, String appIdentifier );
    public void popuplateDevice( String deviceId, Device device );
}
