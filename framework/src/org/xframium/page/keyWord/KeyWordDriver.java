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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.xframium.exception.DataConfigurationException;
import org.xframium.exception.TestConfigurationException;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.keyWord.provider.KeyWordProvider;
import org.xframium.page.listener.KeyWordListener;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyWordDriver.
 */
public class KeyWordDriver
{

    /** The test list. */
    private List<String> testList = new ArrayList<String>( 20 );

    /** The test map. */
    private Map<String, KeyWordTest> testMap = new HashMap<String, KeyWordTest>( 10 );

    /** The inactive test map. */
    private Map<String, KeyWordTest> inactiveTestMap = new HashMap<String, KeyWordTest>( 10 );

    /** The function map. */
    private Map<String, KeyWordTest> functionMap = new HashMap<String, KeyWordTest>( 10 );

    /** The page map. */
    private Map<String, Class<Page>> pageMap = new HashMap<String, Class<Page>>( 10 );

    /** The context map. */
    private ThreadLocal<Map<String, Object>> contextMap = new ThreadLocal<Map<String, Object>>();

    /** The tag map. */
    private Map<String, List<KeyWordTest>> tagMap = new HashMap<String, List<KeyWordTest>>( 10 );

    /** The singleton. */
    private static KeyWordDriver singleton = new KeyWordDriver();

    /** The configuration properties **/
    private Properties configProperties = new Properties();

    private List<KeyWordListener> stepListenerList = new ArrayList<KeyWordListener>( 10 );

    /**
     * Instance.
     *
     * @return the key word driver
     */
    public static KeyWordDriver instance()
    {
        return singleton;
    }

    /**
     * Instantiates a new key word driver.
     */
    private KeyWordDriver()
    {

    }

    /**
     * Load tests.
     *
     * @param keyWordProvider
     *            the key word provider
     */
    public void loadTests( KeyWordProvider keyWordProvider )
    {
        keyWordProvider.readData();
    }

    /** The log. */
    private Log log = LogFactory.getLog( KeyWordDriver.class );

    /**
     * Adds the page.
     *
     * @param pageName
     *            the page name
     * @param pageClass
     *            the page class
     */
    public void addPage( String pageName, Class pageClass )
    {
        if ( log.isDebugEnabled() )
            log.debug( "Mapping Page [" + pageName + "] to [" + pageClass.getName() + "]" );
        pageMap.put( pageName, pageClass );
    }

    public void addStepListener( KeyWordListener stepListener )
    {
        for ( KeyWordListener s : stepListenerList )
        {
            if ( s.equals( stepListener ) )
                return;
        }

        stepListenerList.add( stepListener );
    }

    public void removeStepListener( KeyWordListener stepListener )
    {
        stepListenerList.remove( stepListener );
    }

    public boolean notifyBeforeStep( WebDriver webDriver, KeyWordStep currentStep )
    {
        try
        {
            for ( KeyWordListener k : stepListenerList )
            {
                if ( !k.beforeStep( webDriver, currentStep ) )
                    return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            log.warn( "Before Step notification failed", e );
            return true;
        }
    }

    public void notifyAfterStep( WebDriver webDriver, KeyWordStep currentStep, StepStatus stepStatus )
    {
        try
        {
            for ( KeyWordListener k : stepListenerList )
            {
                k.afterStep( webDriver, currentStep, stepStatus );
            }
        }
        catch ( Exception e )
        {
            log.warn( "After Step notification failed", e );

        }

    }

    public boolean notifyBeforeTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
    {
        try
        {
            for ( KeyWordListener k : stepListenerList )
            {
                if ( !k.beforeTest( webDriver, keyWordTest, contextMap, dataMap, pageMap ) )
                    return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            log.warn( "Before Test notification failed", e );
            return true;
        }
    }

    public void notifyAfterTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass )
    {
        try
        {
            for ( KeyWordListener k : stepListenerList )
            {
                k.afterTest( webDriver, keyWordTest, contextMap, dataMap, pageMap, stepPass );
            }
        }
        catch ( Exception e )
        {
            log.warn( "After Test notification failed", e );
        }
    }

    /**
     * Adds the test.
     *
     * @param test
     *            the test
     */
    public void addTest( KeyWordTest test )
    {
        if ( test.isActive() )
        {
            if ( log.isInfoEnabled() )
                log.info( "Adding test [" + test.getName() + "]" );
            testList.add( test.getName() );
            testMap.put( test.getName(), test );

            //
            // Add the Tagged tests
            //
            for ( String tag : test.getTags() )
            {
                List<KeyWordTest> tagList = tagMap.get( tag.toLowerCase() );
                if ( tagList == null )
                {
                    tagList = new ArrayList<KeyWordTest>( 10 );
                    tagMap.put( tag.toLowerCase(), tagList );
                }

                tagList.add( test );
            }
        }
        else
            inactiveTestMap.put( test.getName(), test );
    }

    /**
     * Adds the function.
     *
     * @param test
     *            the test
     */
    public void addFunction( KeyWordTest test )
    {
        if ( test.isActive() )
        {
            if ( log.isDebugEnabled() )
                log.debug( "Adding function [" + test.getName() + "]" );
            functionMap.put( test.getName(), test );
        }
    }

    /**
     * Gets the page.
     *
     * @param pageName
     *            the page name
     * @return the page
     */
    public Class<Page> getPage( String pageName )
    {
        return pageMap.get( pageName );
    }

    /**
     * Gets the context map.
     *
     * @return the context map
     */
    public Map<String, Object> getContextMap()
    {
        return contextMap.get();
    }

    /**
     * Execution function.
     *
     * @param testName
     *            the test name
     * @param webDriver
     *            the web driver
     * @param dataMap
     *            the data map
     * @param pageMap
     *            the page map
     * @return true, if successful
     * @throws Exception
     *             the exception
     */
    public boolean executionFunction( String testName, WebDriver webDriver, Map<String, PageData> dataMap, Map<String, Page> pageMap ) throws Exception
    {
        if ( log.isDebugEnabled() )
            log.debug( "Attempting to locate function/test [" + testName + "]" );

        KeyWordTest test = functionMap.get( testName );

        if ( test == null )
        {
            test = testMap.get( testName );

            if ( test == null )
            {
                test = inactiveTestMap.get( testName );

                if ( test == null )
                    throw new IllegalArgumentException( "The function [" + testName + "] does not exist" );
            }
        }

        if ( test.getDataProviders() != null )
        {
            if ( log.isInfoEnabled() )
                log.info( "Data Provider set as " + test.getDataProvidersAsString() );

            for ( String dataProvider : test.getDataProviders() )
            {
                String dpMe = dataProvider;
                if ( !dataMap.containsKey( dataProvider ) )
                {
                    //
                    // On a function call we only add the page data if it did
                    // not already exist
                    //

                    PageData pageData = null;
                    if ( dataProvider.contains( "." ) )
                    {
                        String[] typeId = dataProvider.split( "\\." );
                        if ( typeId.length == 2 )
                        {
                            pageData = PageDataManager.instance().getPageData( typeId[0], typeId[1] );
                            dpMe = typeId[0];
                        }
                        else
                            pageData = PageDataManager.instance().getPageData( dataProvider );
                    }
                    else
                        pageData = PageDataManager.instance().getPageData( dataProvider );

                    if ( pageData == null )
                        throw new IllegalArgumentException( "Invalid page data value specified.  Ensure that [" + dataProvider + "] exists in your page data definition" );

                    if ( log.isInfoEnabled() )
                        log.info( "Adding " + dataProvider + " as " + pageData );
                    dataMap.put( dpMe, pageData );
                }
            }
        }

        return test.executeTest( webDriver, contextMap.get(), dataMap, pageMap );
    }

    /**
     * Gets the test.
     *
     * @param testName
     *            the test name
     * @return the test
     */
    public KeyWordTest getTest( String testName )
    {
        KeyWordTest test = testMap.get( testName );

        if ( test == null )
            test = inactiveTestMap.get( testName );

        if ( test == null )
            test = functionMap.get( testName );

        return test;
    }

    /**
     * Gets the tagged tests.
     *
     * @param tagNames
     *            the tag names
     * @return the tagged tests
     */
    public Collection<KeyWordTest> getTaggedTests( String[] tagNames )
    {
        Map<String, KeyWordTest> testMap = new HashMap<String, KeyWordTest>( 10 );

        for ( String tagName : tagNames )
        {
            if ( log.isInfoEnabled() )
                log.info( "Adding Tests by TAG [" + tagName.toLowerCase() + "]" );
            for ( KeyWordTest t : tagMap.get( tagName.toLowerCase() ) )
            {
                if ( log.isDebugEnabled() )
                    log.debug( "Adding Test [" + t.getName() + "]" );
                testMap.put( t.getName(), t );
            }
        }

        return testMap.values();
    }

    /**
     * Gets the tagged tests.
     *
     * @param tagNames
     *            the tag names
     * @return the tagged tests
     */
    public Collection<KeyWordTest> getNamedTests( String[] testNames )
    {
        Map<String, KeyWordTest> testMap = new HashMap<String, KeyWordTest>( 10 );

        for ( String tagName : testNames )
        {
            if ( log.isInfoEnabled() )
                log.info( "Adding Tests by NAME [" + tagName.toLowerCase() + "]" );

            for ( KeyWordTest t : this.testMap.values() )
            {
                if ( tagName.contains( "*" ) )
                {
                    if ( tagName.startsWith( "*" ) )
                    {
                        if ( t.getName().toLowerCase().endsWith( tagName.replace( "*", "" ).trim() ) )
                            testMap.put( t.getName(), t );
                    }
                    else if ( tagName.endsWith( "*" ) )
                    {
                        if ( t.getName().toLowerCase().startsWith( tagName.replace( "*", "" ).trim() ) )
                            testMap.put( t.getName(), t );
                    }
                }
                else
                {
                    if ( tagName.toLowerCase().equals( t.getName().toLowerCase() ) )
                        testMap.put( t.getName(), t );
                }

                if ( log.isDebugEnabled() )
                    log.debug( "Adding Test [" + t.getName() + "]" );
                testMap.put( t.getName(), t );
            }
        }

        return testMap.values();
    }

    /**
     * Execute test.
     *
     * @param testName
     *            the test name
     * @param webDriver
     *            the web driver
     * @return true, if successful
     * @throws Exception
     *             the exception
     */
    public boolean executeTest( String testName, WebDriver webDriver ) throws Exception
    {
        boolean testStarted = false;
        boolean returnValue = false;
        long startTime = System.currentTimeMillis();
        PageManager.instance().getPageCache().clear();

        if ( log.isDebugEnabled() )
            log.debug( "Attempting to locate test [" + testName + "]" );

        KeyWordTest test = testMap.get( testName );

        if ( test == null )
            throw new TestConfigurationException( testName );

        Map<String, PageData> dataMap = new HashMap<String, PageData>( 10 );
        Map<String, Page> pageMap = new HashMap<String, Page>( 10 );

        try
        {
            if ( test.getDataProviders() != null )
            {
                if ( log.isInfoEnabled() )
                    log.info( "Data Provider set as " + test.getDataProvidersAsString() );

                for ( String dataProvider : test.getDataProviders() )
                {
                    String dpMe = dataProvider;
                    PageData pageData = null;
                    if ( dataProvider.contains( "." ) )
                    {
                        String[] typeId = dataProvider.split( "\\." );
                        if ( typeId.length == 2 )
                        {
                            dpMe = typeId[0];
                            pageData = PageDataManager.instance().getPageData( typeId[0], typeId[1] );
                        }
                        else
                            pageData = PageDataManager.instance().getPageData( dataProvider );
                    }
                    else
                        pageData = PageDataManager.instance().getPageData( dataProvider );

                    if ( pageData == null )
                    {
                        log.fatal( "Invalid page data value specified.  Ensure that [" + dataProvider + "] exists in your page data definition" );
                        throw new DataConfigurationException( dataProvider, null );
                    }

                    if ( log.isInfoEnabled() )
                        log.info( "Adding " + dataProvider + " as " + pageData );
                    dataMap.put( dpMe, pageData );
                }
            }

            //
            // If there was a looped driver, then add that
            //
            if ( test.getDataDriver() != null && !test.getDataDriver().isEmpty() )
            {
                String[] testInfo = testName.split( "!" );
                if ( testInfo.length != 2 )
                    throw new IllegalArgumentException( "Could not extract data record from " + testName );

                dataMap.put( test.getDataDriver(), PageDataManager.instance().getPageData( test.getDataDriver(), testInfo[1] ) );
            }

            //
            // Create a new context map and pass it along to all of the steps
            //
            contextMap.set( new HashMap<String, Object>( 10 ) );
            testStarted = true;
            
            if ( !KeyWordDriver.instance().notifyBeforeTest( webDriver, test, contextMap.get(), dataMap, pageMap ) )
            {
                log.warn( "Test was skipped due to a failed test notification listener" );
                return false;
            }
            
            returnValue = test.executeTest( webDriver, contextMap.get(), dataMap, pageMap );
            contextMap.set( null );

            return returnValue;

        }
        catch ( Throwable e )
        {
            if ( !testStarted )
                PageManager.instance().addExecutionLog( null, null, "", testName, testName, startTime, System.currentTimeMillis() - startTime, StepStatus.FAILURE, e.getMessage(), e, 0, e.getMessage(), false, new String[] { e.getMessage() } );
            if ( PageManager.instance().getThrowable() == null )
                PageManager.instance().setThrowable( e );

            log.error( "Error executing Test " + testName, PageManager.instance().getThrowable() );
            return false;
        }
        finally
        {
            if ( testStarted )
                KeyWordDriver.instance().notifyAfterTest( webDriver, test, contextMap.get(), dataMap, pageMap, returnValue );
            
            for ( String key : dataMap.keySet() )
            {
                PageDataManager.instance().putPageData( dataMap.get( key ) );
            }
        }
    }

    /**
     * Gets the test names.
     *
     * @return the test names
     */
    public String[] getTestNames()
    {
        return testList.toArray( new String[0] );
    }

    /**
     * Gets the test names.
     *
     * @param useNames
     *            the use names
     * @return the test names
     */
    public String[] getTestNames( String useNames )
    {
        if ( useNames == null || useNames.isEmpty() )
            return getTestNames();

        List<String> useList = new ArrayList<String>( 5 );

        String[] nameArray = useNames.split( "," );

        for ( String name : nameArray )
        {
            if ( testMap.containsKey( name ) )
                useList.add( name );
        }

        return useList.toArray( new String[0] );
    }

    public Properties getConfigProperties()
    {
        return configProperties;
    }

    public void setConfigProperties( Properties val )
    {
        configProperties = val;
    }

}
