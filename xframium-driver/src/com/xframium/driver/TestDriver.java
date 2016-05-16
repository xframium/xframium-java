/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package com.xframium.driver;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.openqa.selenium.WebDriver;
import org.testng.TestNG;
import com.xframium.Initializable;
import com.xframium.application.ApplicationRegistry;
import com.xframium.application.CSVApplicationProvider;
import com.xframium.application.ExcelApplicationProvider;
import com.xframium.application.XMLApplicationProvider;
import com.xframium.artifact.ArtifactType;
import com.xframium.content.ContentManager;
import com.xframium.content.provider.ExcelContentProvider;
import com.xframium.content.provider.SQLContentProvider;
import com.xframium.content.provider.XMLContentProvider;
import com.xframium.device.ConnectedDevice;
import com.xframium.device.DeviceManager;
import com.xframium.device.cloud.CSVCloudProvider;
import com.xframium.device.cloud.CloudRegistry;
import com.xframium.device.cloud.ExcelCloudProvider;
import com.xframium.device.cloud.SQLCloudProvider;
import com.xframium.device.cloud.XMLCloudProvider;
import com.xframium.device.data.CSVDataProvider;
import com.xframium.device.data.DataManager;
import com.xframium.device.data.DataProvider.DriverType;
import com.xframium.device.data.ExcelDataProvider;
import com.xframium.device.data.NamedDataProvider;
import com.xframium.device.data.SQLDataProvider;
import com.xframium.device.data.XMLDataProvider;
import com.xframium.device.data.perfectoMobile.AvailableHandsetValidator;
import com.xframium.device.data.perfectoMobile.PerfectoMobileDataProvider;
import com.xframium.device.data.perfectoMobile.ReservedHandsetValidator;
import com.xframium.device.logging.ThreadedFileHandler;
import com.xframium.device.ng.AbstractSeleniumTest;
import com.xframium.device.property.PropertyAdapter;
import com.xframium.gesture.GestureManager;
import com.xframium.gesture.device.action.DeviceActionManager;
import com.xframium.gesture.device.action.spi.perfecto.PerfectoDeviceActionFactory;
import com.xframium.gesture.factory.spi.PerfectoGestureFactory;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import com.xframium.integrations.rest.bean.factory.BeanManager;
import com.xframium.integrations.rest.bean.factory.XMLBeanFactory;
import com.xframium.page.PageManager;
import com.xframium.page.data.PageDataManager;
import com.xframium.page.data.provider.ExcelPageDataProvider;
import com.xframium.page.data.provider.SQLPageDataProvider;
import com.xframium.page.data.provider.XMLPageDataProvider;
import com.xframium.page.element.provider.CSVElementProvider;
import com.xframium.page.element.provider.ExcelElementProvider;
import com.xframium.page.element.provider.SQLElementProvider;
import com.xframium.page.element.provider.XMLElementProvider;
import com.xframium.page.keyWord.KeyWordDriver;
import com.xframium.page.keyWord.KeyWordTest;
import com.xframium.page.keyWord.provider.XMLKeyWordProvider;
import com.xframium.spi.CSVRunListener;
import com.xframium.spi.RunDetails;
import com.xframium.utility.SeleniumSessionManager;


public class TestDriver
{
    private static final String[] CLOUD = new String[] { "cloudRegistry.provider", "cloudRegistry.fileName", "cloudRegistry.cloudUnderTest",
                                                         "cloudRegistry.query"  };
    private static final String[] APP = new String[] { "applicationRegistry.provider", "applicationRegistry.fileName", "applicationRegistry.applicationUnderTest" };
    private static final String[] ARTIFACT = new String[] { "artifactProducer.parentFolder" };
    private static final String[] PAGE = new String[] { "pageManagement.siteName", "pageManagement.provider", "pageManagement.fileName",
                                                        "pageManagement.query" };
    private static final String[] DATA = new String[] { "pageManagement.pageData.provider", "pageManagement.pageData.fileName",
                                                        "pageManagement.pageData.query" };
    private static final String[] CONTENT = new String[] { "pageManagement.content.provider", "pageManagement.content.fileName",
                                                           "pageManagement.content.query" };
    private static final String[] DEVICE = new String[] { "deviceManagement.provider", "deviceManagement.driverType",
                                                          "deviceManagement.device.query", "deviceManagement.capability.query" };
    private static final String[] DRIVER = new String[] { "driver.frameworkType", "driver.configName" };
    private static final String[] JDBC = new String[] { "jdbc.username", "jdbc.password", "jdbc.url", "jdbc.driverClassName" };

    public static void main( String[] args )
    {
        if ( args.length != 1 )
        {
            System.err.println( "Usage: TestDriver [configurationFile]" );
            System.exit( -1 );
        }

        File configFile = new File( args[0] );
        if ( !configFile.exists() )
        {
            System.err.println( "[" + configFile.getAbsolutePath() + "] could not be located" );
            System.exit( -1 );
        }

        Properties configProperties = new Properties();
        try
        {
            DeviceActionManager.instance().setDeviceActionFactory( new PerfectoDeviceActionFactory() );
            configProperties.load( new FileInputStream( configFile ) );

            System.out.println( "Reading Cloud Configuration" );
            configureCloudRegistry( configProperties );

            if ( CloudRegistry.instance().getCloud().getProxyHost() != null && !CloudRegistry.instance().getCloud().getProxyHost().isEmpty() && Integer.parseInt( CloudRegistry.instance().getCloud().getProxyPort() ) > 0 )
            {
                System.setProperty( "http.proxyHost", CloudRegistry.instance().getCloud().getProxyHost() );
                System.setProperty( "https.proxyHost", CloudRegistry.instance().getCloud().getProxyHost() );
                System.setProperty( "http.proxyPort", CloudRegistry.instance().getCloud().getProxyPort() );
                System.setProperty( "https.proxyPort", CloudRegistry.instance().getCloud().getProxyPort() );
            }

            System.out.println( "Reading Application Configuration" );
            configureApplicationRegistry( configProperties );

            configureThirdParty( configProperties );

            System.out.println( "Reading Artifact Configuration" );
            configureArtifacts( configProperties );

            System.out.println( "Reading Page Configuration" );
            configurePageManagement( configProperties );

            System.out.println( "Reading Device Configuration" );
            configureDeviceManagement( configProperties );

            System.out.println( "Configuring Driver" );
            validateProperties( configProperties, DRIVER );

            String propertyAdapters = configProperties.getProperty( "driver.propertyAdapters" );
            if ( propertyAdapters != null && !propertyAdapters.isEmpty() )
            {
                String[] adapterList = propertyAdapters.split( "," );
                for ( String adapterName : adapterList )
                {
                    try
                    {
                        DeviceManager.instance().registerPropertyAdapter( (PropertyAdapter) Class.forName( adapterName ).newInstance() );
                    }
                    catch ( Exception e )
                    {
                        System.err.println( "Property Adapter [" + adapterName + "] coudl not be created" );
                        System.exit( -1 );
                    }
                }
            }

            DeviceManager.instance().setConfigurationProperties( configProperties );
            DeviceManager.instance().notifyPropertyAdapter( configProperties );

            GestureManager.instance().setGestureFactory( new PerfectoGestureFactory() );

            String personaNames = configProperties.getProperty( "driver.personas" );
            if ( personaNames != null && !personaNames.isEmpty() )
            {
                DataManager.instance().setPersonas( personaNames );
                PageManager.instance().setWindTunnelEnabled( true );
            }

            DeviceManager.instance().setCachingEnabled( Boolean.parseBoolean( configProperties.getProperty( "driver.enableCaching" ) ) );

            String stepTags = configProperties.getProperty( "driver.stepTags" );
            if ( stepTags != null && !stepTags.isEmpty() )
                PageManager.instance().setTagNames( stepTags );

            String interruptString = configProperties.getProperty( "driver.deviceInterrupts" );
            if ( interruptString != null && !interruptString.isEmpty() )
                DeviceManager.instance().setDeviceInterrupts( interruptString );

            switch ( configProperties.getProperty( DRIVER[0] ).toUpperCase() )
            {
                case "XML":
                    KeyWordDriver.instance().loadTests( new XMLKeyWordProvider( new File( configProperties.getProperty( DRIVER[1] ) ) ) );

                    List<String> testArray = new ArrayList<String>( 10 );

                    //
                    // Extract any named tests
                    //
                    String testNames = configProperties.getProperty( "driver.testNames" );
                    if ( testNames != null && !testNames.isEmpty() )
                        testArray.addAll( Arrays.asList( testNames ) );

                    //
                    // Extract any tagged tests
                    //
                    String tagNames = configProperties.getProperty( "driver.tagNames" );
                    if ( tagNames != null && !tagNames.isEmpty() )
                    {
                        Collection<KeyWordTest> testList = KeyWordDriver.instance().getTaggedTests( tagNames.split( "," ) );

                        if ( testList.isEmpty() )
                        {
                            System.err.println( "No tests contianed the tag(s) [" + tagNames + "]" );
                            System.exit( -1 );
                        }

                        for ( KeyWordTest t : testList )
                            testArray.add( t.getName() );
                    }

                    if ( testArray.size() == 0 )
                        DataManager.instance().setTests( KeyWordDriver.instance().getTestNames() );
                    else
                        DataManager.instance().setTests( testArray.toArray( new String[0] ) );

                    break;
            }

            boolean dryRun = false;
            String validateConfiguration = configProperties.getProperty( "driver.validateConfiguration" );
            if ( validateConfiguration != null )
                dryRun = Boolean.parseBoolean( validateConfiguration );
            
            DeviceManager.instance().setDryRun( dryRun );
            
            if ( dryRun )
                System.out.println( "Performing Dry Run" );
            else
                System.out.println( "Executing Test Cases" );
            
            switch ( configProperties.getProperty( DRIVER[0] ).toUpperCase() )
            {
                case "XML":
                {
                    runTest( configProperties, XMLTestDriver.class );
                    break;
                }
                
                case "OBJ":
                {
                    runTest( configProperties, Class.forName( configProperties.getProperty( DRIVER[1] ) ) );
                    break;
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private static void runTest( Properties configProperties, Class theTest )
    {
        RunDetails.instance().setStartTime();
        
        TestNG testNg = new TestNG( true );
        testNg.setOutputDirectory( configProperties.getProperty( ARTIFACT[0] ) + System.getProperty( "file.separator" ) + "testNg" );
        testNg.setTestClasses( new Class[] { theTest } );
        testNg.run();
        
        if ( testNg.hasFailure() )
            System.exit( -1 );
        else
            System.exit( 0 );

    }

    private static void configureCloudRegistry( Properties configProperties )
    {
        validateProperties( configProperties, CLOUD );

        switch ( (configProperties.getProperty( CLOUD[0] )).toUpperCase() )
        {
            case "XML":
                CloudRegistry.instance().setCloudProvider( new XMLCloudProvider( new File( configProperties.getProperty( CLOUD[1] ) ) ) );
                break;

            case "SQL":
                CloudRegistry.instance().setCloudProvider( new SQLCloudProvider( configProperties.getProperty( JDBC[0] ),
                                                                                 configProperties.getProperty( JDBC[1] ),
                                                                                 configProperties.getProperty( JDBC[2] ),
                                                                                 configProperties.getProperty( JDBC[3] ),
                                                                                 configProperties.getProperty( CLOUD[3] )));
                break;

            case "CSV":
                CloudRegistry.instance().setCloudProvider( new CSVCloudProvider( new File( configProperties.getProperty( CLOUD[1] ) ) ) );
                break;

            case "EXCEL":
                validateProperties( configProperties, new String[] { "cloudRegistry.tabName" } );
                CloudRegistry.instance().setCloudProvider( new ExcelCloudProvider( new File( configProperties.getProperty( CLOUD[1] ) ), configProperties.getProperty( "cloudRegistry.tabName" ) ) );
                break;
        }

        CloudRegistry.instance().setCloud( configProperties.getProperty( CLOUD[2] ) );

        BeanManager.instance().setBeanFactory( new XMLBeanFactory() );
        PerfectoMobile.instance().setUserName( CloudRegistry.instance().getCloud().getUserName() );
        PerfectoMobile.instance().setPassword( CloudRegistry.instance().getCloud().getPassword() );
        PerfectoMobile.instance().setBaseUrl( "https://" + CloudRegistry.instance().getCloud().getHostName() );

    }

    private static void configureThirdParty( Properties configProperties )
    {
        String thirdParty = configProperties.getProperty( "integrations.import" );
        if ( thirdParty != null && !thirdParty.isEmpty() )
        {
            String[] partyArray = thirdParty.split( "," );
            for ( String party : partyArray )
            {
                String className = configProperties.getProperty( party + ".initialization" );
                try
                {
                    System.out.println( "Configuring Third Party support for " + party + " as " + className );

                    Initializable newExtension = (Initializable) Class.forName( className ).newInstance();
                    newExtension.initialize( party, configProperties );

                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void configureApplicationRegistry( Properties configProperties )
    {
        validateProperties( configProperties, APP );

        switch ( (configProperties.getProperty( APP[0] )).toUpperCase() )
        {
            case "XML":
                ApplicationRegistry.instance().setApplicationProvider( new XMLApplicationProvider( new File( configProperties.getProperty( APP[1] ) ) ) );
                break;

            case "CSV":
                ApplicationRegistry.instance().setApplicationProvider( new CSVApplicationProvider( new File( configProperties.getProperty( APP[1] ) ) ) );
                break;

            case "EXCEL":
                validateProperties( configProperties, new String[] { "applicationRegistry.tabName" } );
                ApplicationRegistry.instance().setApplicationProvider( new ExcelApplicationProvider( new File( configProperties.getProperty( APP[1] ) ), configProperties.getProperty( "applicationRegistry.tabName" ) ) );
                break;
        }

        ApplicationRegistry.instance().setAUT( configProperties.getProperty( APP[2] ) );
    }

    private static void configureArtifacts( Properties configProperties )
    {
        validateProperties( configProperties, ARTIFACT );

        DataManager.instance().setReportFolder( new File( configProperties.getProperty( ARTIFACT[0] ) ) );
        String storeImages = configProperties.getProperty( "artifactProducer.storeImages" );
        if ( storeImages != null && !storeImages.isEmpty() )
            PageManager.instance().setStoreImages( Boolean.parseBoolean( storeImages ) );

        String imageLocation = configProperties.getProperty( "artifactProducer.imageLocation" );
        if ( imageLocation != null && !imageLocation.isEmpty() )
            PageManager.instance().setImageLocation( imageLocation );

        String automated = configProperties.getProperty( "artifactProducer.automated" );
        if ( automated != null && !automated.isEmpty() )
        {
            String[] auto = automated.split( "," );
            List<ArtifactType> artifactList = new ArrayList<ArtifactType>( 10 );
            artifactList.add( ArtifactType.EXECUTION_DEFINITION );
            for ( String type : auto )
            {
                try
                {
                    artifactList.add( ArtifactType.valueOf( type ) );
                    if ( ArtifactType.valueOf( type ).equals( ArtifactType.CONSOLE_LOG ) )
                    {
                        String logLevel = configProperties.getProperty( "artifactProducer.logLevel" );
                        if ( logLevel == null )
                            logLevel = "INFO";
                        ThreadedFileHandler threadedHandler = new ThreadedFileHandler();
                        threadedHandler.configureHandler( Level.parse( logLevel.toUpperCase() ) );
                    }
                }
                catch ( Exception e )
                {
                    System.out.println( "No Artifact Type exists as " + type );
                }
            }

            DataManager.instance().setAutomaticDownloads( artifactList.toArray( new ArtifactType[0] ) );

        }
    }

    private static void configurePageManagement( Properties configProperties )
    {
        validateProperties( configProperties, PAGE );

        PageManager.instance().setSiteName( configProperties.getProperty( PAGE[0] ) );

        switch ( (configProperties.getProperty( PAGE[1] )).toUpperCase() )
        {
            case "XML":
                PageManager.instance().setElementProvider( new XMLElementProvider( new File( configProperties.getProperty( PAGE[2] ) ) ) );
                break;

            case "SQL":
                PageManager.instance().setElementProvider( new SQLElementProvider( configProperties.getProperty( JDBC[0] ),
                                                                                   configProperties.getProperty( JDBC[1] ),
                                                                                   configProperties.getProperty( JDBC[2] ),
                                                                                   configProperties.getProperty( JDBC[3] ),
                                                                                   configProperties.getProperty( PAGE[3] )));
                break;

            case "CSV":
                PageManager.instance().setElementProvider( new CSVElementProvider( new File( configProperties.getProperty( PAGE[2] ) ) ) );
                break;

            case "EXCEL":
                String[] fileNames = configProperties.getProperty( PAGE[2] ).split( "," );

                File[] files = new File[fileNames.length];
                for ( int i = 0; i < fileNames.length; i++ )
                    files[i] = new File( fileNames[i] );

                PageManager.instance().setElementProvider( new ExcelElementProvider( files, configProperties.getProperty( PAGE[0] ) ) );
                break;
        }

        String data = configProperties.getProperty( DATA[0] );

        if ( data != null && !data.isEmpty() )
        {
            validateProperties( configProperties, DATA );

            switch ( (configProperties.getProperty( DATA[0] )).toUpperCase() )
            {
                case "XML":
                    PageDataManager.instance().setPageDataProvider( new XMLPageDataProvider( new File( configProperties.getProperty( DATA[1] ) ) ) );
                    break;

                case "SQL":
                    PageDataManager.instance().setPageDataProvider( new SQLPageDataProvider( configProperties.getProperty( JDBC[0] ),
                                                                                             configProperties.getProperty( JDBC[1] ),
                                                                                             configProperties.getProperty( JDBC[2] ),
                                                                                             configProperties.getProperty( JDBC[3] ),
                                                                                             configProperties.getProperty( DATA[2] )));
                    break;

                case "EXCEL":
                    String[] fileNames = configProperties.getProperty( DATA[1] ).split( "," );

                    File[] files = new File[fileNames.length];
                    for ( int i = 0; i < fileNames.length; i++ )
                        files[i] = new File( fileNames[i] );
                    
                    validateProperties( configProperties, new String[] { "pageManagement.pageData.tabNames" } );
                    PageDataManager.instance().setPageDataProvider( new ExcelPageDataProvider( files, configProperties.getProperty( "pageManagement.pageData.tabNames" ) ) );
                    break;

            }
        }

        String content = configProperties.getProperty( CONTENT[0] );
        if ( content != null && !content.isEmpty() )
        {
            validateProperties( configProperties, CONTENT );

            switch ( (configProperties.getProperty( CONTENT[0] )).toUpperCase() )
            {
                case "XML":
                    ContentManager.instance().setContentProvider( new XMLContentProvider( new File( configProperties.getProperty( CONTENT[1] ) ) ) );
                    break;

                case "SQL":
                    ContentManager.instance().setContentProvider( new SQLContentProvider( configProperties.getProperty( JDBC[0] ),
                                                                                          configProperties.getProperty( JDBC[1] ),
                                                                                          configProperties.getProperty( JDBC[2] ),
                                                                                          configProperties.getProperty( JDBC[3] ),
                                                                                          configProperties.getProperty( CONTENT[2] )));
                    break;

                case "EXCEL":
                    validateProperties( configProperties, new String[] { "pageManagement.content.tabName", "pageManagement.content.keyColumn", "pageManagement.content.lookupColumns" } );

                    int keyColumn = Integer.parseInt( configProperties.getProperty( "pageManagement.content.keyColumn" ) );
                    String[] lookupString = configProperties.getProperty( "pageManagement.content.lookupColumns" ).split( "," );

                    int[] lookupColumns = new int[lookupString.length];
                    for ( int i = 0; i < lookupString.length; i++ )
                        lookupColumns[i] = Integer.parseInt( lookupString[i].trim() );

                    ContentManager.instance().setContentProvider( new ExcelContentProvider( new File( configProperties.getProperty( CONTENT[1] ) ), configProperties.getProperty( "pageManagement.content.tabName" ), keyColumn, lookupColumns ) );
                    break;

            }
        }

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

        } );
    }

    private static void configureDeviceManagement( Properties configProperties )
    {
        validateProperties( configProperties, DEVICE );

        switch ( (configProperties.getProperty( DEVICE[0] )).toUpperCase() )
        {
            case "RESERVED":
                DataManager.instance().readData( new PerfectoMobileDataProvider( new ReservedHandsetValidator(), DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            case "AVAILABLE":
                DataManager.instance().readData( new PerfectoMobileDataProvider( new AvailableHandsetValidator(), DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            case "CSV":
                String fileName = configProperties.getProperty( "deviceManagement.fileName" );
                if ( fileName == null )
                {
                    System.err.println( "******* Property [deviceManagement.fileName] was not specified" );
                    System.exit( -1 );
                }
                DataManager.instance().readData( new CSVDataProvider( new File( fileName ), DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            case "XML":
                String xmlFileName = configProperties.getProperty( "deviceManagement.fileName" );
                if ( xmlFileName == null )
                {
                    System.err.println( "******* Property [deviceManagement.fileName] was not specified" );
                    System.exit( -1 );
                }
                DataManager.instance().readData( new XMLDataProvider( new File( xmlFileName ), DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            case "SQL":
                DataManager.instance().readData( new SQLDataProvider( configProperties.getProperty( JDBC[0] ),
                                                                      configProperties.getProperty( JDBC[1] ),
                                                                      configProperties.getProperty( JDBC[2] ),
                                                                      configProperties.getProperty( JDBC[3] ),
                                                                      configProperties.getProperty( DEVICE[2] ),
                                                                      configProperties.getProperty( DEVICE[3] ),
                                                                      DriverType.valueOf( configProperties.getProperty( DEVICE[1] ))));
                break;

            case "EXCEL":
                validateProperties( configProperties, new String[] { "deviceManagement.tabName", "deviceManagement.fileName" } );
                String excelFile = configProperties.getProperty( "deviceManagement.fileName" );

                DataManager.instance().readData( new ExcelDataProvider( new File( excelFile ), configProperties.getProperty( "deviceManagement.tabName" ), DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            case "NAMED":
                String deviceList = configProperties.getProperty( "deviceManagement.deviceList" );
                if ( deviceList == null )
                {
                    System.err.println( "******* Property [deviceManagement.deviceList] was not specified" );
                    System.exit( -1 );
                }
                DataManager.instance().readData( new NamedDataProvider( deviceList, DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            default:
                System.err.println( "Unknown Device Data Provider [" + (configProperties.getProperty( DEVICE[0] )).toUpperCase() + "]" );
                System.exit( -1 );
        }

        String executionReport = configProperties.getProperty( "deviceManagement.executionLog.fileName" );
        if ( executionReport != null )
            DeviceManager.instance().addRunListener( new CSVRunListener( new File( executionReport ) ) );
        DeviceManager.instance().addRunListener( RunDetails.instance() );
        DeviceManager.instance().setDriverType( DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) );
    }

    private static boolean validateProperties( Properties configProperties, String[] propertyNames )
    {
        for ( String name : propertyNames )
        {
            String value = configProperties.getProperty( name );

            if ( value == null || value.isEmpty() )
            {
                System.err.println( "******* Property [" + name + "] was not specified" );
                System.exit( -1 );
            }
        }

        return true;
    }
}
