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

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xframium.container.SuiteContainer;
import org.xframium.device.factory.MorelandWebElement;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.step.spi.KWSMath.MATH_TYPE;
import org.xframium.reporting.ExecutionContextTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSWindow.
 */
public class KWSWindow extends AbstractKeyWordStep
{
	/**
	 * The Enum SwitchType.
	 */
	public enum SwitchType
	{
		BY_WINTITLE(1, "BY_WINTITLE", "Switch to Window with Title"), 
		BY_WINURL(2, "BY_WINURL", "Switch to Window with URL"), 
		BY_FRAME(3, "BY_FRAME", "Switch to Named Frame"),
		BY_NUMBER(4, "BY_NUMBER", "Switch to Indexed frame"),
		BY_PARENTFRAME(5, "BY_PARENTFRAME", "Switch to Parent Frame"), 
		BY_DEFAULT(6, "BY_DEFAULT", "Switch to Default Window"), 
		BY_WINCLOSE(7, "BY_WINCLOSE", "Close the current window"), 
		BY_ELEMENT(8, "BY_ELEMENT", "Switch to the frame by element"),
		BY_MAXIMIZE(9, "BY_MAXIMIZE", "Maximize the current window"),
		BY_WINDOW(10, "BY_WINDOW", "Switch to the named window handle"),
		GET_TITLE(11, "GET_TITLE", "Get the title of the current window"),
		GET_URL(12, "GET_URL", "Get the URL of the current window");
	    
	    public List<SwitchType> getSupported()
        {
            List<SwitchType> supportedList = new ArrayList<SwitchType>( 10 );
            supportedList.add( SwitchType.BY_WINTITLE );
            supportedList.add( SwitchType.BY_WINURL );
            supportedList.add( SwitchType.BY_FRAME );
            supportedList.add( SwitchType.BY_NUMBER );
            supportedList.add( SwitchType.BY_PARENTFRAME );
            supportedList.add( SwitchType.BY_DEFAULT );
            supportedList.add( SwitchType.BY_WINCLOSE );
            supportedList.add( SwitchType.BY_ELEMENT );
            supportedList.add( SwitchType.BY_MAXIMIZE );
            supportedList.add( SwitchType.BY_WINDOW );
            supportedList.add( SwitchType.GET_TITLE );
            supportedList.add( SwitchType.GET_URL );
            return supportedList;
        }
        
        private SwitchType( int id, String name, String description )
        {
            this.id = id;
            this.name= name;
            this.description = description;
        }
        
        private int id;
        private String name;
        private String description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com
	 * .perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext ) throws Exception
	{
		// Verify if the parameter-1 values are correct
		String switchType = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() ) + "";
		String switchExpValue = "";

		switch ( SwitchType.valueOf( switchType ) )
		{
		case BY_WINTITLE:
			if ( getParameterList().size() < 2 )
				throw new ScriptConfigurationException( "Please provide the title for the window as a parameter" );
			switchExpValue = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) + "";
			return verifySwitchWindow( webDriver, switchType, switchExpValue );
		case BY_WINURL:
			if ( getParameterList().size() < 2 )
				throw new ScriptConfigurationException( "Please provide the URL for the window as a parameter" );
			switchExpValue = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) + "";
			return verifySwitchWindow( webDriver, switchType, switchExpValue );
		case BY_FRAME:
			if ( getParameterList().size() < 2 )
				throw new ScriptConfigurationException( "Please provide the Frame id for the Frame as a parameter" );
			switchExpValue = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) + "";
			webDriver.switchTo().frame( switchExpValue );
			break;
		case BY_NUMBER:
			int frameNumber;
			try
			{
				if ( getParameterList().size() < 2 )
                    throw new Exception();
				frameNumber = Integer.parseInt(getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) + "");
			}
			catch (IllegalArgumentException e)
			{
				throw new IllegalArgumentException( "Please provide the Frame number for the Frame as a parameter" );
			}
			webDriver.switchTo().frame( frameNumber );
			break;
		case BY_PARENTFRAME:
			webDriver.switchTo().parentFrame();
			break;
		case BY_DEFAULT:
			webDriver.switchTo().defaultContent();
			break;
		case BY_WINDOW:
			webDriver.switchTo().window(webDriver.getWindowHandle());
			break;
		case BY_WINCLOSE:
			webDriver.close();
			break;

		case BY_ELEMENT:
			Element currentElement = getElement( pageObject, contextMap, webDriver, dataMap, executionContext );
			if ( currentElement == null )
			{
				log.warn( "Attempting to switch to frame identified by " + getName() + " that does not exist" );
				return false;
			}

			WebElement nativeElement = (WebElement) currentElement.getNative();
			if ( nativeElement instanceof MorelandWebElement )
				nativeElement = ( (MorelandWebElement) nativeElement ).getWebElement();
			webDriver.switchTo().frame( nativeElement ); 
			break;                	
		case BY_MAXIMIZE:
			webDriver.manage().window().maximize();
			break;
		case GET_TITLE:
		    String pageTitle = webDriver.getTitle();
		    if ( getParameterList().size() > 1 )
		    {
		        String compareTo = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) + "";
		        if ( !compareTo.equals( pageTitle ) )
		        {
		            throw new ScriptException( "Expected Title of [" + compareTo + "] but received [" + pageTitle + "]" );
		        }
		    }
		    
		    if ( !validateData( pageTitle ) )
	            throw new ScriptException( "GET_TITLE Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + pageTitle + "]" );
		    
		    if ( getContext() != null && !getContext().trim().isEmpty() ) 
		        contextMap.put( getContext(), pageTitle );

		    break;
		    
		case GET_URL:
            String currentUrl = webDriver.getCurrentUrl();
            if ( getParameterList().size() > 1 )
            {
                String compareTo = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) + "";
                if ( !compareTo.equals( currentUrl ) )
                {
                    throw new ScriptException( "Expected Title of [" + compareTo + "] but received [" + currentUrl + "]" );
                }
            }
            
            if ( !validateData( currentUrl ) )
                throw new ScriptException( "GET_URL Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + currentUrl + "]" );
            
            if ( getContext() != null && !getContext().trim().isEmpty() ) 
                contextMap.put( getContext(), currentUrl );

		    break;
		    
	
		default:
			throw new ScriptConfigurationException( "Parameter switchtype should be BY_WINTITLE| BY_WINURL|BY_FRAME|BY_PARENTFRAME|BY_DEFAULT|BY_ALERT|BY_ELEMENT|BY_MAXIMIZE" );
		}

		return true;
	}


	/**
	 * Verify switch window.
	 *
	 * @param webDriver the web driver
	 * @param byTitleOrUrl the by title or url
	 * @param winExpValue the win exp value
	 * @return true, if successful
	 */
	private boolean verifySwitchWindow( WebDriver webDriver, String byTitleOrUrl, String winExpValue )
	{

		boolean bSwitchWindow = false;
		String winActValue = "";
		Set<String> availableWindows = webDriver.getWindowHandles();

		if ( !availableWindows.isEmpty() )
		{
			for ( String windowId : availableWindows )
			{
				if ( byTitleOrUrl.equalsIgnoreCase( "BY_WINTITLE" ) )
				{
					winActValue = webDriver.switchTo().window( windowId ).getTitle().trim().toLowerCase();
				}
				else
				{
					winActValue = webDriver.switchTo().window( windowId ).getCurrentUrl().trim().toLowerCase();
				}

				winExpValue = winExpValue.trim().toLowerCase();

				if ( winActValue.contains( winExpValue ) )
				{
					bSwitchWindow = true;
					break;
				}
			}
		}

		return bSwitchWindow;
	}

}
