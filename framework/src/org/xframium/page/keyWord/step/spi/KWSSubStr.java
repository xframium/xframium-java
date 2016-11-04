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
import org.xframium.exception.ScriptException;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

public class KWSSubStr extends AbstractKeyWordStep
{
    public KWSSubStr()
    {
        kwName = "Substring";
        kwDescription = "Allows the script to get part of a parameter string";
        kwHelp = "https://www.xframium.org/keyword.html#kw-substr";
        orMapping = false;
    }
    
    @Override
    public boolean _executeStep( Page pageObject,
                                 WebDriver webDriver,
                                 Map<String, Object> contextMap,
                                 Map<String, PageData> dataMap,
                                 Map<String, Page> pageMap,
                                 SuiteContainer sC )
    {
        int paramCount = getParameterList().size();
        
        if ( paramCount < 3 )
        {
            throw new ScriptConfigurationException( "The substring step requires at least three parameter" );
        }

        String result = null;

        String theString = (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap );
        int startOffset = getInt( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) );
        int endOffset = getInt( (String) getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) );

        result = theString.substring( startOffset, endOffset );

        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + result + "] for [" + getContext() + "]" );
            
            contextMap.put( getContext(), result );
        }
	
        return true;
    }

    private int getInt( String value )
    {
        int rtn = 0;
        
        try
        {
            rtn = Integer.parseInt( value );
        }
        catch( Exception e )
        {
            throw new IllegalStateException( e );
        }

        return rtn;
    }
}
