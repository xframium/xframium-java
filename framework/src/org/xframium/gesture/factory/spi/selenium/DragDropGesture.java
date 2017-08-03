package org.xframium.gesture.factory.spi.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.gesture.AbstractDragDropGesture;
import io.appium.java_client.AppiumDriver;

public class DragDropGesture extends AbstractDragDropGesture
{

    @Override
    protected boolean _executeGesture( WebDriver webDriver )
    {
        TouchActions dAction = new TouchActions( (WebDriver) ( (DeviceWebDriver) webDriver ).getNativeDriver() );
        
        WebElement fromElement = (WebElement) getFromElement().getNative();
        WebElement toElement = (WebElement) getToElement().getNative();
        
        dAction.clickAndHold( fromElement ).moveToElement( toElement ).release().perform();
        
        return true;
    }

}
