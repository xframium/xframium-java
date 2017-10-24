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
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xframium.container.SuiteContainer;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.step.spi.KWSString2.OperationType;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSAddDevice.
 */
public class KWSContext extends AbstractKeyWordStep
{
    private static String NAME = "Name";
    private static String VALUE = "Value";
    public enum ContextType
    {
        CLEAR( 1, "CLEAR", "Remove a context variable" ), 
        SET( 2, "SET", "Set a context variable" ), 
        ;

        public List<ContextType> getSupported()
        {
            List<ContextType> supportedList = new ArrayList<ContextType>( 10 );
            supportedList.add( ContextType.CLEAR );
            supportedList.add( ContextType.SET );
            return supportedList;
        }

        private ContextType( int id, String name, String description )
        {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        private int id;
        private String name;
        private String description;
    }
    

    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
    {
        String contextName = getParameterValue( getParameter( NAME ), contextMap, dataMap, executionContext.getxFID() );

        switch ( ContextType.valueOf( getName().toUpperCase() ) )
        {
            case CLEAR:
                contextMap.remove( contextName );
                break;
                
            case SET:
                addContext( contextName, getParameterValue( getParameter( VALUE ), contextMap, dataMap, executionContext.getxFID() ), contextMap, executionContext );
                break;
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
    
    public KWSContext()
    {
        kwName = "Context";
        kwDescription = "Allows the manipulation of contezxt variables";
        kwHelp = "https://www.xframium.org/keyword.html#kw-context";
        orMapping = false;
        category="Utility";
    }

}
