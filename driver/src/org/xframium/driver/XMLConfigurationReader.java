package org.xframium.driver;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.openqa.selenium.Platform;
import org.xframium.Initializable;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationRegistry;
import org.xframium.application.CSVApplicationProvider;
import org.xframium.application.ExcelApplicationProvider;
import org.xframium.application.SQLApplicationProvider;
import org.xframium.application.XMLApplicationProvider;
import org.xframium.application.xsd.ObjectFactory;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.cloud.CSVCloudProvider;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.cloud.ExcelCloudProvider;
import org.xframium.device.cloud.SQLCloudProvider;
import org.xframium.device.cloud.XMLCloudProvider;
import org.xframium.device.data.DataManager;
import org.xframium.device.logging.ThreadedFileHandler;
import org.xframium.driver.xsd.XArtifact;
import org.xframium.driver.xsd.XDeviceCapability;
import org.xframium.driver.xsd.XFramiumRoot;
import org.xframium.driver.xsd.XLibrary;
import org.xframium.driver.xsd.XModel;
import org.xframium.driver.xsd.XParameter;
import org.xframium.driver.xsd.XProperty;
import org.xframium.driver.xsd.XStep;
import org.xframium.driver.xsd.XTest;
import org.xframium.driver.xsd.XTestPage;
import org.xframium.driver.xsd.XToken;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.element.provider.CSVElementProvider;
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
import org.xframium.page.keyWord.step.KeyWordStepFactory;

public class XMLConfigurationReader extends AbstractConfigurationReader
{
    private static final String[] JDBC = new String[] { "jdbc.username", "jdbc.password", "jdbc.url", "jdbc.driverClassName" };
    private static final String[] OPT_CLOUD = new String[] { "cloudRegistry.query" };
    private static final String[] OPT_APP = new String[] { "applicationRegistry.query", "applicationRegistry.capQuery" };
    private static final String[] OPT_PAGE = new String[] { "pageManagement.query" };
    private static final String[] OPT_DATA = new String[] { "pageManagement.pageData.query" };
    private static final String[] OPT_CONTENT = new String[] { "pageManagement.content.query" };
    private static final String[] OPT_DEVICE = new String[] { "deviceManagement.device.query", "deviceManagement.capability.query" };
    private XFramiumRoot xRoot;
    private Map<String,String> configProperties = new HashMap<String,String>( 10 );

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
        
        // TODO Auto-generated method stub
        return false;
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
        PageManager.instance().setSiteName( xRoot.getSite().getName() );

        switch ( xRoot.getSite().getProvider() )
        {
            case "XML":

                PageManager.instance().setElementProvider( new XMLElementProvider( findFile( configFolder, new File( xRoot.getSite().getFileName() ) ) ) );
                break;

            case "SQL":
                PageManager.instance().setElementProvider( new SQLElementProvider( configProperties.get( JDBC[0] ),
                                                                                   configProperties.get( JDBC[1] ),
                                                                                   configProperties.get( JDBC[2] ),
                                                                                   configProperties.get( JDBC[3] ),
                                                                                   configProperties.get( OPT_PAGE[0] )));
                break;

            case "CSV":
                PageManager.instance().setElementProvider( new CSVElementProvider( findFile( configFolder, new File( xRoot.getSite().getFileName() ) ) ) );
                break;

            case "EXCEL":
                String[] fileNames = xRoot.getSite().getFileName().split( "," );

                File[] files = new File[fileNames.length];
                for ( int i = 0; i < fileNames.length; i++ )
                    files[i] = findFile( configFolder, new File( fileNames[i] ) );

                PageManager.instance().setElementProvider( new ExcelElementProvider( files, xRoot.getSite().getName() ) );
                break;
                
            case "LOCAL":
                
                break;
        }

        
        return true;
    }
    
    private void parseModel( XModel model )
    {
        for ( XTestPage page : model.getPage() )
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
            parentStep.addParameter( new KeyWordParameter( ParameterType.valueOf( p.getType() ), p.getValue() ) );
        }
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
    protected boolean configureData()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configureContent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configureDevice()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configurePropertyAdapters()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean configureDriver()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean executeTest() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

}
