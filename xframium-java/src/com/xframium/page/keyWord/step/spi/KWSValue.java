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
public class KWSValue extends AbstractKeyWordStep
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
		if ( pageObject == null )
			throw new IllegalStateException( "Page Object was not defined" );

		String elementValue = getElement( pageObject, contextMap, webDriver, dataMap ).getValue();
		
		if ( getParameterList().size() == 1 )
		{
			Object compareTo = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap );
			if ( !elementValue.equals( compareTo ) )
				throw new IllegalStateException( "GET Expected [" + compareTo + "] but found [" + elementValue + "]" );
		}
		
		if ( !validateData( elementValue + "" ) )
			throw new IllegalStateException( "GET Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + elementValue + "]" );
		
		if ( getContext() != null )
		{
			if ( log.isDebugEnabled() )
				log.debug( "Setting Context Data to [" + elementValue + "] for [" + getContext() + "]" );
			contextMap.put( getContext(), elementValue );
		}
		
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
