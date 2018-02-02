/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
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
/*
 * 
 */
package org.xframium.device.factory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ContextAware;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.interrupt.DeviceInterrupt;
import org.xframium.device.interrupt.DeviceInterruptThread;
import org.xframium.device.keepAlive.DeviceKeepAlive;
import org.xframium.page.StepStatus;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.reporting.ReportingWebElementAdapter;
import org.xframium.spi.Device;
import org.xframium.spi.PropertyProvider;
import org.xframium.spi.driver.CachingDriver;
import org.xframium.spi.driver.DeviceProvider;
import org.xframium.spi.driver.NativeDriverProvider;
import org.xframium.spi.driver.ReportiumProvider;
import org.xframium.utility.XMLEscape;
import org.xml.sax.InputSource;
import com.perfecto.reportium.client.ReportiumClient;
import io.appium.java_client.AppiumDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceWebDriver.
 */
public class DeviceWebDriver
        implements HasCapabilities, WebDriver, JavascriptExecutor, ContextAware, ExecuteMethod, NativeDriverProvider, PropertyProvider, TakesScreenshot, DeviceProvider, HasInputDevices, CachingDriver, ReportiumProvider, HasTouchScreen
{

    private static Pattern FORMAT_PATTERN = Pattern.compile( "\\{([\\w\\.]*)\\}" );
    private List<DeviceInterrupt> interruptList;

    private KeepAliveThread keepAliveThread = null;
    
    private DeviceInterruptThread diThread = null;

    /** The web driver. */
    protected WebDriver webDriver;

    /** The execution id. */
    private String executionId;

    /** The report key. */
    private String reportKey;

    /** The wind tunnel report. */
    private String windTunnelReport;

    /** The device name. */
    private String deviceName;

    /** The current device. */
    private Device currentDevice;
    private Device populatedDevice;
    private CloudDescriptor cloud;
    private boolean syntheticConnection;
    
    private long implicitWait = 0;
    private long scriptTimeout = 0;
    private long pageLoadTimeout = 0;
    private DesiredCapabilities dC;
    
    private double widthModifier = 1;
    private double heightModifier = 1;
    
    private boolean keepAliveRunning = false;
    private long lastAction = System.currentTimeMillis();
    
    private boolean reportingElement = false;
    
    private DeviceOptions deviceOptions = null;
    private DeviceTimeouts deviceTimeouts = null;

    private ApplicationDescriptor aut;
    
    private ExecutionContextTest executionContext;
    
    
    private File artifactFolder = null;
    
    
    
    public boolean isKeepAliveRunning()
    {
        return keepAliveRunning;
    }

    public void setKeepAliveRunning( boolean keepAliveRunning )
    {
        this.keepAliveRunning = keepAliveRunning;
    }

    
    
    public long getLastAction()
    {
        return lastAction;
    }

    public void setLastAction( long lastAction )
    {
        this.lastAction = lastAction;
    }
    
    public void setLastAction( )
    {
        this.lastAction = System.currentTimeMillis();
    }

    public String getxFID()
    {
        return executionContext.getxFID();
    }
    
    public void setArtifactFolder( File artifactFolder )
    {
        this.artifactFolder = artifactFolder;
    }
    
    public double getWidthModifier()
    {
        return widthModifier;
    }

    public double getHeightModifier()
    {
        return heightModifier;
    }

    public String getValue( String valueDescriptor )
    {
        String[] valueMap = valueDescriptor.split( "\\." );
        
        switch( valueMap[ 0 ] )
        {
            case "device":
                return _getValue( populatedDevice, valueMap, 1 );
                
            case "data":
                try
                {
                    return executionContext.getDataMap().get( valueMap[ 0 ] ).get( valueMap[ 1 ] ).toString();
                }
                catch( Exception e )
                {
                    log.error( "Could not locate data object " + valueDescriptor, e );
                }
                break;
                
            case "test":
                return _getValue( executionContext, valueMap, 1 );
        }
        
        return null;
    }

    private Field getField( String fieldName, Class currentClass )
    {
        try
        {
            Field currentField = currentClass.getDeclaredField( fieldName );
            if ( currentField != null )
                return currentField;
            else
            {
                if ( currentClass.getSuperclass() != null )
                    return getField( fieldName, currentClass.getSuperclass() );
            }
        }
        catch( Exception e )
        {
            if ( currentClass.getSuperclass() != null )
                return getField( fieldName, currentClass.getSuperclass() );
        }
        
        return null;
    }
    
    private String _getValue( Object currentObject, String[] valueArray, int position )
    {
        try
        {
            Class currentClass = currentObject.getClass();
            Field currentField = getField( valueArray[ position ], currentClass );
            
            boolean iA = currentField.isAccessible();
            currentField.setAccessible( true );
            
            Object newObject = currentField.get( currentObject );
            
            if ( position == valueArray.length - 1 )
            {
                if ( newObject != null )
                    return newObject.toString();
                else
                    return null;
            }
            
            currentField.setAccessible( iA );
            
            return _getValue( newObject, valueArray, position+1 );
            
        }
        catch( Exception e )
        {
            log.error( "Could not find field " + valueArray[ position ] + " on " + currentObject.getClass().getName() );
            return null;
        }
    }
    
    public String toFormattedString( String template )
    {
        List<String> templateArray = new ArrayList<String>( 10 );
        
        
        Matcher templateMatcher = FORMAT_PATTERN.matcher( template );
        
        while ( templateMatcher.find() )
            templateArray.add( templateMatcher.group( 1 ) );
        
        
        String newValue = template;
        
        for ( String fieldName : templateArray )
        {
            String replaceValue = getValue( fieldName );
            
            if ( replaceValue != null )
                newValue = newValue.replaceAll( "\\{" + fieldName + "\\}", replaceValue );
        }
        
        return newValue;
    }
    
    
    public File getArtifactFolder()
    {
        return artifactFolder;
    }
    
    public boolean isReportingElement()
    {
        return reportingElement;
    }

    public void setReportingElement( boolean reportingElement )
    {
        this.reportingElement = reportingElement;
    }

    public ExecutionContextTest getExecutionContext()
    {
        return executionContext;
    }

    public void setExecutionContext( ExecutionContextTest executionContext )
    {
        this.executionContext = executionContext;
        
        this.executionContext.setDesiredCapabilities( dC );
        this.executionContext.setDerivedCapabilities( getCapabilities() );
    }

    public long getImplicitWait()
    {
        return implicitWait;
    }

    public long getScriptTimeout()
    {
        return scriptTimeout;
    }

    public long getPageLoadTimeout()
    {
        return pageLoadTimeout;
    }
    
    public String getLog()
    {
        String log = DeviceManager.instance( executionContext.getxFID() ).getLog();
        DeviceManager.instance( executionContext.getxFID() ).clearLog();
        return log;
    }

    public ApplicationDescriptor getAut()
    {
        return aut;
    }

    public void setAut( ApplicationDescriptor aut, String xFID )
    {
        ExecutionContext.instance( xFID ).setAut( aut );
        this.aut = aut;
    }

    public CloudDescriptor getCloud()
    {
        return cloud;
    }

    public void setCloud( CloudDescriptor cloud )
    {
        this.cloud = cloud;
        
        if ( cloud.getKeepAlive() != null && isConnected() )
        {
            //
            // Configure the keep live thread here
            //
            keepAliveThread = new KeepAliveThread( cloud.getKeepAlive(), this );
            keepAliveThread.start();
        }
        
    }
    
    private class KeepAliveThread extends Thread
    {
        private boolean keepAliveRunning = true;
        private DeviceKeepAlive kA;
        private DeviceWebDriver webDriver;
        
        public KeepAliveThread( DeviceKeepAlive kA, DeviceWebDriver webDriver )
        {
            this.kA = kA;
            this.webDriver = webDriver;
        }

        public void run()
        {
            
            while( keepAliveRunning )
            {
                long timeGap = System.currentTimeMillis() - webDriver.getLastAction();
                if ( timeGap > ( kA.getQuietTime() * 1000 ) )
                {
                    kA.keepAlive( webDriver );
                    webDriver.setLastAction();
                }
                
                try
                {
                    Thread.sleep( kA.getPollTime() * 1000 );
                }
                catch( Exception e )
                {
                    
                }
            }
        }
        
        
        public boolean isKeepAliveRunning()
        {
            return keepAliveRunning;
        }

        public void setKeepAliveRunning( boolean keepAliveRunning )
        {
            this.keepAliveRunning = keepAliveRunning;
        }
        
        
        
    }

    private ReportiumClient reportiumClient;

    public ReportiumClient getReportiumClient()
    {
        return reportiumClient;
    }

    public void setReportiumClient( ReportiumClient reportiumClient )
    {
        this.reportiumClient = reportiumClient;
    }

    public Device getPopulatedDevice()
    {
        if ( populatedDevice == null )
            return currentDevice;
        else
            return populatedDevice;
    }

    public void setPopulatedDevice( Device populatedDevice )
    {
        this.populatedDevice = populatedDevice;
    }

    /** The current context. */
    private String currentContext;

    /** The context handles. */
    private Set<String> contextHandles;


    /** The log. */
    protected Log log = LogFactory.getLog( DeviceWebDriver.class );

    /** The caching enabled. */
    private boolean cachingEnabled = true;

    /** The x path factory. */
    private XPathFactory xPathFactory = XPathFactory.newInstance();

    /** The cached document. */
    private Document cachedDocument = null;

    /** The context switch supported. */
    private boolean contextSwitchSupported = true;

    /** The Constant EXECUTION_ID. */
    private static final String EXECUTION_ID = "EXECUTION_ID";

    /** The Constant REPORT_KEY. */
    private static final String REPORT_KEY = "REPORT_KEY";

    /** The Constant DEVICE_NAME. */
    private static final String DEVICE_NAME = "DEVICE_NAME";

    /** The Constant WIND_TUNNEL. */
    private static final String WIND_TUNNEL = "WIND_TUNNEL";

    private Map<String, String> propertyMap = new HashMap<String, String>( 20 );

    /*
     * (non-Javadoc)
     * 
     * @see com.morelandLabs.spi.driver.DeviceProvider#getDevice()
     */
    @Override
    public Device getDevice()
    {
        return currentDevice;
    }

    @Override
    public String getPageSource()
    {
        setLastAction();
        String pageSource = webDriver.getPageSource();

        if ( pageSource != null )
        {
            // if ( ApplicationRegistry.instance().getAUT() != null ||
            // ApplicationRegistry.instance().getAUT().isWeb() )
            // return XMLEscape.toHTML( pageSource );
            // else
            return XMLEscape.toXML( pageSource );
        }
        else
            return "";
    }

    /**
     * Cache data.
     */
    public void cacheData()
    {
        if ( !cachingEnabled )
            return;

        if ( log.isInfoEnabled() )
            log.info( Thread.currentThread().getName() + ": Caching page data" );
        readXML( getPageSource() );
    }
    
    public void readXML( String pageSource )
    {
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            InputStreamReader streamReader = new InputStreamReader( new ByteArrayInputStream( pageSource.getBytes() ), "UTF-8" );
            InputSource inputSource = new InputSource( streamReader );
            inputSource.setEncoding( "UTF-8" );

            cachedDocument = dBuilder.parse( inputSource );
            cachingEnabled = true;
        }
        catch ( Exception e )
        {
            log.warn( "CACHING HAS BEEN DISABLED", e );
            cachingEnabled = false;
            cachedDocument = null;
        }
    }

    /**
     * Instantiates a new device web driver.
     *
     * @param webDriver
     *            the web driver
     * @param cachingEnabled
     *            the caching enabled
     * @param currentDevice
     *            the current device
     */
    public DeviceWebDriver( WebDriver webDriver, boolean cachingEnabled, Device currentDevice, DesiredCapabilities dC )
    {
        this.webDriver = webDriver;
        this.cachingEnabled = cachingEnabled;
        this.currentDevice = currentDevice;
        this.syntheticConnection = false;
        this.dC = dC;
    }
    
    public DeviceWebDriver( String xmlData, Device currentDevice, DesiredCapabilities dC )
    {
        this.cachingEnabled = true;
        readXML( xmlData );
        this.currentDevice = currentDevice;
        this.syntheticConnection = true;
        this.dC = dC;
    }

    public boolean isConnected()
    {
        return webDriver != null;
    }

    public void setDeviceInterrupts( List<DeviceInterrupt> interruptList )
    {
        this.interruptList = interruptList;

        if ( interruptList != null && interruptList.size() > 0 )
        {
            diThread = new DeviceInterruptThread( interruptList, this );
            new Thread( diThread ).start();
        }
    }

    /**
     * Gets the execution id.
     *
     * @return the execution id
     */
    public String getExecutionId()
    {
        return executionId;
    }

    /**
     * Sets the execution id.
     *
     * @param executionId
     *            the new execution id
     */
    public void setExecutionId( String executionId )
    {
        this.executionId = executionId;

        if ( log.isInfoEnabled() )
            log.info( "Execution ID recorded as [" + executionId + "]" );
    }

    /**
     * Gets the report key.
     *
     * @return the report key
     */
    public String getReportKey()
    {
        return reportKey;
    }

    /**
     * Sets the report key.
     *
     * @param reportKey
     *            the new report key
     */
    public void setReportKey( String reportKey )
    {
        this.reportKey = reportKey;
    }

    /**
     * Gets the wind tunnel report.
     *
     * @return the wind tunnel report
     */
    public String getWindTunnelReport()
    {
        return windTunnelReport;
    }

    /**
     * Sets the wind tunnel report.
     *
     * @param windTunnelReport
     *            the new wind tunnel report
     */
    public void setWindTunnelReport( String windTunnelReport )
    {
        this.windTunnelReport = windTunnelReport;
    }

    /**
     * Gets the device name.
     *
     * @return the device name
     */
    public String getDeviceName()
    {
        return deviceName;
    }

    /**
     * Sets the device name.
     *
     * @param deviceName
     *            the new device name
     */
    public void setDeviceName( String deviceName )
    {
        this.deviceName = deviceName;
    }

    /**
     * Gets the web driver.
     *
     * @return the web driver
     */
    public WebDriver getWebDriver()
    {
        return webDriver;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.gesture.factory.NativeDriverProvider#getNativeDriver()
     */
    @Override
    public WebDriver getNativeDriver()
    {
        return webDriver;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#get(java.lang.String)
     */
    public void get( String url )
    {
        setLastAction();
        webDriver.get( url );
        
        try
        {
            double outerHeight = Integer.parseInt( executeScript( "return window.outerHeight;" ) + "" );
            double outerWidth = Integer.parseInt( executeScript( "return window.outerWidth;" ) + "" );
            
            Dimension windowSize = manage().window().getSize();
            Object f = executeScript( "return window.outerHeight;" );
            heightModifier = (double) windowSize.getHeight() / outerHeight;
            widthModifier = (double) windowSize.getWidth() / outerWidth;
            
        }
        catch( Exception e )
        {
            log.warn( "Could not extract height/width modifiers" );
            heightModifier = 1;
            widthModifier = 1;
        }
    }
    
    public int getModifiedX( int currentX )
    {
        return (int) (currentX * widthModifier);
    }
    
    public int getModifiedY( int currentY )
    {
        return (int) (currentY * heightModifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#getCurrentUrl()
     */
    public String getCurrentUrl()
    {
        setLastAction();
        return webDriver.getCurrentUrl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#getTitle()
     */
    public String getTitle()
    {
        setLastAction();
        return webDriver.getTitle();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#findElements(org.openqa.selenium.By)
     */
    public List<WebElement> findElements( By by )
    {
        setLastAction();
        if ( log.isInfoEnabled() )
            log.info( Thread.currentThread().getName() + ": Locating element using [" + by + "]" );
        
        
        if ( cachingEnabled && cachedDocument == null )
            cacheData();

        if ( cachingEnabled && cachedDocument != null )
        {
            try
            {
                XPath xPath = xPathFactory.newXPath();
                String path = by.toString();
                path = path.substring( path.indexOf( ": " ) + 2 );
                NodeList nodes = (NodeList) xPath.evaluate( path, cachedDocument, XPathConstants.NODESET );

                List<WebElement> elementList = new ArrayList<WebElement>( 10 );

                for ( int i = 0; i < nodes.getLength(); i++ )
                {
                    if ( reportingElement )
                        elementList.add( new ReportingWebElementAdapter( new CachedWebElement( this, webDriver, by, nodes.item( i ) ), this, by ) );
                    else
                        elementList.add( new CachedWebElement( this, webDriver, by, nodes.item( i ) ) );
                }

                return elementList;
            }
            catch ( Exception e )
            {
                log.warn( "Error reading from cache " + e.getMessage() );
                if ( !syntheticConnection )
                {
                    cachingEnabled = false;
                    cachedDocument = null;
                }
                else
                    return null;
            }
        }

        if ( reportingElement )
        {
            List<WebElement> currentList = webDriver.findElements( by );
            if ( !currentList.isEmpty() )
            {
                List<WebElement> newList = new ArrayList<WebElement>( currentList.size() );
                for ( WebElement wE : currentList )
                    newList.add( new ReportingWebElementAdapter( wE, this, by ) );
                return newList;
            }
            else
                return currentList;
            
        }
        else
            return webDriver.findElements( by );
    }

    public void clearCache()
    {
        cachedDocument = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#findElement(org.openqa.selenium.By)
     */
    public WebElement findElement( By by )
    {
        setLastAction();
        if ( log.isInfoEnabled() )
            log.info( Thread.currentThread().getName() + ": Locating element using [" + by + "]" );
        
        if ( by instanceof ByXPath )
        {
            if ( cachingEnabled && cachedDocument == null )
                cacheData();

            if ( cachingEnabled && cachedDocument != null )
            {
                try
                {
                    XPath xPath = xPathFactory.newXPath();
                    String path = by.toString();
                    path = path.substring( path.indexOf( ": " ) + 2 );
                    Node node = (Node) xPath.evaluate( path, cachedDocument, XPathConstants.NODE );

                    if ( node != null )
                    {
                        if ( reportingElement )
                            return new ReportingWebElementAdapter( new CachedWebElement( this, webDriver, by, node ), this, by );
                        else
                            return new CachedWebElement( this, webDriver, by, node );
                    }
                    else
                        cachedDocument = null;
                }
                catch ( Exception e )
                {
                    log.warn( "Error reading from cache ", e );
                    cachingEnabled = false;
                    cachedDocument = null;
                }
            }
        }
        
        
        
        if ( reportingElement )
        {
            try
            {
                executionContext.startStep( KeyWordStepFactory.instance().createStep( by.toString(), "SELENIUM", true, "EXISTS", "", false, StepFailure.IGNORE, false, "", "", "", 0, "", 0, "", "", "", null, "", false, false, "", "", null, "", "", null ), null, null );
                WebElement webElement = new ReportingWebElementAdapter( new MorelandWebElement( this, webDriver.findElement( by ) ), this, by );
                executionContext.completeStep( webElement == null ? StepStatus.FAILURE : StepStatus.SUCCESS, null );
                return webElement;
            }
            catch( Exception e )
            {
                executionContext.completeStep( StepStatus.FAILURE, e );
                return null;
            }
            
        }
        else
            return new MorelandWebElement( this, webDriver.findElement( by ) );
    }

    public boolean isCachingEnabled()
    {
        return cachingEnabled;
    }

    public void setCachingEnabled( boolean cachingEnabled )
    {
        this.cachingEnabled = cachingEnabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#close()
     */
    public void close()
    {
        setLastAction();
        if ( webDriver != null && webDriver instanceof AppiumDriver )
            try
            {
                ((AppiumDriver) webDriver).closeApp();
            }
            catch ( Exception e )
            {
            }
        webDriver.close();
        
        if ( keepAliveThread != null )
            keepAliveThread.setKeepAliveRunning( false );

        if ( diThread != null )
            diThread.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#quit()
     */
    public void quit()
    {
        setLastAction();
        webDriver.quit();
        
        if ( keepAliveThread != null )
            keepAliveThread.setKeepAliveRunning( false );
        
        if ( diThread != null )
            diThread.stop();
        
        try
        {
            String deviceQuietTime = DeviceManager.instance( getxFID() ).getConfigurationProperties().get( "xframium.deviceQuietTime" );
            
            if( deviceQuietTime != null )
            {
                if ( log.isInfoEnabled() )
                    log.info( "Device Quiet Time: " + deviceQuietTime + " seconds" );
                Thread.sleep( Integer.parseInt( deviceQuietTime ) * 1000 );
            }
        }
        catch( Exception e )
        {
            
        }
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#getWindowHandles()
     */
    public Set<String> getWindowHandles()
    {
        setLastAction();
        return webDriver.getWindowHandles();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#getWindowHandle()
     */
    public String getWindowHandle()
    {
        setLastAction();
        try
        {
            return webDriver.getWindowHandle();
        }
        catch ( Exception e )
        {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#switchTo()
     */
    public TargetLocator switchTo()
    {
        setLastAction();
        return webDriver.switchTo();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#navigate()
     */
    public Navigation navigate()
    {
        setLastAction();
        return webDriver.navigate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.WebDriver#manage()
     */
    public Options manage()
    {
        if ( deviceOptions == null )
            deviceOptions = new DeviceOptions( webDriver.manage() );
        return deviceOptions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.ContextAware#context(java.lang.String)
     */
    public WebDriver context( String newContext )
    {
        setLastAction();
        if ( !contextSwitchSupported )
            return webDriver;

        if ( newContext == null || newContext.equals( currentContext ) )
            return webDriver;

        if ( webDriver != null )
        {

            if ( webDriver instanceof RemoteWebDriver )
            {
                log.info( "Switching context to " + newContext );
                RemoteExecuteMethod executeMethod = new RemoteExecuteMethod( (RemoteWebDriver) webDriver );
                Map<String, String> params = new HashMap<String, String>( 5 );
                params.put( "name", newContext );
                executeMethod.execute( DriverCommand.SWITCH_TO_CONTEXT, params );
            }
            else if ( webDriver instanceof AppiumDriver )
            {
                log.info( "Switching context to " + newContext );
                ((AppiumDriver) webDriver).context( newContext );
            }
            else
                return null;

            if ( newContext.equals( _getContext() ) )
                currentContext = newContext;
            else
                throw new IllegalStateException( "Could not change context to " + newContext + " against " + webDriver );
        }

        return webDriver;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.ContextAware#getContext()
     */
    public String getContext()
    {
        setLastAction();
        if ( currentContext != null || !contextSwitchSupported )
            return currentContext;

        currentContext = _getContext();

        if ( currentContext == null )
            currentContext = getPopulatedDevice().getDriverType();
        
        return currentContext;
    }

    /**
     * _get context.
     *
     * @return the string
     */
    private String _getContext()
    {
        if ( webDriver != null )
        {
            try
            {
                if ( webDriver instanceof RemoteWebDriver )
                {
                    RemoteExecuteMethod executeMethod = new RemoteExecuteMethod( (RemoteWebDriver) webDriver );
                    return (String) executeMethod.execute( DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null );
                }
                else if ( webDriver instanceof AppiumDriver )
                {
                    return ((AppiumDriver) webDriver).getContext();
                }
            }
            catch ( Exception e )
            {
                log.warn( "Context Switches are not supported - " + e.getMessage() );
                contextSwitchSupported = false;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.ContextAware#getContextHandles()
     */
    public Set<String> getContextHandles()
    {
        setLastAction();
        if ( contextHandles != null )
            return contextHandles;

        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod( (RemoteWebDriver) webDriver );
        List<String> handleList = (List<String>) executeMethod.execute( DriverCommand.GET_CONTEXT_HANDLES, null );

        contextHandles = new HashSet<String>( 10 );
        contextHandles.addAll( handleList );
        return contextHandles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openqa.selenium.remote.ExecuteMethod#execute(java.lang.String,
     * java.util.Map)
     */
    public Object execute( String commandName, Map<String, ?> parameters )
    {
        setLastAction();
        if ( webDriver instanceof RemoteWebDriver )
        {
            // RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(
            // (RemoteWebDriver) webDriver );
            return ((RemoteWebDriver) webDriver).executeScript( commandName, parameters );
            // return executeMethod.execute( commandName, parameters );
        }
        else
            throw new IllegalArgumentException( "Attempting to execution a remote command on an unsupported driver" );
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.morelandLabs.spi.PropertyProvider#getProperty(java.lang.String)
     */
    @Override
    public String getProperty( String propertyName )
    {
        String returnValue = propertyMap.get( propertyName );

        if ( returnValue != null )
            return returnValue;

        switch ( propertyName )
        {
            case EXECUTION_ID:
                return executionId;

            case REPORT_KEY:
                return reportKey;

            case DEVICE_NAME:
                return deviceName;

        }
        return null;
    }

    @Override
    public void setProperty( String name, String value )
    {
        if ( name != null && value != null )
            propertyMap.put( name, value );

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.openqa.selenium.TakesScreenshot#getScreenshotAs(org.openqa.selenium.
     * OutputType)
     */
    @Override
    public <X> X getScreenshotAs( OutputType<X> target ) throws WebDriverException
    {
        setLastAction();
        if ( webDriver instanceof TakesScreenshot )
            return ((TakesScreenshot) webDriver).getScreenshotAs( target );
        else
            throw new IllegalArgumentException( "Screenshot functionality not supported" );
    }

    //
    // JavascriptExecutor Implementation
    //

    public Object executeScript( String script, Object... args )
    {
        setLastAction();
        return ((JavascriptExecutor) webDriver).executeScript( script, args );
    }

    public Object executeAsyncScript( String script, Object... args )
    {
        setLastAction();
        return ((JavascriptExecutor) webDriver).executeAsyncScript( script, args );
    }

    @Override
    public Keyboard getKeyboard()
    {
        setLastAction();
        if ( webDriver instanceof HasInputDevices )
            return ((HasInputDevices) webDriver).getKeyboard();
        else
            return null;
    }

    @Override
    public Mouse getMouse()
    {
        setLastAction();
        if ( webDriver instanceof HasInputDevices )
            return ((HasInputDevices) webDriver).getMouse();
        else
            return null;
    }

    @Override
    public Capabilities getCapabilities()
    {
        if ( webDriver instanceof HasCapabilities )
            return ((HasCapabilities) webDriver).getCapabilities();
        else
            return null;
    }

    @Override
    public TouchScreen getTouch()
    {
        setLastAction();
        if ( webDriver instanceof HasTouchScreen )
            return ((HasTouchScreen) webDriver).getTouch();
        else
            return null;
    }
    
    
    
    public class DeviceTimeouts implements Timeouts
    {
        private Timeouts timeouts;
        public DeviceTimeouts( Timeouts timeouts )
        {
             this.timeouts = timeouts;
        }
        
        @Override
        public Timeouts implicitlyWait( long time, TimeUnit unit )
        {
            
            timeouts.implicitlyWait( time, unit );
            implicitWait = unit.toMillis( time );
            if ( log.isInfoEnabled() )
                log.info( "Setting IMPLICIT WAIT to " + implicitWait );
            return this;
        }

        @Override
        public Timeouts setScriptTimeout( long time, TimeUnit unit )
        {
            timeouts.setScriptTimeout( time, unit );
            scriptTimeout = unit.toMillis( time );
            return this;
        }

        @Override
        public Timeouts pageLoadTimeout( long time, TimeUnit unit )
        {
            timeouts.pageLoadTimeout( time, unit );
            pageLoadTimeout = unit.toMillis( time );
            return this;
        }
        
    }
    
    public class DeviceOptions implements Options
    {
        private Options options;
        public DeviceOptions( Options options )
        {
            this.options = options;
        }
        
        @Override
        public void addCookie( Cookie cookie )
        {
            options.addCookie( cookie );
            
        }

        @Override
        public void deleteCookieNamed( String name )
        {
            options.deleteCookieNamed( name );
            
        }

        @Override
        public void deleteCookie( Cookie cookie )
        {
            options.deleteCookie( cookie );
            
        }

        @Override
        public void deleteAllCookies()
        {
            options.deleteAllCookies();
            
        }

        @Override
        public Set<Cookie> getCookies()
        {
            return options.getCookies();
        }

        @Override
        public Cookie getCookieNamed( String name )
        {
            return options.getCookieNamed( name );
        }

        @Override
        public Timeouts timeouts()
        {
            if ( deviceTimeouts == null )
                deviceTimeouts = new DeviceTimeouts( options.timeouts() );
            return deviceTimeouts;
        }

        @Override
        public ImeHandler ime()
        {
            return options.ime();
        }

        @Override
        public Window window()
        {
            return options.window();
        }

        @Override
        public Logs logs()
        {
            return options.logs();
        }
        
    }
    
}
