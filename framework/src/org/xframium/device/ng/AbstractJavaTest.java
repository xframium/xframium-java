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

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationProvider;
import org.xframium.application.ApplicationRegistry;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudProvider;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.logging.ThreadedFileHandler;
import org.xframium.gesture.GestureManager;
import org.xframium.gesture.factory.spi.PerfectoGestureFactory;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.element.ElementFactory;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.keyWord.KeyWordPage;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.spi.KeyWordPageImpl;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.page.listener.LoggingExecutionListener;
import org.xframium.reporting.ReportingElementAdapter;
import org.xframium.spi.Device;

/**
 * The base class for Java based tests in xFramium.  
 *
 * @author Allen Geary
 */
public abstract class AbstractJavaTest extends AbstractSeleniumTest
{

    /**
     * The default contstructor required when using Jav based test in xFramium.  This will disable XML mode and wrap the elements for reporting
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
    
    /**
     * Creates a KeyWord step to be used in a Java test
     *
     * @param keyword The name of the keyword
     * @param pageName the page name
     * @param elementName the element name
     * @param parameterList A list of parameter to pass to the keyword.  User name==value for named parameters
     * @return The created step
     */
    public KeyWordStep createStep( String keyword, String pageName, String elementName, String[] parameterList )
    {
        KeyWordStep step = KeyWordStepFactory.instance().createStep( elementName, pageName, true, keyword, null, false, StepFailure.ERROR, false, null, null, null, 0, null, 0, null, null, null, null, null, false, false, null, null, null, null );
        
        if ( parameterList != null )
        {
            for ( String s : parameterList )
            {
                String[] sArray = s.split( "==" );
                if ( s.contains( "==" ) )
                    step.addParameter( new KeyWordParameter( ParameterType.STATIC, sArray.length > 1 ? sArray[1] : "{EMPTY}", sArray[0], null ) );
                else
                    step.addParameter( new KeyWordParameter( ParameterType.STATIC, s, null, null ) );
            }
        }
        
        return step;
        
    }
    
    /**
     * Execute a keyword step
     *
     * @param step the step
     * @param webDriver the web driver
     * @return true, if successful
     * @throws Exception the exception
     */
    public boolean executeStep( KeyWordStep step, DeviceWebDriver webDriver ) throws Exception
    {
        KeyWordPage p = new KeyWordPageImpl();
        p.setPageName( step.getPageName() );
        return step.executeStep( p, webDriver, null, null, null, null, webDriver.getExecutionContext() );
    }
    
    /**
     * Creates and executes a KeyWord step
     *
     * @param keyword The name of the keyword
     * @param pageName the page name
     * @param elementName the element name
     * @param parameterList A list of parameter to pass to the keyword.  User name==value for named parameters
     * @param webDriver The webDriver for the active run
     * @return The created step
     */
    public boolean executeStep( String keyword, String pageName, String elementName, String[] parameterList, DeviceWebDriver webDriver ) throws Exception
    {
        KeyWordPage p = new KeyWordPageImpl();
        p.setPageName( pageName );
        return createStep( keyword, pageName, elementName, parameterList ).executeStep( p, webDriver, null, null, null, null, webDriver.getExecutionContext() );
    }
    
    /**
     * Captures the state (image and source) of the running application
     *
     * @param webDriver the web driver
     * @throws Exception the exception
     */
    public void dumpState( DeviceWebDriver webDriver ) throws Exception
    {
        createStep( "STATE", "", "", new String[ 0 ]).executeStep( null, webDriver, null, null, null, null, webDriver.getExecutionContext() );
    }
    
    /**
     * Captures the state (image and source) of the running application and compares it for deviation against the previous historical values
     *
     * @param webDriver the web driver
     * @param name the name
     * @param historicalCount The number of historical records to compare to
     * @param deviationPercentage The amount of image deviation from 0 to 100
     * @throws Exception the exception
     */
    public void dumpState( DeviceWebDriver webDriver, String name, int historicalCount, int deviationPercentage) throws Exception
    {
        KeyWordStep step = createStep( "STATE", "", "", new String[ 0 ]);
        step.addParameter( new KeyWordParameter( ParameterType.STATIC, name, "checkPointName", null ) );
        step.addParameter( new KeyWordParameter( ParameterType.STATIC, historicalCount + "", "historicalCount", null ) );
        step.addParameter( new KeyWordParameter( ParameterType.STATIC, deviationPercentage + "", "deviationPercentage", null ) );
        step.executeStep( null, webDriver, null, null, null, null, webDriver.getExecutionContext() );
    }
}
