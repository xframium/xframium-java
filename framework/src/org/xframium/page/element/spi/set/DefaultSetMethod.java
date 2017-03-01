package org.xframium.page.element.spi.set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.xframium.device.factory.MorelandWebElement;
import io.appium.java_client.ios.IOSElement;

public class DefaultSetMethod extends AbstractSetMethod
{

    @Override
    public boolean _set( WebElement webElement, WebDriver webDriver, String value, String setType )
    {
        WebElement useElement = webElement;
        
        if ( webElement instanceof MorelandWebElement )
        {
            useElement = ((MorelandWebElement) webElement).getWebElement();
        }
        
        if ( useElement instanceof IOSElement )
            ((IOSElement) useElement).setValue( value );
        else
        {
            if ( setType.equals( "DEFAULT" ) )
            {
                webElement.clear();
                webElement.sendKeys( value );
            }
            else if ( setType.equals( "SINGLE" ) )
                webElement.sendKeys( value );
        }
        
        return true;
    }

}
