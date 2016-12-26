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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.artifact.AbstractArtifactProducer;
import org.xframium.device.artifact.Artifact;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class PerfectoArtifactProducer.
 */
public class PerfectoArtifactProducer extends AbstractArtifactProducer
{
    
	/** The Constant REPORT_KEY. */
	private static final String REPORT_KEY = "REPORT_KEY";
	
	/** The Constant WIND_TUNNEL. */
	private static final String WIND_TUNNEL = "WIND_TUNNEL";
	
	/** The Constant FORMAT. */
	private static final String FORMAT = "format";
	
	/** The Constant DEFAULT_FORMAT. */
	private static final String DEFAULT_FORMAT = "pdf";
	

	/**
	 * Instantiates a new perfecto artifact producer.
	 */
	public PerfectoArtifactProducer()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new perfecto artifact producer.
	 *
	 * @param reportFormat the report format
	 */
	public PerfectoArtifactProducer( String reportFormat )
	{
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.artifact.AbstractArtifactProducer#_getArtifact(org.openqa.selenium.WebDriver, com.perfectoMobile.device.artifact.ArtifactProducer.ArtifactType, com.perfectoMobile.device.ConnectedDevice)
	 */
	@Override
	protected Artifact _getArtifact( WebDriver webDriver, ArtifactType aType, ConnectedDevice connectedDevice, String testName, boolean success, ExecutionContextTest test )
	{
		return null;
	}
	
	private Artifact generateExecutionReport( Device device, String operation, Map<String,String> parameterMap, String reportFormat, String rootFolder, ArtifactType aType)
	{
	    try
        {
	    	CloudDescriptor currentCloud = CloudRegistry.instance().getCloud();
	        if ( device.getCloud() != null && !device.getCloud().isEmpty() )
	            currentCloud = CloudRegistry.instance().getCloud( device.getCloud() );
	        
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append( "https://" ).append( currentCloud.getHostName() ).append( "/services/reports/" ).append( parameterMap.get( REPORT_KEY ) );
            urlBuilder.append( "?operation=" ).append( operation ).append( "&user=" ).append( currentCloud.getUserName() ).append( "&password=" ).append( currentCloud.getPassword() );
            String format = parameterMap.get( FORMAT );
            if (format == null)
            {
                if (reportFormat == null)
                    format = DEFAULT_FORMAT;
                else
                    format = reportFormat;
            }

            urlBuilder.append( "&format=" ).append( format );

            return new Artifact( rootFolder + aType + "." + format, getUrl( new URL( urlBuilder.toString() ) ) );
        }
        catch (Exception e)
        {
            log.error( "Error download artifact data - " + e.getMessage());
            return null;
        }
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.artifact.AbstractArtifactProducer#_getArtifact(org.openqa.selenium.WebDriver, com.perfectoMobile.device.artifact.ArtifactProducer.ArtifactType, java.util.Map, com.perfectoMobile.device.ConnectedDevice)
	 */
	@Override
	protected Artifact _getArtifact( WebDriver webDriver, ArtifactType aType, Map<String, String> parameterMap, ConnectedDevice connectedDevice, String testName, boolean success, ExecutionContextTest test )
	{
	    String rootFolder = testName + System.getProperty( "file.separator" ) + connectedDevice.getDevice().getKey() + System.getProperty( "file.separator" );
	    
	    try
	    {
	    
    		switch (aType)
    		{
    			case EXECUTION_DEFINITION:
    				StringBuilder defBuilder = new StringBuilder();
    				defBuilder.append( "DATE=" ).append( simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) ).append( "\r\n");
    				defBuilder.append( "TIME=" ).append( timeFormat.format( new Date( System.currentTimeMillis() ) ) ).append( "\r\n");
    				defBuilder.append( "TEST_CASE=" ).append( testName ).append( "\r\n");
    				defBuilder.append( "DEVICE=" ).append( connectedDevice.getDevice().getKey() ).append( "\r\n");
    				defBuilder.append( "SUCCESS=" ).append( success ).append( "\r\n");
    				defBuilder.append( "MANUFACTURER=" ).append( connectedDevice.getDevice().getManufacturer() ).append( "\r\n");
    				defBuilder.append( "MODEL=" ).append( connectedDevice.getDevice().getModel() ).append( "\r\n");
    				return new Artifact( rootFolder + "executionDefinition.properties", defBuilder.toString().getBytes() );
    				
    			case EXECUTION_REPORT:
    			case EXECUTION_REPORT_PDF:
    			    return generateExecutionReport( connectedDevice.getPopulatedDevice(), "download", parameterMap, "pdf", rootFolder, aType );
    
    			case FAILURE_SOURCE:
    			    return new Artifact( rootFolder + "failureDOM.xml", webDriver.getPageSource().getBytes());
    			
    			case FAILURE_SOURCE_HTML:
    				return new Artifact( rootFolder + "failureDOM.html", ( "<html><head><link href=\"http://www.xframium.org/output/assets/css/prism.css\" rel=\"stylesheet\"><script src=\"http://www.xframium.org/output/assets/js/prism.js\"></script><body><pre class\"line-numbers\"><code class=\"language-markup\">" + webDriver.getPageSource().replace( "<", "&lt;" ).replace( ">", "&gt;" ).replace( "\t", "  ") + "</code></pre></body></html>" ).getBytes());
    
    			case CONSOLE_LOG:
    			    Artifact consoleArtifact = new Artifact( rootFolder + "console.txt", DeviceManager.instance().getLog().getBytes() );
    			    DeviceManager.instance().clearLog();
    			    return consoleArtifact;
    				
    			case EXECUTION_RECORD_JSON:
    			    return generateJSONRecord( test, testName, rootFolder );
    			    
    			case DEVICE_LOG:
    				try
    				{
    				    ByteArrayInputStream inputStream = new ByteArrayInputStream( generateExecutionReport( connectedDevice.getPopulatedDevice(), "download", parameterMap, "xml", rootFolder, aType ).getArtifactData() );
    				    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		            dbFactory.setNamespaceAware( true );
    		            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		            Document xmlDocument = dBuilder.parse( inputStream );
    		            
    		            NodeList nodeList = getNodes( xmlDocument, "//dataItem[@type='log']/attachment" );
    		            if ( nodeList != null && nodeList.getLength() > 0 )
    		            {
    		                byte[] zipFile = PerfectoMobile.instance().reports().download( parameterMap.get( REPORT_KEY ), nodeList.item( 0 ).getTextContent(), false );
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
    		                
    		                return new Artifact( rootFolder + "deviceLog.txt", outputStream.toByteArray() );
    		                
    		            }
    				    
    		            return new Artifact( rootFolder + "deviceLog.txt", "Could not read file".getBytes() );
    					
    				}
    				catch( Exception e )
    				{
    					log.error( "Error download device log data" + "e.getMessage()" );
    				}
    				return null;
    				
    			case EXECUTION_REPORT_CSV:
    			    return generateExecutionReport( connectedDevice.getPopulatedDevice(), "download", parameterMap, "csv", rootFolder, aType );
    				
    			case EXECUTION_REPORT_HTML:
    			    return generateExecutionReport( connectedDevice.getPopulatedDevice(), "download", parameterMap, "html", rootFolder, aType );
    			
    			case EXECUTION_REPORT_XML:
    			    return generateExecutionReport( connectedDevice.getPopulatedDevice(), "download", parameterMap, "xml", rootFolder, aType );
    				
    			case EXECUTION_RECORD_CSV:
    			    return generateCSVRecord( connectedDevice.getPopulatedDevice(), testName, rootFolder );
    			    
    			case EXECUTION_RECORD_HTML:
                    return generateHTMLRecord( connectedDevice.getPopulatedDevice(), testName, rootFolder, webDriver );
    				
    			case WCAG_REPORT:
    			    return generateWCAG( connectedDevice.getPopulatedDevice(), testName, rootFolder );
    			default:
    				return null;
    
    		}
	    }
	    catch( Exception e )
	    {
	        return new Artifact( rootFolder + "generationFailure.txt", e.getMessage().getBytes() );
	    }
		
	}

	/**
	 * Gets the url.
	 *
	 * @param currentUrl the current url
	 * @return the url
	 */
	public byte[] getUrl( URL currentUrl )
	{
		if (log.isInfoEnabled())
			log.info( "Executing " + currentUrl.toString() );
		InputStream inputStream = null;
		try
		{
			ByteArrayOutputStream resultBuilder = new ByteArrayOutputStream();

			HttpURLConnection y = ( HttpURLConnection ) currentUrl.openConnection();
			y.setReadTimeout( 30000 );


			inputStream = y.getInputStream();
			byte[] buffer = new byte[1024];
			int bytesRead = 0;

			while (( bytesRead = inputStream.read( buffer ) ) > 0)
				resultBuilder.write( buffer, 0, bytesRead );

			return resultBuilder.toByteArray();
		}
		catch (Exception e)
		{
			log.error( "Error performing GET request - " + e.getMessage() );
			return null;
		}
		finally
		{
			try
			{
				inputStream.close();
			}
			catch (Exception e)
			{
			}
		}

	}
}
