/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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

import java.lang.reflect.Method;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import com.xframium.page.Page;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSReturn.
 */
public class KWSReturn extends AbstractKeyWordStep
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
		if ( pageObject == null )
			throw new IllegalStateException( "Page Object was not defined" );
		if ( getParameterList().size() < 1 )
			throw new IllegalArgumentException( "You must provide a parameter 1 parameter to a step in which the type is Validation" );
		
		Object compare = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap );
		
		if ( log.isDebugEnabled() )
			log.debug( "Validation value for comparison [" + compare + "]" );
		
		try
		{
			Object[] parameterArray = getParameters( contextMap, dataMap );
			Method method = findMethod( pageObject.getClass(), getName(), parameterArray );
			Object elementValue = method.invoke( pageObject, parameterArray );
			
			if ( elementValue != null )
			{
				if ( getParameterList().size() == 1 )
				{
					Object compareTo = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap );
					if ( !elementValue.equals( compareTo ) )
						return false;
				}
				
				if ( !validateData( elementValue + "" ) )
					return false;
				
				if ( getContext() != null )
				{
					if ( log.isDebugEnabled() )
						log.debug( "Setting Context Data to [" + elementValue + "] for [" + getContext() + "]" );
					contextMap.put( getContext(), elementValue );
				}
			}
			
			
			return true;
		}
		catch( Exception e )
		{
			log.error( "Error executing function for validation [" + getName() + "] on page [" + getPageName() + "]", e );
			return false;
		}
	}

}
