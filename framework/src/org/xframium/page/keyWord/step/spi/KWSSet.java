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
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element;
import org.xframium.page.element.Element.SetMethod;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

import gherkin.Main;

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
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC )
	{
		if ( pageObject == null )
			throw new ScriptConfigurationException( "There was no Page Object defined" );

		if ( getParameterList().size() < 1 )
			throw new ScriptConfigurationException( "You must provide 1 parameter to setValue" );

		String newValue = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
		String option = null;
		if (getParameterList().size() > 1 ){
			option = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";
		}		
		if ( log.isInfoEnabled() )
			log.info( "Attmepting to set " + getName() + " to [" + newValue + "]" );

		Element elt = getElement( pageObject, contextMap, webDriver, dataMap );
		if(option == null)
		{
			elt.setValue( newValue);
		}
		else 
		{
			SetMethod setMethod = SetMethod.valueOf( option.toUpperCase() );
			
			switch( setMethod )
			{
				case VALIDATE:
					
					int length = -1;
					KeyWordParameter lengthParam = getParameter( "length" );
					if ( lengthParam != null )
						length = Integer.parseInt( getParameterValue( lengthParam, contextMap, dataMap ) + "" );
					
					String useCharacters = "abcdef";
					KeyWordParameter charParam = getParameter( "characters" );
					if ( charParam != null )
						useCharacters = getParameterValue( charParam, contextMap, dataMap ) + "";
					
					String testString = createString( length, useCharacters.getBytes() );
					
					elt.setValue( testString + "xxx" );
					
					try { Thread.sleep( 1000 ); } catch( Exception e ) {}
					
					String setValue = elt.getValue();
					if ( !setValue.equals( testString ) )
						throw new ScriptException( "The length of was exceeded - expected " + length );
					
					elt.setValue( newValue );
					
					
					
				default:
					elt.setValue( newValue,setMethod );
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
