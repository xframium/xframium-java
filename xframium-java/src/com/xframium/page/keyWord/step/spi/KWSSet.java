package com.xframium.page.keyWord.step.spi;

import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.xframium.page.Page;
import com.xframium.page.data.PageData;
import com.xframium.page.element.Element;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSSet.
 */
public class KWSSet extends AbstractKeyWordStep
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
	    if ( pageObject == null )
            throw new IllegalStateException( "There was no Page Object defined" );
		
		if ( getParameterList().size() < 1 )
			throw new IllegalArgumentException( "You must provide 1 parameter to setValue" );
		
		String newValue = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
		
		if ( log.isInfoEnabled() )
			log.info( "Attmepting to set " + getName() + " to [" + newValue + "]" );
		
		Element elt = getElement( pageObject, contextMap, webDriver, dataMap );
                elt.setValue( newValue );
                    
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
	 */
	public boolean isRecordable()
	{
		return false;
	}

}
