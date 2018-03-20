/*******************************************************************************
 * xFramium
 *
 * Copyright 2017 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package org.xframium.examples.java.page.spi;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.xframium.examples.java.page.WebHomePage;
import org.xframium.page.AbstractPage;
import org.xframium.page.StepStatus;
import org.xframium.page.keyWord.KeyWordPage;
import org.xframium.page.keyWord.step.spi.KWSCompare2.CompareType;

public class WebHomePageImpl extends AbstractPage implements WebHomePage
{

    public void initializePage()
    {
        // TODO Auto-generated method stub

    }
    
    public void testKeyword()
    {
        try
        {
            
            //
            // Optionally start a step container to allowing for reporting structure
            //
            String beforeClick = getElement( WebHomePage.TOGGLE_VALUE ).getValue();
            getElement( WebHomePage.TOGGLE_BUTTON ).click();
            String afterClick = getElement( WebHomePage.TOGGLE_VALUE ).getValue();
            
            Assert.assertNotEquals( afterClick,  beforeClick );
            
            String typeAttribute = getElement( WebHomePage.TOGGLE_BUTTON ).getAttribute( "type" );
            
            Assert.assertFalse( getElement( WebHomePage.DELETE_BUTTON ).isVisible() );
            getElement( WebHomePage.ACCORDIAN_OPEN ).click();
            Assert.assertTrue( getElement( WebHomePage.DELETE_BUTTON ).waitForVisible( 12, TimeUnit.SECONDS ) );
            
        }
        catch( Exception e )
        {
            
        }
        
        
        
    }

}
