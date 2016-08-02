package org.xframium.debugger;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.xframium.debugger.handler.SessionHandler;
import org.xframium.debugger.handler.TestHandler;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.listener.KeyWordListener;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings ( "restriction")
public class DebugManager implements KeyWordListener
{
    private HttpServer httpServer;

    private Log log = LogFactory.getLog(DebugManager.class);
    private Map<String, TestContainer> activeTests = new HashMap<String, TestContainer>( 20 );
    private Map<String, TestContainer> completedTests = new HashMap<String, TestContainer>( 20 );

    private static final DebugManager singleton = new DebugManager();

    private DebugManager()
    {

    }

    public static DebugManager instance()
    {
        return singleton;
    }
    
    public Map<String, TestContainer> getActiveTests()
    {
        return activeTests;
    }

    public Map<String, TestContainer> getCompletedTests()
    {
        return completedTests;
    }

    @Override
    public boolean beforeStep( WebDriver webDriver, KeyWordStep currentStep )
    {
        String executionId = ((DeviceWebDriver) webDriver).getExecutionId();
        TestContainer testContainer = activeTests.get( executionId );

        if ( testContainer != null )
            testContainer.addStep( new StepContainer( webDriver, currentStep ) );

        return true;
    }

    @Override
    public void afterStep( WebDriver webDriver, KeyWordStep currentStep, StepStatus stepStatus )
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
    public boolean beforeTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
    {
        activeTests.put( ((DeviceWebDriver) webDriver).getExecutionId(), new TestContainer( webDriver, keyWordTest, contextMap, dataMap, pageMap ) );
        return true;
    }

    @Override
    public void afterTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass )
    {
        String executionId = ((DeviceWebDriver) webDriver).getExecutionId();
        completedTests.put( executionId, activeTests.remove( executionId ) );
    }

    public void startUp( String ipAddress, int portNumber )
    {
        try
        {
            if ( httpServer != null )
                httpServer.stop( 0 );
            
            log.warn( "Starting the DEBUGGER as " + ipAddress + ":" + portNumber );
            
            httpServer = HttpServer.create( new InetSocketAddress( ipAddress, portNumber ), 1000 );
            httpServer.createContext( "/sessions", new SessionHandler() );
            httpServer.createContext( "/testCase", new TestHandler() );
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
