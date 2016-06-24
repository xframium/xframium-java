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
package org.xframium.device.artifact.api;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.artifact.AbstractArtifactProducer;
import org.xframium.device.artifact.Artifact;

// TODO: Auto-generated Javadoc
/**
 * The Class PerfectoArtifactProducer.
 */
public class SeleniumArtifactProducer extends AbstractArtifactProducer
{

    /**
     * Instantiates a new perfecto artifact producer.
     */
    public SeleniumArtifactProducer()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * Instantiates a new perfecto artifact producer.
     *
     * @param reportFormat
     *            the report format
     */
    public SeleniumArtifactProducer( String reportFormat )
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.device.artifact.AbstractArtifactProducer#_getArtifact(
     * org.openqa.selenium.WebDriver,
     * com.perfectoMobile.device.artifact.ArtifactProducer.ArtifactType,
     * com.perfectoMobile.device.ConnectedDevice)
     */
    @Override
    protected Artifact _getArtifact( WebDriver webDriver, ArtifactType aType, ConnectedDevice connectedDevice, String testName, boolean success )
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.device.artifact.AbstractArtifactProducer#_getArtifact(
     * org.openqa.selenium.WebDriver,
     * com.perfectoMobile.device.artifact.ArtifactProducer.ArtifactType,
     * java.util.Map, com.perfectoMobile.device.ConnectedDevice)
     */
    @Override
    protected Artifact _getArtifact( WebDriver webDriver, ArtifactType aType, Map<String, String> parameterMap, ConnectedDevice connectedDevice, String testName, boolean success )
    {
        String rootFolder = testName + System.getProperty( "file.separator" ) + connectedDevice.getDevice().getKey() + System.getProperty( "file.separator" );
        switch ( aType )
        {
            case FAILURE_SOURCE:
                return new Artifact( rootFolder + "failureDOM.xml", webDriver.getPageSource().getBytes() );
                
            case FAILURE_SOURCE_HTML:
            	return new Artifact( rootFolder + "failureDOM.html", ( "<html><head><link href=\"http://www.xframium.org/output/assets/css/prism.css\" rel=\"stylesheet\"><script src=\"http://www.xframium.org/output/assets/js/prism.js\"></script><body><pre class\"line-numbers\"><code class=\"language-markup\">" + webDriver.getPageSource().replace( "<", "&lt;" ).replace( ">", "&gt;" ).replace( "\t", "  ") + "</code></pre></body></html>" ).getBytes());
                
            case EXECUTION_DEFINITION:
				StringBuilder defBuilder = new StringBuilder();
				defBuilder.append( "DATE=" ).append( simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) ).append( "\r\n");
				defBuilder.append( "TIME=" ).append( timeFormat.format( new Date( System.currentTimeMillis() ) ) ).append( "\r\n");
				defBuilder.append( "TEST_CASE=" ).append( testName ).append( "\r\n");
				defBuilder.append( "DEVICE=" ).append( connectedDevice.getDevice().getKey() ).append( "\r\n");
				defBuilder.append( "SUCCESS=" ).append( success ).append( "\r\n");
				defBuilder.append( "MANUFACTURER=" ).append( connectedDevice.getDevice().getManufacturer() ).append( "\r\n");
				defBuilder.append( "MODEL=" ).append( connectedDevice.getDevice().getBrowserName() ).append( "\r\n");
				return new Artifact( rootFolder + "executionDefinition.properties", defBuilder.toString().getBytes() );
    
            case CONSOLE_LOG:
                Artifact consoleArtifact = new Artifact( rootFolder + "console.txt", DeviceManager.instance().getLog().getBytes() );
                DeviceManager.instance().clearLog();
                return consoleArtifact;
    
            case DEVICE_LOG:
    
                try
                {
                    LogEntries logEntries = webDriver.manage().logs().get( LogType.BROWSER );
                    if ( logEntries != null )
                    {
                        StringBuilder logBuilder = new StringBuilder();
                        for ( LogEntry logEntry : logEntries )
                            logBuilder.append( dateFormat.format( new Date( logEntry.getTimestamp() ) ) ).append( ": " ).append( logEntry.getMessage() ).append( "\r\n" );
    
                        return new Artifact( rootFolder + "deviceLog.txt", logBuilder.toString().getBytes() );
                    }
                    return null;
                }
                catch ( Exception e )
                {
                    log.info( "Could not generate device logs" );
                    return null;
                }
    
            case EXECUTION_RECORD_CSV:
                return generateCSVRecord( connectedDevice.getDevice(), testName, rootFolder );
    
            case EXECUTION_RECORD_HTML:
                return generateHTMLRecord( connectedDevice.getDevice(), testName, rootFolder, webDriver );
    
            case WCAG_REPORT:
                return generateWCAG( connectedDevice.getDevice(), testName, rootFolder );
    
            default:
                return null;

        }
    }

    /**
     * Gets the url.
     *
     * @param currentUrl
     *            the current url
     * @return the url
     */
    public byte[] getUrl( URL currentUrl )
    {
        if ( log.isDebugEnabled() )
            log.debug( "Executing " + currentUrl.toString() );
        InputStream inputStream = null;
        try
        {
            ByteArrayOutputStream resultBuilder = new ByteArrayOutputStream();

            HttpURLConnection y = (HttpURLConnection) currentUrl.openConnection();

            inputStream = y.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = 0;

            while ( (bytesRead = inputStream.read( buffer )) > 0 )
                resultBuilder.write( buffer, 0, bytesRead );

            return resultBuilder.toByteArray();
        }
        catch ( Exception e )
        {
            log.error( "Error performing GET request", e );
            return null;
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch ( Exception e )
            {
            }
        }

    }
}
