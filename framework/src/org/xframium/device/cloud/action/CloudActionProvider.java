package org.xframium.device.cloud.action;

import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.spi.Device;

public interface CloudActionProvider
{
    public boolean startApp( String executionId, String deviceId, String appName, String appIdentifier );
    public boolean popuplateDevice( DeviceWebDriver webDriver, String deviceId, Device device );
}
