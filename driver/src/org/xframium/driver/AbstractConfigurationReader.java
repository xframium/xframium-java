package org.xframium.driver;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.TestNG;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationRegistry;
import org.xframium.artifact.ArtifactType;
import org.xframium.debugger.DebugManager;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.logging.ThreadedFileHandler;
import org.xframium.device.ng.AbstractSeleniumTest;
import org.xframium.device.proxy.ProxyRegistry;
import org.xframium.driver.container.ApplicationContainer;
import org.xframium.driver.container.CloudContainer;
import org.xframium.driver.container.DeviceContainer;
import org.xframium.driver.container.DriverContainer;
import org.xframium.gesture.GestureManager;
import org.xframium.gesture.device.action.DeviceActionManager;
import org.xframium.gesture.device.action.spi.perfecto.PerfectoDeviceActionFactory;
import org.xframium.gesture.factory.spi.PerfectoGestureFactory;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.rest.bean.factory.BeanManager;
import org.xframium.integrations.rest.bean.factory.XMLBeanFactory;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordPage;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.provider.SuiteContainer;
import org.xframium.spi.Device;
import org.xframium.spi.RunDetails;
import org.xframium.utility.SeleniumSessionManager;

public abstract class AbstractConfigurationReader implements ConfigurationReader
{
    protected Log log = LogFactory.getLog(ConfigurationReader.class);
    protected File configFolder;
    protected boolean dryRun = false;
    protected boolean displayResults = true;
    
    public abstract boolean readFile( InputStream inputStream );
    public abstract boolean readFile( File configFile );
    public abstract CloudContainer configureCloud( boolean secured );
    protected abstract boolean configureProxy();
    public abstract ApplicationContainer configureApplication();
    protected abstract boolean configureThirdParty();
    public abstract SuiteContainer configureTestCases( PageDataProvider pdp, boolean parseDataIterators);
    public abstract boolean configureArtifacts( DriverContainer driverContainer );
    public abstract ElementProvider configurePageManagement( SuiteContainer sC );
    public abstract PageDataProvider configureData();
    protected abstract boolean configureContent();
    public abstract DeviceContainer configureDevice();
    protected abstract boolean configurePropertyAdapters();
    public abstract DriverContainer configureDriver();
    protected abstract boolean _executeTest() throws Exception;
    
    @Override
    public void readConfiguration( File configFile, boolean runTest )
    {
        configFolder = configFile.getParentFile();
        
        if ( !readFile( configFile ) )
            throw new IllegalArgumentException( "Could not read " + configFile );
        
        
        configureProxy();
        try
        {
            log.info( "Driver: Configuring Driver" );
            DriverContainer driverC = configureDriver();
            
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
            
            DeviceManager.instance().addRunListener( RunDetails.instance() );
            
            log.info( "Property Adapter:  Configuring Property Adapters" );
            if ( !configurePropertyAdapters() ) return;
            
            log.info( "Data: Configuring Data Driven Testing" );
            PageDataProvider pdp = configureData();
            
            PageDataManager.instance().setPageDataProvider( pdp );
            
            log.info( "Data: Configuring Test Cases" );
            SuiteContainer sC = configureTestCases( pdp, true );
            
            if ( sC == null ) 
                return;
            
            log.info( "Extracted " + sC.getTestList().size() + " test cases (" + sC.getActiveTestList().size() + " active)" );
            for ( String modelName : sC.getModelMap().keySet() )
                KeyWordDriver.instance().addPage( modelName, sC.getModelMap().get( modelName ) ); 
            
            KeyWordDriver.instance().loadTests( sC );
            
            
            log.info( "Page: Configuring Object Repository" );
            ElementProvider eP = configurePageManagement( sC );
            if ( eP == null ) return;

            //
            // In XML configuration, the test suite doesn't have a model element, so the calls to
            // KeyWordDriver.instance().addPage() can't have done anything.  So, we'll loop through
            // the pages from configurePageManagement() and add them here.
            //
            
            log.info( "Extracted " + eP.getElementTree().size() + " pages" );
            Iterator<String> pages = eP.getElementTree().keySet().iterator();
            boolean needLoad = KeyWordDriver.instance().getPageCount() == 0;
            while(( pages.hasNext() ) &&
                  ( needLoad ))
            {
                String page = pages.next();
                
                KeyWordDriver.instance().addPage( page, KeyWordPage.class );
            }
            
            PageManager.instance().setSiteName( sC.getSiteName() );
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
                DebugManager.instance().startUp( InetAddress.getLocalHost().getHostAddress(), 8870 );
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
            
            RunDetails.instance().setTestName( (driverC.getSuiteName() != null && !driverC.getSuiteName().isEmpty()) ? driverC.getSuiteName() : ApplicationRegistry.instance().getAUT().getName() );
            
            if ( runTest )
                executeTest();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public boolean executeTest()
    {
        log.info( "Go: Executing Tests" );
        
        try
        {
            if( DataManager.instance().isArtifactEnabled( ArtifactType.DEBUGGER ) )
                DebugManager.instance().launchBrowser( InetAddress.getLocalHost().getHostAddress(), 8870 );
            
            _executeTest();
            
            if( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_RECORD_HTML ) )
            {
            	RunDetails.instance().writeHTMLIndex( DataManager.instance().getReportFolder(), true );
            	
                File htmlFile = RunDetails.instance().getIndex( DataManager.instance().getReportFolder() );
                try
                {
                    Desktop.getDesktop().browse( htmlFile.toURI() );
                }
                catch( Exception e )
                {
                    e.printStackTrace();
                }
            }
            
            if( DataManager.instance().isArtifactEnabled( ArtifactType.DEBUGGER ) )
                DebugManager.instance().shutDown();

        }
        catch( Exception e )
        {
            log.fatal( "Error executing Tests", e );
        }
        finally
        {
            CloudRegistry.instance().shutdown();
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
    
    protected void runTest( String outputFolder, Class theTest )
    {
        RunDetails.instance().setStartTime();
        TestNG testNg = new TestNG( true );
        testNg.setVerbose( 10 );
        testNg.setOutputDirectory( outputFolder + System.getProperty( "file.separator" ) + "testNg" );
        testNg.setTestClasses( new Class[] { theTest } );
        testNg.run();

    }
}
