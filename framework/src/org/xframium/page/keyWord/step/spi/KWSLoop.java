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

import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ObjectConfigurationException;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;



// TODO: Auto-generated Javadoc
/**
 * The Class KWSLoop.
 */
public class KWSLoop extends AbstractKeyWordStep
{
    public KWSLoop()
    {
        kwName = "Loop";
        kwDescription = "Allows the script to loop over data, elements or values and call a function";
        kwHelp = "https://www.xframium.org/keyword.html#kw-loop";
        orMapping = true;
        category = "Flow Control";
    }
    
	/** The Constant DATA_START. */
	private static final String DATA_START = "data{";
	
	/** The Constant DATA_STOP. */
	private static final String DATA_STOP = "}";
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC ) throws Exception
	{
		if ( getParameterList().size() < 2 )
			throw new ScriptConfigurationException( "You must provide one parameter specifying either the loop count or the name of the element to execute on along with a function name to execution" );
		
		String useValue = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
		String functionName = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";
		
		boolean numericalLoop = false;
		int loopCount = -1;
		
		try
		{
			loopCount = Integer.parseInt( useValue );
			numericalLoop = true;
		}
		catch( Exception e )
		{
			
		}
		
		
		if ( numericalLoop )
		{
			if ( log.isInfoEnabled() )
				log.info( "Looping " + loopCount + " times" );
			
			for ( int i=0; i<loopCount; i++ )
			{
			    contextMap.put( Element.LOOP_INDEX, i+1 );
				if ( log.isDebugEnabled() )
					log.debug( "Execution Function " + functionName + " - Iteration " + i + " of " + loopCount );
				try
				{
				
				    if ( sC != null )
				    {
                        if ( !sC.getTest( functionName ).executeTest(webDriver, contextMap, dataMap, pageMap, sC) )
                            return false;
				    }
                    else
                    {
                        if ( !KeyWordDriver.instance().executionFunction( functionName, webDriver, dataMap, pageMap, sC ) )
                            return false;
                    }
				}
				catch( KWSLoopBreak e )
				{
					return true;
				}
			}
			
			return true;
		}
		else
		{
			if ( useValue.startsWith( DATA_START ) )
			{
			    PageData[] dataTable = null;
			    
				//
				// We are using a data table for the loop
				//
				String tableName = useValue.substring( DATA_START.length(), useValue.length() - 1 );
				
				if ( tableName.startsWith( "." ) )
				{
				    //
				    // This is a context table from the current record - we only go 1 level deep now
				    //
				    tableName = tableName.substring( 1 );
				    String[] valueSet = tableName.split( "\\." );
				    if ( valueSet.length == 2 )
				    {
				        PageData rootRecord = dataMap.get( valueSet[ 0 ] );
				        if ( rootRecord == null )
				            log.error( "The root page data record " + valueSet[ 0 ] + " does not exist" );
				        
				        List<PageData> dataArray = rootRecord.getPageData( valueSet[ 1 ] );
				        
				        if ( dataArray == null )
				            log.error( "The sub page data record " + valueSet[ 1 ] + " does not exist in " + valueSet[ 0 ] );
				        
				        dataTable = dataArray.toArray( new PageData[ 0 ] );
				    }
				}
				else
				    dataTable = PageDataManager.instance().getRecords( tableName );
				
				for ( PageData pageData : dataTable )
				{
					try
					{
						dataMap.put( tableName, pageData );
						
						if ( log.isDebugEnabled() )
							log.debug( "Execution Function " + functionName + " - with data " + pageData );
						
						if ( sC != null )
	                    {
	                        if ( !sC.getTest( functionName ).executeTest(webDriver, contextMap, dataMap, pageMap, sC) )
	                            return false;
	                    }
	                    else
	                    {
	                        if ( !KeyWordDriver.instance().executionFunction( functionName, webDriver, dataMap, pageMap, sC ) )
	                            return false;
	                    }
					}
					catch( KWSLoopBreak lb )
					{
						return true;
					}
				}
				
				return true;
			}
			
			if ( log.isInfoEnabled() )
				log.info( "Attempting to locate an array of elements using " + useValue );
			
			Element elementList = null;
			if ( Element.CONTEXT_ELEMENT.equals( useValue ) )
				elementList = (Element) contextMap.get( Element.CONTEXT_ELEMENT );
			else
				elementList = pageObject.getElement( getPageName(), useValue );
			
			if ( elementList != null )
			{
				Element[] elementArray = elementList.getAll();
				
				if ( log.isInfoEnabled() )
					log.info( "Looping " + elementArray.length + " times over the elements found" );
				
				for ( int i=0; i<elementArray.length; i++ )
				{
					if ( log.isDebugEnabled() )
						log.debug( "Execution Function " + functionName + " - Iteration " + i + " of " + elementArray.length );
					
					contextMap.put( Element.CONTEXT_ELEMENT, elementArray[ i ] );
					contextMap.put( Element.CONTEXT_INDEX, i+1 );
					
					try
					{
					    if ( sC != null )
	                    {
	                        if ( !sC.getTest( functionName ).executeTest(webDriver, contextMap, dataMap, pageMap, sC) )
	                            return false;
	                    }
	                    else
	                    {
	                        if ( !KeyWordDriver.instance().executionFunction( functionName, webDriver, dataMap, pageMap, sC ) )
	                            return false;
	                    }
						
					}
					catch( KWSLoopBreak lb )
					{
						return true;
					}
				}
			}
		}
		
		return true;
	}
	
}
