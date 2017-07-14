package org.xframium.page.element.natural;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.element.natural.spi.WEBNLXPath;
import io.appium.java_client.AppiumDriver;

public class NLXpathFactory
{
    private static NLXpathFactory singleton = new NLXpathFactory();
    
    public static NLXpathFactory instance()
    {
        return singleton;
    }
    
    private NLXpathFactory()
    {
        
    }
    
    public NLXpath getGenerator( DeviceWebDriver webDriver )
    {
        if ( webDriver.getNativeDriver() instanceof RemoteWebDriver )
        {
            if ( webDriver.getContext() == null || webDriver.getContext().contains( "WEB" ) )
                return new WEBNLXPath();
            else
            {
                
            }
        }
        else if ( webDriver.getNativeDriver() instanceof AppiumDriver )
        {
            if ( webDriver.getDevice().getOs().toUpperCase().equals( "IOS" ) )
            {
                
            }
            else if ( webDriver.getDevice().getOs().toUpperCase().equals( "ANDROID" ) )
            {
                
            }
        }
        
        return null;
    }
}
