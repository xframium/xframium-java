package org.xframium.driver;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.openqa.selenium.WebDriver;
import org.xframium.Initializable;
import org.xframium.application.ApplicationRegistry;
import org.xframium.application.CSVApplicationProvider;
import org.xframium.application.ExcelApplicationProvider;
import org.xframium.application.SQLApplicationProvider;
import org.xframium.application.XMLApplicationProvider;
import org.xframium.artifact.ArtifactType;
import org.xframium.content.ContentManager;
import org.xframium.content.provider.ExcelContentProvider;
import org.xframium.content.provider.SQLContentProvider;
import org.xframium.content.provider.XMLContentProvider;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CSVCloudProvider;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.cloud.ExcelCloudProvider;
import org.xframium.device.cloud.SQLCloudProvider;
import org.xframium.device.cloud.XMLCloudProvider;
import org.xframium.device.data.CSVDataProvider;
import org.xframium.device.data.DataManager;
import org.xframium.device.data.DataProvider.DriverType;
import org.xframium.device.data.ExcelDataProvider;
import org.xframium.device.data.NamedDataProvider;
import org.xframium.device.data.SQLDataProvider;
import org.xframium.device.data.XMLDataProvider;
import org.xframium.device.data.perfectoMobile.AvailableHandsetValidator;
import org.xframium.device.data.perfectoMobile.PerfectoMobileDataProvider;
import org.xframium.device.data.perfectoMobile.ReservedHandsetValidator;
import org.xframium.device.logging.ThreadedFileHandler;
import org.xframium.device.ng.AbstractSeleniumTest;
import org.xframium.device.property.PropertyAdapter;
import org.xframium.gesture.device.action.DeviceActionManager;
import org.xframium.gesture.device.action.spi.perfecto.PerfectoDeviceActionFactory;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.data.provider.ExcelPageDataProvider;
import org.xframium.page.data.provider.SQLPageDataProvider;
import org.xframium.page.data.provider.XMLPageDataProvider;
import org.xframium.page.element.provider.CSVElementProvider;
import org.xframium.page.element.provider.ExcelElementProvider;
import org.xframium.page.element.provider.SQLElementProvider;
import org.xframium.page.element.provider.XMLElementProvider;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.provider.XMLKeyWordProvider;
import org.xframium.page.keyWord.provider.SQLKeyWordProvider;
import org.xframium.spi.CSVRunListener;
import org.xframium.spi.RunDetails;
import org.xframium.utility.SeleniumSessionManager;

public class TXTConfigurationReader extends AbstractConfigurationReader
{
    private static final String[] CLOUD = new String[] { "cloudRegistry.provider", "cloudRegistry.fileName", "cloudRegistry.cloudUnderTest" };
    private static final String[] OPT_CLOUD = new String[] { "cloudRegistry.query" };
    private static final String[] APP = new String[] { "applicationRegistry.provider", "applicationRegistry.fileName", "applicationRegistry.applicationUnderTest" };
    private static final String[] OPT_APP = new String[] { "applicationRegistry.query", "applicationRegistry.capQuery" };
    private static final String[] ARTIFACT = new String[] { "artifactProducer.parentFolder" };
    private static final String[] PAGE = new String[] { "pageManagement.siteName", "pageManagement.provider", "pageManagement.fileName" };
    private static final String[] OPT_PAGE = new String[] { "pageManagement.query" };
    private static final String[] DATA = new String[] { "pageManagement.pageData.provider", "pageManagement.pageData.fileName" };
    private static final String[] OPT_DATA = new String[] { "pageManagement.pageData.query" };
    private static final String[] CONTENT = new String[] { "pageManagement.content.provider", "pageManagement.content.fileName" };
    private static final String[] OPT_CONTENT = new String[] { "pageManagement.content.query" };
    private static final String[] DEVICE = new String[] { "deviceManagement.provider", "deviceManagement.driverType" };
    private static final String[] OPT_DEVICE = new String[] { "deviceManagement.device.query", "deviceManagement.capability.query" };
    private static final String[] DRIVER = new String[] { "driver.frameworkType", "driver.configName" };
    private static final String[] OPT_DRIVER = new String[] { "driver.suite", "driver.suiteQuery", "driver.pageQuery", "driver.importQuery",  "driver.testQuery",
                                                              "driver.stepQuery", "driver.substepQuery", "driver.paramQuery", "driver.tokenQuery",
                                                              "driver.functionQuery" };
    private static final String[] JDBC = new String[] { "jdbc.username", "jdbc.password", "jdbc.url", "jdbc.driverClassName" };
    
    private Properties configProperties;
    
    @Override
    protected boolean readFile( File configFile )
    {
        configProperties = new Properties();
        try
        {
            DeviceActionManager.instance().setDeviceActionFactory( new PerfectoDeviceActionFactory() );
            configProperties.load( new FileInputStream( configFile ) );
            return true;
        }
        catch( Exception e )
        {
            log.error( "Error reading " + configFile, e );
            return false;
        }
    }

    @Override
    protected boolean configureCloud()
    {
        switch ( (configProperties.getProperty( CLOUD[0] )).toUpperCase() )
        {
            case "XML":
                validateProperties( configProperties, CLOUD );
                CloudRegistry.instance().setCloudProvider( new XMLCloudProvider( findFile( configFolder, new File( configProperties.getProperty( CLOUD[1] ) ) ) ) );
                break;

            case "SQL":
                CloudRegistry.instance().setCloudProvider( new SQLCloudProvider( configProperties.getProperty( JDBC[0] ),
                                                                                 configProperties.getProperty( JDBC[1] ),
                                                                                 configProperties.getProperty( JDBC[2] ),
                                                                                 configProperties.getProperty( JDBC[3] ),
                                                                                 configProperties.getProperty( OPT_CLOUD[0] )));
                break;

            case "CSV":
                validateProperties( configProperties, CLOUD );
                CloudRegistry.instance().setCloudProvider( new CSVCloudProvider( findFile( configFolder, new File( configProperties.getProperty( CLOUD[1] ) ) ) ) );
                break;

            case "EXCEL":
                validateProperties( configProperties, CLOUD );
                validateProperties( configProperties, new String[] { "cloudRegistry.tabName" } );
                CloudRegistry.instance().setCloudProvider( new ExcelCloudProvider( findFile( configFolder, new File( configProperties.getProperty( CLOUD[1] ) ) ), configProperties.getProperty( "cloudRegistry.tabName" ) ) );
                break;
        }

        CloudRegistry.instance().setCloud( configProperties.getProperty( CLOUD[2] ) );

        
        return true;
    }

    @Override
    protected boolean configureApplication()
    {
        File appFile = null;
        
        switch ( (configProperties.getProperty( APP[0] )).toUpperCase() )
        {
            case "XML":
                validateProperties( configProperties, APP );
                appFile = findFile( configFolder, new File( configProperties.getProperty( APP[1] ) ) );
                ApplicationRegistry.instance().setApplicationProvider( new XMLApplicationProvider( appFile ) );
                break;

            case "CSV":
                validateProperties( configProperties, APP );
                appFile = findFile( configFolder, new File( configProperties.getProperty( APP[1] ) ) );
                ApplicationRegistry.instance().setApplicationProvider( new CSVApplicationProvider( appFile ) );
                break;
                
            case "SQL":
                ApplicationRegistry.instance().setApplicationProvider( new SQLApplicationProvider( configProperties.getProperty( JDBC[0] ),
                                                                                                   configProperties.getProperty( JDBC[1] ),
                                                                                                   configProperties.getProperty( JDBC[2] ),
                                                                                                   configProperties.getProperty( JDBC[3] ),
                                                                                                   configProperties.getProperty( OPT_APP[0] ),
                                                                                                   configProperties.getProperty( OPT_APP[1] )));
                break;

            case "EXCEL":
                validateProperties( configProperties, APP );
                validateProperties( configProperties, new String[] { "applicationRegistry.tabName" } );
                ApplicationRegistry.instance().setApplicationProvider( new ExcelApplicationProvider( appFile, configProperties.getProperty( "applicationRegistry.tabName" ) ) );
                break;
        }

        ApplicationRegistry.instance().setAUT( configProperties.getProperty( APP[2] ) );
        return true;
    }

    @Override
    protected boolean configureThirdParty()
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
        return true;
    }

    @Override
    protected boolean configureArtifacts()
    {
        validateProperties( configProperties, ARTIFACT );

        DataManager.instance().setReportFolder( new File( configFolder, configProperties.getProperty( ARTIFACT[0] ) ) );
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
        return true;
    }

    @Override
    protected boolean configurePageManagement()
    {
        PageManager.instance().setSiteName( configProperties.getProperty( PAGE[0] ) );

        switch ( (configProperties.getProperty( PAGE[1] )).toUpperCase() )
        {
            case "XML":
                validateProperties( configProperties, PAGE );
                
                PageManager.instance().setElementProvider( new XMLElementProvider( findFile( configFolder, new File( configProperties.getProperty( PAGE[2] ) ) ) ) );
                break;

            case "SQL":
                PageManager.instance().setElementProvider( new SQLElementProvider( configProperties.getProperty( JDBC[0] ),
                                                                                   configProperties.getProperty( JDBC[1] ),
                                                                                   configProperties.getProperty( JDBC[2] ),
                                                                                   configProperties.getProperty( JDBC[3] ),
                                                                                   configProperties.getProperty( OPT_PAGE[0] )));
                break;

            case "CSV":
                validateProperties( configProperties, PAGE );
                PageManager.instance().setElementProvider( new CSVElementProvider( findFile( configFolder, new File( configProperties.getProperty( PAGE[2] ) ) ) ) );
                break;

            case "EXCEL":
                validateProperties( configProperties, PAGE );
                String[] fileNames = configProperties.getProperty( PAGE[2] ).split( "," );

                File[] files = new File[fileNames.length];
                for ( int i = 0; i < fileNames.length; i++ )
                    files[i] = findFile( configFolder, new File( fileNames[i] ) );

                PageManager.instance().setElementProvider( new ExcelElementProvider( files, configProperties.getProperty( PAGE[0] ) ) );
                break;
        }

        if ( PageManager.instance().getElementProvider() == null )
            return false;
        
        return PageManager.instance().getElementProvider().isInitialized();
    }
    
    @Override
    protected boolean configureData()
    {
        String data = configProperties.getProperty( DATA[0] );

        if ( data != null && !data.isEmpty() )
        {
            switch ( (configProperties.getProperty( DATA[0] )).toUpperCase() )
            {
                case "XML":
                    validateProperties( configProperties, DATA );
                    PageDataManager.instance().setPageDataProvider( new XMLPageDataProvider( findFile( configFolder, new File( configProperties.getProperty( DATA[1] ) ) ) ) );
                    break;

                case "SQL":
                    PageDataManager.instance().setPageDataProvider( new SQLPageDataProvider( configProperties.getProperty( JDBC[0] ),
                                                                                             configProperties.getProperty( JDBC[1] ),
                                                                                             configProperties.getProperty( JDBC[2] ),
                                                                                             configProperties.getProperty( JDBC[3] ),
                                                                                             configProperties.getProperty( OPT_DATA[0] )));
                    break;

                case "EXCEL":
                    validateProperties( configProperties, DATA );
                    String[] fileNames = configProperties.getProperty( DATA[1] ).split( "," );

                    File[] files = new File[fileNames.length];
                    for ( int i = 0; i < fileNames.length; i++ )
                        files[i] = findFile( configFolder, new File( fileNames[i] ) );
                    
                    validateProperties( configProperties, new String[] { "pageManagement.pageData.tabNames" } );
                    PageDataManager.instance().setPageDataProvider( new ExcelPageDataProvider( files, configProperties.getProperty( "pageManagement.pageData.tabNames" ) ) );
                    break;

            }
        }
        
        return true;
    }
    
    @Override
    protected boolean configureContent()
    {
        String content = configProperties.getProperty( CONTENT[0] );
        if ( content != null && !content.isEmpty() )
        {
            switch ( (configProperties.getProperty( CONTENT[0] )).toUpperCase() )
            {
                case "XML":
                    validateProperties( configProperties, CONTENT );
                    ContentManager.instance().setContentProvider( new XMLContentProvider( findFile( configFolder, new File( configProperties.getProperty( CONTENT[1] ) ) ) ) );
                    break;

                case "SQL":
                    ContentManager.instance().setContentProvider( new SQLContentProvider( configProperties.getProperty( JDBC[0] ),
                                                                                          configProperties.getProperty( JDBC[1] ),
                                                                                          configProperties.getProperty( JDBC[2] ),
                                                                                          configProperties.getProperty( JDBC[3] ),
                                                                                          configProperties.getProperty( OPT_CONTENT[0] )));
                    break;

                case "EXCEL":
                    validateProperties( configProperties, new String[] { "pageManagement.content.tabName", "pageManagement.content.keyColumn", "pageManagement.content.lookupColumns" } );

                    int keyColumn = Integer.parseInt( configProperties.getProperty( "pageManagement.content.keyColumn" ) );
                    String[] lookupString = configProperties.getProperty( "pageManagement.content.lookupColumns" ).split( "," );

                    int[] lookupColumns = new int[lookupString.length];
                    for ( int i = 0; i < lookupString.length; i++ )
                        lookupColumns[i] = Integer.parseInt( lookupString[i].trim() );

                    ContentManager.instance().setContentProvider( new ExcelContentProvider( findFile( configFolder, new File( configProperties.getProperty( CONTENT[1] ) ) ), configProperties.getProperty( "pageManagement.content.tabName" ), keyColumn, lookupColumns ) );
                    break;

            }
        }
        return true;
    }

    @Override
    protected boolean configureDevice()
    {
        switch ( (configProperties.getProperty( DEVICE[0] )).toUpperCase() )
        {
            case "RESERVED":
                validateProperties( configProperties, DEVICE );
                DataManager.instance().readData( new PerfectoMobileDataProvider( new ReservedHandsetValidator(), DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            case "AVAILABLE":
                validateProperties( configProperties, DEVICE );
                DataManager.instance().readData( new PerfectoMobileDataProvider( new AvailableHandsetValidator(), DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            case "CSV":
                validateProperties( configProperties, DEVICE );
                String fileName = configProperties.getProperty( "deviceManagement.fileName" );
                if ( fileName == null )
                {
                    System.err.println( "******* Property [deviceManagement.fileName] was not specified" );
                    System.exit( -1 );
                }
                DataManager.instance().readData( new CSVDataProvider( findFile( configFolder, new File( fileName ) ), DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            case "XML":
                validateProperties( configProperties, DEVICE );
                String xmlFileName = configProperties.getProperty( "deviceManagement.fileName" );
                if ( xmlFileName == null )
                {
                    System.err.println( "******* Property [deviceManagement.fileName] was not specified" );
                    System.exit( -1 );
                }
                DataManager.instance().readData( new XMLDataProvider( findFile( configFolder, new File( xmlFileName ) ), DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            case "SQL":
                DataManager.instance().readData( new SQLDataProvider( configProperties.getProperty( JDBC[0] ),
                                                                      configProperties.getProperty( JDBC[1] ),
                                                                      configProperties.getProperty( JDBC[2] ),
                                                                      configProperties.getProperty( JDBC[3] ),
                                                                      configProperties.getProperty( OPT_DEVICE[0] ),
                                                                      configProperties.getProperty( OPT_DEVICE[1] ),
                                                                      DriverType.valueOf( configProperties.getProperty( DEVICE[1] ))));
                break;

            case "EXCEL":
                validateProperties( configProperties, DEVICE );
                validateProperties( configProperties, new String[] { "deviceManagement.tabName", "deviceManagement.fileName" } );
                String excelFile = configProperties.getProperty( "deviceManagement.fileName" );

                DataManager.instance().readData( new ExcelDataProvider( findFile( configFolder, new File( excelFile ) ), configProperties.getProperty( "deviceManagement.tabName" ), DriverType.valueOf( configProperties.getProperty( DEVICE[1] ) ) ) );
                break;

            case "NAMED":
                validateProperties( configProperties, DEVICE );
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
        
        return true;
    }

    @Override
    protected boolean configurePropertyAdapters()
    {
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
        return true;
    }

    @Override
    protected boolean configureDriver()
    {
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

        boolean keywordsloaded = false;

        switch ( configProperties.getProperty( DRIVER[0] ).toUpperCase() )
        {
            case "XML":
            {
                KeyWordDriver.instance().loadTests( new XMLKeyWordProvider( findFile( configFolder, new File( configProperties.getProperty( DRIVER[1] ) ) ) ) );
                keywordsloaded = true;

                break;
            }

            case "SQL":
            case "OBJ-SQL":
            {
                KeyWordDriver.instance().loadTests( new SQLKeyWordProvider( configProperties.getProperty( JDBC[0] ),
                                                                            configProperties.getProperty( JDBC[1] ),
                                                                            configProperties.getProperty( JDBC[2] ),
                                                                            configProperties.getProperty( JDBC[3] ),
                                                                            configProperties.getProperty( OPT_DRIVER[0] ),
                                                                            configProperties.getProperty( OPT_DRIVER[1] ),
                                                                            configProperties.getProperty( OPT_DRIVER[2] ),
                                                                            configProperties.getProperty( OPT_DRIVER[3] ),
                                                                            configProperties.getProperty( OPT_DRIVER[4] ),
                                                                            configProperties.getProperty( OPT_DRIVER[5] ),
                                                                            configProperties.getProperty( OPT_DRIVER[6] ),
                                                                            configProperties.getProperty( OPT_DRIVER[7] ),
                                                                            configProperties.getProperty( OPT_DRIVER[8] ),
                                                                            configProperties.getProperty( OPT_DRIVER[9] )
                                                                            ));
                keywordsloaded = true;
                                                   
                break;
            }
        }

        if ( keywordsloaded )
        {
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
        }

        String validateConfiguration = configProperties.getProperty( "driver.validateConfiguration" );
        if ( validateConfiguration != null )
            dryRun = Boolean.parseBoolean( validateConfiguration );
        
        DeviceManager.instance().setDryRun( dryRun );
        
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
        
        return false;
    }
    
    @Override
    protected boolean executeTest() throws Exception
    {
        switch ( configProperties.getProperty( DRIVER[0] ).toUpperCase() )
        {
            case "XML":
            {
                runTest( configProperties.getProperty( ARTIFACT[0] ), XMLTestDriver.class );
                break;
            }
            
            case "OBJ":
            case "OBJ-SQL":
            {
                runTest( configProperties.getProperty( ARTIFACT[0] ), Class.forName( configProperties.getProperty( DRIVER[1] ) ) );
                break;
            }
        }
        return true;
    }


    protected static boolean validateProperties( Properties configProperties, String[] propertyNames )
    {
        for ( String name : propertyNames )
        {
            String value = configProperties.getProperty( name );

            if ( value == null || value.isEmpty() )
            {
                System.err.println( "******* Property [" + name + "] was not specified" );
                (new Exception()).printStackTrace();
                System.exit( -1 );
            }
        }

        return true;
    }

    

}
