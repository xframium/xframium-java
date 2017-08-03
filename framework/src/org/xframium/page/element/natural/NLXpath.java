package org.xframium.page.element.natural;

import org.openqa.selenium.WebElement;
import org.xframium.device.factory.DeviceWebDriver;

public interface NLXpath
{
    public WebElement getElement( DeviceWebDriver webDriver, NaturalLanguageDescriptor nL );
    
}
