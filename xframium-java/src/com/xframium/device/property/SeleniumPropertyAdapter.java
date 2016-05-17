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
package com.xframium.device.property;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;

public class SeleniumPropertyAdapter extends AbstractPropertyAdapter
{
    private static final String IMPLICIT_TIMEOUT = "selenium.timeouts.implicitWait";
    private static final String PAGE_LOAD_TIMEOUT = "selenium.timeouts.pageLoad";
    private static final String SET_SCRIPT_TIMEOUT = "selenium.timeouts.setScript";
    
    @Override
    public boolean applyProperties( Properties configurationProperties )
    {
        return true;
    }
    
    @Override
    public boolean applyInstanceProperties( Properties configurationProperties, Object wDriver )
    {
        WebDriver webDriver = (WebDriver) wDriver;
        
        webDriver.manage().timeouts().implicitlyWait( getIntProperty( configurationProperties, IMPLICIT_TIMEOUT, 12000 ), TimeUnit.MILLISECONDS );
        webDriver.manage().timeouts().pageLoadTimeout( getIntProperty( configurationProperties, PAGE_LOAD_TIMEOUT, 45000 ), TimeUnit.MILLISECONDS );
        webDriver.manage().timeouts().setScriptTimeout( getIntProperty( configurationProperties, SET_SCRIPT_TIMEOUT, 30000 ), TimeUnit.MILLISECONDS );
        return true;
    }
    
    
}
