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
package org.xframium.device.factory.spi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.xframium.Initializable;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudDescriptor.ProviderType;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.AbstractDriverFactory;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.reporting.ExecutionContext;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating WEBDriver objects.
 */
public class WEBDriverFactory extends AbstractDriverFactory
{

    private boolean isBlank( String value )
    {
        if ( value == null )
            return true;
        
        if ( value.trim().isEmpty() )
            return true;
        
        return false;
                    
                    
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.device.factory.AbstractDriverFactory#_createDriver(com
     * .perfectoMobile.device.Device)
     */
    @Override
    protected DeviceWebDriver _createDriver( Device currentDevice, CloudDescriptor useCloud, String xFID )
    {
        DeviceWebDriver webDriver = null;
        try
        {
            DesiredCapabilities dc = null;
            
            if ( currentDevice.getBrowserName() != null && !currentDevice.getBrowserName().isEmpty() )
                dc = new DesiredCapabilities(  useCloud.getCloudActionProvider().getCloudBrowserName(currentDevice.getBrowserName()), "", Platform.ANY );
            else
                dc = new DesiredCapabilities( "", "", Platform.ANY );
            
            
            DeviceManager.instance( xFID ).setCurrentCloud( useCloud );
            
            

            if ( currentDevice.getDeviceName() != null && !currentDevice.getDeviceName().isEmpty() )
            {
                dc.setCapability( ID, currentDevice.getDeviceName() );
                dc.setCapability( USER_NAME, useCloud.getUserName() );
                dc.setCapability( PASSWORD, useCloud.getPassword() );
            }
            else
            {
                if (!useCloud.isEmbedded())
                {
                    if ( !isBlank( currentDevice.getOs() ) )
                        dc.setCapability( useCloud.getCloudActionProvider().getCloudPlatformName(currentDevice), currentDevice.getOs() );
                    
                    if ( !isBlank( currentDevice.getOsVersion() ) )
                        dc.setCapability( PLATFORM_VERSION, currentDevice.getOsVersion() );
                    
                    if ( !isBlank( currentDevice.getModel()) )
                        dc.setCapability( MODEL, currentDevice.getModel() );
                    
                    if ( !isBlank( useCloud.getUserName() ) )
                        dc.setCapability( USER_NAME, useCloud.getUserName() );
                    
                    if ( !isBlank( useCloud.getPassword() ) )
                        dc.setCapability( PASSWORD, useCloud.getPassword() );
                    
                    
                }
            }

            if ( !isBlank( currentDevice.getBrowserName() ) )
                dc.setCapability( BROWSER_NAME,  useCloud.getCloudActionProvider().getCloudBrowserName(currentDevice.getBrowserName()) );
            
            if ( !useCloud.isEmbedded() )
            {
                if ( !isBlank( currentDevice.getBrowserVersion() ) )
                    dc.setCapability( BROWSER_VERSION, currentDevice.getBrowserVersion() );
            }
            
            for ( String name : currentDevice.getCapabilities().keySet() )
				dc = setCapabilities(currentDevice.getCapabilities().get(name), dc, name);
			if ( ApplicationRegistry.instance( xFID ).getAUT() != null )
			{
                for ( String name : ApplicationRegistry.instance( xFID ).getAUT().getCapabilities().keySet() )
                	dc = setCapabilities(ApplicationRegistry.instance( xFID ).getAUT().getCapabilities().get( name ), dc, name);
			}
			
			if ( ProviderType.BROWSERSTACK.equals( ProviderType.valueOf( useCloud.getProvider() ) ) )
            {
                dc.setCapability( "project", ExecutionContext.instance(xFID).getSuiteName() );
                if ( KeyWordDriver.instance(xFID).getTags() != null && KeyWordDriver.instance(xFID).getTags().length > 0 )                	
                    dc.setCapability( "build", KeyWordDriver.instance(xFID).getTags()[ 0 ] );
                dc.setCapability( "name", currentDevice.getCapabilities().get( "_testName" ) );
            }
			

			if ( useCloud.isEmbedded() )
			{
			    
			    if ( currentDevice.getBrowserName() != null )
			    {
			        if ( log.isInfoEnabled() )
			            log.info( "Configuring LOCAL selenium grid" );
			        //
			        // Automatically download and reference the driver server if using embedded
			        //
			        File driverFile;
			        switch( currentDevice.getBrowserName().toLowerCase() )
			        {
			            case "firefox":
			                
			                if ( log.isInfoEnabled() )
		                        log.info( "webdriver.gecko.driver=" +  System.getProperty( "webdriver.gecko.driver" ) );
			                
			                if ( System.getProperty( "webdriver.gecko.driver" ) == null )
			                {
			                    driverFile = new File( DataManager.instance( xFID ).getReportFolder(), "geckodriver-" + Initializable.VERSION + ".exe" );
			                    
			                    if ( !driverFile.exists() )
			                    {
			                        log.warn( "Downloading http://xframium.org/driver/geckodriver.exe to " + driverFile.getAbsolutePath() );
			                        driverFile = downloadFile( new URL( "http://xframium.org/driver/geckodriver-" + Initializable.VERSION + ".exe" ), driverFile );
			                    }
			                    
			                    if ( driverFile.exists() )
			                        System.setProperty( "webdriver.gecko.driver", driverFile.getAbsolutePath() );
			                    
			                }
			                break;
			                
			            case "chrome":
			                if ( log.isInfoEnabled() )
                                log.info( "webdriver.chrome.driver=" +  System.getProperty( "webdriver.chrome.driver" ) );
			                if ( System.getProperty( "webdriver.chrome.driver" ) == null )
                            {
			                    driverFile = new File( DataManager.instance( xFID ).getReportFolder(), "chromedriver-" + Initializable.VERSION + ".exe" );
			                    
                                if ( !driverFile.exists() )
                                {
                                    log.warn( "Downloading http://xframium.org/driver/chromedriver.exe to " + driverFile.getAbsolutePath() );
                                    driverFile = downloadFile( new URL( "http://xframium.org/driver/chromedriver-" + Initializable.VERSION + ".exe" ), driverFile );
                                }
                                
                                if ( driverFile.exists() )
                                    System.setProperty( "webdriver.chrome.driver", driverFile.getAbsolutePath() );
                            }
                            break;
			            case "internet explorer":
			                if ( log.isInfoEnabled() )
                                log.info( "webdriver.ie.driver=" +  System.getProperty( "webdriver.ie.driver" ) );
			                if ( System.getProperty( "webdriver.ie.driver" ) == null )
                            {
			                    driverFile = new File( DataManager.instance( xFID ).getReportFolder(), "IEDriverServer-" + Initializable.VERSION + ".exe" );
			                    
                                if ( !driverFile.exists() )
                                {
                                    log.warn( "Downloading http://xframium.org/driver/IEDriverServer.exe to " + driverFile.getAbsolutePath() );
                                    driverFile = downloadFile( new URL( "http://xframium.org/driver/IEDriverServer-" + Initializable.VERSION + ".exe" ), driverFile );
                                }
                                
                                if ( driverFile.exists() )
                                    System.setProperty( "webdriver.ie.driver", driverFile.getAbsolutePath() );
                            }
                            break;
			        }
			    }
			}
			
			URL hubUrl = new URL( useCloud.getCloudUrl( dc ) );
            if ( log.isDebugEnabled() )
                log.debug( Thread.currentThread().getName() + ": Acquiring Device as: \r\n" + capabilitiesToString( dc ) + "\r\nagainst " + hubUrl );
            
            webDriver = new DeviceWebDriver( new RemoteWebDriver( hubUrl, dc ), DeviceManager.instance( xFID ).isCachingEnabled(), currentDevice, dc );
            webDriver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );

            Capabilities caps = ((RemoteWebDriver) webDriver.getWebDriver()).getCapabilities();
            
            webDriver.setExecutionId( useCloud.getCloudActionProvider().getExecutionId( webDriver ) );
            webDriver.setReportKey( caps.getCapability( "reportKey" ) + "" );
            webDriver.setDeviceName( caps.getCapability( "deviceName" ) + "" );
            if ( useCloud.getProvider().equals( "PERFECTO" ) && caps.getCapability( "windTunnelReportUrl" ) != null )
                webDriver.setWindTunnelReport( caps.getCapability( "windTunnelReportUrl" ).toString() );
            
            webDriver.setCloud( useCloud );

            if ( ApplicationRegistry.instance( xFID ).getAUT() != null && ApplicationRegistry.instance( xFID ).getAUT().getUrl() != null && !ApplicationRegistry.instance( xFID ).getAUT().getUrl().isEmpty() )
            {
                if ( ApplicationRegistry.instance( xFID ).getAUT().isAutoStart() )
                    useCloud.getCloudActionProvider().openApplication( ApplicationRegistry.instance( xFID ).getAUT().getName(), webDriver, xFID );
                
                webDriver.setAut( ApplicationRegistry.instance( xFID ).getAUT(), xFID );
                String interruptString = ApplicationRegistry.instance( xFID ).getAUT().getCapabilities().get( "deviceInterrupts" )  != null ? (String)ApplicationRegistry.instance( xFID ).getAUT().getCapabilities().get( "deviceInterrupts" ) : DeviceManager.instance( xFID ).getDeviceInterrupts();
                webDriver.setDeviceInterrupts( getDeviceInterrupts( interruptString, webDriver.getExecutionId(), webDriver.getDeviceName() ) );
            }

            return webDriver;
        }
        catch ( Exception e )
        {
            log.fatal( "Could not connect to " + currentDevice + " (" + e.getMessage() + ")", e );
            log.debug( e );
            if ( webDriver != null )
            {
                try
                {
                    webDriver.close();
                }
                catch ( Exception e2 )
                {
                }
                try
                {
                    webDriver.quit();
                }
                catch ( Exception e2 )
                {
                }
            }
            return null;
        }
    }
    
    private File downloadFile( URL urlData, File outputFile )
    {
        log.warn( "Attempting to dowbnload driver from " + urlData + " as " + outputFile.toString() );
        InputStream inputStream = null;
        OutputStream outputStream = null;
        
        try
        {
            outputFile.getParentFile().mkdirs();
            byte[] buffer = new byte[ 512 ];
            int bytesRead = 0;
            inputStream = urlData.openStream();
            outputStream = new FileOutputStream( outputFile );
            
            while ( ( bytesRead = inputStream.read( buffer ) ) > 0 )
            {
                outputStream.write( buffer, 0, bytesRead );
            }
            
            
        }
        catch( Exception e )
        {
            log.error( "Error download driver", e );
        }
        finally
        {
            try { inputStream.close(); } catch( Exception e ) {}
            try { outputStream.flush(); } catch( Exception e ) {}
            try { outputStream.close(); } catch( Exception e ) {}
        }
        
        return outputFile;
        
    }
    
}
