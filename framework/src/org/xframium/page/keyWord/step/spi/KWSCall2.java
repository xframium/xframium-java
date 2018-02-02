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
import org.xframium.container.SuiteContainer;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * <b>Keyword(s):</b> <code>CALL</code><br>
 * The Call keyword allows the developer to execute a function or test defined in XML.  The test or function will inherit any dataProvider and dataDriver
 * data that was provided to the calling test/function.  If a function is called and the function defined a dataProvider that did not exist on the calling test/function
 * then it will be added during execution.  It is possible to override the name of a dataProvider/dataDriver using a parameter as defined below.  This allows a function 
 * to use a single dataProvider name while other calling function may use dataProvider/dataDrivers with different names<br><br>
 * <b>Attributes:</b> Attributes defined here are changes to the base attribute contract
 * <ul>
 * <li><i>name</i>: In this context, name is the function or test name
 * <li><i>page</i>: In this context, page is unused
 * </ul><br><br>
 * <b>Parameters:</b> The only parameter available for the call keyword are the dataProvider/dataDriver overrides.  Each parameter will define a single override and there can be many<br>
 * <i>Extraction Only</i><br>
 * <ul>
 * <li>Data Override: The specifies a single data override in the format of to=from</li>
 * </ul>
 * <br><b>Example(s): </b><ul>
 * <li> This example will call a function name LOGIN<br>
 * {@literal <step name="LOGIN" type="CALL" /> }<br>
 * </li>
 * <li> This example will call a function name LOGIN and override the systemLogin with authData (the function expects authData)<br>
 * {@literal <step name="LOGIN" type="CALL" /> }<br>
 * &nbsp;&nbsp;&nbsp;{@literal  <parameter type="static" value="authData=systemLogin" /> }<br>
 * {@literal </step> }
 * 
 * </ul>
 */
public class KWSCall2 extends AbstractKeyWordStep
{
    public KWSCall2()
    {
        kwName = "Call Test/Function";
        kwDescription = "Allows the script to call another pre-defined function or test.  You can pass named context parameters that are scoped to the =function call by adding parameters with names and values.  OVERRIDE is the only reserved name using for overriding data provider names";
        kwHelp = "https://www.xframium.org/keyword.html#kw-call";
        orMapping = false;
        category = "Flow Control";
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext ) throws Exception
	{
		Map<String,String> localContextMap = new HashMap<String,String>( 20 );
		boolean returnValue = false;
		boolean devicePushed = false;
		
		String functionName = getName();
		
		if ( log.isDebugEnabled() )
			log.debug( "Execution Function " + getName() );
		boolean functionSuccess = false;
		boolean breakCalled = false;
		
		try
		{
			if ( getParameterList() != null && !getParameterList().isEmpty() )
			{
				for ( KeyWordParameter param : getParameterList() )
				{
					if ( param.getName() != null && !param.getName().isEmpty() )
					{
						if ( param.getName().equals( "OVERRIDE") )
						{
							//
							// Override is used for dataMap parameter mappings
							//
							String dataProvider = getParameterValue( param, contextMap, dataMap, executionContext.getxFID() ) + "";
							String[] dpArray = dataProvider.split( "=" );
							if ( dpArray.length == 2 )
							{
								PageData pageData = dataMap.get( dpArray[1] );
								if ( pageData != null )
									dataMap.put( dpArray[0], pageData );
							}
						}
						else if ( param.getName().equals( "FUNCTION_NAME") )
						{
						    functionName = getParameterValue( param, contextMap, dataMap, executionContext.getxFID() ) + "";
						}
						else
						{
							//
							// These are locally used context variables
							//
							localContextMap.put( param.getName(), param.getName() );
							contextMap.put( param.getName(), getParameterValue( param, contextMap, dataMap, executionContext.getxFID() ) + "" );
						}
					}
				}
			}
			
			if ( sC != null )
				returnValue = sC.getTest( functionName ).executeTest(webDriver, contextMap, dataMap, pageMap, sC, executionContext);
			else
			{
			    
			    if ( getDevice() != null && !getDevice().trim().isEmpty() )
                {
			        
                    //
			        // A device was specified at the function level so push it onto the device stack
			        //
			        executionContext.pushDevice( getDevice() );
			        devicePushed = true;
                }
			    
				returnValue = KeyWordDriver.instance( ( (DeviceWebDriver) webDriver ).getxFID() ).executionFunction( functionName, webDriver, dataMap, pageMap, contextMap, sC, executionContext );
				functionSuccess = returnValue;
			}
		}
		catch( Throwable e )
		{
		    if ( e instanceof KWSLoopBreak )
		        breakCalled = true;
		    else
		        functionSuccess = false;
		    throw e;
		}
		finally
		{
		    if ( devicePushed )
		        executionContext.popDevice();
			for ( String name : localContextMap.keySet() )
				contextMap.remove( name );
			
			if ( breakCalled )
			{
    			executionContext.startStep( new SyntheticStep( "Loop Break Called for " + getName(), "CALLBACK", "Loop Break Called for " + getName() ) , contextMap, dataMap );
    			executionContext.completeStep( StepStatus.SUCCESS, null );
			}
			else
			{
			    if ( functionSuccess )
			    {
			        executionContext.startStep( new SyntheticStep( "Return from " + getName(), "CALLBACK", "Return from " + getName() ) , contextMap, dataMap );
	                executionContext.completeStep( StepStatus.SUCCESS, null );
			    }
			    else
			    {
			        executionContext.startStep( new SyntheticStep( "Failed call to " + getName(), "CALLBACK", "Failed call to " + getName() ) , contextMap, dataMap );
	                executionContext.completeStep( StepStatus.FAILURE, null );
			    }
			}
		}
		
		return returnValue;
	}
	
	public boolean isRecordable()
    {
        return false;
    }

}

