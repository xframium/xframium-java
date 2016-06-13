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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.ExecutionRecord;
import org.xframium.page.StepStatus;
import org.xframium.spi.Device;
import org.xframium.wcag.WCAGRecord;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractArtifactProducer.
 */
public abstract class AbstractArtifactProducer implements ArtifactProducer
{
    protected static DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss.SSS");
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
	protected abstract Artifact _getArtifact( WebDriver webDriver, ArtifactType aType, ConnectedDevice connectedDevice, String testName, boolean success );
	
	/**
	 * _get artifact.
	 *
	 * @param webDriver the web driver
	 * @param aType the a type
	 * @param parameterMap the parameter map
	 * @param connectedDevice the connected device
	 * @return the artifact
	 */
	protected abstract Artifact _getArtifact( WebDriver webDriver, ArtifactType aType, Map<String,String> parameterMap, ConnectedDevice connectedDevice, String testName, boolean success );
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.artifact.ArtifactProducer#getArtifact(org.openqa.selenium.WebDriver, com.perfectoMobile.device.artifact.ArtifactProducer.ArtifactType, com.perfectoMobile.device.ConnectedDevice)
	 */
	public Artifact getArtifact( WebDriver webDriver, ArtifactType aType, ConnectedDevice connectedDevice, String testName, boolean success )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Acquiring an Artifact of type " + aType );
		return _getArtifact( webDriver, aType, connectedDevice, testName, success );
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.artifact.ArtifactProducer#getArtifact(org.openqa.selenium.WebDriver, com.perfectoMobile.device.artifact.ArtifactProducer.ArtifactType, java.util.Map, com.perfectoMobile.device.ConnectedDevice)
	 */
	public Artifact getArtifact( WebDriver webDriver, ArtifactType aType, Map<String, String> parameterMap, ConnectedDevice connectedDevice, String testName, boolean success )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Acquiring an Artifact of type " + aType + " using " + parameterMap );
		return _getArtifact( webDriver, aType, parameterMap, connectedDevice, testName, success );
	}
	
	protected Artifact generateHTMLRecord( Device device, String testName, String rootFolder, WebDriver webDriver )
	{
	    StringBuffer stringBuffer = new StringBuffer();
        stringBuffer = new StringBuffer();
        stringBuffer.append( "<html>" );
        stringBuffer.append( "<head><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\"><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\"></head>"  );
        
        int successCount = 0;
        int failureCount = 0;
        int ignoreCount = 0;
        int recordCount = 0;
        
        stringBuffer.append( "<body><div class=\"container\">" );
        stringBuffer.append( "" );
        
        String panelClass = "default";
        if ( DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ) != null && !DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ).isEmpty() )
        {
            panelClass = "success";
            for ( Object item : DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ) )
            {
                ExecutionRecord eItem = (ExecutionRecord) item;
                recordCount++;
                switch( eItem.getStatus() )
                {
                    case FAILURE:
                        failureCount++;
                        panelClass = "danger";
                        break;
                        
                    case FAILURE_IGNORED:
                        ignoreCount++;
                        break;
                        
                    case REPORT:
                    case SUCCESS:
                        successCount++;
                }

            }
            
            
            double successValue = ( ( (double)successCount / (double)recordCount ) * 100 );
            double ignoreValue = ( ( (double)ignoreCount / (double)recordCount ) * 100 );
            double failValue = ( ( (double)failureCount / (double)recordCount ) * 100 );
            stringBuffer.append( "<br/><div class=\"progress\"><div class=\"progress-bar progress-bar-success\" style=\"width: " + (int)successValue + "%\">" ).append( "<span class=\"sr-only\">" ).append( (int)successValue ).append( "% Passed</span></div>" );
            stringBuffer.append( "<div class=\"progress-bar progress-bar-warning progress-bar-striped\" style=\"width: " + (int)ignoreValue + "%\">" ).append( "<span class=\"sr-only\">" ).append( (int)ignoreValue ).append( "% Failed</span></div>" );
            stringBuffer.append( "<div class=\"progress-bar progress-bar-danger\" style=\"width: " + (int)failValue + "%\">" ).append( "<span class=\"sr-only\">" ).append( (int)failValue ).append( "% Failed</span></div></div>" );
        }
                
        stringBuffer.append( "<br/><div class=\"panel panel-" + panelClass + "\"><div class=\"panel-heading\"><h3 class=\"panel-title\"><b>Test execution results</b> from " ).append( simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) ).append( " at " ).append( timeFormat.format( new Date( System.currentTimeMillis() ) ) ).append( "</h3></div><div class=\"panel-body\">" );
        
        if ( device != null )
        {
            stringBuffer.append( "<div class=\"list-group\">" );
            stringBuffer.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Test Name</b> " ).append( testName ).append( "</a>" );
            stringBuffer.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Execution ID</b> " ).append( DeviceManager.instance().getExecutionId() ).append( "</a>" );
            stringBuffer.append( "<a hRef=\"#\" class=\"list-group-item\"><b>OS</b> " ).append( device.getOs() ).append( " (" ).append( device.getOsVersion() ).append( ")</a>" );
            stringBuffer.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Device</b> " ).append( device.getManufacturer() ).append( " " ).append( device.getModel() ).append( " (" ).append( device.getKey() ).append(")</a>" );
            stringBuffer.append( "</div></div></div>" );
            
        }
        else
        {
            stringBuffer.append( "<div class=\"list-group\">" );
            stringBuffer.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Test Name</b> " ).append( testName ).append( "</a>" );
            stringBuffer.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Execution ID</b> " ).append( DeviceManager.instance().getExecutionId() ).append( "</a>" );            
            stringBuffer.append( "</div></div></div>" );
        }

        stringBuffer.append( "<br/><div class=\"table-responsive panel panel-primary\"><div class=\"panel-heading\"><h3 class=\"panel-title\">Individual test actions</h3></div><div class=\"panel-body\"><div class=\"table-responsive\"><table class=\"table table-hover table-condensed\">");
        stringBuffer.append( "<thead><th>Page</th><th>Element</th><th>Description</th><th>Start time</th><th>Time</th><th>Status</th></thead>" );
        
        boolean success = true;
        int spaceCount = 0;
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
                
                if ( eItem.getStatus().equals( StepStatus.FAILURE ) )
                    success = false;
                stringBuffer.append( eItem.toHTML( spaceCount ) );
                spaceCount++;
            }
        }
        
        stringBuffer.append( "</TABLE></div></div></div>" );
        
        stringBuffer.append( "<br/><div class=\"panel panel-primary\"><div class=\"panel-heading\"><h3 class=\"panel-title\">Test Artifacts</h3></div><div class=\"panel-body\">" );
        
        String wtUrl = ( (DeviceWebDriver) webDriver ).getWindTunnelReport();
        if ( wtUrl != null && !wtUrl.isEmpty() )
            stringBuffer.append( "<a hRef=\"" + wtUrl + "\" class=\"list-group-item\">Single Test Report</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.CONSOLE_LOG ) )
            stringBuffer.append( "<a hRef=\"console.txt\" class=\"list-group-item\">Console Output</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_RECORD_CSV ) )
            stringBuffer.append( "<a hRef=\"" + testName + ".csv\" class=\"list-group-item\">Execution Record (CSV)</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_REPORT ) || DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_REPORT_PDF ) )
            stringBuffer.append( "<a hRef=\"EXECUTION_REPORT_PDF.pdf\" class=\"list-group-item\">Legacy Execution Report (PDF)</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_REPORT_HTML ) )
            stringBuffer.append( "<a hRef=\"EXECUTION_REPORT_HTML.html\" class=\"list-group-item\">Legacy Execution Report (HTML)</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_REPORT_XML ) )
            stringBuffer.append( "<a hRef=\"EXECUTION_REPORT_XML.xml\" class=\"list-group-item\">Legacy Execution Report (XML)</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.WCAG_REPORT ) )
            stringBuffer.append( "<a hRef=\"wcag.html\" class=\"list-group-item\">WCAG Report</a>" );
        
        if ( !success && DataManager.instance().isArtifactEnabled( ArtifactType.DEVICE_LOG ) )
            stringBuffer.append( "<a hRef=\"deviceLog.txt\" class=\"list-group-item\">Device Log</a>" );
        
        if ( !success && DataManager.instance().isArtifactEnabled( ArtifactType.FAILURE_SOURCE ) )
        {
            stringBuffer.append( "<a hRef=\"failureDOM.xml\" class=\"list-group-item\">Device State</a>" );
            stringBuffer.append( "<a class=\"thumbnail\" hRef=\"failure-screenshot.png\" class=\"list-group-item\"><img class=\"img-rounded img-responsive\" style=\"height: 200px\" src='failure-screenshot.png'/></a>" );
        }
        
        
        stringBuffer.append( "</div></div></BODY>" );
        stringBuffer.append( "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>");
        stringBuffer.append( "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>" );
        stringBuffer.append( "</HTML>" );
        
        return new Artifact( rootFolder + testName + ".html", stringBuffer.toString().getBytes() );
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
