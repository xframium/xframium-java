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
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

public class KWSConcat extends AbstractKeyWordStep
{
    public KWSConcat()
    {
        kwName = "Concatenate";
        kwDescription = "Allows the script to concatenate two parameter values";
        kwHelp = "https://www.xframium.org/keyword.html#kw-concat";
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
        
        if ( paramCount < 2 )
        {
            throw new ScriptConfigurationException( "String concatenation requires at least two parameter" );
        }

        StringBuilder buffer = new StringBuilder();
        
        for( Object param : getParameterList() )
        {
            buffer.append( (( param != null ) ? getParameterValue( (KeyWordParameter) param, contextMap, dataMap ).toString() : "" ));
        }

        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + buffer.toString() + "] for [" + getContext() + "]" );
            
            contextMap.put( getContext(), buffer.toString() );
        }
	
        return true;
    }
}
