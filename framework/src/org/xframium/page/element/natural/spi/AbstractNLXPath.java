package org.xframium.page.element.natural.spi;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.element.natural.NLXpath;
import org.xframium.page.element.natural.NaturalLanguageDescriptor;

public abstract class AbstractNLXPath implements NLXpath
{
    public abstract String generateXPath( NaturalLanguageDescriptor nL );
    
    @Override
    public WebElement getElement( DeviceWebDriver webDriver, NaturalLanguageDescriptor nL )
    {
        List<WebElement> elementList = webDriver.findElements( By.xpath( generateXPath( nL ) ) );

        if ( !elementList.isEmpty() )
        {
            int useIndex = 0;
            
            switch( nL.getPosition() )
            {
                case FIRST:
                    useIndex = 0;
                    break;
                    
                case SECOND:
                    useIndex = 1;
                    break;
                        
                case THIRD:
                    useIndex = 2;
                    break;
                    
                case FOURTH:
                    useIndex = 3;
                    break;
                    
                case FIFTH:
                    useIndex = 4;
                    break;
                    
                case SIXTH:
                    useIndex = 5;
                    break;
                    
                case SEVENTH:
                    useIndex = 6;
                    break;
                    
                case EIGHTH:
                    useIndex = 7;
                    break;
                    
                case NINTH:
                    useIndex = 8;
                    break;
                    
                case TENTH:
                    useIndex = 9;
                    break;
                    
                case LAST:
                    useIndex = elementList.size() - 1;
                    break;
                    
                case ONLY:
                    if ( elementList.size() == 1 )
                        return elementList.get( 0 );
                    else
                        return null;
                    
            }
            
            if ( elementList.size() >= useIndex )
                return elementList.get( useIndex );
            else
                return null;
        }
        
        
        
        
        // TODO Auto-generated method stub
        return null;
    }
}
