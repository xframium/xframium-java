package com.xframium.page.keyWord.step.spi;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import com.xframium.page.Page;
import com.xframium.page.data.PageData;
import com.xframium.page.element.Element;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

public class KWSFocus extends AbstractKeyWordStep
{
    
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
	    if ( pageObject == null )
            throw new IllegalStateException( "There was no Page Object defined" );
        
        Element currentElement = getElement( pageObject, contextMap, webDriver, dataMap );
        
        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + currentElement.getValue() + "] for [" + getContext() + "]" );
            contextMap.put( getContext(), currentElement.getValue() );
        }
        
        return currentElement.isFocused();
	}

}
