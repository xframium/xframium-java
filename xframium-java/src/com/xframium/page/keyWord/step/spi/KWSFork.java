package com.xframium.page.keyWord.step.spi;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import com.xframium.page.Page;
import com.xframium.page.data.PageData;
import com.xframium.page.element.Element;
import com.xframium.page.element.ElementFork;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSFork.
 */
public class KWSFork extends AbstractKeyWordStep
{
	
	/**
	 * Instantiates a new KWS fork.
	 */
	public KWSFork()
	{
		setFork( true );
	}
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap ) throws Exception
	{
			
		if ( pageObject == null )
			throw new IllegalStateException( "Page Object was not defined" );
		if ( getParameterList().size() < 2 )
			throw new IllegalArgumentException( "You must provide at least 2 elements to check for" );
		
		if ( getStepList().size() != getParameterList().size() )
			throw new IllegalArgumentException( "Your sub step count must equal your parameter count for a proper fork" );
		
		if ( log.isInfoEnabled() )
			log.info( "Creating fork against " + getParameterList().size() + " elements" );
		
		Element[] elementArray = new Element[ getParameterList().size() ];
		
		for ( int i=0; i<getParameterList().size(); i++ )
		{
			elementArray[ i ] = pageObject.getElement( getPageName(), getParameterValue( getParameterList().get( i ), contextMap, dataMap ) + "" );
		}
		
		ElementFork elementFork = new ElementFork( elementArray, 15 );
		if ( elementFork.findElement() )
		{
			if ( log.isInfoEnabled() )
				log.info( "Fork path " + elementFork.getElementIndex() + " chosen as " + elementFork.getElementFound().toString() + " was found" );	
			return getStepList().get( elementFork.getElementIndex() ).executeStep( pageObject, webDriver, contextMap, dataMap, pageMap );
		}
		else
			return false;
	}

}
