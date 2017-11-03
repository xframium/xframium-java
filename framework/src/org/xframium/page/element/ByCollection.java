package org.xframium.page.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class ByCollection extends By implements Serializable, ReportableBy
{

    private static final long serialVersionUID = 4573668832699497306L;

    private By[] bys;
    private List<ByResult> resultList = new ArrayList<ByResult>( 10 );

    public ByCollection( By... bys )
    {
        this.bys = bys;
    }

    @Override
    public WebElement findElement( SearchContext context )
    {
        List<WebElement> elements = findElements( context );
        if ( elements.isEmpty() )
        {
            throw new NoSuchElementException( "Cannot locate an element using " + toString() );
        }
        return elements.get( 0 );
    }

    @Override
    public List<WebElement> findElements( SearchContext context )
    {
        List<WebElement> elems = new ArrayList<>();
        for ( By by : bys )
        {
            List<WebElement> elementList = by.findElements( context );
            resultList.addAll( ( (ReportableBy) by ).getResults() );
            if ( elementList != null && !elementList.isEmpty())
            {
                elems.addAll( by.findElements( context ) );
                return elems;
            }
        }

        return elems;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder( "By.collection(" );
        stringBuilder.append( "{" );

        boolean first = true;
        for ( By by : bys )
        {
            stringBuilder.append( (first ? "" : ",") ).append( by );
            first = false;
        }
        stringBuilder.append( "})" );
        return stringBuilder.toString();
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
