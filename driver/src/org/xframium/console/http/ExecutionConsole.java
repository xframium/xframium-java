package org.xframium.console.http;

import java.awt.Desktop;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationDescriptor.AppType;
import org.xframium.application.ApplicationVersion;
import org.xframium.artifact.ArtifactType;
import org.xframium.console.ExecutionContainer;
import org.xframium.console.http.handler.spi.ExecuteTest;
import org.xframium.console.http.handler.spi.ListFolder;
import org.xframium.console.http.handler.spi.OpenConsole;
import org.xframium.console.http.handler.spi.OpenFile;
import org.xframium.console.http.handler.spi.OpenSuite;
import org.xframium.console.http.handler.spi.TestStatus;
import org.xframium.container.ApplicationContainer;
import org.xframium.container.CloudContainer;
import org.xframium.container.DeviceContainer;
import org.xframium.container.DriverContainer;
import org.xframium.container.FavoriteContainer;
import org.xframium.container.FileContainer;
import org.xframium.container.ModelContainer;
import org.xframium.container.PageContainer;
import org.xframium.container.SiteContainer;
import org.xframium.container.SuiteContainer;
import org.xframium.container.TagContainer;
import org.xframium.device.DeviceCap;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudDescriptor.ProviderType;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.driver.SuiteListener;
import org.xframium.driver.XMLConfigurationReader;
import org.xframium.gesture.Gesture.GestureType;
import org.xframium.gesture.device.action.DeviceAction.ActionType;
import org.xframium.page.BY;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataContainer;
import org.xframium.page.data.provider.ExcelPageDataProvider;
import org.xframium.page.data.provider.SQLPageDataProvider;
import org.xframium.page.data.provider.XMLPageDataProvider;
import org.xframium.page.element.Element.WAIT_FOR;
import org.xframium.page.element.SeleniumElement;
import org.xframium.page.element.SubElement;
import org.xframium.page.element.provider.XMLElementProvider;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.spi.KeyWordPageImpl;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.keyWord.step.spi.KWSAlert.ALERT_TYPE;
import org.xframium.page.keyWord.step.spi.KWSApplication.ApplicationAction;
import org.xframium.page.keyWord.step.spi.KWSBrowser.SwitchType;
import org.xframium.page.keyWord.step.spi.KWSClick;
import org.xframium.page.keyWord.step.spi.KWSMath.MATH_TYPE;
import org.xframium.page.keyWord.step.spi.KWSString2.OperationType;
import org.xframium.page.listener.KeyWordListener;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;
import com.sun.net.httpserver.HttpServer;
import com.xframium.serialization.SerializationManager;
import com.xframium.serialization.json.MapSerializer;
import com.xframium.serialization.json.ReflectionSerializer;

@SuppressWarnings ( "restriction")
public class ExecutionConsole implements KeyWordListener, SuiteListener
{
    private HttpServer httpServer;

    private Log log = LogFactory.getLog(ExecutionConsole.class);
    private static ExecutionConsole singleton = new ExecutionConsole();
    
    private Map<String,ExecutionContainer> syncMap = new HashMap<String,ExecutionContainer>( 25 );
    private Map<String,ExecutionContainer> executionMap = new HashMap<String,ExecutionContainer>( 25 );
    
    
    
    
    public static ExecutionConsole instance()
    {
        return singleton;
    }
    
    public static void main(String[] args) 
    {
        try
        {
            ExecutionConsole.instance().startUp( "127.0.0.1", 8145 );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void addSync( ExecutionContainer eContain )
    {
        synchronized ( syncMap )
        {
            syncMap.put( eContain.getSessionId(), eContain );
        }
    }
    
    public List<ExecutionContainer> drainSync()
    {
        synchronized ( syncMap )
        {
            List<ExecutionContainer> eList = new ArrayList<ExecutionContainer>( 10 );
            eList.addAll( syncMap.values() );
            syncMap.clear();
            return eList;
        }
    }
    
    private ExecutionConsole() 
    {
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( KeyWordStep.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( KWSClick.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( AbstractKeyWordStep.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( KeyWordTest.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( KeyWordPageImpl.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( Page.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( PageData.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( KeyWordParameter.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( KeyWordToken.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( SeleniumElement.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( Capabilities.class, new MapSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( XMLElementProvider.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( XMLPageDataProvider.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ExcelPageDataProvider.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( SQLPageDataProvider.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( SuiteContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ApplicationContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( CloudContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( DeviceContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ApplicationDescriptor.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( Device.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( CloudDescriptor.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( XMLConfigurationReader.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( PageContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( BY.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( GestureType.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ActionType.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( WAIT_FOR.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ALERT_TYPE.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( FileContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( PageDataContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( SwitchType.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( MATH_TYPE.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( AppType.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ProviderType.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( DriverContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ArtifactType.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( DeviceCap.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( TagContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( SiteContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ModelContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( OperationType.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( FavoriteContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ApplicationAction.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( SubElement.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ApplicationVersion.class, new ReflectionSerializer() );
        SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ).addCustomMapping( ExecutionContainer.class, new ReflectionSerializer() );
        KeyWordDriver.instance().addStepListener( this );
    }
    
    public void startUp( String ipAddress, int portNumber )
    {
        try
        {
            if ( httpServer != null )
                httpServer.stop( 0 );
            
            httpServer = HttpServer.create( new InetSocketAddress( ipAddress, portNumber ), 1000 );
            httpServer.createContext( "/executionConsole", new OpenConsole() );
            httpServer.createContext( "/js", new OpenFile() );
            httpServer.createContext( "/css", new OpenFile() );
            httpServer.createContext( "/executionConsole/open", new OpenSuite() );
            httpServer.createContext( "/executionConsole/folderList", new ListFolder() );
            httpServer.createContext( "/executionConsole/executeTest", new ExecuteTest() );
            httpServer.createContext( "/executionConsole/status", new TestStatus() );

            httpServer.start();
            
            Thread.sleep( 500 );
            
            Desktop.getDesktop().browse( new URI( "http://" + ipAddress + ":" + portNumber + "/executionConsole") );

        }
        catch ( Exception e )
        {
            log.error( "Error starting DEBUGGER", e );
        }
    }

    public void shutDown()
    {
        httpServer.stop( 0 );
    }

    
    
    @Override
    public boolean beforeStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
    {
        
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void afterStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, StepStatus stepStatus, SuiteContainer sC, ExecutionContextTest eC )
    {
        String executionId = ( (DeviceWebDriver) webDriver ).getExecutionId();
        executionMap.get( executionId ).setStepCount( executionMap.get( executionId ).getStepCount()+1 );
        addSync( executionMap.get( executionId ) );
    }

    @Override
    public boolean beforeTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
    {
        String executionId = ( (DeviceWebDriver) webDriver ).getExecutionId();
        
        ExecutionContainer eContain = new ExecutionContainer( executionId );
        eContain.setTestName( keyWordTest.getName() );
        eContain.setFullTestName( eC.getTestName() );
        eContain.setDevice( eC.getDevice() );
        eContain.setStartTime( System.currentTimeMillis() );
        eContain.setRootFolder( ExecutionContext.instance().getReportFolder().getAbsolutePath() );
        executionMap.put( executionId, eContain );
        
        return true;
    }

    @Override
    public void afterTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
    {
        String executionId = ( (DeviceWebDriver) webDriver ).getExecutionId();
        executionMap.get( executionId ).setTestStatus( eC.getTestStatus() );
        executionMap.get( executionId ).setStopTime( System.currentTimeMillis() );
        executionMap.get( executionId ).setFolderName( eC.getTestName() + System.getProperty( "file.separator" ) + eC.getDevice().getKey() );
        addSync( executionMap.get( executionId ) );
    }

    private boolean testExecuting = false;
    
    
    
    public boolean isTestExecuting()
    {
        return testExecuting;
    }

    @Override
    public void beforeSuite( String suiteName, File fileName )
    {
        DeviceManager.instance().clear();
        testExecuting = true;
        executionMap.clear();
        syncMap.clear();
        ExecutionContext.instance().clear();
    }

    @Override
    public void afterSuite( String suiteName, File fileName, File outputFolder )
    {
        testExecuting = false;
    }
}
