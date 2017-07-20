package org.xframium.page.element.spi.set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class AbstractSetMethod implements SetMethod
{
    protected Log log = LogFactory.getLog( AbstractSetMethod.class );
    protected abstract boolean _set( WebElement webElement, WebDriver webDriver, String value, String setType, String xFID );
    @Override
    public boolean set( WebElement webElement, WebDriver webDriver, String value, String setType, String xFID )
    {
        if ( log.isInfoEnabled())
            log.info( Thread.currentThread().getName() + ": Setting value to " + value + " using [" + getClass().getSimpleName() + "]" );
        return _set( webElement, webDriver, value, setType, xFID );
    }

}
