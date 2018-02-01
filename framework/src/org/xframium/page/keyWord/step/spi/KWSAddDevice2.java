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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xframium.artifact.ArtifactManager;
import org.xframium.artifact.ArtifactType;
import org.xframium.container.SuiteContainer;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSAddDevice.
 */
public class KWSAddDevice2 extends AbstractKeyWordStep
{
    private static final String REGISTRY_NAME = "registry name";
    private static final String DRIVER_TYPE = "driver type"; 

    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
    {
        String registryName = null;
        String driverType = null;
        DesiredCapabilities dC = new DesiredCapabilities();
        
        for ( KeyWordParameter p : getParameterList() )
        {
            if ( p.getName() != null )
            {
                if ( REGISTRY_NAME.equals( p.getName().toLowerCase() ) )
                    registryName = getParameterValue(p, contextMap, dataMap, executionContext.getxFID() );
                else if ( DRIVER_TYPE.equals( p.getName().toLowerCase() ) )
                    driverType = getParameterValue(p, contextMap, dataMap, executionContext.getxFID() );
                else
                    dC.setCapability( p.getName(), getParameterValue(p, contextMap, dataMap, executionContext.getxFID() ) );
            }
        }
        
        if ( driverType == null )
            throw new ScriptConfigurationException( "You must provide a 'Device Type' specifying the type of web driver (WEB,APPIUM,etc)" );
        
        ConnectedDevice wD = null;
        
        if( registryName != null )
        {
            //
            // Adding a device defined in the device registry
            //
            if ( DeviceManager.instance( executionContext.getxFID() ).getDevice(registryName.toString()) == null )
                throw new ScriptConfigurationException( "Device Name should be configured in DeviceRegistry with inactive status" );
            
            wD = DeviceManager.instance( executionContext.getxFID() ).getInactiveDevice( registryName + "", executionContext.getxFID()  );
            wD.getWebDriver().setExecutionContext( executionContext );
            executionContext.getDeviceMap().put( getName() + "", wD );
            if ( getContext() != null )
            {
                addContext( getContext(), wD.getPopulatedDevice().getDeviceName(), contextMap, executionContext );
                addContext( getContext() + ".id", wD.getWebDriver().getExecutionId(), contextMap, executionContext );
                addContext( getContext() + ".addedTo", ( (DeviceWebDriver) webDriver ).getPopulatedDevice().getDeviceName(), contextMap, executionContext );
                if ( ( (DeviceWebDriver) webDriver ).getPopulatedDevice().getPhoneNumber() != null  )
                {
                    addContext( getContext() + ".phoneNumber", wD.getWebDriver().getPopulatedDevice().getPhoneNumber(), contextMap, executionContext );
                }
            }
        }
        else
        {
            wD = DeviceManager.instance( executionContext.getxFID() ).getConnectedDevice( getName(), dC, driverType, executionContext.getxFID() );
            wD.getWebDriver().setExecutionContext( executionContext );
            executionContext.getDeviceMap().put( getName() + "", wD );
            if ( getContext() != null )
            {
                addContext( getContext(), wD.getPopulatedDevice().getDeviceName(), contextMap, executionContext );
                addContext( getContext() + ".id", wD.getWebDriver().getExecutionId(), contextMap, executionContext );
                addContext( getContext() + ".addedTo", ( (DeviceWebDriver) webDriver ).getPopulatedDevice().getDeviceName(), contextMap, executionContext );
                if ( ( (DeviceWebDriver) webDriver ).getPopulatedDevice().getPhoneNumber() != null  )
                {
                    addContext( getContext() + ".phoneNumber", wD.getWebDriver().getPopulatedDevice().getPhoneNumber(), contextMap, executionContext );
                }
            }
        }
        
        if ( wD.getWebDriver().getCloud().getProvider().equals( "PERFECTO" ) )
        {
            if ( ArtifactManager.instance( executionContext.getxFID() ).isArtifactEnabled( ArtifactType.REPORTIUM.name() )  )
            {
                //
                // Reportium Integration
                //
                List<String> tagList = new ArrayList<String>( 5 );
                tagList.add( "xFramium" );
                if ( CloudRegistry.instance(executionContext.getxFID()).getCloud() != null && CloudRegistry.instance(executionContext.getxFID()).getCloud().getUserName() != null && !CloudRegistry.instance(executionContext.getxFID()).getCloud().getUserName().isEmpty() )
                    tagList.add( CloudRegistry.instance(executionContext.getxFID()).getCloud().getUserName() );
                
                if ( wD.getWebDriver().getAut() != null && wD.getWebDriver().getAut().getName() != null && !wD.getWebDriver().getAut().getName().isEmpty() && !wD.getWebDriver().getAut().getName().equals( "NOOP" ) )
                    tagList.add( wD.getWebDriver().getAut().getName() );
                
                if ( executionContext.getTest().getTags() != null && executionContext.getTest().getTags().length > 0 )
                {
                    for ( String tag : executionContext.getTest().getTags() )
                        tagList.add( tag );
                }
                
                tagList.add( "Additional Device" );
                
                PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder().withProject( new Project( ExecutionContext.instance( executionContext.getxFID() ).getSuiteName(), "" ) ).withContextTags( tagList.toArray( new String[ 0 ] ) )
                        .withWebDriver( wD.getWebDriver() ).build();
                wD.getWebDriver().setReportiumClient( new ReportiumClientFactory().createPerfectoReportiumClient( perfectoExecutionContext ) );

                if ( wD.getWebDriver().getReportiumClient() != null )
                    wD.getWebDriver().getReportiumClient().testStart( executionContext.getTestName() + "-->" + getName(), new com.perfecto.reportium.test.TestContext() );
            }
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
    
    public KWSAddDevice2()
    {
        kwName = "Add device";
        kwDescription = "Allows the script to add a additional device to the same script to validate device/device actions";
        kwHelp = "https://www.xframium.org/keyword.html#kw-adddevice2";
        orMapping = false;
        category="Flow Control";
    }

}
