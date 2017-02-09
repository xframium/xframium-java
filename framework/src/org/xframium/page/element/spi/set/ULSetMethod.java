package org.xframium.page.element.spi.set;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ULSetMethod extends AbstractSetMethod
{
    @Override
    public boolean _set( WebElement webElement, WebDriver webDriver, String value, String setType )
    {
        List<WebElement> dropdownToggle = webElement.findElements( By.xpath( "./preceding-sibling::*[@uib-dropdown-toggle]" ) );
        if ( dropdownToggle.isEmpty() )
            dropdownToggle = webElement.findElements( By.xpath( "./preceding-sibling::*[@data-toggle]" ) );
        
        if ( !dropdownToggle.isEmpty() )
            dropdownToggle.get( 0 ).click();
        
        List<WebElement> selectList = webElement.findElements( By.xpath( "./li/a[contains( text(), '" + value + "')]" ) );
        
        if ( selectList.isEmpty() )
            selectList = webElement.findElements( By.xpath( ".//a[contains( text(), '" + value + "')]" ) );
        
        if ( selectList.isEmpty() )
            return false;
        else
        {
            selectList.get( 0 ).click();
        }
        return true;
    }

}
