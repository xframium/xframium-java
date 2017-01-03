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

import java.lang.reflect.Method;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * <b>Keyword(s):</b> <code>FUNCTION</code><br>
 * The Function keyword will locate a Java function of the page object and execute using the parameter passed in.  For Functions to work
 * the Page object needs to be defined using the Java Page interface and Page implementation.   <br><br>
 * <b>Attributes:</b> Attributes defined here are changes to the base attribute contract
 * <ul>
 * <li><i>name</i>: In this context, name is the Java function
 * </ul><br><br>
 * <b>Parameters:</b> Parameters are passed directly to the function.  The underlying code will do its best to match parameter positions with
 * their appropriate values using reflection<br>
 * <i>Extraction Only</i><br>
 * <ul>
 * <li>Data Override: The specifies a single data override in the format of to=from</li>
 * </ul>
 * <br><b>Example(s): </b><ul>
 * <li> This example will call a function name loginUser on the LoginPage object<br>
 * {@literal <step name="loginUser" type="FUNCTION" page="LoginPage" /> }<br>
 * </li>
 * </ul>
 */
public class KWSFunction extends AbstractKeyWordStep
{
    public KWSFunction()
    {
        kwName = "Hybrid Execution";
        kwDescription = "Allows the script to execute a named Java function on a Java page object implementation";
        kwHelp = "https://www.xframium.org/keyword.html#kw-function";
        orMapping = false;
        category = "Utility";
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
	{
		if ( pageObject == null )
			throw new ScriptConfigurationException( "Page Object was not defined" );
		try
		{
			Object[] parameterArray = getParameters( contextMap, dataMap );
			Method method = findMethod( pageObject.getClass(), getName(), parameterArray );
			method.invoke( pageObject, parameterArray );
			return true;
		}
		catch( Exception e )
		{
			throw new ScriptException( "Function Call Failed " +  e.getMessage() );
		}
	}

}
