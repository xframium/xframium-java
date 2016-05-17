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
import java.util.Set;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.xframium.page.Page;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSWindow.
 */
public class KWSWindow extends AbstractKeyWordStep
{

    /**
     * The Enum SwitchType.
     */
    private enum SwitchType
    {
        
        /** The by wintitle. */
        BY_WINTITLE, 
 /** The by winurl. */
 BY_WINURL, 
 /** The by frame. */
 BY_FRAME, 
 /** The by parentframe. */
 BY_PARENTFRAME, 
 /** The by default. */
 BY_DEFAULT, 
 /** The by winclose. */
 BY_WINCLOSE, 
 /** The by alert. */
 BY_ALERT;
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
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap ) throws Exception
    {
        if ( log.isDebugEnabled() )
            log.debug( "Execution Function " + getName() );

        if ( getParameterList().size() < 1 )
            throw new IllegalArgumentException( "First Parameter Switchtype should be provided with values BY_WINTITLE| BY_WINURL|BY_FRAME|BY_PARENTFRAME|BY_DEFAULT" );

        try
        {
            // Verify if the parameter-1 values are correct
            String switchType = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
            String switchExpValue = "";

            switch ( SwitchType.valueOf( switchType ) )
            {
                case BY_WINTITLE:
                    if ( getParameterList().size() < 2 )
                        throw new IllegalArgumentException( "Please provide the title for the window as a parameter" );
                    switchExpValue = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";
                    return verifySwitchWindow( webDriver, switchType, switchExpValue );
                case BY_WINURL:
                    if ( getParameterList().size() < 2 )
                        throw new IllegalArgumentException( "Please provide the URL for the window as a parameter" );
                    switchExpValue = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";
                    return verifySwitchWindow( webDriver, switchType, switchExpValue );
                case BY_FRAME:
                    if ( getParameterList().size() < 2 )
                        throw new IllegalArgumentException( "Please provide the Frame id for the Frame as a parameter" );
                    switchExpValue = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";
                    webDriver.switchTo().frame( switchExpValue );
                    break;
                case BY_PARENTFRAME:
                    webDriver.switchTo().parentFrame();
                    break;
                case BY_DEFAULT:
                    webDriver.switchTo().defaultContent();
                    break;
                case BY_WINCLOSE:
                    webDriver.close();
                    break;
                case BY_ALERT:
                    WebDriverWait alertWait = new WebDriverWait( webDriver, 5 );
                    alertWait.until( ExpectedConditions.alertIsPresent() );
                    Alert alert = webDriver.switchTo().alert();
                    alert.accept();
                    break;
                default:
                    throw new IllegalArgumentException( "Parameter switchtype should be BY_WINTITLE| BY_WINURL|BY_FRAME|BY_PARENTFRAME|BY_DEFAULT|BY_ALERT" );
            }

        }
        catch ( Exception e )
        {
            log.error( "Error executing function for validation [" + getName() + "] on page [" + getPageName() + "]", e );
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
     */
    public boolean isRecordable()
    {
        return false;
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
                System.out.println( "Exp value  " + winExpValue + "Actual value :" + winActValue );
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
