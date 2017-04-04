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
/*
 * 
 */
package org.xframium.device.ng;

import java.util.HashMap;
import java.util.Map;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.element.ElementFactory;
import org.xframium.page.keyWord.KeyWordPage;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.spi.KeyWordPageImpl;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.reporting.ReportingElementAdapter;

/**
 * The base class for Java based tests in xFramium.  
 *
 * @author Allen Geary
 */
public abstract class AbstractJavaTest extends AbstractSeleniumTest
{

    /**
     * The default constructor required when using Java based test in xFramium.  This will disable XML mode and wrap the elements for reporting
     */
    public AbstractJavaTest()
    {
        System.setProperty( ElementFactory.WRAPPER_IMPL, ReportingElementAdapter.class.getName() );
        setXmlMode( false );
    }
    
    /**
     * Allows the script developer to wrap commands inside of a step for reporting purposes
     *
     * @param testName the test name
     * @param stepName the step name
     * @param description This will show in the report
     */
    public void startStep( TestName testName, String stepName, String description )
    {
        testName.getTest().startStep( new SyntheticStep( stepName, stepName, description ), null, null );
    }
    
    /**
     * Allows creation of a configuration backed page object without having to define the interface and implemention classes
     * @param pageName The name of the page matched to the name of the page in your object repository
     * @param webDriver The webDriver object
     * @return A synthetic page object
     */
    public Page createPage( String pageName, DeviceWebDriver webDriver )
    {
        KeyWordPage wPage = (KeyWordPage) PageManager.instance().createPage( KeyWordPage.class, webDriver );
        wPage.setPageName( pageName );
        return wPage;
    }
    
    /**
     * Creates a new page object based off of the supplied interface
     * @param pageInterface The interface to create - must have a class in the spi package named the same as the interface with Impl appended 
     * @param webDriver The webDriver object
     * @return A synthetic page object
     */
    public Page createPage( Class pageInterface, DeviceWebDriver webDriver )
    {
        return (Page) PageManager.instance().createPage( pageInterface, webDriver );
    }
    
    /**
     * Completes a custom step
     *
     * @param testName the test name
     * @param stepStatus the step status
     * @param e provided on failure
     */
    public void stopStep( TestName testName, StepStatus stepStatus, Throwable e )
    {
        testName.getTest().completeStep( stepStatus, e );
    }
    
    
    
    
}
