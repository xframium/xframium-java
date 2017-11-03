package org.xframium.page.element.spi.set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SpinnerSetMethod extends AbstractSetMethod
{
    @Override
    public boolean _set( WebElement webElement, WebDriver webDriver, String value, String setType, String xFID )
    {
        webElement.click();
        webDriver.findElement( By.xpath( "//android.widget.TextView[@text='" + value + "']" ) ).click();
        
        return true;
    }

}
