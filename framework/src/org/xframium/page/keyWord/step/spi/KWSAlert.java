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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.gesture.Gesture.GestureType;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

public class KWSAlert extends AbstractKeyWordStep
{
    
    public KWSAlert()
    {
        kwName = "Alert Interaction";
        kwDescription = "Allows the script to interact with web based alerts";
        kwHelp = "https://www.xframium.org/keyword.html#kw-alert";
        orMapping = false;
        category = "Web";
    }
    
    public enum ALERT_TYPE
    {
        ACCEPT( 1, "ACCEPT", "Accept"),
        DISMISS( 2, "DISMISS", "Dismiss"),
        SEND_KEYS (3, "SEND_KEYS", "Send Keys"),
        AUTHENTICATE( 4, "AUTHENTICATE", "Authenticate");


		private ALERT_TYPE( int id, String name, String description )
        {
            this.id = id;
            this.name= name;
            this.description = description;
        }
        
        private int id;
        private String name;
        private String description;
        
        public List<ALERT_TYPE> getSupported()
        {
            List<ALERT_TYPE> alertList = new ArrayList<ALERT_TYPE>( 10 );
            alertList.add( ACCEPT );
            alertList.add( DISMISS );
            alertList.add( SEND_KEYS );
            return alertList;
        }
        
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
			WebDriverWait alertWait = new WebDriverWait( webDriver, 5 );
			alertWait.until( ExpectedConditions.alertIsPresent() );
    		Alert currentAlert = webDriver.switchTo().alert();
    		
    		if ( getContext() != null && !getContext().isEmpty() )
    		    contextMap.put( getContext(), currentAlert.getText() );
    		
    		switch( ALERT_TYPE.valueOf( getName() ) )
    		{
    		    case ACCEPT:
    		        currentAlert.accept();
    		        break;
    		        		        
    		    case DISMISS:
    		        currentAlert.dismiss();
    		        break;
    		        
    		    case SEND_KEYS:
    		        currentAlert.sendKeys( getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "" );
    		        currentAlert.accept();
    		        break;

    		    case AUTHENTICATE:
    		        currentAlert.authenticateUsing( new UserAndPassword(  getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "",  getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "" ) );
                    break;
    		        
    		    default:
    		        log.warn( "Unhandled Alert Type: " + getName() );
    		            
    		}
		}
		catch( NoAlertPresentException e )
		{
		    return false;
		}
		
		
		return true;
	}
	
	

}
