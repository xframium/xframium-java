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
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

public class KWSAlign extends AbstractKeyWordStep
{
    private enum ALIGN
    {
        LEFT,RIGHT,TOP,BOTTOM;
    }
    
    public KWSAlign()
    {
        kwName = "Control Alignment";
        kwDescription = "Allows the script to validate the alignment of one or more elements with the top, bottom, let or right of another element";
        kwHelp = "https://www.xframium.org/keyword.html#kw-align";
        category = "Visual";
    }
    

    
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC )
	{
		if ( pageObject == null )
			throw new ScriptConfigurationException( "Page Object was not defined" );
		
		for ( int i=0; i<getParameterList().size(); i++ )
		{
		    String[] dataSet = ( getParameterValue( getParameterList().get( i ), contextMap, dataMap ) + "" ).split( "=" );
		    if ( dataSet.length != 2 )
		        throw new ScriptConfigurationException( ( getParameterValue( getParameterList().get( i ), contextMap, dataMap ) + "" ) + " is not formatted correctly for an alignment check" );
		 
		    ALIGN align = ALIGN.valueOf( dataSet[ 0 ] );
		    String elementName = dataSet[ 1 ];
		    int deviation = 0;
		    if ( elementName.contains( "(" ) )
		    {
		        
		        String deviationString = elementName.substring( elementName.indexOf( "(" ) + 1 ).replace( ")", " " ).trim();
		        deviation = Integer.parseInt( deviationString );
		        elementName = elementName.substring( 0, elementName.indexOf( "(" ) );
		    }
		    
		    Element baseElement = getElement( pageObject, contextMap, webDriver, dataMap );
		    Element compareElement = getElement( pageObject, contextMap, webDriver, dataMap, elementName );
		    int actualDeviation = 0;
		    
		    switch ( align )
		    {
		        case BOTTOM:
		            actualDeviation = Math.abs( (baseElement.getAt().getY() + baseElement.getSize().getHeight()) - (compareElement.getAt().getY() + compareElement.getSize().getHeight()) );
		            if ( actualDeviation > deviation )
		                throw new ScriptException( "Alignment to the bottom of [" + getName() + "] of the element [" + elementName + "] was off by " + actualDeviation );
                    break;
                    
		        case LEFT:
		            actualDeviation = Math.abs( baseElement.getAt().getX() - compareElement.getAt().getX() );
		            if ( actualDeviation > deviation )
		                throw new ScriptException( "Alignment to the left of [" + getName() + "] of the element [" + elementName + "] was off by " + actualDeviation );
		            break;
		            
		        case RIGHT:
		            actualDeviation = Math.abs( (baseElement.getAt().getX() + baseElement.getSize().getWidth()) - (compareElement.getAt().getX() + compareElement.getSize().getWidth()) );
		            if ( actualDeviation > deviation )
		                throw new ScriptException( "Alignment to the right of [" + getName() + "] of the element [" + elementName + "] was off by " + actualDeviation );
                    break;
                    
		        case TOP:
		            actualDeviation = Math.abs( baseElement.getAt().getY() - compareElement.getAt().getY() );
		            if ( actualDeviation > deviation )
		                throw new ScriptException( "Alignment to the top of [" + getName() + "] of the element [" + elementName + "] was off by " + actualDeviation );
                    break;
		            
		    }
		    
		}
		
		return true;
	}

}
