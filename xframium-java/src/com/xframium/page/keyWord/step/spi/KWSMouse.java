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
public class KWSMouse extends AbstractKeyWordStep
{

    
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
	    String mouseAction = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
	    
	    switch( mouseAction.toUpperCase() )
	    {
	        case "MOVE_TO":
	            return getElement( pageObject, contextMap, webDriver, dataMap ).moveTo();
	            
	        case "PRESS":
                return getElement( pageObject, contextMap, webDriver, dataMap ).press();
                        
	        case "RELEASE":
                return getElement( pageObject, contextMap, webDriver, dataMap ).release();
	            
	    }
		
		return true;
	}

}
