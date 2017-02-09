package org.xframium.page.element.spi.set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface SetMethod
{
    public boolean set( WebElement webElement, WebDriver webDriver, String value, String setType );
}
