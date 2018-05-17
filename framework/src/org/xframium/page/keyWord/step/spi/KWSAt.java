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

import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.xframium.container.SuiteContainer;
import org.xframium.device.cloud.action.CloudActionProvider;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

public class KWSAt extends AbstractKeyWordStep
{
    public KWSAt()
    {
        kwName = "Element dimensions";
        kwDescription = "Extract the size and location of a named element";
        kwHelp = "https://www.xframium.org/keyword.html#kw-at";
        category = "Verification";
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
        Element currentElement = getElement( pageObject, contextMap, webDriver, dataMap, executionContext ).cloneElement();
        currentElement.setCacheNative( true );
        
        
        CloudActionProvider aP = ( (DeviceWebDriver) webDriver ).getCloud().getCloudActionProvider();
        
        Point at = aP.translatePoint( ( (DeviceWebDriver) webDriver), ((WebElement) currentElement.getNative()).getLocation() );
        Dimension size = aP.translateDimension( (DeviceWebDriver) webDriver, ((WebElement) currentElement.getNative()).getSize() );
        Dimension windowSize = aP.translateDimension( (DeviceWebDriver) webDriver, webDriver.manage().window().getSize() );
        
        
        
        String contextName = getContext();
        if ( contextName == null )
            contextName = getName();

        if ( getContext() != null )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Setting Context Data to [" + currentElement.getValue() + "] for [" + getContext() + "]" );

            addContext( contextName + ".x", at.getX() + "", contextMap, executionContext );
            addContext( contextName + ".y", at.getY() + "", contextMap, executionContext );

            addContext( contextName + ".x%", ((int) (((double) at.getX() / (double) windowSize.getWidth()) * 100.0)) + "", contextMap, executionContext );
            addContext( contextName + ".y%", ((int) (((double) at.getY() / (double) windowSize.getHeight()) * 100.0)) + "", contextMap, executionContext );

            addContext( contextName + ".width", size.getWidth() + "", contextMap, executionContext );
            addContext( contextName + ".height", size.getHeight() + "", contextMap, executionContext );
            addContext( contextName + ".centerx", at.getX() + (size.getWidth() / 2) + "", contextMap, executionContext );
            addContext( contextName + ".centery", at.getY() + (size.getHeight() / 2) + "", contextMap, executionContext );
            addContext( contextName + ".centerx%", ((int) ((((double) at.getX() + ((double) size.getWidth() / 2.0)) / (double) windowSize.getWidth()) * 100.0)) + "", contextMap, executionContext );
            addContext( contextName + ".centery%", ((int) ((((double) at.getY() + ((double) size.getHeight() / 2.0)) / (double) windowSize.getHeight()) * 100.0)) + "", contextMap, executionContext );

            
            
        }

        return currentElement.isPresent();
    }

}
