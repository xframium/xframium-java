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
package org.xframium.device.artifact;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xframium.Initializable;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.ExecutionRecord;
import org.xframium.page.StepStatus;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;
import org.xframium.spi.PropertyProvider;
import org.xframium.spi.driver.ReportiumProvider;
import org.xframium.wcag.WCAGRecord;
import org.yaml.snakeyaml.util.UriEncoder;
import com.xframium.serialization.SerializationManager;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractArtifactProducer.
 */
public abstract class AbstractArtifactProducer implements ArtifactProducer
{
    protected static DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss z");
    protected static DateFormat dateFormat = new SimpleDateFormat( "MM-dd_HH-mm-ss");
    protected static DateFormat simpleDateFormat = new SimpleDateFormat( "MM-dd-yyyy");
    private static final String COMMA = ",";
    private XPathFactory xPathFactory = XPathFactory.newInstance();
    
	/** The log. */
	protected Log log = LogFactory.getLog( ArtifactProducer.class );
	
	/**
	 * _get artifact.
	 *
	 * @param webDriver the web driver
	 * @param aType the a type
	 * @param connectedDevice the connected device
	 * @return the artifact
	 */
	protected abstract Artifact _getArtifact( WebDriver webDriver, ArtifactType aType, ConnectedDevice connectedDevice, String testName, boolean success, ExecutionContextTest test );
	
	/**
	 * _get artifact.
	 *
	 * @param webDriver the web driver
	 * @param aType the a type
	 * @param parameterMap the parameter map
	 * @param connectedDevice the connected device
	 * @return the artifact
	 */
	protected abstract Artifact _getArtifact( WebDriver webDriver, ArtifactType aType, Map<String,String> parameterMap, ConnectedDevice connectedDevice, String testName, boolean success, ExecutionContextTest test );
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.artifact.ArtifactProducer#getArtifact(org.openqa.selenium.WebDriver, com.perfectoMobile.device.artifact.ArtifactProducer.ArtifactType, com.perfectoMobile.device.ConnectedDevice)
	 */
	public Artifact getArtifact( WebDriver webDriver, ArtifactType aType, ConnectedDevice connectedDevice, String testName, boolean success, ExecutionContextTest test )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Acquiring an Artifact of type " + aType );
		return _getArtifact( webDriver, aType, connectedDevice, testName, success, test );
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.artifact.ArtifactProducer#getArtifact(org.openqa.selenium.WebDriver, com.perfectoMobile.device.artifact.ArtifactProducer.ArtifactType, java.util.Map, com.perfectoMobile.device.ConnectedDevice)
	 */
	public Artifact getArtifact( WebDriver webDriver, ArtifactType aType, Map<String, String> parameterMap, ConnectedDevice connectedDevice, String testName, boolean success, ExecutionContextTest test )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Acquiring an Artifact of type " + aType + " using " + parameterMap );
		return _getArtifact( webDriver, aType, parameterMap, connectedDevice, testName, success, test );
	}
	
	protected Artifact generateJSONRecord( ExecutionContextTest test, String testName, String rootFolder )
    {   
	    if ( test == null )
	        return null;
	    
	    test.setFolderName( rootFolder );
	    String outputData = "var testData = " + new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), test, 0 ) ) + ";";
        return new Artifact( rootFolder + "Test.js", outputData.getBytes() );
    }
	
	protected Artifact generateHTMLRecord( Device device, String testName, String rootFolder, WebDriver webDriver )
	{
	    StringBuilder stringBuffer = new StringBuilder();
        InputStream inputStream = null;
	    try
	    {
    	    if ( System.getProperty( "reportTemplateFolder" ) == null )
            {
                if ( System.getProperty( "reportTemplate" ) == null )
                    inputStream = getClass().getClassLoader().getResourceAsStream( "org/xframium/reporting/html/dark/Test.html" );
                else
                    inputStream = getClass().getClassLoader().getResourceAsStream( "org/xframium/reporting/html/" + System.getProperty( "reportTemplate" ) + "/Test.html" );
            }
            else
                inputStream = new FileInputStream( new File(  System.getProperty( "reportTemplateFolder" ), "Test.html" ) );
            
            File fileName = new File( rootFolder, "index.html" );
            
            if ( !fileName.getParentFile().exists() )
                fileName.getParentFile().mkdirs();
            
            int bytesRead = 0;
            byte[] buffer = new byte[ 512 ];
            
            while ( ( bytesRead = inputStream.read( buffer ) ) > 0 )
            {
                stringBuffer.append( new String( buffer, 0, bytesRead ) );
            }
	    }
	    catch( Exception e )
	    {
	        log.error( "Error generating HTML", e );
	        return null;
	    }
	    finally
        {
            try { inputStream.close(); } catch( Exception e ) {}
        }
        
        return new Artifact( rootFolder + "index.html", stringBuffer.toString().getBytes() );
	}
	
	protected Artifact generateWCAG( Device device, String testName, String rootFolder )
	{
	    StringBuilder wcagBuffer = new StringBuilder();
        
        wcagBuffer.append( "<html><body><table><tr><td align='right'><b>Device Name:</b></td><td>" ).append( device.getManufacturer() ).append( " " ).append( device.getModel() ).append( " (" ).append( device.getKey() ).append(")</td></tr>");
        wcagBuffer.append( "<tr><td align='right'><b>OS:</b></td><td>" ).append( device.getOs() ).append( " (" ).append( device.getOsVersion() ).append(")</td></tr>");
        wcagBuffer.append( "<tr><td align='right'><b>Test Name:</b></td><td>" ).append( testName ).append("</td></tr><tr><td align='center' colspan='2'><h2>WCAG Report</h2></td></tr></table><table cellpadding='5' cellspacing='0'><br><br>");
        wcagBuffer.append(  "<tr><th>Page</th><th>Element</th><th>Type</th><th>Time</th><th>Duration</th><th>Success</th><th>Expected</th><th>Actual</th></tr>" );
        
        if ( DeviceManager.instance().getArtifacts( ArtifactType.WCAG_REPORT ) != null && !DeviceManager.instance().getArtifacts( ArtifactType.WCAG_REPORT ).isEmpty() )
        {
            for ( Object item : DeviceManager.instance().getArtifacts( ArtifactType.WCAG_REPORT ) )
            {
                WCAGRecord wcagItem = (WCAGRecord) item;
                
                String backgroundColor = null;
                if ( !wcagItem.getStatus() )
                    backgroundColor = " bgcolor='#ff3333' ";
                else
                    backgroundColor = " bgcolor='#66FF99' ";
                    

                wcagBuffer.append( "<tr " + backgroundColor + "><td>" ).append( wcagItem.getPageName() ).append( "</td>" );
                wcagBuffer.append( "<td>" ).append( wcagItem.getElementName() ).append( "</td>" );
                wcagBuffer.append( "<td>" ).append( wcagItem.getType() ).append( "</td>" );
                wcagBuffer.append( "<td>" ).append( timeFormat.format( new Date( wcagItem.getTimeStamp() ) ) ).append( "</td>" );
                wcagBuffer.append( "<td>" ).append( wcagItem.getDuration() ).append( "</td>" );
                wcagBuffer.append( "<td>" ).append( wcagItem.getStatus() ).append( "</td>" );
                wcagBuffer.append( "<td>" ).append( wcagItem.getExpectedValue() ).append( "</td>" );
                wcagBuffer.append( "<td>" ).append( wcagItem.getActualValue() ).append( "</td></tr>" );
                wcagBuffer.append( "<tr " + backgroundColor + "><td colspan='6'><i>" ).append( wcagItem.getFailureMessage() ).append( "</i></td>" );
                wcagBuffer.append( "<td colspan='2'><img height='100' src='file://" ).append( wcagItem.getImageLocation() ).append( "'/></td></tr>" );
            }
        }
        
        wcagBuffer.append( "</table></body></html>" );
        
        return new Artifact( rootFolder + "wcag.html", wcagBuffer.toString().getBytes() );
	}
	
	protected Artifact generateCSVRecord( Device device, String testName, String rootFolder )
	{
        StringBuffer stringBuffer = new StringBuffer();
        
        if ( DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ) != null && !DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ).isEmpty() )
        {
            for ( Object item : DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ) )
            {
                ExecutionRecord eItem = (ExecutionRecord) item;

                if (( eItem.getDeviceName() != null ) &&
                    ( device.getDeviceName() != null ) &&
                    ( !eItem.getDeviceName().equals( device.getDeviceName() )))
                {
                    continue;
                }
                
                stringBuffer.append( device.getManufacturer() ).append( COMMA ).append( device.getModel() ).append( COMMA );
                stringBuffer.append( device.getOs() ).append( COMMA ).append( device.getOsVersion()).append( COMMA );
                stringBuffer.append( eItem.getGroup() ).append( COMMA ).append( eItem.getName()).append( COMMA );
                stringBuffer.append( eItem.getType() ).append( COMMA ).append( dateFormat.format( new Date( eItem.getTimeStamp() ) ) ).append( COMMA );
                stringBuffer.append( eItem.getRunTime() ).append( COMMA ).append( eItem.getStatus() ).append( COMMA ).append( "\r\n" );
            }
        }
        
        return new Artifact( rootFolder + testName + ".csv", stringBuffer.toString().getBytes() );
	}
	
	protected NodeList getNodes( Document xmlDocument, String xPathExpression )
    {
        try
        {
            if (log.isDebugEnabled())
                log.debug( "Attempting to return Nodes for [" + xPathExpression + "]" );

            XPath xPath = xPathFactory.newXPath();
            return ( NodeList ) xPath.evaluate( xPathExpression, xmlDocument, XPathConstants.NODESET );
        }
        catch (Exception e)
        {
            log.error( "Error parsing xPath Expression [" + xPathExpression + "]" );
            return null;
        }
    }

}
