package org.xframium.page.element;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.element.natural.NLXpath;
import org.xframium.page.element.natural.NLXpathFactory;
import org.xframium.page.element.natural.NaturalLanguageDescriptor;

public class ByNaturalLanguage extends By
{

    private final String queryString;
    private final NaturalLanguageDescriptor nL;
    private final DeviceWebDriver webDriver;

    public ByNaturalLanguage( String queryString, DeviceWebDriver webDriver )
    {
        this.queryString = queryString;
        nL = new NaturalLanguageDescriptor( queryString );
        this.webDriver = webDriver;
    }

    @Override
    public List<WebElement> findElements( SearchContext context )
    {
        NLXpath xPath = NLXpathFactory.instance().getGenerator( webDriver );
        List<WebElement> eList = new ArrayList<WebElement>( 10 );
        WebElement wE = xPath.getElement( webDriver, nL ); 
        if ( wE != null )
            eList.add( wE );
        
        return eList;
    }

    @Override
    public WebElement findElement( SearchContext context )
    {
        NLXpath xPath = NLXpathFactory.instance().getGenerator( webDriver );
        return xPath.getElement( webDriver, nL );
    }

    @Override
    public String toString()
    {
        return "By.naturalLanguage: " + queryString;
    }

}
