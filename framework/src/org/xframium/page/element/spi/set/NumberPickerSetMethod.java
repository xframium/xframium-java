package org.xframium.page.element.spi.set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.factory.MorelandWebElement;
import org.xframium.exception.ScriptException;
import org.xframium.page.element.SeleniumElement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;

public class NumberPickerSetMethod extends AbstractSetMethod
{
    @Override
    public boolean _set( WebElement webElement, WebDriver webDriver, String value, String setType, String xFID )
    {
        WebElement selectedValue = null;
        try
        {
            selectedValue = webElement.findElement( By.xpath( "./android.widget.EditText" ) );
        }
        catch( Exception e )
        {
            //
            // If this fails then exit out as failed
            //
            selectedValue = webElement.findElement( By.xpath( "./android.widget.TextView" ) );
        }
        
        
        int originalValue = 0;
        int setValue = 0;
        try
        {
            originalValue = Integer.parseInt( selectedValue.getAttribute( "text" ) );
            setValue = Integer.parseInt( value );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            throw new ScriptException( "Could not determine original value for NumberPicker" );
        }
        
        while ( !value.equals( selectedValue.getAttribute( "text" ) ) )
        {
            String currentValue = selectedValue.getAttribute( "text" );
            if ( setValue < originalValue )
            {
                clickAt( webElement, webDriver, 50, 25 );
            }
            else
                clickAt( webElement, webDriver, 50, 75 );
            
            try
            {
                Thread.sleep( 500 );
            }
            catch( Exception e )
            {
                if ( currentValue.equals( selectedValue.getAttribute( "text" ) ) )
                    throw new ScriptException( "Could not find specified value" );
            }
        }
        
        return true;
    }
    
    private void clickAt( WebElement webElement, WebDriver webDriver, int x, int y )
    {
        if ( webElement != null )
        {

            Dimension elementSize = webElement.getSize();

            int useX = (int) ((double) elementSize.getWidth() * ((double) x / 100.0)) + webElement.getLocation().getX();
            int useY = (int) ((double) elementSize.getHeight() * ((double) y / 100.0)) + webElement.getLocation().getY();


            if ( ((DeviceWebDriver) webDriver).getNativeDriver() instanceof AppiumDriver )
            {
                new TouchAction( (AppiumDriver) ((DeviceWebDriver) webDriver).getNativeDriver() ).press( PointOption.point( useX, useY ) ).perform();
            }
            else if ( ((DeviceWebDriver) webDriver).getNativeDriver() instanceof RemoteWebDriver )
            {
                if ( ((DeviceWebDriver) webDriver).getNativeDriver() instanceof HasTouchScreen )
                    new TouchActions( webDriver ).move( useX, useY ).click().build().perform();
                else
                    new Actions( webDriver ).moveByOffset(useX,  useY ).click().build().perform();
            }

        }
    }

}
