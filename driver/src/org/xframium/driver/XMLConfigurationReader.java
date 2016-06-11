package org.xframium.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPathFactory;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.xframium.Initializable;
import org.xframium.application.ApplicationDescriptor;
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
import org.xframium.device.SimpleDevice;
import org.xframium.device.cloud.CSVCloudProvider;
import org.xframium.device.cloud.CloudDescriptor;
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
import org.xframium.driver.xsd.ObjectFactory;
import org.xframium.driver.xsd.XArtifact;
import org.xframium.driver.xsd.XDevice;
import org.xframium.driver.xsd.XDeviceCapability;
import org.xframium.driver.xsd.XElement;
import org.xframium.driver.xsd.XFramiumRoot;
import org.xframium.driver.xsd.XLibrary;
import org.xframium.driver.xsd.XModel;
import org.xframium.driver.xsd.XPage;
import org.xframium.driver.xsd.XParameter;
import org.xframium.driver.xsd.XProperty;
import org.xframium.driver.xsd.XPropertyAdapter;
import org.xframium.driver.xsd.XStep;
import org.xframium.driver.xsd.XTest;
import org.xframium.driver.xsd.XToken;
import org.xframium.page.BY;
import org.xframium.page.ElementDescriptor;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.data.provider.ExcelPageDataProvider;
import org.xframium.page.data.provider.SQLPageDataProvider;
import org.xframium.page.data.provider.XMLPageDataProvider;
import org.xframium.page.element.Element;
import org.xframium.page.element.ElementFactory;
import org.xframium.page.element.provider.CSVElementProvider;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.element.provider.ExcelElementProvider;
import org.xframium.page.element.provider.SQLElementProvider;
import org.xframium.page.element.provider.XMLElementProvider;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordPage;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.KeyWordStep.ValidationType;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.KeyWordToken.TokenType;
import org.xframium.page.keyWord.provider.XMLKeyWordProvider;
import org.xframium.page.keyWord.provider.SQLKeyWordProvider;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.spi.Device;
import org.xframium.spi.RunDetails;
import org.xframium.utility.SeleniumSessionManager;

public class XMLConfigurationReader extends AbstractConfigurationReader implements ElementProvider
{
    private static final String[] JDBC = new String[] { "jdbc.username", "jdbc.password", "jdbc.url", "jdbc.driverClassName" };
    private static final String[] OPT_CLOUD = new String[] { "cloudRegistry.query" };
    private static final String[] OPT_APP = new String[] { "applicationRegistry.query", "applicationRegistry.capQuery" };
    private static final String[] OPT_PAGE = new String[] { "pageManagement.query" };
    private static final String[] OPT_DATA = new String[] { "pageManagement.pageData.query" };
    private static final String[] OPT_CONTENT = new String[] { "pageManagement.content.query" };
    private static final String[] OPT_DEVICE = new String[] { "deviceManagement.device.query", "deviceManagement.capability.query" };
    private static final String[] OPT_DRIVER = new String[] { "driver.suite", "driver.suiteQuery", "driver.pageQuery", "driver.importQuery",  "driver.testQuery",
                                                              "driver.stepQuery", "driver.substepQuery", "driver.paramQuery", "driver.tokenQuery",
                                                              "driver.functionQuery" };
    private XFramiumRoot xRoot;
    private Map<String,String> configProperties = new HashMap<String,String>( 10 );
    
    private Map<String,Element> elementMap = new HashMap<String,Element>(20);
    
    private static XPathFactory xPathFactory = XPathFactory.newInstance();

    
    private boolean pageInitialized = false;
    
    @Override
    public boolean isInitialized()
    {
        return pageInitialized;
    }
    
    @Override
    protected boolean readFile( File configFile )
    {
        try
        {
            configFolder = configFile.getParentFile();
            JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>) u.unmarshal( new FileInputStream( configFile ) );

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
    protected boolean configureCloud()
    {
        switch ( xRoot.getCloud().getProvider() )
        {
            case "XML":
                CloudRegistry.instance().setCloudProvider( new XMLCloudProvider( findFile( configFolder, new File( xRoot.getCloud().getFileName() ) ) ) );
                break;

            case "SQL":
                CloudRegistry.instance().setCloudProvider( new SQLCloudProvider( configProperties.get( JDBC[0] ),
                                                                                 configProperties.get( JDBC[1] ),
                                                                                 configProperties.get( JDBC[2] ),
                                                                                 configProperties.get( JDBC[3] ),
                                                                                 configProperties.get( OPT_CLOUD[0] )));
                break;

            case "CSV":
                CloudRegistry.instance().setCloudProvider( new CSVCloudProvider( findFile( configFolder, new File( xRoot.getCloud().getFileName() ) ) ) );
                break;

            case "EXCEL":
                CloudRegistry.instance().setCloudProvider( new ExcelCloudProvider( findFile( configFolder, new File( xRoot.getCloud().getFileName() ) ), configProperties.get( "cloudRegistry.tabName" ) ) );
                break;
                
            case "LOCAL":
                CloudDescriptor cloud = new CloudDescriptor( xRoot.getCloud().getName(), xRoot.getCloud().getUserName(), xRoot.getCloud().getPassword(), xRoot.getCloud().getHostName(), xRoot.getCloud().getProxyHost(), xRoot.getCloud().getProxyPort().intValue() + "", "", xRoot.getCloud().getGrid() );
                CloudRegistry.instance().addCloudDescriptor( cloud );
                break;
        }

        CloudRegistry.instance().setCloud( xRoot.getCloud().getName() );
        
        return true;
    }

    @Override
    protected boolean configureApplication()
    {
        
        switch ( xRoot.getApplication().getProvider() )
        {
            case "XML":
                ApplicationRegistry.instance().setApplicationProvider( new XMLApplicationProvider( findFile( configFolder, new File( xRoot.getApplication().getFileName() ) ) ) );
                break;

            case "CSV":
                ApplicationRegistry.instance().setApplicationProvider( new CSVApplicationProvider( findFile( configFolder, new File( xRoot.getApplication().getFileName() ) ) ) );
                break;
                
            case "SQL":
                ApplicationRegistry.instance().setApplicationProvider( new SQLApplicationProvider( configProperties.get( JDBC[0] ),
                                                                                                   configProperties.get( JDBC[1] ),
                                                                                                   configProperties.get( JDBC[2] ),
                                                                                                   configProperties.get( JDBC[3] ),
                                                                                                   configProperties.get( OPT_APP[0] ),
                                                                                                   configProperties.get( OPT_APP[1] )));
                break;

            case "EXCEL":
                ApplicationRegistry.instance().setApplicationProvider( new ExcelApplicationProvider( findFile( configFolder, new File( xRoot.getApplication().getFileName() ) ), configProperties.get( "applicationRegistry.tabName" ) ) );
                break;
                
            case "LOCAL":
                ApplicationDescriptor app = new ApplicationDescriptor( xRoot.getApplication().getName(), "", xRoot.getApplication().getAppPackage(), xRoot.getApplication().getBundleId(), xRoot.getApplication().getUrl(), xRoot.getApplication().getIosInstall(), xRoot.getApplication().getAndroidInstall(), createCapabilities( xRoot.getApplication().getCapability() ) );
                ApplicationRegistry.instance().addApplicationDescriptor( app );
                break;
        }

        ApplicationRegistry.instance().setAUT( xRoot.getApplication().getName() );
        return true;
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
        
        for( XArtifact artifact : xRoot.getDriver().getArtifact() )
        {
            artifactList.add( ArtifactType.valueOf( artifact.getType() ) );
        }
        
        DataManager.instance().setAutomaticDownloads( artifactList.toArray( new ArtifactType[0] ) );
        return true;
    }

    @Override
    protected boolean configurePageManagement()
    {
        PageManager.instance().setSiteName( xRoot.getModel().getSiteName() );

        switch ( xRoot.getModel().getProvider() )
        {
            case "XML":

                PageManager.instance().setElementProvider( new XMLElementProvider( findFile( configFolder, new File( xRoot.getModel().getFileName() ) ) ) );
                break;

            case "SQL":
                PageManager.instance().setElementProvider( new SQLElementProvider( configProperties.get( JDBC[0] ),
                                                                                   configProperties.get( JDBC[1] ),
                                                                                   configProperties.get( JDBC[2] ),
                                                                                   configProperties.get( JDBC[3] ),
                                                                                   configProperties.get( OPT_PAGE[0] )));
                break;

            case "CSV":
                PageManager.instance().setElementProvider( new CSVElementProvider( findFile( configFolder, new File( xRoot.getModel().getFileName() ) ) ) );
                break;

            case "EXCEL":
                String[] fileNames = xRoot.getModel().getFileName().split( "," );

                File[] files = new File[fileNames.length];
                for ( int i = 0; i < fileNames.length; i++ )
                    files[i] = findFile( configFolder, new File( fileNames[i] ) );

                PageManager.instance().setElementProvider( new ExcelElementProvider( files, xRoot.getModel().getSiteName() ) );
                break;
                
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

                PageManager.instance().setElementProvider( this );
                
                break;
        }

        
        if ( PageManager.instance().getElementProvider() == null )
            return false;
        
        return PageManager.instance().getElementProvider().isInitialized();
    }
    
    @Override
    protected boolean configureData()
    {
        if ( xRoot.getData() == null )
            return true;
        switch ( xRoot.getData().getProvider() )
        {
            case "XML":
                PageDataManager.instance().setPageDataProvider( new XMLPageDataProvider( findFile( configFolder, new File( xRoot.getData().getFileName() ) ) ) );
                break;

            case "SQL":
                PageDataManager.instance().setPageDataProvider( new SQLPageDataProvider( configProperties.get( JDBC[0] ),
                                                                                         configProperties.get( JDBC[1] ),
                                                                                         configProperties.get( JDBC[2] ),
                                                                                         configProperties.get( JDBC[3] ),
                                                                                         configProperties.get( OPT_DATA[0] )));
                break;

            case "EXCEL":
                String[] fileNames = xRoot.getData().getFileName().split( "," );

                File[] files = new File[fileNames.length];
                for ( int i = 0; i < fileNames.length; i++ )
                    files[i] = findFile( configFolder, new File( fileNames[i] ) );
                
                PageDataManager.instance().setPageDataProvider( new ExcelPageDataProvider( files, configProperties.get( "pageManagement.pageData.tabNames" ) ) );
                break;

        }
        return true;
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
    protected boolean configureDevice()
    {
        switch ( xRoot.getDevices().getProvider() )
        {
            case "RESERVED":
                DataManager.instance().readData( new PerfectoMobileDataProvider( new ReservedHandsetValidator(), DriverType.valueOf( xRoot.getDriver().getType() ) ) );
                break;

            case "AVAILABLE":
                DataManager.instance().readData( new PerfectoMobileDataProvider( new AvailableHandsetValidator(), DriverType.valueOf( xRoot.getDriver().getType() ) ) );
                break;

            case "CSV":
                DataManager.instance().readData( new CSVDataProvider( findFile( configFolder, new File( xRoot.getDevices().getFileName() ) ), DriverType.valueOf( xRoot.getDriver().getType() ) ) );
                break;

            case "XML":

                DataManager.instance().readData( new XMLDataProvider( findFile( configFolder, new File( xRoot.getDevices().getFileName() ) ), DriverType.valueOf( xRoot.getDriver().getType() ) ) );
                break;

            case "SQL":
                DataManager.instance().readData( new SQLDataProvider( configProperties.get( JDBC[0] ),
                                                                      configProperties.get( JDBC[1] ),
                                                                      configProperties.get( JDBC[2] ),
                                                                      configProperties.get( JDBC[3] ),
                                                                      configProperties.get( OPT_DEVICE[0] ),
                                                                      configProperties.get( OPT_DEVICE[1] ),
                                                                      DriverType.valueOf( xRoot.getDriver().getType())));
                break;

            case "EXCEL":
                DataManager.instance().readData( new ExcelDataProvider( findFile( configFolder, new File( xRoot.getDevices().getFileName() ) ), configProperties.get( "deviceManagement.tabName" ), DriverType.valueOf( xRoot.getDriver().getType() ) ) );
                break;

            case "NAMED":
                String deviceList = configProperties.get( "deviceManagement.deviceList" );
                if ( deviceList == null )
                {
                    System.err.println( "******* Property [deviceManagement.deviceList] was not specified" );
                    System.exit( -1 );
                }
                DataManager.instance().readData( new NamedDataProvider( deviceList, DriverType.valueOf( xRoot.getDriver().getType() ) ) );
                break;
                
            case "LOCAL":
                for ( XDevice device : xRoot.getDevices().getDevice() )
                {
                    parseDevice( device );
                }
                
        }

        DeviceManager.instance().addRunListener( RunDetails.instance() );
        DeviceManager.instance().setDriverType( DriverType.valueOf( xRoot.getDriver().getType() ) );
        return true;
    }
    
    private void parseDevice( XDevice device )
    {
        if (!device.isActive() )
            return;
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
        
        Device currentDevice = new SimpleDevice(device.getName(), device.getManufacturer(), device.getModel(), device.getOs(), device.getOsVersion(), device.getBrowserName(), null, device.getAvailableDevices().intValue(), driverName, device.isActive(), device.getId() );
        if ( device.getCapability() != null )
        {
            for ( XDeviceCapability cap : device.getCapability() )
            {
                switch( cap.getClazz() )
                {
                    case "BOOLEAN":
                        currentDevice.addCapability( cap.getName(), Boolean.parseBoolean( cap.getValue() ) );
                        break;
                        
                    case "OBJECT":
                        currentDevice.addCapability( cap.getName(), cap.getValue() );
                        break;
                        
                    case "STRING":
                        currentDevice.addCapability( cap.getName(), cap.getValue() );
                        break;
                        
                    case "PLATFORM":
                        currentDevice.addCapability( cap.getName(), Platform.valueOf( cap.getValue().toUpperCase() ) );
                        break;
                }
            }
        }

        if ( currentDevice.isActive() )
        {               
            if (log.isDebugEnabled())
                log.debug( "Extracted: " + currentDevice );

            DeviceManager.instance().registerDevice( currentDevice );
        }
        
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
    protected boolean configureDriver()
    {
        String personaNames = xRoot.getDriver().getPersonas();
        if ( personaNames != null && !personaNames.isEmpty() )
        {
            DataManager.instance().setPersonas( personaNames );
            PageManager.instance().setWindTunnelEnabled( true );
        }

        DeviceManager.instance().setCachingEnabled( xRoot.getDriver().isCachingEnabled() );

        String stepTags = xRoot.getDriver().getStepTags();
        if ( stepTags != null && !stepTags.isEmpty() )
            PageManager.instance().setTagNames( stepTags );

        switch ( xRoot.getSuite().getProvider() )
        {
            case "LOCAL":
                parseModel( xRoot.getModel() );
                for( XTest test : xRoot.getSuite().getTest() )
                {
                    if (KeyWordDriver.instance().getTest( test.getName() ) != null)
                    {
                        log.warn( "The test [" + test.getName() + "] is already defined and will not be added again" );
                        continue;
                    }
                    
                    KeyWordTest currentTest = parseTest( test, "test" );
                    
                    if (currentTest.getDataDriver() != null && !currentTest.getDataDriver().isEmpty())
                    {
                        PageData[] pageData = PageDataManager.instance().getRecords( currentTest.getDataDriver() );
                        if (pageData == null)
                        {
                            log.warn( "Specified Data Driver [" + currentTest.getDataDriver() + "] could not be located. Make sure it exists and it was populated prior to initializing your keyword factory" );
                            KeyWordDriver.instance().addTest( currentTest );
                        }
                        else
                        {
                            String testName = currentTest.getName();

                            for (PageData record : pageData)
                            {
                                KeyWordDriver.instance().addTest( currentTest.copyTest( testName + "!" + record.getName() ) );
                            }
                        }
                    }
                    else
                        KeyWordDriver.instance().addTest( currentTest );
                }
                
                for( XTest test : xRoot.getSuite().getFunction() )
                {
                    if (KeyWordDriver.instance().getTest( test.getName() ) != null)
                    {
                        log.warn( "The function [" + test.getName() + "] is already defined and will not be added again" );
                        continue;
                    }
                    
                    KeyWordDriver.instance().addFunction( parseTest( test, "function" ) );
                }
                break;
                
                
            
            case "XML":
                KeyWordDriver.instance().loadTests( new XMLKeyWordProvider( findFile( configFolder, new File( xRoot.getSuite().getFileName() ) ) ) );

                break;

            case "SQL":
            case "LOCAL-SQL":
            {
                KeyWordDriver.instance().loadTests( new SQLKeyWordProvider( configProperties.get( JDBC[0] ),
                                                                            configProperties.get( JDBC[1] ),
                                                                            configProperties.get( JDBC[2] ),
                                                                            configProperties.get( JDBC[3] ),
                                                                            xRoot.getSuite().getFileName(),
                                                                            configProperties.get( OPT_DRIVER[1] ),
                                                                            configProperties.get( OPT_DRIVER[2] ),
                                                                            configProperties.get( OPT_DRIVER[3] ),
                                                                            configProperties.get( OPT_DRIVER[4] ),
                                                                            configProperties.get( OPT_DRIVER[5] ),
                                                                            configProperties.get( OPT_DRIVER[6] ),
                                                                            configProperties.get( OPT_DRIVER[7] ),
                                                                            configProperties.get( OPT_DRIVER[8] ),
                                                                            configProperties.get( OPT_DRIVER[9] )
                                                                            ));
                                                   
                break;
            }
        }

        List<String> testArray = new ArrayList<String>( 10 );

        //
        // Extract any named tests
        //
        String testNames = xRoot.getDriver().getTestNames();
        if ( testNames != null && !testNames.isEmpty() )
            testArray.addAll( Arrays.asList( testNames ) );

        //
        // Extract any tagged tests
        //
        String tagNames = xRoot.getDriver().getTagNames();
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
        
        dryRun = xRoot.getDriver().isDryRun();
        
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
        
        return true;
    }
    
    
    
    private void parseModel( XModel model )
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
    
                KeyWordDriver.instance().addPage( page.getName(), useClass );
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
        KeyWordTest test = new KeyWordTest( xTest.getName(), xTest.isActive(), xTest.getDataProvider(), xTest.getDataDriver(), xTest.isTimed(), xTest.getLinkId(), xTest.getOs(), xTest.getThreshold().intValue(), "", xTest.getTagNames() );
        
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
                                                                                 (xStep.getValidationType() != null && !xStep.getValidationType().isEmpty() ) ? ValidationType.valueOf( xStep.getValidationType() ) : null, xStep.getTagNames() );
            
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
            KeyWordParameter kp = new KeyWordParameter( ParameterType.valueOf( p.getType() ), p.getValue() );
            
            if ( p.equals( ParameterType.FILE ) )
            {
                File dataFile = new File( p.getValue() );
                if ( dataFile.isFile() )
                {
                    try
                    {
                        kp.setValue( readFile( new FileInputStream( dataFile ) ) );
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
                            kp.setValue( readFile( new FileInputStream( dataFile ) ) );
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
    
    private String readFile( InputStream inputStream )
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

}
