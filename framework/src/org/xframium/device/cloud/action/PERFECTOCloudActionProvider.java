package org.xframium.device.cloud.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openqa.selenium.ContextAware;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.SimpleDevice;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.DeviceConfigurationException;
import org.xframium.exception.DeviceException;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.integrations.common.PercentagePoint;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.bean.Execution;
import org.xframium.integrations.perfectoMobile.rest.bean.Handset;
import org.xframium.integrations.perfectoMobile.rest.bean.ImageExecution;
import org.xframium.integrations.perfectoMobile.rest.bean.Item;
import org.xframium.integrations.perfectoMobile.rest.bean.ItemCollection;
import org.xframium.integrations.perfectoMobile.rest.services.Repositories.RepositoryType;
import org.xframium.integrations.perfectoMobile.rest.services.WindTunnel.TimerPolicy;
import org.xframium.page.BY;
import org.xframium.page.element.Element;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;
import edu.emory.mathcs.backport.java.util.Collections;

public class PERFECTOCloudActionProvider extends AbstractCloudActionProvider
{
    private static final Pattern REPO_PATTERN = Pattern.compile( "\\?(\\w*):\\(([^\\\\)]*)\\)(\\w*)(?::\\((.*))*" );
    
	/** The Constant PLATFORM_NAME. */
	public static final String PLATFORM_NAME = "platformName";
	
	private String createTimerId( Element element )
	{
	    StringBuilder stringBuilder = new StringBuilder();
	    if ( element != null )
	        stringBuilder.append( element.getPageName() ).append( "." ).append( element.getName() );
	    else
	        stringBuilder.append( "Unknown Action" );
	    
	    return stringBuilder.toString();
	}
	
	@Override
	public Rectangle findImage( DeviceWebDriver webDriver, String imageName, Map<String, String> propertyMap )
	{
	    
	    ImageExecution imageExec = PerfectoMobile.instance( webDriver.getxFID() ).imaging().imageExists( webDriver.getExecutionId(), webDriver.getDeviceName(), imageName, propertyMap );
	    
	    if ( imageExec != null && Boolean.parseBoolean( imageExec.getStatus() ) )
	        return new Rectangle( new Point( Integer.parseInt( imageExec.getLeft() ), Integer.parseInt( imageExec.getTop() ) ), new Dimension( Integer.parseInt( imageExec.getWidth() ), Integer.parseInt( imageExec.getHeight() ) ) );
	    
	    return null;
	}
	
	@Override
	public Rectangle findText( DeviceWebDriver webDriver, String text, Map<String, String> propertyMap )
	{
	    ImageExecution imageExec = PerfectoMobile.instance( webDriver.getxFID() ).imaging().textExists( webDriver.getExecutionId(), webDriver.getDeviceName(), text, propertyMap );
	    
	    
	    if ( imageExec != null && Boolean.parseBoolean( imageExec.getStatus() ) )
            return new Rectangle( new Point( Integer.parseInt( imageExec.getLeft() ), Integer.parseInt( imageExec.getTop() ) ), new Dimension( Integer.parseInt( imageExec.getWidth() ), Integer.parseInt( imageExec.getHeight() ) ) );
	    
	    return null;
	}
	
	private Document getExecutionReport( DeviceWebDriver webDriver )
	{
	    try
        {  
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware( true );
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse( getReport( webDriver, "xml" ) );
        }
        catch (Exception e)
        {
            log.error( "Error download artifact data - " + e.getMessage());
            return null;
        }
	}
	
	public InputStream getReport( DeviceWebDriver webDriver, String reportType )
	{
	    try
        {
            CloudDescriptor currentCloud = webDriver.getCloud();
            
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append( "https://" ).append( currentCloud.getHostName() ).append( "/services/reports/" ).append( webDriver.getReportKey());
            urlBuilder.append( "?operation=download" ).append( "&user=" ).append( currentCloud.getUserName() ).append( "&password=" ).append( currentCloud.getPassword() );
            urlBuilder.append( "&format=xml" );
            
            return new URL( urlBuilder.toString() ).openStream();
        }
        catch (Exception e)
        {
            log.error( "Error downloading PERFECT execution report " + e.getMessage());
            return null;
        }
	}
	
	@Override
	public String getLog( DeviceWebDriver webDriver )
	{
	    try
        {
            Document xmlDocument = getExecutionReport( webDriver );
            
            NodeList nodeList = getNodes( xmlDocument, "//dataItem[@type='log']/attachment" );
            if ( nodeList != null && nodeList.getLength() > 0 )
            {
                byte[] zipFile = PerfectoMobile.instance( webDriver.getxFID() ).reports().download( webDriver.getReportKey() , nodeList.item( 0 ).getTextContent(), false );
                ZipInputStream zipStream = new ZipInputStream( new ByteArrayInputStream( zipFile ) );
                ZipEntry entry = zipStream.getNextEntry();
                
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] bytesIn = new byte[ 512 ];
                int bytesRead = 0;
                while ( ( bytesRead = zipStream.read( bytesIn ) ) != -1 )
                {
                    outputStream.write( bytesIn, 0, bytesRead );
                }
                
                zipStream.close();
                
                return outputStream.toString();
            }
            
            return null;
            
        }
        catch( Exception e )
        {
            log.error( "Error download device log data", e );
        }
        return null;
	}
	
	@Override
    public String getVitals( DeviceWebDriver webDriver )
    {
        try
        {
            Document xmlDocument = getExecutionReport( webDriver );
            
            NodeList nodeList = getNodes( xmlDocument, "//dataItem[@type='monitor']/attachment" );
            if ( nodeList != null && nodeList.getLength() > 0 )
            {
                byte[] zipFile = PerfectoMobile.instance( webDriver.getxFID() ).reports().download( webDriver.getReportKey() , nodeList.item( 0 ).getTextContent(), false );
                
                
                return new String( zipFile );
            }
            
            return null;
            
        }
        catch( Exception e )
        {
            log.error( "Error download device log data", e );
        }
        return null;
    }

	@Override
	public void tap( DeviceWebDriver webDriver, PercentagePoint location, int lengthInMillis )
	{
	    PerfectoMobile.instance( webDriver.getxFID() ).gestures().tap( webDriver.getExecutionId(), webDriver.getDeviceName(), location, lengthInMillis / 1000 );
	}
	
	@Override
	public boolean getSupportedTimers( DeviceWebDriver webDriver, String timerId, ExecutionContextTest executionContext, String type )
	{
	    if( timerId == null )
	        return false;

	    executionContext.getStep().addTiming( "perfecto.timerStart", executionContext.getTimerStart() );
	    executionContext.getStep().addTiming( (type != null && !type.isEmpty()) ? "ux." + type : "ux", PerfectoMobile.instance( webDriver.getxFID() ).windTunnel().getTimer( webDriver.getExecutionId(), timerId, "ux", "milliseconds" ).getReturnValue() );
	    executionContext.getStep().addTiming( (type != null && !type.isEmpty()) ? "device." + type : "device", PerfectoMobile.instance( webDriver.getxFID() ).windTunnel().getTimer( webDriver.getExecutionId(), timerId, "device", "milliseconds" ).getReturnValue() );
	    executionContext.getStep().addTiming( (type != null && !type.isEmpty()) ? "system." + type : "system", PerfectoMobile.instance( webDriver.getxFID() ).windTunnel().getTimer( webDriver.getExecutionId(), timerId, "system", "milliseconds" ).getReturnValue() );
	    executionContext.getStep().addTiming( (type != null && !type.isEmpty()) ? "elapsed." + type : "elapsed", PerfectoMobile.instance( webDriver.getxFID() ).windTunnel().getTimer( webDriver.getExecutionId(), timerId, "elapsed", "milliseconds" ).getReturnValue() );
	    stopTimer( webDriver, timerId, executionContext );
	    executionContext.getStep().addTiming( "perfecto.timerStop", System.currentTimeMillis() );
	    return true;
	}
	
	@Override
	public String startTimer( DeviceWebDriver webDriver, Element element, ExecutionContextTest executionContext )
	{
	    String timerId = createTimerId( element );
	    PerfectoMobile.instance( webDriver.getxFID() ).windTunnel().startTimer( webDriver.getExecutionId(), timerId, TimerPolicy.reset ).getReturnValue();
	    executionContext.setTimerName( timerId );
	    return timerId;
	}
	
	@Override
	public void stopTimer( DeviceWebDriver webDriver, String timerId, ExecutionContextTest executionContext )
	{
	    PerfectoMobile.instance( webDriver.getxFID() ).windTunnel().stopTimer( webDriver.getExecutionId(), timerId );
	    executionContext.clearTimer();

	}
	
	
	@Override
    public boolean startApp( DeviceWebDriver webDriver, String executionId, String deviceId, String appName, String appIdentifier )
    {
        PerfectoMobile.instance( webDriver.getxFID() ).application().open( executionId, deviceId, appName, appIdentifier );
        return true;
    }

    @Override
    public boolean popuplateDevice( DeviceWebDriver webDriver, String deviceId, Device device, String xFID )
    {
        try
        {
            Handset handSet = PerfectoMobile.instance( xFID ).devices().getDevice( deviceId );

            device.setOs( handSet.getOs() );
            if ( device.getOs().toLowerCase().equals( "android" ) || device.getOs().toLowerCase().equals( "ios" ) )
                device.setModel( handSet.getModel() );
            
            device.setOsVersion( handSet.getOsVersion() );
            device.setResolution( handSet.getResolution() );
            device.setManufacturer( handSet.getManufacturer() );
            device.setDeviceName( handSet.getDeviceId() );
            
            ((SimpleDevice) device).setDeviceName( deviceId );
            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }
    
    @Override
    public boolean isDescriptorSupported( BY descriptorType )
    {
        return true;
    }
    
    @Override
    public String getExecutionId( DeviceWebDriver webDriver )
    {
        return webDriver.getCapabilities().getCapability( "executionId" ) + "";
    }
    
    @Override
    public void disableLogging( DeviceWebDriver webDriver )
    {
        PerfectoMobile.instance( webDriver.getxFID() ).device().startDebug( webDriver.getExecutionId(), webDriver.getDeviceName() );
        
    }
    
    @Override
    public void enabledLogging( DeviceWebDriver webDriver )
    {
        PerfectoMobile.instance( webDriver.getxFID() ).device().stopDebug( webDriver.getExecutionId(), webDriver.getDeviceName() );
        
    }

    @Override
	public String getCloudPlatformName(Device device) {
		// TODO Auto-generated method stub
		return PLATFORM_NAME;
	}
	
	@Override
	public String getCloudBrowserName(String currBrowserName) {
		if(currBrowserName.equalsIgnoreCase("Chrome")){
			return "Chrome";
		}else if (currBrowserName.equalsIgnoreCase("internet explorer")||currBrowserName.equalsIgnoreCase("internetexplorer")){
			return "Internet Explorer";
		}else if(currBrowserName.equalsIgnoreCase("firefox")){
			return "Firefox";
		}else{
			return currBrowserName;
		}
	}

    @Override
    public boolean installApplication( String applicationName, DeviceWebDriver webDriver, boolean instrumentApp, boolean instrumentSensor )
    {
        ApplicationDescriptor appDesc = ApplicationRegistry.instance(webDriver.getxFID()).getApplication( applicationName );
    
        Handset localDevice = PerfectoMobile.instance( webDriver.getxFID() ).devices().getDevice( webDriver.getPopulatedDevice().getDeviceName() );
        
        Execution appExec = null;
        
        if ( localDevice.getOs().toLowerCase().equals( "ios" ) )                
            appExec = PerfectoMobile.instance( webDriver.getxFID() ).application().install( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), getInstallLocation( appDesc.getIosInstallation(), webDriver.getxFID() ), instrumentApp ? "instrument" : "noinstrument", instrumentSensor ? "sensor" : "nosensor" );
        else if ( localDevice.getOs().toLowerCase().equals( "android" ) )
            appExec = PerfectoMobile.instance( webDriver.getxFID() ).application().install( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), getInstallLocation( appDesc.getAndroidInstallation(), webDriver.getxFID() ), instrumentApp ? "instrument" : "noinstrument", instrumentSensor ? "sensor" : "nosensor" );
        else
            throw new DeviceConfigurationException( "Could not install application to " + webDriver.getPopulatedDevice().getEnvironment() + "(" + webDriver.getDevice().getDeviceName() + ")" );
        
        if ( appExec != null )
        {
            if ( appExec.getStatus().toLowerCase().equals( "success" ) )
                return true;
            else
                throw new DeviceConfigurationException( "Failed to install application " + appDesc.getName() );
        }
        else 
            throw new DeviceConfigurationException( "Failed to install application " + appDesc.getName() );
        
    }
    
    private class RegexComparator implements Comparator<String>
    {
        private Pattern regex;
        public RegexComparator( Pattern regex )
        {
            this.regex = regex;
        }
        
        @Override
        public int compare( String o1, String o2 )
        {
            Matcher m1 = regex.matcher( o2 );
            Matcher m2 = regex.matcher( o1 );
            
            if ( m1.find() && m2.find() )
            {
                for ( int i=1; i<=m1.groupCount(); i++ )
                {
                    int compareValue = m1.group( i ).compareTo( m2.group( i ) );
                    if ( compareValue != 0 )
                        return compareValue;
     
                }
            }
            
            return 0;
        }
    }
    
    private class AnyComparator implements Comparator<String>
    {

        @Override
        public int compare( String o1, String o2 )
        {
            return o2.compareTo( o1 );
        }
        
    }
    
    private String getInstallLocation( String installLocation, String xFID )
    {

        if ( installLocation.startsWith( "?" ) )
        {
            //
            // This is a repository lookup
            //
            Matcher m = REPO_PATTERN.matcher( installLocation );
            if ( !m.matches() )
                throw new IllegalArgumentException( "Your install location must be in the format of LATEST:(LOCATION)REGEX|ANY:(FILTER CRITERIA)");
            
            String lookupMethod = m.group( 1 );
            if ( !lookupMethod.equals( "LATEST" ) )
                throw new IllegalArgumentException( "Unsupported lookup method: " + lookupMethod );
            
            String location = m.group( 2 );
            String filterType = m.group( 3 );
            
            String filter = null;
            if ( filterType.equals( "REGEX" ) )
                filter = m.group( 4 ).substring( 0, m.group( 4 ).length() - 1 );
            
            ItemCollection itemList = PerfectoMobile.instance( xFID ).repositories().list( RepositoryType.MEDIA, location );
            
            List<String> fileList = new ArrayList<String>( 10 );
            
            if ( itemList != null && itemList.getItemList() != null )
            {
                for ( Item item : itemList.getItemList() )
                    fileList.add( item.getTextContext() );
            }
            
            System.out.println( fileList );
            
            Iterator<String> fI = fileList.iterator();
            switch( filterType )
            {
                case "REGEX":
                    //
                    // First, remove any who does not match
                    //
                    Pattern filterPattern = Pattern.compile( filter );
                    
                    while ( fI.hasNext() )
                    {
                        String currentValue = fI.next();
                        if ( !filterPattern.matcher( currentValue ).matches() )
                            fI.remove();
                    }
                    
                    //
                    // Then order each individual group
                    //
                    Collections.sort( fileList, new RegexComparator( filterPattern ) );
                    break;
                    
                    
                case "ANY":
                    while ( fI.hasNext() )
                    {
                        String currentValue = fI.next();
                        if ( currentValue.equals( location ) )
                            fI.remove();
                    }
                    Collections.sort( fileList, new AnyComparator() );
                    break;
            }
            
            return fileList.get( 0 );
        }
        else
            return installLocation;
    }

    @Override
    public boolean uninstallApplication( String applicationName, DeviceWebDriver webDriver )
    {
        ApplicationDescriptor appDesc = ApplicationRegistry.instance(webDriver.getxFID()).getApplication( applicationName );
    
        Handset localDevice = PerfectoMobile.instance( webDriver.getxFID() ).devices().getDevice( webDriver.getPopulatedDevice().getDeviceName() );
        
        if ( localDevice.getOs().toLowerCase().equals( "ios" ) )                
            PerfectoMobile.instance( webDriver.getxFID() ).application().uninstall( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appDesc.getAppleIdentifier() );
        else if ( localDevice.getOs().toLowerCase().equals( "android" ) )
            PerfectoMobile.instance( webDriver.getxFID() ).application().uninstall( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appDesc.getAndroidIdentifier() );
        else
            throw new DeviceException( "Could not uninstall application from " + localDevice.getOs() );
        return true;
    }

    @Override
    public boolean openApplication( String applicationName, DeviceWebDriver webDriver, String xFID )
    {

        ApplicationDescriptor appDesc = ApplicationRegistry.instance(xFID).getApplication( applicationName );
        
        if ( appDesc == null )
            throw new ScriptConfigurationException( "The Application " + applicationName + " does not exist" );
    
        if ( appDesc.isWeb() )
        {
            //String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,"t");
            //if ( webDriver.getWindowHandles() != null && webDriver.getWindowHandles().size() > 0 )
            //    webDriver.findElement(By.tagName("body")).sendKeys(selectLinkOpeninNewTab);
            
            webDriver.get( appDesc.getUrl() );
                
        }
        else
        {
            Handset localDevice = PerfectoMobile.instance( xFID ).devices().getDevice( webDriver.getPopulatedDevice().getDeviceName() );
            Execution appExec = null;
            
            String[] applicationArray = null;
            
            if ( localDevice.getOs().toLowerCase().equals( "ios" ) )
                applicationArray = appDesc.getAppleIdentifier().split( ";" );
            else if ( localDevice.getOs().toLowerCase().equals( "android" ) )    
                applicationArray = appDesc.getAndroidIdentifier().split( ";" );
            else
                throw new ScriptException( "Could not install application to " + localDevice.getOs() );
            
            
            for ( String appId : applicationArray )
            {
                log.info( "Attempting to launch [" + appId + "] on [" + localDevice.getOs() + "]" );
                appExec = PerfectoMobile.instance( xFID).application().open( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appId );
                if ( appExec != null )
                {
                    if ( appExec.getStatus().toLowerCase().equals( "success" ) )
                    {
                        if ( webDriver instanceof ContextAware )
                            ( ( ContextAware ) webDriver ).context( "NATIVE_APP" );
                        return true;
                    }
                }
            }
            
            throw new ScriptException( "Failed to launch application " + appDesc.getName() );
        }
        return true;
    }

    @Override
    public boolean closeApplication( String applicationName, DeviceWebDriver webDriver )
    {

        ApplicationDescriptor appDesc = ApplicationRegistry.instance(webDriver.getxFID()).getApplication( applicationName );
    
        Handset localDevice = PerfectoMobile.instance( webDriver.getxFID() ).devices().getDevice( webDriver.getPopulatedDevice().getDeviceName() );
        
        if ( localDevice.getOs().toLowerCase().equals( "ios" ) )                
            PerfectoMobile.instance( webDriver.getxFID() ).application().close( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appDesc.getAppleIdentifier() );
        else if ( localDevice.getOs().toLowerCase().equals( "android" ) )
            PerfectoMobile.instance( webDriver.getxFID() ).application().close( webDriver.getExecutionId(), webDriver.getPopulatedDevice().getDeviceName(), appDesc.getName(), appDesc.getAndroidIdentifier() );
        else
            log.warn( "Could not close application on " + localDevice.getOs() );
        return true;
    }
    
}
