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
package com.xframium.utility;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BrowserCacheLogic
{

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

            //
            // swipe to expose safari and click once exposed
            //

            boolean found = false;
            int count = 0;

            while(( !found ) && ( count < 10 ))
            {
                found = clickIfPresent( driver, "//cell[@name='Safari']" );

                if ( !found )
                {
                    params.clear();
                    params.put("start", "50%,75%");
                    params.put("end", "50%,25%");
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

            //
            // clear Cache
            //
        
            params.clear();
            params.put("value", "//*[starts-with(text(),'Clear History')]");
            params.put("framework", "perfectoMobile");
            driver.executeScript("mobile:application.element:click", params);
            params.put("value", "//*[(@class='UIAButton' or @class='UIATableCell') and starts-with(@label,'Clear') and @isvisible='true']");
            driver.executeScript("mobile:application.element:click", params);
        
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
        
            driver.executeScript("mobile:application:close", params);
        }
        finally
        {
            switchToContext(driver, currentContext);
        }
    }

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

            if ( osVer[0] >= 5 )
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
                sleep(1000);

                //
                // select chrome menu
                //
                // resource-id: com.android.chrome:id/document_menu_button -- contains menu_button
                // content-desc: More options
                //

                params.clear();
                params.put("value", "//*[starts-with(@resource-id,'menu_button')]");
                params.put("framework", "perfectoMobile");
                driver.executeScript("mobile:application.element:click", params);

                //
                // select history
                //
                // text: History
                // resource-id: com.android.chrome:id/menu_item_text
                // content-desc: History
                //

                params.clear();
                params.put("value", "//*[starts-with(text(),'History')]");
                params.put("framework", "perfectoMobile");
                driver.executeScript("mobile:application.element:click", params);

                //
                // select clear browsing data
                //
                // text: Clear browsing data...
                // id: clear-browsing-data
                // cssSelector: #clear-browsing-data
                //

                params.clear();
                params.put("value", "//*[starts-with(text(),'Clear browsing')]");
                params.put("framework", "perfectoMobile");
                driver.executeScript("mobile:application.element:click", params);

                //
                // do it!
                //
                // text: Clear
                //

                params.clear();
                params.put("value", "//*[starts-with(text(),'Clear')]");
                params.put("framework", "perfectoMobile");
                driver.executeScript("mobile:application.element:click", params);

                //
                // Close Chrome
                //
        
                params.clear();
                params.put("name", "Chrome");
        
                driver.executeScript("mobile:application:close", params);
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
