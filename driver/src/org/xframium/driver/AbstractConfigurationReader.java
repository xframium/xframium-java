package org.xframium.driver;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.TestNG;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.gesture.GestureManager;
import org.xframium.gesture.device.action.DeviceActionManager;
import org.xframium.gesture.device.action.spi.perfecto.PerfectoDeviceActionFactory;
import org.xframium.gesture.factory.spi.PerfectoGestureFactory;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.rest.bean.factory.BeanManager;
import org.xframium.integrations.rest.bean.factory.XMLBeanFactory;
import org.xframium.spi.RunDetails;

public abstract class AbstractConfigurationReader implements ConfigurationReader
{
    protected Log log = LogFactory.getLog(ConfigurationReader.class);
    protected File configFolder;
    protected boolean dryRun = false;
    
    protected abstract boolean readFile( File configFile );
    protected abstract boolean configureCloud();
    protected abstract boolean configureApplication();
    protected abstract boolean configureThirdParty();
    protected abstract boolean configureArtifacts();
    protected abstract boolean configurePageManagement();
    protected abstract boolean configureData();
    protected abstract boolean configureContent();
    protected abstract boolean configureDevice();
    protected abstract boolean configurePropertyAdapters();
    protected abstract boolean configureDriver();
    protected abstract boolean executeTest() throws Exception;
    
    @Override
    public void readConfiguration( File configFile )
    {
        configFolder = configFile.getParentFile();
        
        if ( !readFile( configFile ) )
            throw new IllegalArgumentException( "Could not read " + configFile );
        
        DeviceActionManager.instance().setDeviceActionFactory( new PerfectoDeviceActionFactory() );
        GestureManager.instance().setGestureFactory( new PerfectoGestureFactory() );
        
        log.info( "Cloud: Configuring Cloud Registry" );
        configureCloud();
        log.info( "Cloud: configured as " + CloudRegistry.instance().getCloud().getName() + " at " + CloudRegistry.instance().getCloud().getHostName() );
        
        if ( CloudRegistry.instance().getCloud().getProxyHost() != null && !CloudRegistry.instance().getCloud().getProxyHost().isEmpty() && Integer.parseInt( CloudRegistry.instance().getCloud().getProxyPort() ) > 0 )
        {
            log.info( "Cloud: Proxy configured as " + CloudRegistry.instance().getCloud().getProxyHost() + ":" + CloudRegistry.instance().getCloud().getProxyPort() );
            System.setProperty( "http.proxyHost", CloudRegistry.instance().getCloud().getProxyHost() );
            System.setProperty( "https.proxyHost", CloudRegistry.instance().getCloud().getProxyHost() );
            System.setProperty( "http.proxyPort", CloudRegistry.instance().getCloud().getProxyPort() );
            System.setProperty( "https.proxyPort", CloudRegistry.instance().getCloud().getProxyPort() );
        }
        
        BeanManager.instance().setBeanFactory( new XMLBeanFactory() );
        PerfectoMobile.instance().setUserName( CloudRegistry.instance().getCloud().getUserName() );
        PerfectoMobile.instance().setPassword( CloudRegistry.instance().getCloud().getPassword() );
        PerfectoMobile.instance().setBaseUrl( "https://" + CloudRegistry.instance().getCloud().getHostName() );
        
        log.info( "Application: Configuring Application Registry" );
        configureApplication();
        log.info( "Application: Configured as " + ApplicationRegistry.instance().getAUT().getName() );
        
        log.info( "Third Party: Configuring Third Party Library Support" );
        configureThirdParty();
        
        log.info( "Artifact: Configuring Artifact Production" );
        configureArtifacts();
        
        log.info( "Page: Configuring Object Repository" );
        configurePageManagement();
        
        log.info( "Data: Configuring Data Driven Testing" );
        configureData();
        
        log.info( "Content: Configuring Content Engine" );
        configureContent();
        
        log.info( "Device: Configuring Device Acquisition Engine " );
        configureDevice();
        
        log.info( "Property Adapter:  Configuring Property Adapters" );
        configurePropertyAdapters();
        
        log.info( "Driver: Configuring Driver" );
        configureDriver();
        
        log.info( "Go: Executing Tests" );
        
        try
        {
            executeTest();
        }
        catch( Exception e )
        {
            log.fatal( "Error executing Tests", e );
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
    
    protected void runTest( String outputFolder, Class theTest )
    {
        RunDetails.instance().setStartTime();
        
        TestNG testNg = new TestNG( true );
        testNg.setOutputDirectory( outputFolder + System.getProperty( "file.separator" ) + "testNg" );
        testNg.setTestClasses( new Class[] { theTest } );
        testNg.run();
    }
}
