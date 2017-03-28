package org.xframium.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.xframium.Initializable;
import org.xframium.application.CSVApplicationProvider;
import org.xframium.application.ExcelApplicationProvider;
import org.xframium.application.SQLApplicationProvider;
import org.xframium.application.XMLApplicationProvider;
import org.xframium.container.ApplicationContainer;
import org.xframium.container.CloudContainer;
import org.xframium.container.DeviceContainer;
import org.xframium.container.DriverContainer;
import org.xframium.container.FavoriteContainer;
import org.xframium.container.SuiteContainer;
import org.xframium.content.ContentManager;
import org.xframium.content.provider.ExcelContentProvider;
import org.xframium.content.provider.SQLContentProvider;
import org.xframium.content.provider.XMLContentProvider;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CSVCloudProvider;
import org.xframium.device.cloud.CloudProvider;
import org.xframium.device.cloud.EncryptedCloudProvider;
import org.xframium.device.cloud.ExcelCloudProvider;
import org.xframium.device.cloud.SQLCloudProvider;
import org.xframium.device.cloud.XMLCloudProvider;
import org.xframium.device.data.CSVDataProvider;
import org.xframium.device.data.DataProvider.DriverType;
import org.xframium.device.data.ExcelDataProvider;
import org.xframium.device.data.NamedDataProvider;
import org.xframium.device.data.SQLDataProvider;
import org.xframium.device.data.XMLDataProvider;
import org.xframium.device.data.perfectoMobile.AvailableHandsetValidator;
import org.xframium.device.data.perfectoMobile.PerfectoMobileDataProvider;
import org.xframium.device.data.perfectoMobile.PerfectoMobilePluginProvider;
import org.xframium.device.data.perfectoMobile.ReservedHandsetValidator;
import org.xframium.device.property.PropertyAdapter;
import org.xframium.device.proxy.ProxyRegistry;
import org.xframium.gesture.device.action.DeviceActionManager;
import org.xframium.gesture.device.action.spi.perfecto.PerfectoDeviceActionFactory;
import org.xframium.page.data.provider.ExcelPageDataProvider;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.data.provider.SQLPageDataProvider;
import org.xframium.page.data.provider.XMLPageDataProvider;
import org.xframium.page.element.provider.CSVElementProvider;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.element.provider.ExcelElementProvider;
import org.xframium.page.element.provider.SQLElementProvider;
import org.xframium.page.element.provider.XMLElementProvider;
import org.xframium.page.keyWord.provider.ExcelKeyWordProvider;
import org.xframium.page.keyWord.provider.SQLKeyWordProvider;
import org.xframium.page.keyWord.provider.XMLKeyWordProvider;
import org.xframium.spi.Device;

public class TXTConfigurationReader extends AbstractConfigurationReader
{
	private static final String[] PROXY_SETTINGS = new String[] { "proxy.host", "proxy.port", "proxy.ignoreHost" };
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
    private static final String[] OPT_DRIVER = new String[] { "driver.suiteName", "driver.modelQuery", "driver.testSuiteQuery", "driver.testCaseQuery" };
    private static final String[] JDBC = new String[] { "jdbc.username", "jdbc.password", "jdbc.url", "jdbc.driverClassName" };
    
    private Map<String,String> configProperties = new HashMap<String,String>(20);
    
    @Override
    public boolean readFile( File configFile )
    {
        configFolder = configFile.getParentFile();
        try
        {
            return readFile( new FileInputStream( configFile ) );
        }
        catch( Exception e )
        {
            log.error( "Error reading " + configFile, e );
            return false;
        }
    }
    
    protected Map<String,String> getConfigurationProperties()
    {
        return configProperties;
    }
    
    @Override
    public boolean readFile( InputStream inputStream )
    {
        Properties cP = new Properties();
        try
        {
            DeviceActionManager.instance().setDeviceActionFactory( new PerfectoDeviceActionFactory() );
            cP.load( inputStream );
            for ( Object key : cP.keySet() )
                configProperties.put( (String) key, cP.getProperty( (String) key ) );

            return true;
        }
        catch( Exception e )
        {
            log.error( "Error reading configruation file", e );
            return false;
        }
    }
    
    @Override
    public FavoriteContainer configureFavorites()
    {
        String favorites = configProperties.get( "favorites" );
        
        return new FavoriteContainer( favorites );
        
    }

    @Override
    public CloudContainer configureCloud( boolean secured )
    {
        CloudContainer cC = new CloudContainer();
        CloudProvider cloudProvider = null;
        switch ( (configProperties.get( CLOUD[0] )).toUpperCase() )
        {
            case "XML":
                validateProperties( configProperties, CLOUD );
                cloudProvider =  new XMLCloudProvider( findFile( configFolder, new File( configProperties.get( CLOUD[1] ) ) ) );
                break;

            case "SQL":
                cloudProvider =  new SQLCloudProvider( configProperties.get( JDBC[0] ),
                                                     configProperties.get( JDBC[1] ),
                                                     configProperties.get( JDBC[2] ),
                                                     configProperties.get( JDBC[3] ),
                                                     configProperties.get( OPT_CLOUD[0] ));
                break;

            case "CSV":
                validateProperties( configProperties, CLOUD );
                cloudProvider =  new CSVCloudProvider( findFile( configFolder, new File( configProperties.get( CLOUD[1] ) ) ) );
                break;

            case "EXCEL":
                validateProperties( configProperties, CLOUD );
                validateProperties( configProperties, new String[] { "cloudRegistry.tabName" } );
                cloudProvider =  new ExcelCloudProvider( findFile( configFolder, new File( configProperties.get( CLOUD[1] ) ) ), configProperties.get( "cloudRegistry.tabName" ) );
                break;
        }

        if ( secured )
            cloudProvider = new EncryptedCloudProvider( cloudProvider );
        
        cC.setCloudList( cloudProvider.readData() );
        
        cC.setCloudName( configProperties.get( CLOUD[2] ) );

        
        return cC;
    }

    @Override
    public ApplicationContainer configureApplication()
    {
        ApplicationContainer appContainer = new ApplicationContainer();
        File appFile = null;
        
        switch ( (configProperties.get( APP[0] )).toUpperCase() )
        {
            case "XML":
                validateProperties( configProperties, APP );
                appFile = findFile( configFolder, new File( configProperties.get( APP[1] ) ) );
                appContainer.setAppList( new XMLApplicationProvider( appFile ).readData()  );
                break;

            case "CSV":
                validateProperties( configProperties, APP );
                appFile = findFile( configFolder, new File( configProperties.get( APP[1] ) ) );
                appContainer.setAppList( new CSVApplicationProvider( appFile ).readData()  );
                break;
                
            case "SQL":
                appContainer.setAppList( new SQLApplicationProvider( configProperties.get( JDBC[0] ),
                                                                                                   configProperties.get( JDBC[1] ),
                                                                                                   configProperties.get( JDBC[2] ),
                                                                                                   configProperties.get( JDBC[3] ),
                                                                                                   configProperties.get( OPT_APP[0] ),
                                                                                                   configProperties.get( OPT_APP[1] )).readData() );
                break;

            case "EXCEL":
                validateProperties( configProperties, APP );
                validateProperties( configProperties, new String[] { "applicationRegistry.tabName" } );
                appContainer.setAppList(  new ExcelApplicationProvider( appFile, configProperties.get( "applicationRegistry.tabName" ) ).readData()  );
                break;
        }
        appContainer.setApplicationName( configProperties.get( APP[2] ) );
        return appContainer;
    }

    @Override
    protected boolean configureThirdParty()
    {
        String thirdParty = configProperties.get( "integrations.import" );
        if ( thirdParty != null && !thirdParty.isEmpty() )
        {
            String[] partyArray = thirdParty.split( "," );
            for ( String party : partyArray )
            {
                String className = configProperties.get( party + ".initialization" );
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
    public boolean configureArtifacts( DriverContainer driverC )
    {
        driverC.setReportFolder( configProperties.get( ARTIFACT[0] ) );
        driverC.setArtifactList( configProperties.get( "artifactProducer.automated" ) );
        
        return true;
    }

    @Override
    public ElementProvider configurePageManagement( SuiteContainer sC )
    {
        if ( sC != null )
            sC.setSiteName( configProperties.get( PAGE[0] ) );

        switch ( (configProperties.get( PAGE[1] )).toUpperCase() )
        {
            case "XML":
                validateProperties( configProperties, PAGE );
                
                return new XMLElementProvider( findFile( configFolder, new File( configProperties.get( PAGE[2] ) ) ) );

            case "SQL":
                return new SQLElementProvider( configProperties.get( JDBC[0] ),
                                                                                   configProperties.get( JDBC[1] ),
                                                                                   configProperties.get( JDBC[2] ),
                                                                                   configProperties.get( JDBC[3] ),
                                                                                   configProperties.get( OPT_PAGE[0] ));
                
            case "CSV":
                validateProperties( configProperties, PAGE );
                return new CSVElementProvider( findFile( configFolder, new File( configProperties.get( PAGE[2] ) ) ) );


            case "EXCEL":
                validateProperties( configProperties, PAGE );
                String[] fileNames = configProperties.get( PAGE[2] ).split( "," );

                File[] files = new File[fileNames.length];
                for ( int i = 0; i < fileNames.length; i++ )
                    files[i] = findFile( configFolder, new File( fileNames[i] ) );

                return new ExcelElementProvider( files, configProperties.get( PAGE[0] ) );
        }

        return null;
    }
    
    @Override
    public PageDataProvider configureData()
    {
        String data = configProperties.get( DATA[0] );

        PageDataProvider dataProvider = null;
        if ( data != null && !data.isEmpty() )
        {
            switch ( (configProperties.get( DATA[0] )).toUpperCase() )
            {
                case "XML":
                    validateProperties( configProperties, DATA );
                    dataProvider = new XMLPageDataProvider( findFile( configFolder, new File( configProperties.get( DATA[1] ) ) ) );
                    break;

                case "SQL":
                    dataProvider = new SQLPageDataProvider( configProperties.get( JDBC[0] ),
                                                                 configProperties.get( JDBC[1] ),
                                                                 configProperties.get( JDBC[2] ),
                                                                 configProperties.get( JDBC[3] ),
                                                                 configProperties.get( OPT_DATA[0] ));
                    break;

                case "EXCEL":
                    validateProperties( configProperties, DATA );
                    String[] fileNames = configProperties.get( DATA[1] ).split( "," );

                    File[] files = new File[fileNames.length];
                    for ( int i = 0; i < fileNames.length; i++ )
                        files[i] = findFile( configFolder, new File( fileNames[i] ) );
                    
                    validateProperties( configProperties, new String[] { "pageManagement.pageData.tabNames" } );
                    dataProvider = new ExcelPageDataProvider( files, configProperties.get( "pageManagement.pageData.tabNames" ) ) ;
                    break;

            }
        }
        
        return dataProvider;
    }
    
    @Override
    public boolean configureContent()
    {
        String content = configProperties.get( CONTENT[0] );
        if ( content != null && !content.isEmpty() )
        {
            switch ( (configProperties.get( CONTENT[0] )).toUpperCase() )
            {
                case "XML":
                    validateProperties( configProperties, CONTENT );
                    ContentManager.instance().setContentProvider( new XMLContentProvider( findFile( configFolder, new File( configProperties.get( CONTENT[1] ) ) ) ) );
                    break;

                case "SQL":
                    ContentManager.instance().setContentProvider( new SQLContentProvider( configProperties.get( JDBC[0] ),
                                                                                          configProperties.get( JDBC[1] ),
                                                                                          configProperties.get( JDBC[2] ),
                                                                                          configProperties.get( JDBC[3] ),
                                                                                          configProperties.get( OPT_CONTENT[0] )));
                    break;

                case "EXCEL":
                    validateProperties( configProperties, new String[] { "pageManagement.content.tabName", "pageManagement.content.keyColumn", "pageManagement.content.lookupColumns" } );

                    int keyColumn = Integer.parseInt( configProperties.get( "pageManagement.content.keyColumn" ) );
                    String[] lookupString = configProperties.get( "pageManagement.content.lookupColumns" ).split( "," );

                    int[] lookupColumns = new int[lookupString.length];
                    for ( int i = 0; i < lookupString.length; i++ )
                        lookupColumns[i] = Integer.parseInt( lookupString[i].trim() );

                    ContentManager.instance().setContentProvider( new ExcelContentProvider( findFile( configFolder, new File( configProperties.get( CONTENT[1] ) ) ), configProperties.get( "pageManagement.content.tabName" ), keyColumn, lookupColumns ) );
                    break;

            }
        }
        return true;
    }

    @Override
    public DeviceContainer configureDevice()
    {
        DeviceContainer dC = new DeviceContainer();
        List<Device> deviceList = null;
        
        switch ( (configProperties.get( DEVICE[0] )).toUpperCase() )
        {
            case "PERFECTO_PLUGIN":
                deviceList = new PerfectoMobilePluginProvider( configProperties.get( "deviceManagement.deviceList" ) + "", DriverType.valueOf( configProperties.get( DEVICE[1] ) ), configProperties.get( "deviceManagement.pluginType" ) ).readData();
                break;
            
            case "RESERVED":
                validateProperties( configProperties, DEVICE );
                deviceList = new PerfectoMobileDataProvider( new ReservedHandsetValidator(), DriverType.valueOf( configProperties.get( DEVICE[1] ) ) ).readData();
                break;

            case "AVAILABLE":
                validateProperties( configProperties, DEVICE );
                deviceList = new PerfectoMobileDataProvider( new AvailableHandsetValidator(), DriverType.valueOf( configProperties.get( DEVICE[1] ) ) ).readData();
                break;

            case "CSV":
                validateProperties( configProperties, DEVICE );
                String fileName = configProperties.get( "deviceManagement.fileName" );
                if ( fileName == null )
                {
                    System.err.println( "******* Property [deviceManagement.fileName] was not specified" );
                    System.exit( -1 );
                }
                deviceList = new CSVDataProvider( findFile( configFolder, new File( fileName ) ), DriverType.valueOf( configProperties.get( DEVICE[1] ) ) ).readData();
                break;

            case "XML":
                validateProperties( configProperties, DEVICE );
                String xmlFileName = configProperties.get( "deviceManagement.fileName" );
                if ( xmlFileName == null )
                {
                    System.err.println( "******* Property [deviceManagement.fileName] was not specified" );
                    System.exit( -1 );
                }
                deviceList = new XMLDataProvider( findFile( configFolder, new File( xmlFileName ) ), DriverType.valueOf( configProperties.get( DEVICE[1] ) ) ).readData();
                break;

            case "SQL":
                deviceList = new SQLDataProvider( configProperties.get( JDBC[0] ),
                                                                      configProperties.get( JDBC[1] ),
                                                                      configProperties.get( JDBC[2] ),
                                                                      configProperties.get( JDBC[3] ),
                                                                      configProperties.get( OPT_DEVICE[0] ),
                                                                      configProperties.get( OPT_DEVICE[1] ),
                                                                      DriverType.valueOf( configProperties.get( DEVICE[1] ))).readData();
                break;

            case "EXCEL":
                validateProperties( configProperties, DEVICE );
                validateProperties( configProperties, new String[] { "deviceManagement.tabName", "deviceManagement.fileName" } );
                String excelFile = configProperties.get( "deviceManagement.fileName" );

                deviceList = new ExcelDataProvider( findFile( configFolder, new File( excelFile ) ), configProperties.get( "deviceManagement.tabName" ), DriverType.valueOf( configProperties.get( DEVICE[1] ) ) ).readData();
                break;

            case "NAMED":
                validateProperties( configProperties, DEVICE );
                String devices = configProperties.get( "deviceManagement.deviceList" );
                if ( devices == null )
                {
                    System.err.println( "******* Property [deviceManagement.deviceList] was not specified" );
                    System.exit( -1 );
                }
                deviceList = new NamedDataProvider( devices, DriverType.valueOf( configProperties.get( DEVICE[1] ) ) ).readData();
                break;

            default:
                System.err.println( "Unknown Device Data Provider [" + (configProperties.get( DEVICE[0] )).toUpperCase() + "]" );
                System.exit( -1 );
        }
        
        dC.setdType( DriverType.valueOf( configProperties.get( DEVICE[1] ) ) );
        
        if ( deviceList != null )
        {
            String deviceOverride = configProperties.get( "deviceOverrides" );
            if ( deviceOverride != null )
            {
                Map<String,Boolean> deviceMap = new HashMap<String,Boolean>( 10 );
                String[] overrideArray = deviceOverride.split( "," );
                for ( String override : overrideArray )
                    deviceMap.put( override, true );
                
                for ( Device d : deviceList )
                {
                    if ( deviceMap.containsKey( d.getKey() ) )
                    {
                        d.setActive( true );
                        dC.addDevice( d );
                    }
                    else
                    {
                        d.setActive( false );
                        dC.addDevice( d );
                    }
                }
            }
            else
            {
                for ( Device d : deviceList )
                    dC.addDevice( d );
            }
        }

        return dC;
    }

    @Override
    protected boolean configurePropertyAdapters()
    {
        String propertyAdapters = configProperties.get( "driver.propertyAdapters" );
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
    public SuiteContainer configureTestCases( PageDataProvider pdp, boolean parseDataIterators )
    {
        SuiteContainer sC = null;
        
        if ( configProperties.get( DRIVER[0] ) == null )
        {
            sC = new SuiteContainer();
        }
        else
            {
            
            switch ( configProperties.get( DRIVER[0] ).toUpperCase() )
            {
                case "XML":
                {
                    sC = new XMLKeyWordProvider( findFile( configFolder, new File( configProperties.get( DRIVER[1] ) ) ), configProperties ).readData( true );
                    break;
                }
                
                case "EXCEL":
                {
                    sC = new ExcelKeyWordProvider( findFile( configFolder, new File( configProperties.get( DRIVER[1] ) ) ), configProperties ).readData( true );
    
                    break;
                }
    
                case "SQL":
                case "OBJ-SQL":
                {
                    sC = new SQLKeyWordProvider( configProperties.get( JDBC[0] ),
                                                                                configProperties.get( JDBC[1] ),
                                                                                configProperties.get( JDBC[2] ),
                                                                                configProperties.get( JDBC[3] ),
                                                                                configProperties.get( OPT_DRIVER[0] ),
                                                                                configProperties.get( PAGE[0] ),
                                                                                configProperties.get( OPT_DRIVER[1] ),
                                                                                configProperties.get( OPT_DRIVER[2] ),
                                                                                configProperties.get( OPT_DRIVER[3] ),
                                                                                configProperties ).readData( true );
                                                       
                    break;
                }
            }
        
            }
        if ( sC != null )
            sC.setDataProvider( pdp );
        return sC;
    }
    
    @Override
    public DriverContainer configureDriver(Map<String, String> customConfig)
    {
        DriverContainer dC = new DriverContainer();

        if ( customConfig != null )
            configProperties.putAll( customConfig );
        
        String personaNames = configProperties.get( "driver.personas" );
        if ( personaNames != null && !personaNames.isEmpty() )
            dC.setPerfectoPersonas( personaNames );
        
        dC.setDisplayReport( Boolean.parseBoolean( configProperties.get( "driver.displayResults" ) ) );
        dC.setSmartCaching( Boolean.parseBoolean( configProperties.get( "driver.enableCaching" ) ) );
        dC.setEmbeddedServer( Boolean.parseBoolean( configProperties.get( "driver.embeddedServer" ) ) );
        dC.setSecureCloud( Boolean.parseBoolean( configProperties.get( "security.secureCloud" ) ) );
        dC.setStepTags( configProperties.get( "driver.stepTags" ) );
        dC.setDeviceTags( configProperties.get( "driver.deviceTags" ) );

        for ( Object key : configProperties.keySet() )
            dC.getPropertyMap().put( (String)key, configProperties.get( (String)key ) );
        
        
        //
        // Extract any named tests
        //
        dC.setDeviceInterrupts( configProperties.get( "driver.deviceInterrupts" ) );
        dC.setTestNames( configProperties.get( "driver.testNames" ) );
        dC.setTestTags( configProperties.get( "driver.tagNames" ) );
        dC.setDryRun( Boolean.parseBoolean( configProperties.get( "driver.validateConfiguration" ) ) );
        dC.setSuiteName( configProperties.get( "driver.suiteName" ) );
        dC.setDriverType( DriverType.valueOf( configProperties.get( DEVICE[1] ) ) );
        return dC;
    }
    
    @Override
    protected boolean _executeTest( SuiteContainer sC ) throws Exception
    {
        switch ( configProperties.get( DRIVER[0] ).toUpperCase() )
        {
            case "XML":
            {
                runTest( configProperties.get( ARTIFACT[0] ), XMLTestDriver.class, sC );
                break;
            }
            
            case "OBJ":
            case "OBJ-SQL":
            {
                runTest( configProperties.get( ARTIFACT[0] ), Class.forName( configProperties.get( DRIVER[1] ) ), sC );
                break;
            }
        }
        return true;
    }


    protected static boolean validateProperties( Map<String,String> configProperties, String[] propertyNames )
    {
        for ( String name : propertyNames )
        {
            String value = configProperties.get( name );

            if ( value == null || value.isEmpty() )
            {
                System.err.println( "******* Property [" + name + "] was not specified" );
                (new Exception()).printStackTrace();
                System.exit( -1 );
            }
        }

        return true;
    }

    /**
     * configure proxy settings from driver config file
     * @return
     */
    protected boolean configureProxy()
    {
    	if ( configProperties.get(PROXY_SETTINGS[0]) != null 
    			&& !configProperties.get(PROXY_SETTINGS[0]).isEmpty() 
    			&& Integer.parseInt( configProperties.get(PROXY_SETTINGS[1]) ) > 0 )
        {
    		log.info( "Proxy configured as " + configProperties.get(PROXY_SETTINGS[0]) + ":" + configProperties.get(PROXY_SETTINGS[1]) );
    		ProxyRegistry.instance().setProxyHost(configProperties.get(PROXY_SETTINGS[0]));
            ProxyRegistry.instance().setProxyPort(configProperties.get(PROXY_SETTINGS[1]));
            ProxyRegistry.instance().setIgnoreHost(configProperties.get(PROXY_SETTINGS[2]));
        }
        return true;
    }    

}
