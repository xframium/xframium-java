package com.xframium.page.keyWord.step.spi;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import com.xframium.page.Page;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSValue.
 */
public class KWSString extends AbstractKeyWordStep
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
	    String originalValue = null;
	    String operationName = null;
	    
	    if ( getParameterList().size() == 1 )
        {
            originalValue = getElement( pageObject, contextMap, webDriver, dataMap ).getValue();
            operationName = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
        }
	    else if ( getParameterList().size() == 2 )
	    {
	        originalValue = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
	        operationName = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";
	    }

		String newValue = null;
		
		switch ( operationName.toLowerCase() )
		{
		    case "trim":
		        newValue = originalValue.trim();
		        break;
		        
		    case "lower":
		        newValue = originalValue.toLowerCase();
		        break;
		        
		    case "upper":
		        newValue = originalValue.toUpperCase();
		        break;
		}
		
		
		if ( !validateData( newValue + "" ) )
            throw new IllegalStateException( "STRING Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + newValue + "]" );
        
        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + newValue + "] for [" + getContext() + "]" );
            contextMap.put( getContext(), newValue );
        }
		
		return true;
	}

}
