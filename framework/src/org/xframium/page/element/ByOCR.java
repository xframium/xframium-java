package org.xframium.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.factory.DeviceWebDriver;

public class ByOCR extends By
{

    private String text;
    private DeviceWebDriver webDriver;
    private Map<String,String> propertyMap;
    
    public ByOCR( String text, Map<String,String> propertyMap, DeviceWebDriver webDriver )
    {
        this.text = text;
        this.propertyMap = propertyMap;
        this.webDriver = webDriver;
    }
    
    @Override
    public List<WebElement> findElements( SearchContext context )
    {
        if ( text != null )
        {
            Rectangle r = webDriver.getCloud().getCloudActionProvider().findText( webDriver, text, propertyMap );
            if ( r != null )
            {
                List<WebElement> elementList = new ArrayList<WebElement>( 1 );
                elementList.add( new VisualWebElement( webDriver, r ) );
                return elementList;
            }
        }
        
        return null;
    }

}
