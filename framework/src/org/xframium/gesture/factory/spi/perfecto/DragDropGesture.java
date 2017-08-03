package org.xframium.gesture.factory.spi.perfecto;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.gesture.AbstractDragDropGesture;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class DragDropGesture extends AbstractDragDropGesture
{

    @Override
    protected boolean _executeGesture( WebDriver webDriver )
    {
        WebElement fromElement = (WebElement) getFromElement().getNative();
        Point fromPoint = new Point( fromElement.getLocation().getX() + ( fromElement.getSize().getWidth() / 2 ), fromElement.getLocation().getY() + ( fromElement.getSize().getHeight() / 2 ) );
        Point toPoint = new Point( fromElement.getLocation().getX() + ( fromElement.getSize().getWidth() / 2 ), fromElement.getLocation().getY() + ( fromElement.getSize().getHeight() / 2 ) );
        
        TouchActions tActions = new TouchActions( (WebDriver) ( (DeviceWebDriver) webDriver).getNativeDriver() );
        tActions.down( fromPoint.getX(), fromPoint.getY() ).move( toPoint.getX(), toPoint.getY() ).release().perform();
        return true;
    }

}
