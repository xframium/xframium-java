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

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.xframium.container.SuiteContainer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element;
import org.xframium.page.element.Element.SetMethod;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSSet.
 */
public class KWSSet extends AbstractKeyWordStep
{
    public KWSSet()
    {
        kwName = "Set Value";
        kwDescription = "Allows the script to type or select a value";
        kwHelp = "https://www.xframium.org/keyword.html#kw-set";
        category = "Interaction";
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
	{

		String newValue = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() ) + "";
		String option = null;
		if (getParameterList().size() > 1 ){
			option = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) + "";
		}		
		if ( log.isInfoEnabled() )
			log.info( "Attmepting to set " + getName() + " to [" + newValue + "]" );

		String xFID = executionContext.getxFID();
		
		if(option == null)
		{
		    Element elt = getElement( pageObject, contextMap, webDriver, dataMap, executionContext );
			elt.setValue( newValue, xFID);
		}
		else 
		{
			SetMethod setMethod = SetMethod.valueOf( option.toUpperCase() );
			
			switch( setMethod )
			{
				case VALIDATE:
					
				    Element elt = getElement( pageObject, contextMap, webDriver, dataMap, executionContext );
					int length = -1;
					KeyWordParameter lengthParam = getParameter( "length" );
					if ( lengthParam != null )
						length = Integer.parseInt( getParameterValue( lengthParam, contextMap, dataMap, executionContext.getxFID() ) + "" );
					
					String useCharacters = "abcdef";
					KeyWordParameter charParam = getParameter( "characters" );
					if ( charParam != null )
						useCharacters = getParameterValue( charParam, contextMap, dataMap, executionContext.getxFID() ) + "";
					
					String testString = createString( length, useCharacters.getBytes() );
					
					elt.setValue( testString + "xxx", xFID );
					
					try { Thread.sleep( 1000 ); } catch( Exception e ) {}
					
					String setValue = elt.getValue();
					if ( !setValue.equals( testString ) )
						throw new ScriptException( "The length of was exceeded - expected " + length );
					
					elt.setValue( newValue, xFID );
					break;
				
				case DELAYED:
					Element delayedElement = getElement( pageObject, contextMap, webDriver, dataMap, executionContext );
					( (WebElement) delayedElement.getNative() ).clear();
					
					KeyWordParameter delayedLengthParam = getParameter( "Delay" );
				    int delayedLength = 5000;
				    
				    if ( delayedLengthParam != null )
				    	delayedLength = Integer.parseInt( getParameterValue( delayedLengthParam, contextMap, dataMap, executionContext.getxFID() ) );
					
	                byte[] delayedBuffer = newValue.getBytes();
	                for ( int i=0; i<delayedBuffer.length; i++ )
	                {
	                	( (WebElement) delayedElement.getNative() ).sendKeys( new String( new byte[] { delayedBuffer[ i ] } ) );
	                    try { Thread.sleep( delayedLength ); } catch( Exception e ) {}
	                    
	                }
	                break;
					
				case PERFECTO:
				    KeyWordParameter delayLengthParam = getParameter( "Delay" );
				    int delayLength = 0;
				    
				    if ( delayLengthParam != null )
				        delayLength = Integer.parseInt( getParameterValue( delayLengthParam, contextMap, dataMap, executionContext.getxFID() ) );
				    
				    if ( log.isInfoEnabled() )
				        log.info( "PERFECTO Type (" + delayLength + " )" );
				    
				    if ( delayLength > 0 )
				    {
    				    byte[] buffer = newValue.getBytes();
    	                for ( int i=0; i<buffer.length; i++ )
    	                {
    	                    Map<String, Object> params = new HashMap<>();
    	                    params.put( "text", new String( new byte[] { buffer[ i ] } ) );
    	                    ( (DeviceWebDriver) webDriver ).executeScript( "mobile:typetext", params );
    	                    try { Thread.sleep( delayLength ); } catch( Exception e ) {}
    	                }
				    }
				    else
				    {
				        Map<String, Object> params = new HashMap<>();
                        params.put( "text", newValue );
				    }
				    break;
				    
					
				default:
				    Element elt2 = getElement( pageObject, contextMap, webDriver, dataMap, executionContext );
					elt2.setValue( newValue,setMethod, xFID );
			}
			
			
		}		
		return true;
      }	

	
	private static String createString( int length, byte[] useChars )
	{
		StringBuilder stringBuilder = new StringBuilder();
		int currentPosition = 0;
		for ( int i=0; i<length; i++ )
		{
			if ( currentPosition >= useChars.length )
				currentPosition = 0;
			
			stringBuilder.append( new String( useChars, currentPosition++, 1 ) );
		}
		
		return stringBuilder.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
	 */
	public boolean isRecordable()
	{
		return false;
	}

}
