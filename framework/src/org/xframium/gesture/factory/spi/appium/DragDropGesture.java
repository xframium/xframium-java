package org.xframium.gesture.factory.spi.appium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.gesture.AbstractDragDropGesture;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class DragDropGesture extends AbstractDragDropGesture
{

    @Override
    protected boolean _executeGesture( WebDriver webDriver )
    {
        TouchAction dAction = new TouchAction( ( (AppiumDriver) ( (DeviceWebDriver) webDriver ).getNativeDriver() ) );
        
        WebElement fromElement = (WebElement) getFromElement().getNative();
        WebElement toElement = (WebElement) getToElement().getNative();
        
        switch( getInitialAction() )
        {
            case LONG_PRESS:
                dAction.longPress( fromElement );
                break;
                
            case PRESS:
                dAction.press( fromElement );
                break;
        }
        
        dAction.moveTo( toElement ).release().perform();
        
        return true;
    }

}
