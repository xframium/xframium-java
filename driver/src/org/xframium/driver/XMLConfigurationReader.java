package org.xframium.driver;

import gherkin.parser.Parser;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.xframium.Initializable;
import org.xframium.application.*;
import org.xframium.artifact.ArtifactType;
import org.xframium.content.ContentManager;
import org.xframium.content.provider.ExcelContentProvider;
import org.xframium.content.provider.SQLContentProvider;
import org.xframium.content.provider.XMLContentProvider;
import org.xframium.debugger.DebugManager;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.SimpleDevice;
import org.xframium.device.cloud.*;
import org.xframium.device.data.*;
import org.xframium.device.data.DataProvider.DriverType;
import org.xframium.device.data.perfectoMobile.AvailableHandsetValidator;
import org.xframium.device.data.perfectoMobile.PerfectoMobileDataProvider;
import org.xframium.device.data.perfectoMobile.PerfectoMobilePluginProvider;
import org.xframium.device.data.perfectoMobile.ReservedHandsetValidator;
import org.xframium.device.logging.ThreadedFileHandler;
import org.xframium.device.ng.AbstractSeleniumTest;
import org.xframium.device.property.PropertyAdapter;
import org.xframium.device.proxy.ProxyRegistry;
import org.xframium.driver.container.ApplicationContainer;
import org.xframium.driver.container.CloudContainer;
import org.xframium.driver.container.DeviceContainer;
import org.xframium.driver.container.DriverContainer;
import org.xframium.driver.xsd.*;
import org.xframium.page.*;
import org.xframium.page.data.PageData;
import org.xframium.page.data.provider.ExcelPageDataProvider;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.data.provider.SQLPageDataProvider;
import org.xframium.page.data.provider.XMLPageDataProvider;
import org.xframium.page.element.Element;
import org.xframium.page.element.ElementFactory;
import org.xframium.page.element.provider.*;
import org.xframium.page.keyWord.*;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.KeyWordStep.ValidationType;
import org.xframium.page.keyWord.KeyWordToken.TokenType;
import org.xframium.page.keyWord.gherkinExtension.XMLFormatter;
import org.xframium.page.keyWord.matrixExtension.MatrixTest;
import org.xframium.page.keyWord.provider.ExcelKeyWordProvider;
import org.xframium.page.keyWord.provider.SQLKeyWordProvider;
import org.xframium.page.keyWord.provider.SuiteContainer;
import org.xframium.page.keyWord.provider.XMLKeyWordProvider;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.spi.Device;
import org.xframium.utility.SeleniumSessionManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.Level;

public class XMLConfigurationReader extends AbstractConfigurationReader implements ElementProvider
{
	private static final String[] PROXY_SETTINGS = new String[] { "proxy.host", "proxy.port", "proxy.ignoreHost" };
    private static final String[] JDBC = new String[] { "jdbc.username", "jdbc.password", "jdbc.url", "jdbc.driverClassName" };
    private static final String[] OPT_CLOUD = new String[] { "cloudRegistry.query" };
    private static final String[] OPT_APP = new String[] { "applicationRegistry.query", "applicationRegistry.capQuery" };
    private static final String[] OPT_PAGE = new String[] { "pageManagement.query" };
    private static final String[] OPT_DATA = new String[] { "pageManagement.pageData.query" };
    private static final String[] OPT_CONTENT = new String[] { "pageManagement.content.query" };
    private static final String[] OPT_DEVICE = new String[] { "deviceManagement.device.query", "deviceManagement.capability.query" };
    private static final String[] OPT_DRIVER = new String[] { "driver.suiteName", "driver.modelQuery", "driver.testSuiteQuery", "driver.testCaseQuery" };
    private XFramiumRoot xRoot;
    private Map<String,String> configProperties = new HashMap<String,String>( 10 );
    
    private Map<String,Element> elementMap = new HashMap<String,Element>(20);
    private Map<String,PageContainer> elementTree = new HashMap<String,PageContainer>(20);
    
    private static XPathFactory xPathFactory = XPathFactory.newInstance();
    

    
    private boolean pageInitialized = false;
    
    @Override
    public boolean isInitialized()
    {
        return pageInitialized;
    }
    
    
    
    @Override
    public boolean readFile( InputStream inputStream )
    {
        try
        {
            
            JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>) u.unmarshal( inputStream );

            xRoot = (XFramiumRoot) rootElement.getValue();
            
            for ( XProperty currentProp : xRoot.getDriver().getProperty() )
            {
                configProperties.put( currentProp.getName(), currentProp.getValue() );
            }
            return true;
        }
        catch ( Exception e )
        {
            log.fatal( "Error reading CSV Element File", e );
            return false;
        }
    }
    
    @Override
    public boolean readFile( File configFile )
    {
        try
        {
            configFolder = configFile.getParentFile();
            return readFile( new FileInputStream( configFile ) );
        }
        catch ( Exception e )
        {
            log.fatal( "Error reading configuration File", e );
            return false;
        }
       
    }

    @Override
    public CloudContainer configureCloud()
    {
        CloudContainer cC = new CloudContainer();
        switch ( xRoot.getCloud().getProvider() )
        {
            case "XML":
                
                cC.setCloudList( new XMLCloudProvider( findFile( configFolder, new File( xRoot.getCloud().getFileName() ) ) ).readData() );
                break;

            case "SQL":
                cC.setCloudList( new SQLCloudProvider( configProperties.get( JDBC[0] ),
                                                                                 configProperties.get( JDBC[1] ),
                                                                                 configProperties.get( JDBC[2] ),
                                                                                 configProperties.get( JDBC[3] ),
                                                                                 configProperties.get( OPT_CLOUD[0] )).readData());
                break;

            case "CSV":
                cC.setCloudList( new CSVCloudProvider( findFile( configFolder, new File( xRoot.getCloud().getFileName() ) ) ).readData() );
                break;

            case "EXCEL":
                cC.setCloudList( new ExcelCloudProvider( findFile( configFolder, new File( xRoot.getCloud().getFileName() ) ), configProperties.get( "cloudRegistry.tabName" ) ).readData() );
                break;
                
            case "LOCAL":
                CloudDescriptor cloud = new CloudDescriptor( xRoot.getCloud().getName(), xRoot.getCloud().getUserName(), xRoot.getCloud().getPassword(), xRoot.getCloud().getHostName(), xRoot.getCloud().getProxyHost(), xRoot.getCloud().getProxyPort().intValue() + "", "", xRoot.getCloud().getGrid(), xRoot.getCloud().getProviderType(), xRoot.getCloud().getGesture(), xRoot.getCloud().getDeviceAction() );
                cC.setCloudList( new ArrayList<CloudDescriptor>( 10 ) );
                cC.getCloudList().add( cloud );
                break;
        }

        cC.setCloudName( xRoot.getCloud().getName() );
        
        return cC;
    }

    @Override
    public ApplicationContainer configureApplication()
    {
        ApplicationContainer appContainer = new ApplicationContainer();
        
        switch ( xRoot.getApplication().getProvider() )
        {
            case "XML":
                appContainer.setAppList( new XMLApplicationProvider( findFile( configFolder, new File( xRoot.getApplication().getFileName() ) ) ).readData() );
                break;

            case "CSV":
                appContainer.setAppList( new CSVApplicationProvider( findFile( configFolder, new File( xRoot.getApplication().getFileName() ) ) ).readData() );
                break;
                
            case "SQL":
                appContainer.setAppList( new SQLApplicationProvider( configProperties.get( JDBC[0] ),
                                                                                                   configProperties.get( JDBC[1] ),
                                                                                                   configProperties.get( JDBC[2] ),
                                                                                                   configProperties.get( JDBC[3] ),
                                                                                                   configProperties.get( OPT_APP[0] ),
                                                                                                   configProperties.get( OPT_APP[1] )).readData());
                break;

            case "EXCEL":
                appContainer.setAppList( new ExcelApplicationProvider( findFile( configFolder, new File( xRoot.getApplication().getFileName() ) ), configProperties.get( "applicationRegistry.tabName" ) ).readData() );
                break;
                
            case "LOCAL":
                appContainer.getAppList().add( new ApplicationDescriptor( xRoot.getApplication().getName(), "", xRoot.getApplication().getAppPackage(), xRoot.getApplication().getBundleId(), xRoot.getApplication().getUrl(), xRoot.getApplication().getIosInstall(), xRoot.getApplication().getAndroidInstall(), createCapabilities( xRoot.getApplication().getCapability() ) ) );
                break;
        }
        appContainer.setApplicationName( xRoot.getApplication().getName() );
        return appContainer;
    }

    private Map<String,Object> createCapabilities( List<XDeviceCapability> capabilityList )
    {
        Map<String,Object> capabilities = new HashMap<String,Object>( 5 );
        if ( capabilityList != null )
        {
            for ( XDeviceCapability cap : capabilityList )
            {
                switch ( cap.getClazz() )
                {
                    case "BOOLEAN":
                        capabilities.put( cap.getName(), Boolean.parseBoolean( cap.getValue() ) );
                        break;
    
                    case "OBJECT":
                        capabilities.put( cap.getName(), cap.getValue() );
                        break;
    
                    case "STRING":
                        capabilities.put( cap.getName(), cap.getValue() );
                        break;
    
                    case "PLATFORM":
                        capabilities.put( cap.getName(), Platform.valueOf( cap.getValue().toUpperCase() ) );
                        break;
                }
            }
        }
        
        return capabilities;
    }
    
    @Override
    protected boolean configureThirdParty()
    {
        for ( XLibrary lib : xRoot.getDriver().getLibrary() )
        {;
            try
            {
                log.info( "Configuring Third Party support for " + lib.getName() + " as " + lib.getClassName() );

                Initializable newExtension = (Initializable) Class.forName( lib.getClassName() ).newInstance();
                Properties props = new Properties();
                props.putAll( configProperties );
                newExtension.initialize( lib.getName(), props );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        
        return true;
    }

    @Override
    protected boolean configureArtifacts()
    {
        DataManager.instance().setReportFolder( new File( configFolder, xRoot.getDriver().getOutputFolder() ) );
        PageManager.instance().setStoreImages( true );
        PageManager.instance().setImageLocation( new File( configFolder, xRoot.getDriver().getOutputFolder() ).getAbsolutePath() );
        
        ThreadedFileHandler threadedHandler = new ThreadedFileHandler();
        threadedHandler.configureHandler( Level.INFO );
        
        List<ArtifactType> artifactList = new ArrayList<ArtifactType>( 10 );
        artifactList.add( ArtifactType.EXECUTION_DEFINITION );
        
        
        boolean debuggerEnabled = false;
        for( XArtifact artifact : xRoot.getDriver().getArtifact() )
        {
            artifactList.add( ArtifactType.valueOf( artifact.getType() ) );
            if ( artifact.getType().equals( "FAILURE_SOURCE" ) )
            	artifactList.add( ArtifactType.FAILURE_SOURCE_HTML );
            else if ( artifact.getType().equals( "DEBUGGER" ) )
            {
                try
                {
                    debuggerEnabled = true;
                    DebugManager.instance().startUp( InetAddress.getLocalHost().getHostAddress(), 8870 );
                    KeyWordDriver.instance().addStepListener( DebugManager.instance() );
                }
                catch( Exception e )
                {
                    e.printStackTrace();
                }
            }
                
        }
        
        if ( System.getProperty( "X_DEBUGGER" ) != null && System.getProperty( "X_DEBUGGER" ).equals( "true" ) && !debuggerEnabled )
        {
            try
            {
                debuggerEnabled = true;
                artifactList.add( ArtifactType.DEBUGGER );
                DebugManager.instance().startUp( InetAddress.getLocalHost().getHostAddress(), 8870 );
                KeyWordDriver.instance().addStepListener( DebugManager.instance() );
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
        
        DataManager.instance().setAutomaticDownloads( artifactList.toArray( new ArtifactType[0] ) );
        return true;
    }

    @Override
    public ElementProvider configurePageManagement( SuiteContainer sC )
    {
        sC.setSiteName( xRoot.getModel().getSiteName() );

        switch ( xRoot.getModel().getProvider() )
        {
            case "XML":

                return new XMLElementProvider( findFile( configFolder, new File( xRoot.getModel().getFileName() ) ) );

            case "SQL":
                return new SQLElementProvider( configProperties.get( JDBC[0] ),
                                                                                   configProperties.get( JDBC[1] ),
                                                                                   configProperties.get( JDBC[2] ),
                                                                                   configProperties.get( JDBC[3] ),
                                                                                   configProperties.get( OPT_PAGE[0] ));

            case "CSV":
                return new CSVElementProvider( findFile( configFolder, new File( xRoot.getModel().getFileName() ) ) );

            case "EXCEL":
                String[] fileNames = xRoot.getModel().getFileName().split( "," );

                File[] files = new File[fileNames.length];
                for ( int i = 0; i < fileNames.length; i++ )
                    files[i] = findFile( configFolder, new File( fileNames[i] ) );

                return new ExcelElementProvider( files, xRoot.getModel().getSiteName() );
                
            case "LOCAL":
                ElementDescriptor elementDescriptor = null;
                Element currentElement = null;
                boolean elementsRead = true;
                for ( XPage page : xRoot.getModel().getPage() )
                {
                    for ( XElement ele : page.getElement() )
                    {
                        elementDescriptor = new ElementDescriptor( xRoot.getModel().getSiteName(), page.getName(), ele.getName() );
                        currentElement = ElementFactory.instance().createElement( BY.valueOf( ele.getDescriptor() ), ele.getValue(), ele.getName(), page.getName(), ele.getContextName() );
                        
                        if (log.isDebugEnabled())
                            log.debug( "Adding XML Element using [" + elementDescriptor.toString() + "] as [" + currentElement + "]" );
                        boolean elementRead = true;
                        try
                        {
                            if ( currentElement.getBy() == BY.XPATH )
                                xPathFactory.newXPath().compile( currentElement.getKey().replace( "{", "" ).replace( "}", "" ) );
                            
                            elementMap.put(elementDescriptor.toString(), currentElement );

                            PageContainer eltList = elementTree.get( elementDescriptor.getPageName() );
                            if ( eltList == null )
                            {
                                Class className = KeyWordDriver.instance().getPage( elementDescriptor.getPageName() );
                                eltList = new PageContainer( elementDescriptor.getPageName(), className != null ? className.getName() : "" );
                                elementTree.put( elementDescriptor.getPageName(), eltList );
                            }
                            eltList.getElementList().add( currentElement );
                        }
                        catch( Exception e )
                        {
                            log.fatal( "Could not process page element identified by [" + elementDescriptor.toString() + "] as [" + currentElement.getKey() + "]" );
                            elementRead = false;
                        }
                        
                        elementsRead = elementsRead & elementRead;
                    }
                }
                
                pageInitialized = elementsRead;

               return this;
        }

        return null;
    }
    
    @Override
    public PageDataProvider configureData()
    {
        if ( xRoot.getData() == null )
            return null;
        switch ( xRoot.getData().getProvider() )
        {
            case "XML":
                return new XMLPageDataProvider( findFile( configFolder, new File( xRoot.getData().getFileName() ) ) );

            case "SQL":
                return new SQLPageDataProvider( configProperties.get( JDBC[0] ),
                                                             configProperties.get( JDBC[1] ),
                                                             configProperties.get( JDBC[2] ),
                                                             configProperties.get( JDBC[3] ),
                                                             configProperties.get( OPT_DATA[0] ));

            case "EXCEL":
                String[] fileNames = xRoot.getData().getFileName().split( "," );

                File[] files = new File[fileNames.length];
                for ( int i = 0; i < fileNames.length; i++ )
                    files[i] = findFile( configFolder, new File( fileNames[i] ) );
                
                return new ExcelPageDataProvider( files, configProperties.get( "pageManagement.pageData.tabNames" ) );

        }
        return null;
    }

    @Override
    protected boolean configureContent()
    {
        if ( xRoot.getContent() == null )
            return true;
        switch ( xRoot.getContent().getProvider() )
        {
            case "XML":
                ContentManager.instance().setContentProvider( new XMLContentProvider( findFile( configFolder, new File( xRoot.getContent().getFileName() ) ) ) );
                break;

            case "SQL":
                ContentManager.instance().setContentProvider( new SQLContentProvider( configProperties.get( JDBC[0] ),
                                                                                      configProperties.get( JDBC[1] ),
                                                                                      configProperties.get( JDBC[2] ),
                                                                                      configProperties.get( JDBC[3] ),
                                                                                      configProperties.get( OPT_CONTENT[0] )));
                break;

            case "EXCEL":

                int keyColumn = Integer.parseInt( configProperties.get( "pageManagement.content.keyColumn" ) );
                String[] lookupString = configProperties.get( "pageManagement.content.lookupColumns" ).split( "," );

                int[] lookupColumns = new int[lookupString.length];
                for ( int i = 0; i < lookupString.length; i++ )
                    lookupColumns[i] = Integer.parseInt( lookupString[i].trim() );

                ContentManager.instance().setContentProvider( new ExcelContentProvider( findFile( configFolder, new File( xRoot.getContent().getFileName() ) ), configProperties.get( "pageManagement.content.tabName" ), keyColumn, lookupColumns ) );
                break;

        }
        return true;
    }

    @Override
    public DeviceContainer configureDevice()
    {
        DeviceContainer dC = new DeviceContainer();
        List<Device> deviceList = null;
        switch ( xRoot.getDevices().getProvider() )
        {
            case "PERFECTO_PLUGIN":
                deviceList = new PerfectoMobilePluginProvider( configProperties.get( "deviceManagement.deviceList" ) + "", DriverType.valueOf( xRoot.getDriver().getType() ), configProperties.get( "deviceManagement.pluginType" ) ).readData();
                break;
            
            case "RESERVED":
                deviceList = new PerfectoMobileDataProvider( new ReservedHandsetValidator(), DriverType.valueOf( xRoot.getDriver().getType() ) ).readData();
                break;

            case "AVAILABLE":
                deviceList = new PerfectoMobileDataProvider( new AvailableHandsetValidator(), DriverType.valueOf( xRoot.getDriver().getType() ) ).readData();
                break;

            case "CSV":
                deviceList = new CSVDataProvider( findFile( configFolder, new File( xRoot.getDevices().getFileName() ) ), DriverType.valueOf( xRoot.getDriver().getType() ) ).readData();
                break;

            case "XML":

                deviceList =  new XMLDataProvider( findFile( configFolder, new File( xRoot.getDevices().getFileName() ) ), DriverType.valueOf( xRoot.getDriver().getType() ) ).readData();
                break;

            case "SQL":
                deviceList = new SQLDataProvider( configProperties.get( JDBC[0] ),
                                                                      configProperties.get( JDBC[1] ),
                                                                      configProperties.get( JDBC[2] ),
                                                                      configProperties.get( JDBC[3] ),
                                                                      configProperties.get( OPT_DEVICE[0] ),
                                                                      configProperties.get( OPT_DEVICE[1] ),
                                                                      DriverType.valueOf( xRoot.getDriver().getType())).readData();
                break;

            case "EXCEL":
                deviceList = new ExcelDataProvider( findFile( configFolder, new File( xRoot.getDevices().getFileName() ) ), configProperties.get( "deviceManagement.tabName" ), DriverType.valueOf( xRoot.getDriver().getType() ) ).readData();
                break;

            case "NAMED":
                String devices = configProperties.get( "deviceManagement.deviceList" );
                if ( devices == null )
                {
                    System.err.println( "******* Property [deviceManagement.deviceList] was not specified" );
                    System.exit( -1 );
                }
                deviceList = new NamedDataProvider( devices, DriverType.valueOf( xRoot.getDriver().getType() ) ).readData();
                break;
                
            case "LOCAL":
                deviceList = new ArrayList<Device>( 10 );
                for ( XDevice device : xRoot.getDevices().getDevice() )
                {
                    deviceList.add( parseDevice( device ) );
                }
                
        }

        dC.setdType( DriverType.valueOf( xRoot.getDriver().getType() ) );
        
        if ( deviceList != null )
        {
            for ( Device d : deviceList )
                dC.addDevice( d );
        }
        
        return dC;

    }
    
    private Device parseDevice( XDevice device )
    {
        String driverName = "";
        switch( xRoot.getDriver().getType() )
        {
            case "APPIUM":
                if ( device.getOs().toUpperCase().equals( "IOS" ) )
                    driverName = "IOS";
                else if ( device.getOs().toUpperCase().equals( "ANDROID" ) )
                    driverName = "ANDROID";
                else
                    log.warn( "Appium is not supported on the following OS " + device.getOs().toUpperCase() + " - this device will be ignored" );
                break;
                
            case "PERFECTO":
                driverName = "PERFECTO";
                break;
                
            case "WEB":
                driverName = "WEB";
                break;
        }
        
        SimpleDevice currentDevice = new SimpleDevice(device.getName(), device.getManufacturer(), device.getModel(), device.getOs(), device.getOsVersion(), device.getBrowserName(), device.getBrowserVersion(), device.getAvailableDevices().intValue(), driverName, device.isActive(), device.getId() );
        if ( device.getCloud() != null && !device.getCloud().isEmpty() )
            currentDevice.setCloud( device.getCloud() );
        
        List<Object> list = null;
        String factoryName = null;
        Map<String, Object> keyOptions = null;
        Map<String, Object> browserOptionMap = null;
        
        if ( device.getCapability() != null )
        {
            for ( XDeviceCapability cap : device.getCapability() )
            {
                switch( cap.getClazz() )
                {
                    case "BOOLEAN":
                        currentDevice.addCapability( cap.getName(), Boolean.parseBoolean( cap.getValue() ) );
                        break;
                        
                    case "STRING":
                        currentDevice.addCapability( cap.getName(), cap.getValue() );
                        break;
                        
                    case "PLATFORM":
                        currentDevice.addCapability( cap.getName(), Platform.valueOf( cap.getValue().toUpperCase() ) );
                        break;
                }
            }
            
            // Parse the Object Capability element for browser options
            if ( device.getObjectCapability() != null )
            {

                for ( XObjectDeviceCapability cap : device.getObjectCapability() )
                {

                    browserOptionMap = new HashMap<String, Object>();

                    if ( cap.getCapabilities() != null )
                    {

                        for ( XCapabilities capabilities : cap.getCapabilities() )
                        {

                            factoryName = capabilities.getFactoryName();

                            if ( capabilities.getOptions() != null )
                            {

                                for ( XOptions option : capabilities.getOptions() )
                                {

                                    if ( option.getKey() == null )
                                    {

                                        if ( browserOptionMap.get( option.getName() ) == null )
                                        {
                                            list = new ArrayList<Object>();

                                        }
                                        else
                                        {
                                            list = (List<Object>) browserOptionMap.get( option.getName() );
                                        }
                                        browserOptionMap.put( option.getName(), list );
                                        list.add( option.getValue() );

                                    }
                                    else
                                    {

                                        if ( browserOptionMap.get( option.getName() ) == null )
                                        {
                                            keyOptions = new HashMap<String, Object>();

                                        }
                                        else
                                        {
                                            keyOptions = (HashMap<String, Object>) browserOptionMap.get( option.getName() );
                                        }
                                        keyOptions.put( option.getKey(), option.getValue() );
                                        browserOptionMap.put( option.getName(), keyOptions );
                                    }
                                    currentDevice.addCapability( factoryName, browserOptionMap );
                                }
                            }
                        }
                    }
                }
            }
            
        }

        return currentDevice;
        
    }

    @Override
    protected boolean configurePropertyAdapters()
    {
        for ( XPropertyAdapter xProp : xRoot.getDriver().getPropertyAdapter() )
        {
            try
            {
                DeviceManager.instance().registerPropertyAdapter( (PropertyAdapter) Class.forName( xProp.getClassName() ).newInstance() );
            }
            catch( Exception e )
            {
                log.error( "Error creating proeprty adapter", e );
            }
        }
        Properties props = new Properties();
        props.putAll( configProperties );
        DeviceManager.instance().setConfigurationProperties( props );
        DeviceManager.instance().notifyPropertyAdapter( props );
        return true;
    }

    @Override
    public SuiteContainer configureTestCases( PageDataProvider pdp, boolean parseDataIterators )
    {
        SuiteContainer sC = null;
        switch ( xRoot.getSuite().getProvider() )
        {
            case "LOCAL":
                sC = new SuiteContainer(); 
                parseModel( sC, xRoot.getModel() );
                for( XTest test : xRoot.getSuite().getTest() )
                {
                    if ( sC.testExists( test.getName() ) )
                    {
                        log.warn( "The test [" + test.getName() + "] is already defined and will not be added again" );
                        continue;
                    }
                    
                    if ( test.getType().equals( "BDD" ) )
                    {
                        XMLFormatter xmlFormatter = new XMLFormatter( sC.getDataProvider() );
                        Parser bddParser = new Parser( xmlFormatter );
                        bddParser.parse( test.getDescription().getValue(), "", 0 );
                        sC.setDataProvider( xmlFormatter );
                    }
                    else if ( test.getType().equals( "CSV" ) )
                    {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append( test.getName() );
                        stringBuilder.append( ",Test,," );
                        stringBuilder.append( test.getDataProvider()!= null ? test.getDataProvider() : "" );
                        stringBuilder.append( "," );
                        stringBuilder.append( test.getDataDriver()!= null ? test.getDataDriver() : "" );
                        stringBuilder.append( "," );
                        stringBuilder.append( test.getTagNames() != null ? test.getTagNames() : "" );
                        stringBuilder.append( ",," );
                        stringBuilder.append( test.isTimed() );
                        stringBuilder.append( ",0," );
                        stringBuilder.append( test.isActive() );
                        stringBuilder.append( "," );
                        stringBuilder.append( test.getOs()!= null ? test.getOs() : "" );
                        
                        MatrixTest matrixTest = new MatrixTest( stringBuilder.toString(), test.getDescription().getValue() );
                        if ( matrixTest.isActive() )
                            sC.addActiveTest( matrixTest.createTest() );
                        else
                            sC.addInactiveTest( matrixTest.createTest() );
                    }
                    else if ( test.getType().equals( "XML" ) )
                    {
                        KeyWordTest currentTest = parseTest( test, "test" );
                        
                        if ( currentTest.getDataDriver() != null && !currentTest.getDataDriver().isEmpty() && parseDataIterators )
                        {
                            PageData[] pageData = pdp.getRecords( currentTest.getDataDriver() );
                            if (pageData == null)
                            {
                                log.warn( "Specified Data Driver [" + currentTest.getDataDriver() + "] could not be located. Make sure it exists and it was populated prior to initializing your keyword factory" );
                                if ( currentTest.isActive() )
                                    sC.addActiveTest( currentTest );
                                else
                                    sC.addInactiveTest( currentTest );
                            }
                            else
                            {
                                String testName = currentTest.getName();
    
                                for (PageData record : pageData)
                                {
                                    if ( currentTest.isActive() )
                                        sC.addActiveTest( currentTest.copyTest( testName + "!" + record.getName() ) );
                                    else
                                        sC.addInactiveTest( currentTest.copyTest( testName + "!" + record.getName() ) );
                                }
                            }
                        }
                        else
                        {
                            if ( currentTest.isActive() )
                                sC.addActiveTest( currentTest );
                            else
                                sC.addInactiveTest( currentTest );
                        }
                    }
                }
                
                for( XTest test : xRoot.getSuite().getFunction() )
                {
                    if ( sC.testExists( test.getName() ) )
                    {
                        log.warn( "The function [" + test.getName() + "] is already defined and will not be added again" );
                        continue;
                    }
                    
                    sC.addFunction( parseTest( test, "function" ) );
                }
                break;
                
                
            
            case "XML":
                sC = new XMLKeyWordProvider( findFile( configFolder, new File( xRoot.getSuite().getFileName() ) ) ).readData( true );

                break;
                
            case "EXCEL":
                sC =  new ExcelKeyWordProvider( findFile( configFolder, new File( xRoot.getSuite().getFileName() ) ) ).readData( true );

                break;

            case "SQL":
            case "LOCAL-SQL":
            {
                sC = new SQLKeyWordProvider( configProperties.get( JDBC[0] ),
                                                                            configProperties.get( JDBC[1] ),
                                                                            configProperties.get( JDBC[2] ),
                                                                            configProperties.get( JDBC[3] ),
                                                                            configProperties.get( OPT_DRIVER[0] ),
                                                                            xRoot.getModel().getSiteName(),
                                                                            configProperties.get( OPT_DRIVER[1] ),
                                                                            configProperties.get( OPT_DRIVER[2] ),
                                                                            configProperties.get( OPT_DRIVER[3] )
                                                                            ).readData( true ) ;
                                                   
                break;
            }
        }
        
        if ( sC != null )
            sC.setDataProvider( pdp );
        return sC;
    }
    
    @Override
    public DriverContainer configureDriver()
    {
        DriverContainer dC = new DriverContainer();
        

        String personaNames = xRoot.getDriver().getPersonas();
        if ( personaNames != null && !personaNames.isEmpty() )
            dC.setPerfectoPersonas( personaNames );
        
        dC.setDisplayReport( xRoot.getDriver().isDisplayResults() );
        dC.setSmartCaching( xRoot.getDriver().isCachingEnabled() );

        dC.setStepTags( xRoot.getDriver().getStepTags() );

        dC.getPropertyMap().putAll( configProperties );


        //
        // Extract any named tests
        //
        dC.setTestNames( xRoot.getDriver().getTestNames() );
        dC.setTestTags( xRoot.getDriver().getTagNames() );
        dC.setDryRun( xRoot.getDriver().isDryRun() );

        return dC;
    }
    
    private void parseModel( SuiteContainer sC, XModel model )
    {
        for ( XPage page : model.getPage() )
        {
            try
            {
                Class useClass = KeyWordPage.class;
                if ( page.getClazz() != null && !page.getClazz().isEmpty() )
                    useClass = ( Class<Page> ) Class.forName( page.getClazz() );
                
                if (log.isDebugEnabled())
                    log.debug( "Creating page as " + useClass.getSimpleName() + " for " + page.getName() );
    
                sC.addPageModel( page.getName(), useClass );
            }
            catch( Exception e )
            {
                log.error( "Error creating instance of [" + page.getClazz() + "]" );
            }
        }
    }

    /**
     * Parses the test.
     *
     * @param xTest the x test
     * @param typeName the type name
     * @return the key word test
     */
    private KeyWordTest parseTest( XTest xTest, String typeName )
    { 
        KeyWordTest test = new KeyWordTest( xTest.getName(), xTest.isActive(), xTest.getDataProvider(), xTest.getDataDriver(), xTest.isTimed(), xTest.getLinkId(), xTest.getOs(), xTest.getThreshold().intValue(), xTest.getDescription() != null ? xTest.getDescription().getValue() : null, xTest.getTagNames(), xTest.getContentKeys() );
        
        KeyWordStep[] steps = parseSteps( xTest.getStep(), xTest.getName(), typeName );

        for (KeyWordStep step : steps)
            test.addStep( step );

        return test;
    }

    /**
     * Parses the steps.
     *
     * @param xSteps the x steps
     * @param testName the test name
     * @param typeName the type name
     * @return the key word step[]
     */
    private KeyWordStep[] parseSteps( List<XStep> xSteps, String testName, String typeName )
    {

        if (log.isDebugEnabled())
            log.debug( "Extracted " + xSteps.size() + " Steps" );

        List<KeyWordStep> stepList = new ArrayList<KeyWordStep>( 10 );

        for ( XStep xStep : xSteps )
        {
            KeyWordStep step = KeyWordStepFactory.instance().createStep( xStep.getName(), xStep.getPage(), xStep.isActive(), xStep.getType(),
                                                                                 xStep.getLinkId(), xStep.isTimed(), StepFailure.valueOf( xStep.getFailureMode() ), xStep.isInverse(),
                                                                                 xStep.getOs(), xStep.getPoi(), xStep.getThreshold().intValue(), "", xStep.getWait().intValue(),
                                                                                 xStep.getContext(), xStep.getValidation(), xStep.getDevice(),
                                                                                 (xStep.getValidationType() != null && !xStep.getValidationType().isEmpty() ) ? ValidationType.valueOf( xStep.getValidationType() ) : null, xStep.getTagNames(), xStep.isStartAt(), xStep.isBreakpoint() );
            
            parseParameters( xStep.getParameter(), testName, xStep.getName(), typeName, step );
            parseTokens( xStep.getToken(), testName, xStep.getName(), typeName, step );
            
            step.addAllSteps( parseSteps( xStep.getStep(), testName, typeName ) );
            stepList.add( step );
        }

        return stepList.toArray( new KeyWordStep[0] );
    }

    /**
     * Parses the parameters.
     *
     * @param pList the list
     * @param testName the test name
     * @param stepName the step name
     * @param typeName the type name
     * @param parentStep the parent step
     * @return the key word parameter[]
     */
    private void parseParameters( List<XParameter> pList, String testName, String stepName, String typeName, KeyWordStep parentStep )
    {
        if (log.isDebugEnabled())
            log.debug( "Extracted " + pList.size() + " Parameters" );
        

        for ( XParameter p : pList )
        {
            KeyWordParameter kp = new KeyWordParameter( ParameterType.valueOf( p.getType() ), p.getValue(), null, null );
            
            if ( p.getToken() != null && !p.getToken().isEmpty() )
            {
                for ( XToken t : p.getToken() )
                {
                    kp.addToken( new KeyWordToken( TokenType.valueOf(t.getType() ), t.getValue(), t.getName() ) );
                }
            }
            
            if ( p.equals( ParameterType.FILE ) )
            {
                File dataFile = new File( p.getValue() );
                if ( dataFile.isFile() )
                {
                    try
                    {
                        kp.setValue( readIFile( new FileInputStream( dataFile ) ) );
                        kp.setFileName( dataFile.getAbsolutePath() );
                    }
                    catch( FileNotFoundException e )
                    {
                        log.error( "Error reading parameter file", e );
                    }
                }
                else
                {
                    dataFile = new File( configFolder, p.getValue() );
                    if ( dataFile.isFile() )
                    {
                        try
                        {
                            kp.setValue( readIFile( new FileInputStream( dataFile ) ) );
                            kp.setFileName( dataFile.getAbsolutePath() );
                        }
                        catch( FileNotFoundException e )
                        {
                            log.error( "Error reading parameter file", e );
                        }
                    }
                }  
            }
            
            parentStep.addParameter( kp );
        }
    }
    
    private String readIFile( InputStream inputStream )
    {
        
        try
        {
            StringBuilder stringBuilder = new StringBuilder();
            int bytesRead = 0;
            byte[] buffer = new byte[512];

            while ( ( bytesRead = inputStream.read( buffer ) ) > 0 )
            {
                stringBuilder.append( new String( buffer, 0, bytesRead ) );
            }
            
            return stringBuilder.toString();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            try { inputStream.close(); } catch( Exception e ) {}
        }
        
        return null;
    }

    /**
     * Parses the tokens.
     *
     * @param tList the t list
     * @param testName the test name
     * @param stepName the step name
     * @param typeName the type name
     * @param parentStep the parent step
     * @return the key word token[]
     */
    private void parseTokens( List<XToken> tList, String testName, String stepName, String typeName, KeyWordStep parentStep )
    {
        if (log.isDebugEnabled())
            log.debug( "Extracted " + tList + " Tokens" );

        for ( XToken t : tList )
        {
            parentStep.addToken( new KeyWordToken( TokenType.valueOf(t.getType() ), t.getValue(), t.getName() ) );
        }

    }

    

    @Override
    protected boolean _executeTest() throws Exception
    {
        switch ( xRoot.getSuite().getProvider() )
        {
            case "XML":
            case "SQL":
            case "EXCEL": 
            case "LOCAL":
            case "LOCAL-SQL":
            {
                runTest( xRoot.getDriver().getOutputFolder(), XMLTestDriver.class );
                break;
            }

        }
        return true;
    }

    @Override
    public Element getElement( ElementDescriptor elementDescriptor )
    {
        return elementMap.get(  elementDescriptor.toString() );
    }

    @Override
    public Map<String,PageContainer> getElementTree()
    {
        return elementTree;
    }
    
    /**
     * Configure the proxy settings from the driver config file
     * @return
     */
    protected boolean configureProxy()
    {
    	if ( xRoot.getProxy() != null && xRoot.getProxy().getProxyHost() != null 
    			&& !xRoot.getProxy().getProxyHost().isEmpty() 
    			&& Integer.parseInt( xRoot.getProxy().getProxyPort() ) > 0 )
        {
            log.info( "Proxy configured as " + xRoot.getProxy().getProxyHost() + ":" + xRoot.getProxy().getProxyPort() );
            ProxyRegistry.instance().setProxyHost(xRoot.getProxy().getProxyHost());
            ProxyRegistry.instance().setProxyPort(xRoot.getProxy().getProxyPort());
            ProxyRegistry.instance().setIgnoreHost(xRoot.getProxy().getProxyIgnoreHost());
        }
        return true;
    }

}
