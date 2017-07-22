package org.xframium.driver;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.TestNG;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationRegistry;
import org.xframium.application.XMLApplicationProvider;
import org.xframium.artifact.ArtifactManager;
import org.xframium.artifact.ArtifactTime;
import org.xframium.artifact.ArtifactType;
import org.xframium.container.ApplicationContainer;
import org.xframium.container.CloudContainer;
import org.xframium.container.DeviceContainer;
import org.xframium.container.DriverContainer;
import org.xframium.container.FavoriteContainer;
import org.xframium.container.ModelContainer;
import org.xframium.container.SuiteContainer;
import org.xframium.debugger.DebugManager;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.logging.ThreadedFileHandler;
import org.xframium.device.ng.AbstractSeleniumTest;
import org.xframium.device.proxy.ProxyRegistry;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.gesture.GestureManager;
import org.xframium.gesture.device.action.DeviceActionManager;
import org.xframium.gesture.device.action.spi.perfecto.PerfectoDeviceActionFactory;
import org.xframium.gesture.factory.spi.PerfectoGestureFactory;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.rest.bean.factory.BeanManager;
import org.xframium.integrations.rest.bean.factory.XMLBeanFactory;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.element.provider.XMLElementProvider;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.provider.XMLKeyWordProvider;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.page.listener.KeyWordListener;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;
import org.xframium.utility.SeleniumSessionManager;

public abstract class AbstractConfigurationReader implements ConfigurationReader
{
    protected Log log = LogFactory.getLog( ConfigurationReader.class );
    protected File configFolder;
    protected boolean dryRun = false;
    protected boolean displayResults = true;
    protected String xFID = null;
    
    
    
    public String getxFID()
    {
        return xFID;
    }

    public void setxFID( String xFID )
    {
        this.xFID = xFID;
    }

    private SuiteContainer suiteContainer;

    public SuiteContainer getSuiteContainer()
    {
        return suiteContainer;
    }

    public void setSuiteContainer( SuiteContainer suiteContainer )
    {
        this.suiteContainer = suiteContainer;
    }

    public abstract boolean readFile( InputStream inputStream );

    public abstract boolean readFile( File configFile );

    public abstract CloudContainer configureCloud( boolean secured );

    protected abstract boolean configureProxy();

    public abstract ApplicationContainer configureApplication();

    protected abstract boolean configureThirdParty();

    public abstract SuiteContainer configureTestCases( PageDataProvider pdp, boolean parseDataIterators );

    public abstract boolean configureArtifacts( DriverContainer driverContainer );

    public abstract ElementProvider configurePageManagement( SuiteContainer sC );
    
    public abstract PageDataProvider configureData();

    public abstract boolean configureContent();

    public abstract DeviceContainer configureDevice();

    protected abstract boolean configurePropertyAdapters();

    public abstract DriverContainer configureDriver( Map<String, String> customConfig );

    public abstract FavoriteContainer configureFavorites();

    protected abstract boolean _executeTest( SuiteContainer sC ) throws Exception;
    
    protected abstract Map<String,String> getConfigurationProperties();

    @Override
    public SuiteContainer readConfiguration( File configurationFile, boolean runTest )
    {
        return readConfiguration( configurationFile, runTest, null );
        
    }
    
    private String suiteName;
    public String getSuiteName()
    {
        return suiteName;
    }
    
    
    protected String getValue( String keyName, String attributeValue, Map<String,String> overrideMap )
    {
        if ( System.getProperty( keyName ) != null )
            return System.getProperty( keyName );
        if ( overrideMap.containsKey( keyName ) )
            return overrideMap.get( keyName );
        else
            return attributeValue;
    }
    
    @Override
    public SuiteContainer readConfiguration( File configFile, boolean runTest, Map<String, String> customConfig )
    {
        xFID = customConfig.get( "xF-ID" );
        
        log.info( "xF ID: " + xFID );
        
        configFolder = configFile.getParentFile();
        
        if ( !readFile( configFile ) )
            throw new IllegalArgumentException( "Could not read " + configFile );
        
        
        configureProxy();
        try
        {
            log.info( "Driver: Configuring Driver" );
            final DriverContainer driverC = configureDriver( customConfig );
            
            driverC.setPropertyAdapters( DeviceManager.instance( xFID ).getPropertyAdapters() );
            
            suiteName = driverC.getSuiteName();
            
            DeviceManager.instance( xFID ).setInitializationName( driverC.getBeforeDevice() );
            
            if ( driverC.getBeforeTest() != null  )
            {
                KeyWordDriver.instance( xFID ).addStepListener( new KeyWordListener()
                {
                    @Override
                    public boolean beforeTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
                    {
                        boolean returnValue = false;
                        try
                        {
                            eC.startStep( new SyntheticStep( driverC.getBeforeTest(), "CALL2" ), contextMap, dataMap );
                            
                            KeyWordTest kTest = KeyWordDriver.instance( xFID ).getTest( driverC.getBeforeTest() );
                            if ( kTest != null )
                            {
                                 returnValue = kTest.executeTest( webDriver, contextMap, dataMap, pageMap, sC, eC );
                                 eC.completeStep( returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, null );
                            }
                            else
                            {
                                returnValue = false;
                                eC.completeStep( StepStatus.FAILURE, new ScriptConfigurationException( "Could not locate pre-test function [" + driverC.getBeforeTest() + "]" ) );
                            }
                        }
                        catch( Exception e )
                        {
                            eC.completeStep( StepStatus.FAILURE, e );
                            return false;
                        }
                        
                        return returnValue;
                    }
                    
                    @Override
                    public boolean beforeStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
                    {
                        // TODO Auto-generated method stub
                        return true;
                    }
                    
                    @Override
                    public void afterTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
                    {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void afterStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, StepStatus stepStatus, SuiteContainer sC, ExecutionContextTest eC )
                    {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void afterArtifacts( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
                    {
                        // TODO Auto-generated method stub
                        
                    }
                });
            }
            
            if ( driverC.getAfterTest() != null  )
            {
                KeyWordDriver.instance( xFID ).addStepListener( new KeyWordListener()
                {
                    @Override
                    public boolean beforeTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
                    {
                       return true;
                    }
                    
                    @Override
                    public boolean beforeStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
                    {
                        // TODO Auto-generated method stub
                        return true;
                    }
                    
                    @Override
                    public void afterTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
                    {
                        boolean returnValue = false;
                        try
                        {
                            eC.startStep( new SyntheticStep( driverC.getAfterTest(), "CALL2" ), contextMap, dataMap );
                            
                            KeyWordTest kTest = KeyWordDriver.instance( xFID ).getTest( driverC.getAfterTest() );
                            if ( kTest != null )
                            {
                                 returnValue = kTest.executeTest( webDriver, contextMap, dataMap, pageMap, sC, eC );
                                 eC.completeStep( returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, null );
                            }
                            else
                            {
                                eC.completeStep( StepStatus.FAILURE, new ScriptConfigurationException( "Could not locate pre-test function [" + driverC.getBeforeTest() + "]" ) );
                            }
                        }
                        catch( Exception e )
                        {
                            eC.completeStep( StepStatus.FAILURE, e );
                        }
                        
                        
                    }
                    
                    @Override
                    public void afterStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, StepStatus stepStatus, SuiteContainer sC, ExecutionContextTest eC )
                    {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void afterArtifacts( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
                    {
                        // TODO Auto-generated method stub
                        
                    }
                });
            }
            
            log.info( "Cloud: Configuring Cloud Registry" );
            CloudContainer cC = configureCloud( driverC.isSecureCloud() );
            log.info( "Cloud: Extracted " + cC.getCloudList().size() + " cloud entries" );
            for ( CloudDescriptor c : cC.getCloudList() )
                CloudRegistry.instance( xFID ).addCloudDescriptor( c );
            
            CloudRegistry.instance( xFID ).setCloud( cC.getCloudName() );
            
            log.info( "Cloud: configured as " + CloudRegistry.instance( xFID ).getCloud().getName() + " at " + CloudRegistry.instance( xFID ).getCloud().getHostName() );
            
            if ( ProxyRegistry.instance().getProxyHost() != null && !ProxyRegistry.instance().getProxyHost().isEmpty() 
            		&& Integer.parseInt( ProxyRegistry.instance().getProxyPort() ) > 0 )
            {
                log.info( "Cloud: Proxy configured as " + ProxyRegistry.instance().getProxyHost() + ":" + ProxyRegistry.instance().getProxyPort() );
                System.setProperty( "http.proxyHost", ProxyRegistry.instance().getProxyHost() );
                System.setProperty( "https.proxyHost", ProxyRegistry.instance().getProxyHost() );
                System.setProperty( "http.proxyPort", ProxyRegistry.instance().getProxyPort() );
                System.setProperty( "https.proxyPort", ProxyRegistry.instance().getProxyPort() );
                
            }
            else if ( CloudRegistry.instance( xFID ).getCloud().getProxyHost() != null && !CloudRegistry.instance( xFID ).getCloud().getProxyHost().isEmpty() && Integer.parseInt( CloudRegistry.instance( xFID ).getCloud().getProxyPort() ) > 0 )
            {
            	ProxyRegistry.instance().setProxyHost(CloudRegistry.instance( xFID ).getCloud().getProxyHost());
            	ProxyRegistry.instance().setProxyPort(CloudRegistry.instance( xFID ).getCloud().getProxyPort());
            	log.info( "Cloud: Proxy configured as " + ProxyRegistry.instance().getProxyHost() + ":" + ProxyRegistry.instance().getProxyPort() );
                System.setProperty( "http.proxyHost", ProxyRegistry.instance().getProxyHost() );
                System.setProperty( "https.proxyHost", ProxyRegistry.instance().getProxyHost() );
                System.setProperty( "http.proxyPort", ProxyRegistry.instance().getProxyPort() );
                System.setProperty( "https.proxyPort", ProxyRegistry.instance().getProxyPort() );
            }
            
            if ( ProxyRegistry.instance().getIgnoreHost() != null && !ProxyRegistry.instance().getIgnoreHost().isEmpty() ) {
            	System.setProperty( "http.nonProxyHosts", ProxyRegistry.instance().getIgnoreHost() );
            	System.setProperty( "https.nonProxyHosts", ProxyRegistry.instance().getIgnoreHost() );
            }
            
            BeanManager.instance().setBeanFactory( new XMLBeanFactory() );
            PerfectoMobile.instance( xFID ).setUserName( CloudRegistry.instance( xFID ).getCloud().getUserName() );
            PerfectoMobile.instance( xFID ).setPassword( CloudRegistry.instance( xFID ).getCloud().getPassword() );
            PerfectoMobile.instance( xFID ).setBaseUrl( "https://" + CloudRegistry.instance( xFID ).getCloud().getHostName() );

            log.info( "Device: Configuring Device Acquisition Engine " );
            DeviceContainer dC = configureDevice();
            log.info( "Device: Extract " + dC.getActiveDevices().size() + " active devices and " + dC.getInactiveDevices().size() + " inactive devices" );
            
            if ( dC.getActiveDevices().isEmpty() ) 
                return null;
            
            for ( Device d : dC.getActiveDevices() )
                DeviceManager.instance( xFID ).registerDevice( d );
            
            for ( Device d : dC.getInactiveDevices() )
                DeviceManager.instance( xFID ).registerInactiveDevice( d );
            
            
            DeviceActionManager.instance().setDeviceActionFactory( new PerfectoDeviceActionFactory() );
            GestureManager.instance().setGestureFactory( new PerfectoGestureFactory() );
            
            log.info( "Application: Configuring Application Registry" );
            ApplicationContainer appContainer = configureApplication();
            if ( appContainer == null )
                return null;
            else
            {
                for ( ApplicationDescriptor aD : appContainer.getAppList() )
                    ApplicationRegistry.instance( xFID ).addApplicationDescriptor( aD );
                
                ApplicationRegistry.instance( xFID ).setAUT( appContainer.getApplicationName() );
            }
            
            //
            // Add the build in applications
            //
            XMLApplicationProvider internalApplications = new XMLApplicationProvider( "org/xframium/resource/script/applications/applicationRegistry.xml" );
            for ( ApplicationDescriptor aD : internalApplications.readData() )
                ApplicationRegistry.instance( xFID ).addApplicationDescriptor( aD );
            
            
            
            log.info( "Application: Configured as " + ApplicationRegistry.instance( xFID ).getAUT().getName() );
            
            log.info( "Third Party: Configuring Third Party Library Support" );
            if ( !configureThirdParty() ) return null;
            
            
            log.info( "Content: Configuring Content Engine" );
            if ( !configureContent() ) return null;
            
            log.info( "Property Adapter:  Configuring Property Adapters" );
            if ( !configurePropertyAdapters() ) return null;
            
            log.info( "Data: Configuring Data Driven Testing" );
            PageDataProvider pdp = configureData();
            
            if ( pdp != null )
                PageDataManager.instance( xFID ).setPageDataProvider( pdp );
            
            log.info( "Data: Configuring Test Cases" );
            SuiteContainer sC = configureTestCases( pdp, true );

            if ( sC == null ) 
                return null;
            
            sC.setxFID( xFID );
            //
            // Add the internal fucntions
            //
            XMLKeyWordProvider internalFunctions = new XMLKeyWordProvider( "org/xframium/resource/script/xfNative/functions/functions-xfNative.xml", driverC.getPropertyMap() );
            SuiteContainer sCInternal = internalFunctions.readData( true );
            sC.getFunctionList().addAll( sCInternal.getFunctionList() );
            
            
            
            log.info( "Page: Configuring Object Repository" );
            ElementProvider eP = configurePageManagement( sC );
            if ( eP == null ) return null;
            
            //
            // Add the internal opbject repository
            //
            XMLElementProvider internalObjectRepository = new XMLElementProvider( "org/xframium/resource/script/xfNative/objectRepository/site-xfNative.xml" );
            eP.addElementProvider( internalObjectRepository );
            
            
            PageManager.instance( xFID ).setSiteName( sC.getSiteName() );
            log.info( "Extracted " + sC.getTestList().size() + " test cases (" + sC.getActiveTestList().size() + " active)" );

            for ( ModelContainer mC : sC.getModel() )
                KeyWordDriver.instance( xFID ).addPage( mC.getSiteName(), mC.getPageName(), mC.getClassName() ); 
            
            KeyWordDriver.instance( xFID ).loadTests( sC );
            
            PageManager.instance( xFID ).setElementProvider( eP );
            

            log.info( "Artifact: Configuring Artifact Production" );
            if ( !configureArtifacts( driverC ) ) return null;
            
            DataManager.instance( xFID ).setReportFolder( new File( configFolder, driverC.getReportFolder() ) );
            PageManager.instance( xFID ).setStoreImages( true );
            PageManager.instance( xFID ).setImageLocation( new File( configFolder, driverC.getReportFolder() ).getAbsolutePath() );
            
            if ( driverC.isArtifactEnabled( ArtifactType.CONSOLE_LOG.name() ) )
            {
                ThreadedFileHandler threadedHandler = new ThreadedFileHandler( xFID );
                threadedHandler.configureHandler( Level.INFO );
            }
            
            if ( System.getProperty( "X_DEBUGGER" ) != null && System.getProperty( "X_DEBUGGER" ).equals( "true" ) && !driverC.isArtifactEnabled( ArtifactType.DEBUGGER.name() ) )
            {
                driverC.addArtifact( ArtifactType.DEBUGGER.name() );
            }
            
            if ( driverC.isArtifactEnabled( ArtifactType.DEBUGGER.name() ) )
            {
                String debuggerHost = System.getProperty( "X_DEBUGGER_HOST" );
                if ( debuggerHost == null )
                {
                    debuggerHost = "127.0.0.1";
                }
                
                DebugManager.instance( xFID ).startUp( debuggerHost, 8870, xFID );
                KeyWordDriver.instance( xFID ).addStepListener( DebugManager.instance( xFID ) );
            }
            

            if ( driverC.getArtifactList() != null )
            {
                for ( String artifactType : driverC.getArtifactList() )
                {
                    ArtifactManager.instance( xFID ).enableArtifact( artifactType );
                }
            }
            
            DataManager.instance( xFID ).setPersonas( driverC.getPerfectoPersonas().toArray( new String[ 0 ] ) );
            PageManager.instance( xFID ).setWindTunnelEnabled( driverC.isPerfectoWindTunnel() );
            DeviceManager.instance( xFID ).setDryRun( driverC.isDryRun() );
            
            ArtifactManager.instance( xFID ).setDisplayArtifact( driverC.getDisplayReport() );
            DeviceManager.instance( xFID ).setCachingEnabled( driverC.isSmartCaching() );
            String stepTags = driverC.getStepTags();
            if ( stepTags != null && !stepTags.isEmpty() )
                PageManager.instance( xFID ).setTagNames( stepTags );
            
            Properties props = new Properties();
            props.putAll( driverC.getPropertyMap() );
            KeyWordDriver.instance( xFID ).setConfigProperties( props );
            
            List<String> testArray = new ArrayList<String>( 10 );
            
            if ( driverC.getTestNames().size() > 0 )
            {
                Collection<KeyWordTest> testList = KeyWordDriver.instance( xFID ).getNamedTests( driverC.getTestNames().toArray( new String[ 0 ] ) );
                
                if ( testList.isEmpty() )
                {
                    System.err.println( "No tests contained the names(s) [" + driverC.getTestNames() + "]" );
                }
                
                testArray.addAll( driverC.getTestNames() );
            }
            
            //
            // Extract any tagged tests
            //
            String tagNames = driverC.getTestTags();
            if ( tagNames != null && !tagNames.isEmpty() )
            {
                DeviceManager.instance( xFID ).setTagNames( tagNames.split( "," ) );
                ExecutionContext.instance( xFID ).setTestTags( tagNames.split( "," ) );
                Collection<KeyWordTest> testList = KeyWordDriver.instance( xFID ).getTaggedTests( tagNames.split( "," ) );

                if ( testList.isEmpty() )
                {
                    System.err.println( "No tests contained the tag(s) [" + tagNames + "]" );
                }

                for ( KeyWordTest t : testList )
                    testArray.add( t.getName() );
            }
            
            if ( testArray.size() == 0 )
                DataManager.instance( xFID ).setTests( KeyWordDriver.instance( xFID ).getTestNames() );
            else
                DataManager.instance( xFID ).setTests( testArray.toArray( new String[0] ) );
            
            
            if ( runTest && driverC.isEmbeddedServer() )
                CloudRegistry.instance( xFID ).startEmbeddedCloud();
            
            ExecutionContext.instance( xFID ).setSuiteName( (driverC.getSuiteName() != null && !driverC.getSuiteName().isEmpty()) ? driverC.getSuiteName() : ApplicationRegistry.instance( xFID ).getAUT().getName() );
            ExecutionContext.instance( xFID ).setPhase( driverC.getPhase() );
            ExecutionContext.instance( xFID ).setDomain( driverC.getDomain() );
            ExecutionContext.instance( xFID ).setConfigProperties( getConfigurationProperties() );
            
            if ( runTest )
                executeTest( sC );
            
            suiteContainer = sC;
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        
        return suiteContainer;
    }

    public void afterSuite()
    {
        ExecutionContext.instance( xFID ).setEndTime( new Date( System.currentTimeMillis()) );
        List<String> aList = ArtifactManager.instance( xFID ).getEnabledArtifacts( ArtifactTime.BEFORE_SUITE_ARTIFACTS );
        if ( aList != null )
        {
            for ( String artifactType : aList )
            {
                ArtifactManager.instance( xFID ).generateArtifact( artifactType, ExecutionContext.instance( xFID ).getReportFolder( xFID ).getAbsolutePath(), null, xFID );
            }
        }
        
        aList = ArtifactManager.instance( xFID ).getEnabledArtifacts( ArtifactTime.AFTER_SUITE );
        
        if ( aList != null )
        {
            for ( String artifactType : aList )
            {
                ArtifactManager.instance( xFID ).generateArtifact( artifactType, ExecutionContext.instance( xFID ).getReportFolder( xFID ).getAbsolutePath(), null, xFID );
            }
        }
        
        
        
        aList = ArtifactManager.instance( xFID ).getEnabledArtifacts( ArtifactTime.AFTER_SUITE_ARTIFACTS );
        
        if ( aList != null )
        {
            for ( String artifactType : aList )
            {
                ArtifactManager.instance( xFID ).generateArtifact( artifactType, ExecutionContext.instance( xFID ).getReportFolder( xFID ).getParent(), null, xFID );
            }
        }
    }
    
    public boolean executeTest( SuiteContainer sC )
    {
        log.info( "Go: Executing Tests: " + xFID );
        ExecutionContext.instance( xFID ).isEnabled();
        try
        {
            if ( ArtifactManager.instance( xFID ).isArtifactEnabled( ArtifactType.DEBUGGER.name() ) )
            {
                String debuggerHost = System.getProperty( "X_DEBUGGER_HOST" );
                if ( debuggerHost == null )
                {
                    debuggerHost = "127.0.0.1";
                }
                DebugManager.instance( xFID ).launchBrowser( debuggerHost, 8870 );
            }

            _executeTest( sC == null ? suiteContainer : sC );

            afterSuite();

            
            if ( ArtifactManager.instance( xFID ).isArtifactEnabled( ArtifactType.DEBUGGER.name() ) )
                DebugManager.instance(xFID).shutDown();

        }
        catch ( Exception e )
        {
            log.fatal( "Error executing Tests", e );
        }

        return true;
    }

    
    
    protected File findFile( File rootFolder, File useFile )
    {
        if ( useFile.exists() || useFile.isAbsolute() )
            return useFile;

        File myFile = new File( rootFolder, useFile.getPath() );
        if ( myFile.exists() )
            return myFile;

        throw new IllegalArgumentException( "Could not find " + useFile.getName() + " at " + useFile.getPath() + " or " + myFile.getAbsolutePath() );

    }

    protected void runTest( String outputFolder, Class theTest, SuiteContainer sC )
    {
        int threadCount = Integer.parseInt( System.getProperty( "xF-ThreadCount", "10" ) );
        int verboseLevel = Integer.parseInt( System.getProperty( "xF-VerboseLevel", "10" ) );

        
        TestNG testNg = new TestNG( true );
        testNg.setVerbose( verboseLevel );
        testNg.setThreadCount( threadCount );
        testNg.setDataProviderThreadCount( threadCount );
        testNg.setOutputDirectory( outputFolder + System.getProperty( "file.separator" ) + "testNg" );
        testNg.setTestClasses( new Class[] { theTest } );
        testNg.run();

    }
}
