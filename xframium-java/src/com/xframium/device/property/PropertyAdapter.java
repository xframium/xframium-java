package com.xframium.device.property;

import java.util.Properties;

public interface PropertyAdapter
{
    public enum PropertyApplication
    {
        Selenium;        
    }
    
    public boolean applyProperties( Properties configurationProperties );
    public boolean applyInstanceProperties( Properties configurationProperties, Object webDriver );
}
