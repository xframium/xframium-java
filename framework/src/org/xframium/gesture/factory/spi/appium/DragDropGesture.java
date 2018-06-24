package org.xframium.gesture.factory.spi.appium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.gesture.AbstractDragDropGesture;

import com.perfectomobile.intellij.connector.impl.client.ProcessOutputLogAdapter;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

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
            	
                dAction.longPress( LongPressOptions.longPressOptions().withElement( ElementOption.element( fromElement ) ) );
                break;
                
            case PRESS:
                dAction.press( createPoint( fromElement ) );
                break;
        }
        
        dAction.moveTo( createPoint( fromElement ) ).release().perform();
        
        return true;
    }
    
    
    

}
