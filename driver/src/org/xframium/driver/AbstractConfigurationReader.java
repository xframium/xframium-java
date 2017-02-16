package org.xframium.driver;

import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import org.xframium.device.artifact.Artifact;
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
import org.xframium.history.HistoryWriter;
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
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.page.listener.KeyWordListener;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;
import org.xframium.utility.SeleniumSessionManager;
import com.xframium.serialization.SerializationManager;

public abstract class AbstractConfigurationReader implements ConfigurationReader
{
    protected Log log = LogFactory.getLog( ConfigurationReader.class );
    protected File configFolder;
    protected boolean dryRun = false;
    protected boolean displayResults = true;
    
    private SuiteContainer suiteContainer;

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

    @Override
    public void readConfiguration( File configurationFile, boolean runTest )
    {
        readConfiguration( configurationFile, runTest, null );
        
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
    public void readConfiguration( File configFile, boolean runTest, Map<String, String> customConfig )
    {
        configFolder = configFile.getParentFile();
        
        if ( !readFile( configFile ) )
            throw new IllegalArgumentException( "Could not read " + configFile );
        
        
        configureProxy();
        try
        {
            log.info( "Driver: Configuring Driver" );
            final DriverContainer driverC = configureDriver( customConfig );
            suiteName = driverC.getSuiteName();
            
            DeviceManager.instance().setInitializationName( driverC.getBeforeDevice() );
            
            if ( driverC.getBeforeTest() != null  )
            {
                KeyWordDriver.instance().addStepListener( new KeyWordListener()
                {
                    @Override
                    public boolean beforeTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
                    {
                        boolean returnValue = false;
                        try
                        {
                            eC.startStep( new SyntheticStep( "Test Initialization", "CALL2" ), contextMap, dataMap );
                            
                            KeyWordTest kTest = KeyWordDriver.instance().getTest( driverC.getBeforeTest() );
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
                });
            }
            
            if ( driverC.getAfterTest() != null  )
            {
                KeyWordDriver.instance().addStepListener( new KeyWordListener()
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
                            eC.startStep( new SyntheticStep( "Test Initialization", "CALL2" ), contextMap, dataMap );
                            
                            KeyWordTest kTest = KeyWordDriver.instance().getTest( driverC.getBeforeTest() );
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
                });
            }
            
            log.info( "Cloud: Configuring Cloud Registry" );
            CloudContainer cC = configureCloud( driverC.isSecureCloud() );
            log.info( "Cloud: Extracted " + cC.getCloudList().size() + " cloud entries" );
            for ( CloudDescriptor c : cC.getCloudList() )
                CloudRegistry.instance().addCloudDescriptor( c );
            
            CloudRegistry.instance().setCloud( cC.getCloudName() );
            
            log.info( "Cloud: configured as " + CloudRegistry.instance().getCloud().getName() + " at " + CloudRegistry.instance().getCloud().getHostName() );
            
            if ( ProxyRegistry.instance().getProxyHost() != null && !ProxyRegistry.instance().getProxyHost().isEmpty() 
            		&& Integer.parseInt( ProxyRegistry.instance().getProxyPort() ) > 0 )
            {
                log.info( "Cloud: Proxy configured as " + ProxyRegistry.instance().getProxyHost() + ":" + ProxyRegistry.instance().getProxyPort() );
                System.setProperty( "http.proxyHost", ProxyRegistry.instance().getProxyHost() );
                System.setProperty( "https.proxyHost", ProxyRegistry.instance().getProxyHost() );
                System.setProperty( "http.proxyPort", ProxyRegistry.instance().getProxyPort() );
                System.setProperty( "https.proxyPort", ProxyRegistry.instance().getProxyPort() );
                
            }
            else if ( CloudRegistry.instance().getCloud().getProxyHost() != null && !CloudRegistry.instance().getCloud().getProxyHost().isEmpty() && Integer.parseInt( CloudRegistry.instance().getCloud().getProxyPort() ) > 0 )
            {
            	ProxyRegistry.instance().setProxyHost(CloudRegistry.instance().getCloud().getProxyHost());
            	ProxyRegistry.instance().setProxyPort(CloudRegistry.instance().getCloud().getProxyPort());
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
            PerfectoMobile.instance().setUserName( CloudRegistry.instance().getCloud().getUserName() );
            PerfectoMobile.instance().setPassword( CloudRegistry.instance().getCloud().getPassword() );
            PerfectoMobile.instance().setBaseUrl( "https://" + CloudRegistry.instance().getCloud().getHostName() );

            log.info( "Device: Configuring Device Acquisition Engine " );
            DeviceContainer dC = configureDevice();
            log.info( "Device: Extract " + dC.getActiveDevices().size() + " active devices and " + dC.getInactiveDevices().size() + " inactive devices" );
            
            if ( dC.getActiveDevices().isEmpty() ) 
                return;
            
            for ( Device d : dC.getActiveDevices() )
                DeviceManager.instance().registerDevice( d );
            
            for ( Device d : dC.getInactiveDevices() )
                DeviceManager.instance().registerInactiveDevice( d );
            
            
            DeviceActionManager.instance().setDeviceActionFactory( new PerfectoDeviceActionFactory() );
            GestureManager.instance().setGestureFactory( new PerfectoGestureFactory() );
            
            log.info( "Application: Configuring Application Registry" );
            ApplicationContainer appContainer = configureApplication();
            if ( appContainer == null )
                return;
            else
            {
                for ( ApplicationDescriptor aD : appContainer.getAppList() )
                    ApplicationRegistry.instance().addApplicationDescriptor( aD );
                
                ApplicationRegistry.instance().setAUT( appContainer.getApplicationName() );
            }
            
            
            
            log.info( "Application: Configured as " + ApplicationRegistry.instance().getAUT().getName() );
            
            log.info( "Third Party: Configuring Third Party Library Support" );
            if ( !configureThirdParty() ) return;
            
            
            log.info( "Content: Configuring Content Engine" );
            if ( !configureContent() ) return;
            
            log.info( "Property Adapter:  Configuring Property Adapters" );
            if ( !configurePropertyAdapters() ) return;
            
            log.info( "Data: Configuring Data Driven Testing" );
            PageDataProvider pdp = configureData();
            
            PageDataManager.instance().setPageDataProvider( pdp );
            
            log.info( "Data: Configuring Test Cases" );
            SuiteContainer sC = configureTestCases( pdp, true );
            
            if ( sC == null ) 
                return;
            
            log.info( "Page: Configuring Object Repository" );
            ElementProvider eP = configurePageManagement( sC );
            if ( eP == null ) return;
            
            PageManager.instance().setSiteName( sC.getSiteName() );
            log.info( "Extracted " + sC.getTestList().size() + " test cases (" + sC.getActiveTestList().size() + " active)" );

            for ( ModelContainer mC : sC.getModel() )
                KeyWordDriver.instance().addPage( mC.getSiteName(), mC.getPageName(), mC.getClassName() ); 
            
            KeyWordDriver.instance().loadTests( sC );
            
            PageManager.instance().setElementProvider( eP );
            

            log.info( "Artifact: Configuring Artifact Production" );
            if ( !configureArtifacts( driverC ) ) return;
            
            DataManager.instance().setReportFolder( new File( configFolder, driverC.getReportFolder() ) );
            PageManager.instance().setStoreImages( true );
            PageManager.instance().setImageLocation( new File( configFolder, driverC.getReportFolder() ).getAbsolutePath() );
            
            if ( driverC.isArtifactEnabled( ArtifactType.CONSOLE_LOG ) )
            {
                ThreadedFileHandler threadedHandler = new ThreadedFileHandler();
                threadedHandler.configureHandler( Level.INFO );
            }
            
            if ( System.getProperty( "X_DEBUGGER" ) != null && System.getProperty( "X_DEBUGGER" ).equals( "true" ) && !driverC.isArtifactEnabled( ArtifactType.DEBUGGER ) )
            {
                driverC.addArtifact( ArtifactType.DEBUGGER );
            }
            
            if ( driverC.isArtifactEnabled( ArtifactType.DEBUGGER ) )
            {
                String debuggerHost = System.getProperty( "X_DEBUGGER_HOST" );
                if ( debuggerHost == null )
                {
                    debuggerHost = "127.0.0.1";
                }
                
                DebugManager.instance().startUp( debuggerHost, 8870 );
                KeyWordDriver.instance().addStepListener( DebugManager.instance() );
            }
            
            DataManager.instance().setAutomaticDownloads( driverC.getArtifactList().toArray( new ArtifactType[0] ) );
            
            DataManager.instance().setPersonas( driverC.getPerfectoPersonas().toArray( new String[ 0 ] ) );
            PageManager.instance().setWindTunnelEnabled( driverC.isPerfectoWindTunnel() );
            DeviceManager.instance().setDryRun( driverC.isDryRun() );
            
            displayResults = driverC.isDisplayReport();
            DeviceManager.instance().setCachingEnabled( driverC.isSmartCaching() );
            String stepTags = driverC.getStepTags();
            if ( stepTags != null && !stepTags.isEmpty() )
                PageManager.instance().setTagNames( stepTags );
            
            Properties props = new Properties();
            props.putAll( driverC.getPropertyMap() );
            KeyWordDriver.instance().setConfigProperties( props );
            
            List<String> testArray = new ArrayList<String>( 10 );
            
            if ( driverC.getTestNames().size() > 0 )
            {
                Collection<KeyWordTest> testList = KeyWordDriver.instance().getNamedTests( driverC.getTestNames().toArray( new String[ 0 ] ) );
                
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
                DeviceManager.instance().setTagNames( tagNames.split( "," ) );
                Collection<KeyWordTest> testList = KeyWordDriver.instance().getTaggedTests( tagNames.split( "," ) );

                if ( testList.isEmpty() )
                {
                    System.err.println( "No tests contained the tag(s) [" + tagNames + "]" );
                }

                for ( KeyWordTest t : testList )
                    testArray.add( t.getName() );
            }
            
            if ( testArray.size() == 0 )
                DataManager.instance().setTests( KeyWordDriver.instance().getTestNames() );
            else
                DataManager.instance().setTests( testArray.toArray( new String[0] ) );
            
            //
            // add in support for multiple devices
            //

            PageManager.instance().setAlternateWebDriverSource( new SeleniumSessionManager()
            {
                public WebDriver getAltWebDriver( String name )
                {
                    WebDriver rtn = null;

                    ConnectedDevice device = AbstractSeleniumTest.getConnectedDevice( name );

                    if ( device != null )
                    {
                        rtn = device.getWebDriver();
                    }

                    return rtn;
                }

                public void registerAltWebDriver( String name, String deviceId )
                {
                    AbstractSeleniumTest.registerSecondaryDeviceOnName( name, deviceId );
                }
                
                public void registerInactiveWebDriver(String name) 
                {
                    AbstractSeleniumTest.registerInactiveDeviceOnName( name );
                }

            } );
            
            if ( driverC.isEmbeddedServer() )
                CloudRegistry.instance().startEmbeddedCloud();
            
            ExecutionContext.instance().setSuiteName( (driverC.getSuiteName() != null && !driverC.getSuiteName().isEmpty()) ? driverC.getSuiteName() : ApplicationRegistry.instance().getAUT().getName() );
            
            if ( runTest )
                executeTest( sC );
            
            suiteContainer = sC;
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    public boolean executeTest( SuiteContainer sC )
    {
        log.info( "Go: Executing Tests" );
        ExecutionContext.instance().isEnabled();
        try
        {
            if ( DataManager.instance().isArtifactEnabled( ArtifactType.DEBUGGER ) )
            {
                String debuggerHost = System.getProperty( "X_DEBUGGER_HOST" );
                if ( debuggerHost == null )
                {
                    debuggerHost = "127.0.0.1";
                }
                DebugManager.instance().launchBrowser( debuggerHost, 8870 );
            }

            _executeTest( sC == null ? suiteContainer : sC );

            if ( DataManager.instance().isArtifactEnabled( ArtifactType.GRID_REPORT ) )
            {
                generateGridReport( ExecutionContext.instance().getReportFolder() );
            }
            
            if ( ExecutionContext.instance().isEnabled() )
            {
                ExecutionContext.instance().setEndTime( new Date( System.currentTimeMillis() ) );
                ExecutionContext.instance().popupateSystemProperties();
                
                if ( DataManager.instance().isArtifactEnabled( ArtifactType.GRID_REPORT ) )
                {
                    ExecutionContext.instance().setGridUrl( "grid.html" );
                }

                String outputData = "var testData = " + new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), ExecutionContext.instance(), 0 ) ) + ";";
                Artifact jsArtifact = new Artifact( "Suite.js", outputData.getBytes() );
                jsArtifact.writeToDisk( ExecutionContext.instance().getReportFolder() );

                new HistoryWriter( DataManager.instance().getReportFolder() ).updateHistory();

                StringBuilder stringBuffer = new StringBuilder();
                InputStream inputStream = null;
                try
                {
                    if ( System.getProperty( "reportTemplateFolder" ) == null )
                    {
                        if ( System.getProperty( "reportTemplate" ) == null )
                            inputStream = this.getClass().getClassLoader().getResourceAsStream( "org/xframium/reporting/html/dark/Suite.html" );
                        else
                            inputStream = this.getClass().getClassLoader().getResourceAsStream( "org/xframium/reporting/html/" + System.getProperty( "reportTemplate" ) + "/Suite.html" );
                    }
                    else
                        inputStream = new FileInputStream( new File( System.getProperty( "reportTemplateFolder" ), "Suite.html" ) );

                    int bytesRead = 0;
                    byte[] buffer = new byte[512];

                    while ( (bytesRead = inputStream.read( buffer )) > 0 )
                    {
                        stringBuffer.append( new String( buffer, 0, bytesRead ) );
                    }

                    Artifact indexArtifact = new Artifact( "index.html", stringBuffer.toString().getBytes() );
                    indexArtifact.writeToDisk( ExecutionContext.instance().getReportFolder() );

                    File htmlFile = new File( ExecutionContext.instance().getReportFolder(), "index.html" );

                    try
                    {
                        if ( htmlFile.exists() )
                            Desktop.getDesktop().browse( htmlFile.toURI() );
                    }
                    catch ( Exception e )
                    {
                        e.printStackTrace();
                    }
                }
                catch ( Exception e )
                {
                    log.error( "Error generating INDEX", e );

                }
                finally
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch ( Exception e )
                    {
                    }
                }
            }

            
            if ( DataManager.instance().isArtifactEnabled( ArtifactType.DEBUGGER ) )
                DebugManager.instance().shutDown();

        }
        catch ( Exception e )
        {
            log.fatal( "Error executing Tests", e );
        }

        return true;
    }

    public boolean generateGridReport( File rootFolder )
    {
        File artifactFolder = new File( rootFolder, "artifacts" );
        
        File[] fileList = artifactFolder.listFiles( new GRIDFileFilter() );
        
        if ( fileList == null )
            return true;
        
        Map<String,List<String>> fileMap = new HashMap<String,List<String>>( 20 );
        
        for ( File file : fileList )
        {
            String[] fileParts = file.getName().split( "-" );
            List<String> gridList = fileMap.get( fileParts[ 1 ] );
            if ( gridList == null )
            {
                gridList = new ArrayList<String>( 10 );
                fileMap.put( fileParts[ 1 ], gridList );
            }
            
            gridList.add( file.getName() );
        }
        
        String outputData = "var testData = " + new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), fileMap, 0 ) ) + ";";
        
        Artifact jsArtifact = new Artifact( "Grid.js", outputData.getBytes() );
        jsArtifact.writeToDisk( ExecutionContext.instance().getReportFolder() );
        
        StringBuilder stringBuffer = new StringBuilder();
        InputStream inputStream = null;
        try
        {
            if ( System.getProperty( "reportTemplateFolder" ) == null )
            {
                if ( System.getProperty( "reportTemplate" ) == null )
                    inputStream = getClass().getClassLoader().getResourceAsStream( "org/xframium/reporting/html/dark/Grid.html" );
                else
                    inputStream = getClass().getClassLoader().getResourceAsStream( "org/xframium/reporting/html/" + System.getProperty( "reportTemplate" ) + "/Grid.html" );
            }
            else
                inputStream = new FileInputStream( new File( System.getProperty( "reportTemplateFolder" ), "Grid.html" ) );

            int bytesRead = 0;
            byte[] buffer = new byte[512];

            while ( (bytesRead = inputStream.read( buffer )) > 0 )
            {
                stringBuffer.append( new String( buffer, 0, bytesRead ) );
            }

            Artifact indexArtifact = new Artifact( "grid.html", stringBuffer.toString().getBytes() );
            indexArtifact.writeToDisk( ExecutionContext.instance().getReportFolder() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch ( Exception e )
            {
            }
        }
        
        return true;
    }

    private static class GRIDFileFilter implements FileFilter
    {

        @Override
        public boolean accept( File pathname )
        {
            return pathname.getName().startsWith( "grid" );
        }
        
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
        int verboseLevel = Integer.parseInt( System.getProperty( "xF-VerboseLevel", "1" ) );

        TestNG testNg = new TestNG( true );
        testNg.setVerbose( verboseLevel );
        testNg.setThreadCount( threadCount );
        testNg.setDataProviderThreadCount( Integer.parseInt( System.getProperty( "xF-ThreadCount", "10" ) ) );
        testNg.setOutputDirectory( outputFolder + System.getProperty( "file.separator" ) + "testNg" );
        testNg.setTestClasses( new Class[] { theTest } );
        testNg.run();

    }
}
