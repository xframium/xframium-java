package org.xframium.page.element;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class ByWrapper extends By implements ReportableBy
{
    private By by;
    private List<ByResult> resultList = new ArrayList<ByResult>( 10 );

    public ByWrapper( By by )
    {
        this.by = by;
    }

    @Override
    public List<WebElement> findElements( SearchContext context )
    {
        long startTime = System.currentTimeMillis();
        List<WebElement> returnValue = by.findElements( context );
        addResult( toString(), (int) (System.currentTimeMillis() - startTime), returnValue != null && !returnValue.isEmpty() );
        return returnValue; 
    }

    @Override
    public WebElement findElement( SearchContext context )
    {
        long startTime = System.currentTimeMillis();
        WebElement returnValue = null;
        try
        {
            returnValue = by.findElement( context );
        }
        finally
        {
            addResult( toString(), (int) (System.currentTimeMillis() - startTime), returnValue != null );
        }
        
        
        return returnValue;
    }

    @Override
    public String toString()
    {
        return by.toString();
    }
    
    public void addResult( String locator, int time, boolean success )
    {
        resultList.add( new ByResult( locator, time, success ) );
    }
    
    public List<ByResult> getResults()
    {
        return resultList;
    }
    
    

}
