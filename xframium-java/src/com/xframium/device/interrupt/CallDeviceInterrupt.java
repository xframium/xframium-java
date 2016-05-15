package com.xframium.device.interrupt;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.xframium.device.factory.DeviceWebDriver;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;

public class CallDeviceInterrupt extends AbstractDeviceInterrupt
{
    private static final String DECLINE = "//UIAButton[@name='Decline']";
    private static final String SLIDE_DECLINE = "//*[resource-id='android:id/statusBarBackground'";
    @Override
    public void interruptDevice( DeviceWebDriver webDriver )
    {
        PerfectoMobile.instance().device().call( executionId, deviceId );
        
        try
        {
            Thread.sleep( 5000 );
            
            if ( getElement( DECLINE, webDriver ) != null )
            {
                getElement( DECLINE, webDriver ).click();
            }
            else
            {
                
            }
        }
        catch( Exception e )
        {
            
        }
    }
    
    private WebElement getElement( String elementLocation, DeviceWebDriver webDriver )
    {
        try
        {
            if ( log.isInfoEnabled() )
                log.info( "Attempting to find Call Element as " + elementLocation );
            return webDriver.findElement( By.xpath( elementLocation ) );
        }
        catch( Exception e )
        {
            return null;
        }
    }

}
