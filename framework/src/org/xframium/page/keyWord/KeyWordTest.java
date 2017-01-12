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
package org.xframium.page.keyWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.exception.ObjectConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyWordTest.
 */
public class KeyWordTest
{

    /** The log. */
    private Log log = LogFactory.getLog( KeyWordTest.class );

    /** The name. */
    private String name;

    /** The active. */
    private boolean active;

    /** The data providers. */
    private String[] dataProviders;

    /** The data driver. */
    private String dataDriver;

    /** The timed. */
    private boolean timed;

    /** The link id. */
    private String linkId;

    /** The os. */
    private String os;

    /** The description. */
    private String description;

    /** The threshold. */
    private int threshold;

    /** The test tags. */
    private String[] testTags;

    /** The content keys */
    private String[] contentKeys;
    
    private String[] deviceTags;
    
    private String inputPage;
    private String outputPage;
    private String[] operationList;
    private String mode = "function";
    
    private List<KeyWordParameter> expectedParameters = new ArrayList<KeyWordParameter>( 5 );
    
    
    
    private int count;

    /** The step list. */
    private List<KeyWordStep> stepList = new ArrayList<KeyWordStep>( 10 );

    /**
     * Instantiates a new key word test.
     *
     * @param name
     *            the name
     * @param active
     *            the active
     * @param dataProviders
     *            the data providers
     * @param dataDriver
     *            the data driver
     * @param timed
     *            the timed
     * @param linkId
     *            the link id
     * @param os
     *            the os
     * @param threshold
     *            the threshold
     * @param description
     *            the description
     * @param testTags
     *            the test tags
     * @param contentKeys
     *            the content keys
     */
    public KeyWordTest( String name, boolean active, String dataProviders, String dataDriver, boolean timed, String linkId, String os, int threshold, String description, String testTags, String contentKeys, String deviceTags, Map<String,String> overrideMap, int count, String inputPage, String outputPages, String mode, String operationList )
    {
        this.name = name;
        this.active = Boolean.parseBoolean( getValue( name, "active", active + "", overrideMap ) );
        
        String dP = getValue( name, "dataProvider", dataProviders, overrideMap );
        
        if ( dP != null )
            this.dataProviders = dP.split( "," );
        this.dataDriver = getValue( name, "dataDriver", dataDriver, overrideMap );
        this.timed = Boolean.parseBoolean( getValue( name, "timed", timed + "", overrideMap ) );
        this.linkId = getValue( name, "linkId", linkId, overrideMap );
        this.os = getValue( name, "os", os, overrideMap );
        
        if ( threshold > 0 )
        {
            String thresholdModifier = overrideMap.get( "thresholdModifier" );
            if ( thresholdModifier != null )
            {
                double modifierPercent = ( Integer.parseInt( thresholdModifier ) / 100.0 );
                double modifierValue = Math.abs( modifierPercent ) * (double) threshold;
                
                if ( modifierPercent > 0 )
                    this.threshold = threshold + (int)modifierValue;
                else
                    this.threshold = threshold - (int)modifierValue;
            }
        }
        else
            this.threshold = threshold;
        
        this.description = getValue( name, "description", description, overrideMap );
        
        String value = getValue( name, "testTags", testTags, overrideMap );
        
        if ( value != null )
            this.testTags = value.split( "," );
        else
            this.testTags = new String[] { "" };

        value = getValue( name, "contentKeys", contentKeys, overrideMap );
        if ( value != null )
            this.contentKeys = value.split( "\\|" );
        else
            this.contentKeys = new String[] { "" };
        
        value = getValue( name, "deviceTags", deviceTags, overrideMap );
        if ( value != null && !value.trim().isEmpty() )
            this.deviceTags = value.split( "," );
        
        this.count = Integer.parseInt( getValue( name, "count", count + "", overrideMap ) );
        
        setMode( mode );
        setInputPage( inputPage );
        setOutputPage( outputPages );
        setOperationList( operationList );
    }
    
    public List<KeyWordParameter> getExpectedParameters()
    {
        return expectedParameters;
    }

    public void setExpectedParameters( List<KeyWordParameter> expectedParameters )
    {
        this.expectedParameters = expectedParameters;
    }

    public String getInputPage()
    {
        return inputPage;
    }

    public void setInputPage( String inputPage )
    {
        this.inputPage = inputPage;
    }



    public String getOutputPage()
    {
        return outputPage;
    }

    public void setOutputPage( String outputPage )
    {
        this.outputPage = outputPage;
    }

    public String[] getOperationList()
    {
        return operationList;
    }

    public void setOperationList( String[] operationList )
    {
        this.operationList = operationList;
    }

    public void setOperationList( String operationList )
    {
        if ( operationList != null && !operationList.trim().isEmpty() )
            this.operationList = operationList.split( "," );
    }
    
    public String getMode()
    {
        return mode;
    }



    public void setMode( String mode )
    {
        this.mode = mode;
    }



    private String getValue( String testName, String attributeName, String attributeValue, Map<String,String> overrideMap )
    {
        String keyName = testName + "." + attributeName;
        if ( System.getProperty( keyName ) != null )
            return System.getProperty( keyName );
        if ( overrideMap.containsKey( keyName ) )
            return overrideMap.get( keyName );
        else
            return attributeValue;
    }

    
    
    public int getCount()
    {
        return count;
    }

    public void setCount( int count )
    {
        this.count = count;
    }


    public String[] getDeviceTags()
    {
        return deviceTags;
    }

    /**
     * Gets the data driver.
     *
     * @return the data driver
     */
    public String getDataDriver()
    {
        return dataDriver;
    }

    public String getDescription()
    {
        return description;
    }
    
    

    public String[] getTestTags()
    {
        return testTags;
    }

    /**
     * Adds the step.
     *
     * @param step
     *            the step
     */
    public void addStep( KeyWordStep step )
    {
        if ( log.isDebugEnabled() )
            log.debug( "Adding Step [" + step.getName() + "] to [" + name + "]" );
        
        if ( step.isStartAt() )
        {
            log.warn( "Clearing steps out of " + getName() + " as the startAt flag was set" );
            for ( KeyWordStep kS : stepList )
                kS.setActive( false );
        }
        
        stepList.add( step );
    }
    

    /**
     * Get step at offset.
     *
     * @param step
     *            the step
     */
    public KeyWordStep getStepAt( int offset )
    {
        return (KeyWordStep) stepList.get( offset );
    }

    /**
     * Gets the data providers.
     *
     * @return the data providers
     */
    public String[] getDataProviders()
    {
        return dataProviders;
    }

    /**
     * Gets the data providers.
     *
     * @return the data providers
     */
    public String getDataProvidersAsString()
    {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append( " [" );
        for ( String provider : dataProviders )
            sBuilder.append( provider ).append( ", " );
        sBuilder.append( "]" );
        return sBuilder.toString();
    }

    @Override
    public String toString()
    {
        return "KeyWordTest [name=" + name + ", active=" + active + ", dataProviders=" + Arrays.toString( dataProviders ) + ", dataDriver=" + dataDriver + ", timed=" + timed + ", linkId=" + linkId + ", os=" + os + ", description=" + description
                + ", threshold=" + threshold + ", testTags=" + Arrays.toString( testTags ) + ", contentKeys=" + Arrays.toString( contentKeys ) + ", deviceTags=" + Arrays.toString( deviceTags ) + "]";
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive()
    {
        return active;
    }

    /**
     * Checks if is timed.
     *
     * @return true, if is timed
     */
    public boolean isTimed()
    {
        return timed;
    }

    /**
     * Execute test.
     *
     * @param webDriver
     *            the web driver
     * @param contextMap
     *            the context map
     * @param dataMap
     *            the data map
     * @param pageMap
     *            the page map
     * @return true, if successful
     * @throws Exception
     *             the exception
     */
    public boolean executeTest( WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext ) throws Exception
    {
        boolean stepSuccess = true;
        

        if ( log.isInfoEnabled() )
            log.info( "*** Executing Test " + name + (linkId != null ? " linked to " + linkId : "") );

        long startTime = System.currentTimeMillis();

        
        String executionId = PageManager.instance().getExecutionId( webDriver );
        String deviceName = PageManager.instance().getDeviceName( webDriver );
        

        for ( KeyWordStep step : stepList )
        {
            if ( !step.isActive() )
                continue;
            
            if ( log.isDebugEnabled() )
                log.debug( "Executing Step [" + step.getName() + "]" );

            Page page = null;
            String siteName = step.getSiteName();
            if ( siteName == null || siteName.isEmpty() )
            {
                if ( sC != null )
                    siteName = sC.getSiteName();
                else
                    siteName = PageManager.instance().getSiteName();
            }
            
            if ( step.getPageName() != null )
            {
                page = pageMap.get( siteName + "." + step.getPageName() );
                if ( page == null )
                {
                    if ( log.isInfoEnabled() )
                        log.info( "Creating Page [" + siteName + "." + step.getPageName() + "]" );

                    page = PageManager.instance().createPage( KeyWordDriver.instance().getPage(step.getSiteName() != null && step.getSiteName().trim().length() > 0 ? step.getSiteName() : PageManager.instance().getSiteName(), step.getPageName() ), webDriver );
                    if ( page == null )
                    {
                        
                        executionContext.startStep( new SyntheticStep( step.getPageName(), "PAGE" ), contextMap, dataMap );
                        executionContext.completeStep( StepStatus.FAILURE, new ObjectConfigurationException( step.getSiteName() == null ? PageManager.instance().getSiteName() : step.getSiteName(), step.getPageName(), null ) );
                        
                        stepSuccess = false;
                        return false;
                    }
                    pageMap.put( siteName + "." + step.getPageName(), page );
                }
            }

            stepSuccess = step.executeStep( page, webDriver, contextMap, dataMap, pageMap, sC, executionContext );

            if ( !stepSuccess )
            {
                if ( log.isWarnEnabled() )
                    log.warn( "***** Step [" + step.getName() + "] Failed" );

                if ( timed )
                    PageManager.instance().addExecutionTiming( executionId, deviceName, getName(), System.currentTimeMillis() - startTime, StepStatus.FAILURE, description, threshold );

                stepSuccess = false;
                return false;

            }

        }
        if ( timed )
            PageManager.instance().addExecutionTiming( executionId, deviceName, getName(), System.currentTimeMillis() - startTime, StepStatus.SUCCESS, description, threshold );

        return stepSuccess;
    }

    /**
     * Gets the os.
     *
     * @return the os
     */
    public String getOs()
    {
        return os;
    }

    /**
     * Sets the os.
     *
     * @param os
     *            the new os
     */
    public void setOs( String os )
    {
        this.os = os;
    }

    /**
     * Checks if is tagged.
     *
     * @param tagName
     *            the tag name
     * @return true, if is tagged
     */
    public boolean isTagged( String tagName )
    {
        if ( testTags == null )
            return false;

        for ( String testTag : testTags )
        {
            if ( tagName.equalsIgnoreCase( testTag ) )
                return true;
        }

        return false;
    }
    
    public boolean isDeviceTagged( String tagName )
    {
        if ( deviceTags == null || deviceTags.length <= 0 )
            return false;

        for ( String testTag : deviceTags )
        {
            if ( tagName.equalsIgnoreCase( testTag ) )
                return true;
        }

        return false;
    }

    /**
     * Gets the tags.
     *
     * @return the tags
     */
    public String[] getTags()
    {
        if ( testTags == null )
            return new String[] { "" };
        else
            return testTags;
    }

    /**
     * Gets the content keys.
     *
     * @return the content keys
     */
    public String[] getContentKeys()
    {
        return contentKeys;
    }

}
