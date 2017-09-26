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
package org.xframium.utility;

import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BrowserCacheLogic
{

	/**
	 * clears the Safari browser Cache for IOS environment
	 * @param driver - RemoteWebDriver instance
	 * @throws Exception
	 */
    public static void clearSafariIOSCache( RemoteWebDriver driver )
        throws Exception
    {
        HashMap<String, Object> params = new HashMap();

        String currentContext = getCurrentContextHandle( driver );

        try
        {

            switchToContext(driver, "NATIVE_APP");

            //
            // get os version
            //

            params.clear();
            params.put("property", "osVersion");
            String osVerStr = (String) driver.executeScript("mobile:handset:info", params);
            int[] osVer = parseVersion( osVerStr );
        

            //
            // Launch Settings Application on it's main page
            //
        
            params.clear();
            params.put("name", "Settings");
            try {
                driver.executeScript("mobile:application:close", params);
            } catch (Exception e) {}
            driver.executeScript("mobile:application:open", params);
            sleep(1000);

            //
            // Make sure we're on the home page
            //
            
            clickIfPresent( driver, "//button[@name='Settings']" );

            //
            // Scroll to Top
            //
        
            params.clear();
            params.put("location", "60%,1%");
            driver.executeScript("mobile:touch:tap", params);
            params.clear();
            params.put("start", "10%,10%");
            params.put("end", "10%,40%");
            driver.executeScript("mobile:touch:swipe", params);

            if ( osVer[0] > 8 )
            {
                driver.findElementByXPath("//UIASearchBar").sendKeys("Clear History");
                driver.findElementByXPath("//*[@label=\"Clear History and Website Data\"]").click();
            }
            //
            // swipe to expose safari and click once exposed
            //

            else
            {
                boolean found = false;
                int count = 0;

                while(( !found ) && ( count < 10 ))
                {
                    found = clickIfPresent( driver, "//cell[@name='Safari']" );

                    if ( !found )
                    {
                        params.clear();
                        params.put("start", "27%,55%");
                        params.put("end", "29%,52%");
                        driver.executeScript("mobile:touch:swipe", params);

                        ++count;
                    }
                }

                //
                // swipe to bottom
                //

                params.clear();
                params.put("start", "50%,90%");
                params.put("end", "50%,10%");
                for (int i=0;i<3;i++){
                    driver.executeScript("mobile:touch:swipe", params);
                }
            }

            //
            // clear Cache
            //
            switchToContext(driver, "NATIVE_APP");
            clickIfPresent( driver, "//*[@label=\"Cancel\"]" );
            params.clear();
            //params.put("value", "//*[@value=\"Clear History and Website Data\"]");
            //params.put("framework", "appium-1.3.4");
            //driver.executeScript("mobile:application.element:click", params);
            driver.findElementByXPath("//*[@value=\"Clear History and Website Data\"]").click();
           //params.put("value", "//UIAButton[contains(@label,\"Clear\")]");
            driver.findElementByXPath("//UIAButton[contains(@label,\"Clear\")]").click();
            //driver.executeScript("mobile:application.element:click", params);
        
            //
            // below version 8 need to clear also data
            //
        
            if ( osVer[0] < 8 )
            {
                params.put("value", "//*[starts-with(text(),'Clear Cookies')]");
                params.put("framework", "perfectoMobile");
                driver.executeScript("mobile:application.element:click", params);
                params.put("value", "//*[(@class='UIAButton' or @class='UIATableCell') and starts-with(@label,'Clear') and @isvisible='true']");
                params.put("framework", "perfectoMobile");
                driver.executeScript("mobile:application.element:click", params);
            }

            //
            // Close Settings
            //
        
            params.clear();
            params.put("name", "Settings");

            try {
                driver.executeScript("mobile:application:close", params);
            } catch (Exception e) {}

            params.clear();
            params.put("automation", "simulated");
            driver.executeScript("mobile:browser:open", params);

        }
        finally
        {
            switchToContext(driver, currentContext);
        }
    }

    /**
     * clears the Chrome browser Cache for Android environment
     * @param driver - RemoteWebDriver instance
     * @throws Exception
     */
    public static void clearChromeAndroidCache( RemoteWebDriver driver )
        throws Exception
    {
        HashMap<String, Object> params = new HashMap();
        String currentContext = getCurrentContextHandle( driver );

        try
        {
            switchToContext(driver, "NATIVE_APP");

            //
            // get os version
            //
        
            params.put("property", "osVersion");
            String osVerStr = (String) driver.executeScript("mobile:handset:info", params);
            int[] osVer = parseVersion( osVerStr );

            if ( osVer[0] >= 4 )
            {
                //
                // Launch Chrome
                //
        
                params.clear();
                params.put("name", "Chrome");
                try {
                    driver.executeScript("mobile:application:close", params);
                } catch (Exception e) {}
                driver.executeScript("mobile:application:open", params);
                sleep(5000);

                //
                // select chrome menu
                //
                // resource-id: com.android.chrome:id/document_menu_button -- contains menu_button
                // content-desc: More options
                //

                params.clear();
                switchToContext(driver, "NATIVE_APP");

                //params.put("value", "//*[@resource-id=\"com.android.chrome:id/menu_button\"]");
                //params.put("framework", "appium-1.3.4");
                //driver.executeScript("mobile:application.element:click", params);
                driver.findElementByXPath("//*[@resource-id=\"com.android.chrome:id/menu_button\"]").click();
                //
                // select history
                //
                // text: History
                // resource-id: com.android.chrome:id/menu_item_text
                // content-desc: History
                //
                sleep(5000);
                params.clear();
                //params.put("value", "//*[@content-desc='History']");
                //params.put("framework", "appium-1.3.4");
                //driver.executeScript("mobile:application.element:click", params);
                driver.findElementByXPath("//*[@content-desc='History']").click();

                //
                // select clear browsing data
                //
                // text: Clear browsing data...
                // id: clear-browsing-data
                // cssSelector: #clear-browsing-data
                //
                
                switchToContext(driver, "WEBVIEW");
                driver.findElementByXPath("//*[@id=\"clear-browsing-data\"]").click();
                switchToContext(driver, "NATIVE_APP");
                //driver.findElementById("clear-browsing-data");//params.put("value", "//*[@id=\"clear-browsing-data\"]");
                //params.put("framework", "appium-1.3.4");
                //driver.executeScript("mobile:application.element:click", params);*/

                //
                // do it!
                //
                // text: Clear
                //
                try {
                    //params.clear();
                    //params.put("value", "//*[@resource-id=\"android:id/button1\"]");
                    //params.put("framework", "appium-1.3.4");
                   // driver.executeScript("mobile:application.element:click", params);
                    driver.findElementByXPath("//*[@resource-id=\"android:id/button1\"]").click();
                } catch (Exception e) {
                    try {
                        switchToContext(driver, "WEBVIEW");
                        params.clear();
                        //params.put("value", "//*[@resource-id=\"com.android.chrome:id/button_preference\"]");
                        //params.put("framework", "appium-1.3.4");
                        //driver.executeScript("mobile:application.element:click", params);
                        driver.findElementByXPath("//*[@resource-id=\"com.android.chrome:id/button_preference\"]").click();
                    } catch (Exception e2) {
                        //do nothing
                    }
                }

                //
                // Close Chrome
                //
        
                params.clear();
                params.put("name", "Chrome");

                try {
                    driver.executeScript("mobile:application:close", params);
                } catch (Exception e) {}
                driver.executeScript("mobile:application:open", params);
            }
            else
            {
                throw new IllegalStateException( "not supported before Android 5.0" );
            }
        }
        finally
        {
            switchToContext(driver, currentContext);
        }
    }

    //
    // Helpers
    //

    private static void switchToContext(RemoteWebDriver driver, String context)
    {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        
        Map<String,String> params = new HashMap<>();
        params.put("name", context);
        
        executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
    }

    private static String getCurrentContextHandle(RemoteWebDriver driver)
    {          
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        
        String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
        
        return context;
    }

    private static void sleep(long millis) 
    {
        try 
        {
            Thread.sleep(millis);
        } 
        catch (InterruptedException e) 
        {}
    }

    private static int[] parseVersion( String str )
    {
        String[] tokens = str.split( "\\." );
        int[] rtn = new int[ tokens.length ];

        for( int i = 0; i < tokens.length; ++i )
        {
            rtn[ i ] = parseToken( tokens[ i ] );
        }

        return rtn;
    }

    private static int parseToken( String str )
    {
        StringBuilder buff = new StringBuilder();

        for( int i = 0; i < str.length(); ++i )
        {
            char thisChar = str.charAt( i );

            if ( Character.isDigit( thisChar ))
            {
                buff.append( thisChar );
            }
        }

        return Integer.parseInt( buff.toString() );
    }

    private static boolean clickIfPresent( RemoteWebDriver driver, String xpath )
    {
        boolean rtn = false;

        HashMap<String, Object> params = new HashMap();
        params.put("value", xpath);
        params.put("framework", "perfectoMobile");

        String result = (String) driver.executeScript("mobile:application.element:find", params);

        if (( result != null ) && ( !( "false".equalsIgnoreCase( result ) )))
        {
            //
            // This shouldn't be necessary, but I'm seeing the 'find' call finding
            // elements that are scrolled off of the screen/page.
            //
            
            try
            {
                driver.executeScript("mobile:application.element:click", params);

                rtn = true;
            }
            catch( Throwable e )
            {}
        }

        return rtn;
    }


}
