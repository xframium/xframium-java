/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.page.keyWord.step.spi;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element;
import org.xframium.page.element.ElementFork;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

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
		orMapping = false;
		category = "Flow Control";
	}
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext ) throws Exception
	{
			
		
		if ( getStepList().size() != getParameterList().size() )
			throw new ScriptConfigurationException( "Your sub step count must equal your parameter count for a proper fork" );
		
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
			return getStepList().get( elementFork.getElementIndex() ).executeStep( pageObject, webDriver, contextMap, dataMap, pageMap, sC, executionContext );
		}
		else
			return false;
	}

}
