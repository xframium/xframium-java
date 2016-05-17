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
package com.xframium.page.keyWord.step.spi;

import java.util.Map;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.xframium.page.Page;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;
import com.xframium.spi.driver.NativeDriverProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSAddCookie.
 */
public class KWSAddCookie extends AbstractKeyWordStep
{

    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
    {
        if ( pageObject == null )
        {
            throw new IllegalStateException( "Page Object was not defined" );
        }

        Object name = null;
        Object value = null;
                
        if ( getParameterList().size() == 2 )
        {
            name = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap );
            value = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap );
            if ( !( name instanceof String ) )
                throw new IllegalStateException( "Cookie name must be of type String" );
            if ( !( value instanceof String ) )
                throw new IllegalStateException( "Cookie value must be of type String" );
        }
        else
        {
            throw new IllegalStateException( "Add cookie requires two string properties (name, value)" );
        }

        Cookie cookie = new Cookie( (String)name, (String)value );
        if ( webDriver instanceof RemoteWebDriver )
            ((RemoteWebDriver) webDriver).manage().addCookie( cookie );
        else if ( webDriver instanceof NativeDriverProvider && ( (NativeDriverProvider) webDriver).getNativeDriver() instanceof RemoteWebDriver )
        {
            ( (RemoteWebDriver) ( (NativeDriverProvider) webDriver).getNativeDriver() ).manage().addCookie( cookie );
        }
				
        return true;
    }
	
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
     */
    public boolean isRecordable()
    {
        return false;
    }

}
