package org.xframium.page.element.natural;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.element.natural.spi.AndroidNLXPath;
import org.xframium.page.element.natural.spi.IOSNLXPath;
import org.xframium.page.element.natural.spi.WEBNLXPath;
import io.appium.java_client.AppiumDriver;

public class NLXpathFactory
{
    private static NLXpathFactory singleton = new NLXpathFactory();
    
    private IOSNLXPath ios = new IOSNLXPath();
    private AndroidNLXPath android = new AndroidNLXPath();
    private WEBNLXPath web = new WEBNLXPath();
    
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
                return web;
            else
            {
                if ( webDriver.getDevice().getOs().toUpperCase().equals( "IOS" ) )
                {
                    return ios;
                }
                else if ( webDriver.getDevice().getOs().toUpperCase().equals( "ANDROID" ) )
                {
                    return android;
                }
            }
        }
        else if ( webDriver.getNativeDriver() instanceof AppiumDriver )
        {
            if ( webDriver.getDevice().getOs().toUpperCase().equals( "IOS" ) )
            {
                return ios;
            }
            else if ( webDriver.getDevice().getOs().toUpperCase().equals( "ANDROID" ) )
            {
                return android;
            }
        }
        
        return null;
    }
}
