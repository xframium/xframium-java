package org.xframium.device.cloud.action;

import org.xframium.device.artifact.ArtifactProducer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.BY;
import org.xframium.spi.Device;

public interface CloudActionProvider
{
    public boolean startApp( String executionId, String deviceId, String appName, String appIdentifier );
    public boolean popuplateDevice( DeviceWebDriver webDriver, String deviceId, Device device );
    public ArtifactProducer getArtifactProducer();
    public void enabledLogging( DeviceWebDriver webDriver );
    public void disableLogging( DeviceWebDriver webDriver );
    public String getExecutionId( DeviceWebDriver webDriver );
    public String getCloudPlatformName(Device device);
    public String getCloudBrowserName(String currBrowserName);
    
    public boolean installApplication( String applicationName, DeviceWebDriver webDriver, boolean instrumentApp );
    public boolean uninstallApplication( String applicationName, DeviceWebDriver webDriver );
    public boolean openApplication( String applicationName, DeviceWebDriver webDriver );
    public boolean closeApplication( String applicationName, DeviceWebDriver webDriver );
    public boolean isDescriptorSupported( BY descriptorType );
    
}
