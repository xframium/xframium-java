package com.xframium.page.keyWord.step.spi;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import com.xframium.page.Page;
import com.xframium.page.PageManager;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSWait.
 */
public class KWSWait extends AbstractKeyWordStep
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
		int waitTime = 5;
		
		if ( getParameterList().size() > 0 )
		{
			try
			{
				waitTime = Integer.parseInt( getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "" );
			}
			catch( Exception e ) {}
		}
		
		try { Thread.sleep( waitTime * 1000 ); } catch( Exception e ) {}
		

		return true;
	}

}
