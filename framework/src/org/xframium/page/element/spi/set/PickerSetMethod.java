package org.xframium.page.element.spi.set;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.xframium.device.factory.MorelandWebElement;
import org.xframium.gesture.GestureManager;
import io.appium.java_client.ios.IOSElement;

public class PickerSetMethod extends AbstractSetMethod
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
        {
            try
            {
                log.info( "Attempting to select picker using sendKeys" );
                ((IOSElement) useElement).sendKeys( value );
                return true;
            }
            catch( Exception e )
            {
                
            }
            
            try
            {
                log.info( "Attempting to select picker using setValue" );
                ((IOSElement) useElement).setValue( value );
                return true;
            }
            catch( Exception e2 )
            {
                
            }
            
            
            //
            // If we are here then we need to manually swipe
            //
            log.info( "Attempting to select picker using scroll and search" );
            String previousSelection = null;
            if ( setType == null || setType.equals( "UP_FIRST" ) || setType.equals( "DEFAULT" ) )
            {
                boolean valueChanged = true;
                while( valueChanged )
                {
                    GestureManager.instance().createSwipe( new Point( 50, 55 ), new Point( 50, 45 ) ).executeGesture( webDriver, webElement );
                    try{ Thread.sleep( 500 ); } catch( Exception e ) {}
                    String currentSelection = webElement.getAttribute( "value" );
                    
                    if ( currentSelection.toLowerCase().equals( value.toLowerCase() ) )
                        return true;
                    
                    if ( currentSelection == null || currentSelection.equals( previousSelection ) )
                        valueChanged = false;
                    
                    previousSelection = currentSelection;
                }
                
                valueChanged = true;
                while( valueChanged )
                {
                    GestureManager.instance().createSwipe( new Point( 50, 45 ), new Point( 50, 55 ) ).executeGesture( webDriver, webElement );
                    try{ Thread.sleep( 500 ); } catch( Exception e ) {}
                    String currentSelection = webElement.getAttribute( "value" );
                    
                    if ( currentSelection.toLowerCase().equals( value.toLowerCase() ) )
                        return true;
                    
                    if ( currentSelection == null || currentSelection.equals( previousSelection ) )
                        valueChanged = false;
                    
                    previousSelection = currentSelection;
                }
            }
            else
            {
                boolean valueChanged = true;
                while( valueChanged )
                {
                    GestureManager.instance().createSwipe( new Point( 50, 45 ), new Point( 50, 55 ) ).executeGesture( webDriver, webElement );
                    try{ Thread.sleep( 500 ); } catch( Exception e ) {}
                    String currentSelection = webElement.getAttribute( "value" );
                    
                    if ( currentSelection.toLowerCase().equals( value.toLowerCase() ) )
                        return true;
                    
                    if ( currentSelection == null || currentSelection.equals( previousSelection ) )
                        valueChanged = false;
                    
                    previousSelection = currentSelection;
                }
                
                valueChanged = true;
                while( valueChanged )
                {
                    GestureManager.instance().createSwipe( new Point( 50, 55 ), new Point( 50, 45 ) ).executeGesture( webDriver, webElement );
                    try{ Thread.sleep( 500 ); } catch( Exception e ) {}
                    String currentSelection = webElement.getAttribute( "value" );
                    
                    if ( currentSelection.toLowerCase().equals( value.toLowerCase() ) )
                        return true;
                    
                    if ( currentSelection == null || currentSelection.equals( previousSelection ) )
                        valueChanged = false;
                    
                    previousSelection = currentSelection;
                }
            }
        }
        
        return false;
    }

}
