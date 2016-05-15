package com.xframium.page.keyWord.step.spi;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import com.xframium.page.Page;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.KeyWordParameter;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSValue.
 */
public class KWSMath extends AbstractKeyWordStep
{
    
    /**
     * Format string.
     *
     * @param parameterValue the parameter value
     * @return the string
     */
    private String formatString( String parameterValue )
    {
        return parameterValue.replace( "$", " " ).replace( "%", " " ).trim();
    }
    
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
	    String compareToString = null;
	    double currentValue = 0;
	    for ( int i=0; i<getParameterList().size(); i++ )
	    {
	        boolean valueAdded = false;

	        String currentParameter = getParameterValue( getParameterList().get( i ), contextMap, dataMap ) + "";
	        
	        if ( getParameterList().get( i ).getValue().startsWith( "=" ) )
	        {
	            KeyWordParameter localParam = new KeyWordParameter( getParameterList().get( i ).getType(), getParameterList().get( i ).getValue().substring( 1 ) );
	            compareToString = getParameterValue( localParam, contextMap, dataMap ) + "";
	        }
	        else
	        {
	            switch ( getName().toLowerCase() )
	            {
	                case "add":
	                    currentValue += Double.parseDouble( formatString( currentParameter ) );
	                    break;
	                    
	                case "subtract":
	                    if ( valueAdded )
	                        currentValue -= Double.parseDouble( formatString( currentParameter ) );
	                    else
	                    {
	                        currentValue = Double.parseDouble( formatString( currentParameter ) );
	                        valueAdded = true;
	                    }
	            }
	        }
	    }
	    
	    if ( compareToString != null )
	    {
	        double compareTo = Double.parseDouble( formatString( compareToString ) );
	        
	        if ( compareTo != currentValue )
                throw new IllegalStateException( "GET Expected [" + compareTo + "] but found [" + currentValue + "]" );
	    }
		
		if ( !validateData( currentValue + "" ) )
            throw new IllegalStateException( "MATH Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + currentValue + "]" );
        
        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + currentValue + "] for [" + getContext() + "]" );
            contextMap.put( getContext(), currentValue + "" );
        }
		
		return true;
	}

}
