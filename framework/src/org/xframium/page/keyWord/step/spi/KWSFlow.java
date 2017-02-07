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
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.FlowException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

public class KWSFlow extends AbstractKeyWordStep
{
    public KWSFlow()
    {
        kwName = "Flow Control";
        kwDescription = "Allows you to control the flow of the running test";
        kwHelp = "https://www.xframium.org/keyword.html#kw-flow";
        category = "Flow Control";
        orMapping = false;
    }
    
    public enum FlowType
    {
        END_SUCCESS( 1, "END_SUCCESS", "End test successfully" ),
        END_FAILURE( 1, "END_FAILURE", "End test in failure" );
        
        
        public List<FlowType> getSupported()
        {
            List<FlowType> supportedList = new ArrayList<FlowType>( 10 );
            supportedList.add( FlowType.END_SUCCESS );
            supportedList.add( FlowType.END_FAILURE );
            
            return supportedList;
        }

        private FlowType( int id, String name, String description )
        {
            this.id = id;
            this.name = name;
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
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
    {
        
        switch( FlowType.valueOf( getName() ) )
        {
            case END_FAILURE:
                throw new FlowException( false );
            case END_SUCCESS:
                throw new FlowException( true );
        }

        return true;
    }

}
