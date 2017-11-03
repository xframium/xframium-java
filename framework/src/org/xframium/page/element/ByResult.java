package org.xframium.page.element;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.element.natural.NLXpath;
import org.xframium.page.element.natural.NLXpathFactory;
import org.xframium.page.element.natural.NaturalLanguageDescriptor;

public class ByResult
{
    private String locator;
    private int time;
    private boolean success;
    
    
    
    public ByResult( String locator, int time, boolean success )
    {
        super();
        this.locator = locator;
        this.time = time;
        this.success = success;
    }
    public String getLocator()
    {
        return locator;
    }
    public void setLocator( String locator )
    {
        this.locator = locator;
    }
    public int getTime()
    {
        return time;
    }
    public void setTime( int time )
    {
        this.time = time;
    }
    public boolean isSuccess()
    {
        return success;
    }
    public void setSuccess( boolean success )
    {
        this.success = success;
    }
    
    

}
