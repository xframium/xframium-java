package com.xframium.page.keyWord.step.spi;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.xframium.page.Page;
import com.xframium.page.PageManager;
import com.xframium.page.data.PageData;
import com.xframium.page.element.Element;
import com.xframium.page.element.Element.WAIT_FOR;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSWaitFor.
 */
public class KWSWaitFor extends AbstractKeyWordStep
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
	    if ( pageObject == null )
            throw new IllegalStateException( "There was no Page Object defined" );
		
		int waitFor = 15;
		
		if ( getParameterList().size() > 0 )
		{
			try
			{
				waitFor = Integer.parseInt( getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "" );
			}
			catch( Exception e ) {}
		}
		boolean returnValue = false;
		try
		{
	        String[] waitType = getName().split( "\\." );
	        if ( waitType.length == 2 )
	            returnValue = getElement( pageObject, contextMap, webDriver, dataMap, waitType[ 0 ] ).waitFor( waitFor, TimeUnit.SECONDS, WAIT_FOR.valueOf( waitType[ 1 ] ), "" );
	        else
	            returnValue = getElement( pageObject, contextMap, webDriver, dataMap ).waitForPresent( waitFor, TimeUnit.SECONDS );
		}
		finally
		{
			
		}
		
		return returnValue;
		
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
	 */
	public boolean isRecordable()
	{
		return false;
	}

}
