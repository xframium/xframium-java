package org.xframium.device.cloud.action;

import org.openqa.selenium.Dimension;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.spi.Device;
import eu.bitwalker.useragentutils.UserAgent;

public class SELENIUMCloudActionProvider extends AbstractCloudActionProvider
{
    @Override
    public boolean startApp( String executionId, String deviceId, String appName, String appIdentifier )
    {
        
        return true;
    }
    
    @Override
    public boolean popuplateDevice( DeviceWebDriver webDriver, String deviceId, Device device )
    {
        String uAgent = (String) webDriver.executeScript("return navigator.userAgent;");
        UserAgent userAgent = new UserAgent( uAgent );
        device.setBrowserName( userAgent.getBrowser().getName() );
        device.setManufacturer( userAgent.getOperatingSystem().getManufacturer().getName() );
        
        Dimension winDim = webDriver.manage().window().getSize();
        if ( winDim != null )
            device.setResolution( winDim.getWidth() + " x " + winDim.height );
        else
            device.setResolution( null );
        
        return true;
    }
}
