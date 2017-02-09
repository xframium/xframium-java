package org.xframium.page.element.spi.set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SelectSetMethod extends AbstractSetMethod
{
    @Override
    public boolean _set( WebElement webElement, WebDriver webDriver, String value, String setType )
    {
        if ( setType.equals( "DEFAULT" ) )
        {
            Select selectElement = new Select( webElement );
            if ( selectElement.isMultiple() )
                selectElement.deselectAll();
            try
            {
                selectElement.selectByVisibleText( value );
            }
            catch ( Exception e )
            {
                selectElement.selectByValue( value );
            }
        }
        else if ( setType.equals( "BY_VISIBLE_TEXT" ) )
        {
            Select selectElement = new Select( webElement );
            if ( selectElement.isMultiple() )
                selectElement.deselectAll();

            selectElement.selectByVisibleText( value );
        }
        else if ( setType.equals( "BY_VALUE" ) )
        {
            Select selectElement = new Select( webElement );
            if ( selectElement.isMultiple() )
                selectElement.deselectAll();

            selectElement.selectByValue( value );
        }
        else if ( setType.equals( "MULTISELECT" ) )
        {
            Select multipleselect = new Select( webElement );
            String multiSelectTokens[] = value.split( "," );
            for ( String tokens : multiSelectTokens )
            {
                if ( multipleselect.isMultiple() )
                {
                    try
                    {
                        multipleselect.selectByVisibleText( tokens );
                    }
                    catch ( Exception e )
                    {
                        multipleselect.selectByValue( tokens );
                    }
                }
            }
        }
        
        return true;
    }

}
