package org.xframium.debugger;

import java.awt.Desktop;
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
import org.xframium.container.SuiteContainer;
import org.xframium.debugger.handler.AheadHandler;
import org.xframium.debugger.handler.ExtractHandler;
import org.xframium.debugger.handler.ExtractXMLHandler;
import org.xframium.debugger.handler.PauseHandler;
import org.xframium.debugger.handler.ResumeHandler;
import org.xframium.debugger.handler.SessionHandler;
import org.xframium.debugger.handler.SnapshotHandler;
import org.xframium.debugger.handler.TestHandler;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.element.SeleniumElement;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.spi.KeyWordPageImpl;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.page.listener.KeyWordListener;
import org.xframium.reporting.ExecutionContextTest;

import com.sun.net.httpserver.HttpServer;
import com.xframium.serialization.SerializationManager;
import com.xframium.serialization.json.MapSerializer;
import com.xframium.serialization.json.ReflectionSerializer;

@SuppressWarnings ( "restriction")
public class DebugManager implements KeyWordListener
{
    private HttpServer httpServer;

    private Log log = LogFactory.getLog(DebugManager.class);
    private Map<String, TestContainer> activeTests = new HashMap<String, TestContainer>( 20 );
    private Map<String, TestContainer> completedTests = new HashMap<String, TestContainer>( 20 );

    private static final Map<String,DebugManager> singleton = new HashMap<String,DebugManager>(5);

    private DebugManager()
    {
        SerializationManager.instance().setDefaultAdapter( SerializationManager.JSON_SERIALIZATION );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( TestContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( StepContainer.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KeyWordStep.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( AbstractKeyWordStep.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KeyWordTest.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KeyWordPageImpl.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( Page.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( PageData.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KeyWordParameter.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( KeyWordToken.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( SeleniumElement.class, new ReflectionSerializer() );
        SerializationManager.instance().getDefaultAdapter().addCustomMapping( Capabilities.class, new MapSerializer() );
    }

    public static DebugManager instance( String xFID )
    {
        if ( singleton.containsKey( xFID ) )
            return singleton.get( xFID );
        else
        {
            singleton.put( xFID, new DebugManager() );
            return singleton.get( xFID );
        }
    }
    
    public Map<String, TestContainer> getActiveTests()
    {
        return activeTests;
    }

    public Map<String, TestContainer> getCompletedTests()
    {
        return completedTests;
    }
    public Map<String,Object> getTests()
    {
    	Map<String,Object> testMap = new HashMap<String,Object>(10);
    	testMap.put( "active", activeTests );
    	testMap.put( "complete", completedTests );
    	return testMap;
    }

    @Override
    public boolean beforeStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
    {
        String executionId = ((DeviceWebDriver) webDriver).getExecutionId();
        TestContainer testContainer = activeTests.get( executionId );
        
        if ( testContainer != null )
        {
            
            
            List<String> parameterList = new ArrayList<String>( 10 );
            for ( KeyWordParameter p : currentStep.getParameterList() )
                parameterList.add( currentStep.getParameterValue( p, contextMap, dataMap, eC.getxFID() ) + "" );
            
            
            List<String[]> tokenList = new ArrayList<String[]>( 10 );
            for ( KeyWordToken t : currentStep.getTokenList() )
                tokenList.add( new String[] { t.getName(), currentStep.getTokenValue( t, contextMap, dataMap, eC.getxFID() ) + "" } );
            
            testContainer.addStep( new StepContainer( webDriver, currentStep, contextMap, pageObject, parameterList, tokenList ) );
            
            
            if ( !testContainer.isStepAhead() )
            	testContainer.waitFor( false );
        }

        return true;
    }

    @Override
    public void afterStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, StepStatus stepStatus, SuiteContainer sC, ExecutionContextTest eC )
    {
        String executionId = ((DeviceWebDriver) webDriver).getExecutionId();
        TestContainer testContainer = activeTests.get( executionId );
        
        

        if ( testContainer != null )
        {
            for ( int i = testContainer.getSteps().size() - 1; i >= 0; i-- )
            {
                if ( testContainer.getSteps().get( i ).getStatus() == null )
                {
                    testContainer.getSteps().get( i ).setStatus( stepStatus );
                    break;
                }
            }
        }
    }

    @Override
    public boolean beforeTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
    {
        activeTests.put( ((DeviceWebDriver) webDriver).getExecutionId(), new TestContainer( webDriver, keyWordTest, contextMap, dataMap, pageMap, eC.getCloud(), eC.getDevice() ) );
        return true;
    }

    @Override
    public void afterTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
    {
        String executionId = ((DeviceWebDriver) webDriver).getExecutionId();
        completedTests.put( executionId, activeTests.remove( executionId ) );
    }
    
    @Override
    public void afterArtifacts( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
    {
       
    }

    public void launchBrowser( String ipAddress, int portNumber )
    {
        try
        {
            Desktop.getDesktop().browse( new URI( "http://" + ipAddress + ":" + portNumber + "/sessions") );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void startUp( String ipAddress, int portNumber, String xFID )
    {
        try
        {
            if ( httpServer != null )
                httpServer.stop( 0 );
            
            log.warn( "Starting the DEBUGGER as " + ipAddress + ":" + portNumber );
            
            httpServer = HttpServer.create( new InetSocketAddress( ipAddress, portNumber ), 1000 );
            httpServer.createContext( "/sessions", new SessionHandler( xFID ) );
            httpServer.createContext( "/testCase", new TestHandler( xFID ) );
            httpServer.createContext( "/pause", new PauseHandler( xFID ) );
            httpServer.createContext( "/resume", new ResumeHandler( xFID ) );
            httpServer.createContext( "/stepAhead", new AheadHandler( xFID ) );
            httpServer.createContext( "/extract", new ExtractHandler( xFID ) );
            httpServer.createContext( "/extractXml", new ExtractXMLHandler( xFID ) );
            httpServer.createContext( "/extractXml", new ExtractXMLHandler( xFID ) );
            httpServer.createContext( "/snapshot", new SnapshotHandler( xFID ) );
            httpServer.start();
            
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
}
