package com.xframium.device.property;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;

public class SeleniumPropertyAdapter extends AbstractPropertyAdapter
{
    private static final String IMPLICIT_TIMEOUT = "selenium.timeouts.implicitWait";
    private static final String PAGE_LOAD_TIMEOUT = "selenium.timeouts.pageLoad";
    private static final String SET_SCRIPT_TIMEOUT = "selenium.timeouts.setScript";
    
    @Override
    public boolean applyProperties( Properties configurationProperties )
    {
        return true;
    }
    
    @Override
    public boolean applyInstanceProperties( Properties configurationProperties, Object wDriver )
    {
        WebDriver webDriver = (WebDriver) wDriver;
        
        webDriver.manage().timeouts().implicitlyWait( getIntProperty( configurationProperties, IMPLICIT_TIMEOUT, 12000 ), TimeUnit.MILLISECONDS );
        webDriver.manage().timeouts().pageLoadTimeout( getIntProperty( configurationProperties, PAGE_LOAD_TIMEOUT, 45000 ), TimeUnit.MILLISECONDS );
        webDriver.manage().timeouts().setScriptTimeout( getIntProperty( configurationProperties, SET_SCRIPT_TIMEOUT, 30000 ), TimeUnit.MILLISECONDS );
        return true;
    }
    
    
}
