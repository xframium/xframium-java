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

import java.io.File;
import java.io.FileInputStream;
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
import org.xframium.application.ApplicationRegistry;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.ConnectedDevice;
import org.xframium.device.DeviceManager;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.ExecutionRecord;
import org.xframium.page.StepStatus;
import org.xframium.spi.Device;
import org.xframium.spi.PropertyProvider;
import org.xframium.spi.RunDetails;
import org.xframium.spi.driver.ReportiumProvider;
import org.xframium.wcag.WCAGRecord;
import org.yaml.snakeyaml.util.UriEncoder;

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
        stringBuffer.append( "<head><link href=\"http://fonts.googleapis.com/css?family=Roboto:300,400,500,700,400italic\" rel=\"stylesheet\"><link href=\"http://www.xframium.org/output/assets/css/toolkit-inverse.css\" rel=\"stylesheet\"><link href=\"http://www.xframium.org/output/assets/css/application.css\" rel=\"stylesheet\"><style>iframe {background-color: #eaeae1;} .abscenter { margin: auto; position: absolute; top: 0; left: 0; bottom: 0; right: 0; } .pass {color: #1bc98e;}.fail {color: #e64759;} .pageName { color: #e4d836; } .elementName { color: #e4d836; }</style></head>" );
        
        int successCount = 0;
        int failureCount = 0;
        int ignoreCount = 0;
        int recordCount = 0;
        
        stringBuffer.append( "<body><div class=\"container\">" );
        
        stringBuffer.append( "<div class=\"col-sm-12 content\"><div class=\"dashhead\"><span class=\"pull-right text-muted\">" ).append( simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) ).append( " at " ).append( timeFormat.format( new Date( System.currentTimeMillis() ) ) ).append( "</span><h6 class=\"dashhead-subtitle\">xFramium  " + Initializable.VERSION + "</h6><h3 class=\"dashhead-title\">" + testName + "</h3><h6>" + device.getEnvironment() + "</h6>" );

        if ( webDriver instanceof PropertyProvider )
        {
            PropertyProvider pProvider = (PropertyProvider) webDriver;
            if ( pProvider.getProperty( "testDescription" ) != null && !pProvider.getProperty( "testDescription" ).isEmpty() )
                stringBuffer.append( "<h6 class=\"text-muted\"><i>" + pProvider.getProperty( "testDescription" ) + "</i></h6>" );
        }
        
        stringBuffer.append( "</div>" );
        
        String panelClass = "default";
        if ( DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ) != null && !DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ).isEmpty() )
        {
            panelClass = "success";
            long startTime = -1;
            long runTime = 0;
            for ( Object item : DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ) )
            {
                ExecutionRecord eItem = (ExecutionRecord) item;
                
                if ( startTime < 0 )
                    startTime = eItem.getTimeStamp();
                
                runTime += eItem.getRunTime();
                
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
            String runLength = String.format( "%2dh %2dm %2ds", TimeUnit.MILLISECONDS.toHours( runTime ), TimeUnit.MILLISECONDS.toMinutes( runTime ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( runTime ) ),TimeUnit.MILLISECONDS.toSeconds( runTime ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( runTime ) ) );
            
            
            stringBuffer.append( "<br /><div class=\"row statcards\"><div class=\"col-sm-1 m-b\"></div>" );
            stringBuffer.append( "<div class=\"col-sm-2 m-b\"><div class=\"statcard statcard-success\"><div class=\"p-a\"><span class=\"statcard-desc\">Passed</span><h4 class=\"statcard-number\">" + successCount + "</h4></div></div></div>" );
            stringBuffer.append( "<div class=\"col-sm-2 m-b\"><div class=\"statcard statcard-warning\"><div class=\"p-a\"><span class=\"statcard-desc\">Ignored</span><h4 class=\"statcard-number\">" + ignoreCount + "</h4></div></div></div>" );
            stringBuffer.append( "<div class=\"col-sm-2 m-b\"><div class=\"statcard statcard-danger\"><div class=\"p-a\"><span class=\"statcard-desc\">Failed</span><h4 class=\"statcard-number\">" + failureCount + "</h4></div></div></div>" );
            stringBuffer.append( "<div class=\"col-sm-2 m-b\"><div class=\"statcard statcard-info\"><div class=\"p-a\"><span class=\"statcard-desc\">Total Steps</span><h4 class=\"statcard-number\">" + recordCount + "</h4></div></div></div>" );
            stringBuffer.append( "<div class=\"col-sm-2 m-b\"><div class=\"statcard statcard-info\"><div class=\"p-a\"><span class=\"statcard-desc\">Duration</span><h4 class=\"statcard-number\">" + runLength + "</h4></div></div></div>" );
            stringBuffer.append( "</div><br />" );
        }

        boolean success = true;
        int spaceCount = 0;
        if ( DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ) != null && !DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ).isEmpty() )
        {
            for ( Object item : DeviceManager.instance().getArtifacts( ArtifactType.EXECUTION_RECORD ) )
            {
                ExecutionRecord eItem = (ExecutionRecord) item;
                
                if ( eItem.getStatus().equals( StepStatus.FAILURE ) )
                    success = false;
            }
        }
        
        stringBuffer.append( "<br/>" );
        stringBuffer.append( "<ul class=\"nav nav-tabs\" role=\"tablist\">" );
        stringBuffer.append( "<li role=\"presentation\" class=\"active\"><a href=\"#detail\" aria-controls=\"detail\" role=\"tab\" data-toggle=\"tab\">Steps</a></li>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.CONSOLE_LOG ) )
            stringBuffer.append( "<li role=\"presentation\"><a href=\"#console\" aria-controls=\"console\" role=\"tab\" data-toggle=\"tab\">Console Log</a></li>" );
        
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.WCAG_REPORT ) )
            stringBuffer.append( "<li role=\"presentation\"><a href=\"#wcag\" aria-controls=\"wcag\" role=\"tab\" data-toggle=\"tab\">WCAG Report</a></li>" );
        
        if ( !success && DataManager.instance().isArtifactEnabled( ArtifactType.DEVICE_LOG ) )
            stringBuffer.append( "<li role=\"presentation\"><a href=\"#deviceLog\" aria-controls=\"deviceLog\" role=\"tab\" data-toggle=\"tab\">Device Log</a></li>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_RECORD_CSV ) )
            stringBuffer.append( "<li role=\"presentation\"><a class=link-tab hRef=\"" + testName + ".csv\" class=\"list-group-item\">Execution Record</a></li>" );
        
        
        
        stringBuffer.append( "<li role=\"presentation\"><a href=\"#external\" aria-controls=\"external\" role=\"tab\" data-toggle=\"tab\">Links</a></li>" );
        

        stringBuffer.append( "<span class=\"pull-right text-muted\"><a hRef=\"../../index.html\">Return to Test Execution Summary</a></span></ul>" );
        
        
        stringBuffer.append( "<div class=\"tab-content\">" );
        
        
        stringBuffer.append( "<div role=\"tabpanel\" class=\"tab-pane active\" id=\"detail\">" );
        stringBuffer.append( "<div class=\"table-responsive table-bordered\"><table class=\"table table-hover table-condensed\">");
        stringBuffer.append( "<thead><th width=\"80%\">Steps Performed</th><th width=\"20%\">Started</th><th align=center width=\"0%\">Status</th></thead>" );
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
                
                stringBuffer.append( eItem.toHTML( spaceCount ) );
                spaceCount++;
            }
        }
        
        stringBuffer.append( "</TABLE></div></div>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.CONSOLE_LOG ) )
            stringBuffer.append( "<div role=\"tabpanel\" class=\"tab-pane\" id=\"console\"><div id=\"list\"><p><iframe src=\"console.txt\" frameborder=\"0\" height=\"100%\" width=\"100%\"></iframe></p></div></div>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.WCAG_REPORT ) )
            stringBuffer.append( "<div role=\"tabpanel\" class=\"tab-pane\" id=\"wcag\"><div id=\"list\"><p><iframe src=\"wcag.html\" frameborder=\"0\" height=\"100%\" width=\"100%\"></iframe></p></div></div>" );
        
        if ( !success && DataManager.instance().isArtifactEnabled( ArtifactType.DEVICE_LOG ) )
            stringBuffer.append( "<div role=\"tabpanel\" class=\"tab-pane\" id=\"deviceLog\"><div id=\"list\"><p><iframe src=\"deviceLog.txt\" frameborder=\"0\" height=\"100%\" width=\"100%\"></iframe></p></div></div>" );
        
        
        stringBuffer.append( "<div role=\"tabpanel\" class=\"tab-pane\" id=\"external\">" );
        stringBuffer.append( "<div class=\"list-group\">" );

        String wtUrl = ( (DeviceWebDriver) webDriver ).getWindTunnelReport();
        if ( wtUrl != null && !wtUrl.isEmpty() )
            stringBuffer.append( "<a target=_blank hRef=\"" + UriEncoder.encode( wtUrl ).replace( "%3F", "?" ) + "\" class=\"list-group-item\">Perfecto Single Test Report</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_REPORT_CSV ) )
            stringBuffer.append( "<a target=_blank hRef=\"EXECUTION_REPORT_CSV.csv\" class=\"list-group-item\">Perfecto Execution Report (CSV)</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_REPORT ) || DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_REPORT_PDF ) )
        	stringBuffer.append( "<a target=_blank hRef=\"EXECUTION_REPORT_PDF.pdf\" class=\"list-group-item\">Perfecto Execution Report (PDF)</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_REPORT_HTML ) )
        	stringBuffer.append( "<a target=_blank hRef=\"EXECUTION_REPORT_HTML.html\" class=\"list-group-item\">Perfecto Execution Report (HTML)</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.EXECUTION_REPORT_XML ) )
        	stringBuffer.append( "<a target=_blank hRef=\"EXECUTION_REPORT_XML.xml\" class=\"list-group-item\">Perfecto Execution Report (XML)</a>" );
        
        if ( DataManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM ) )
        {
            if ( ( (ReportiumProvider) webDriver ).getReportiumClient() != null )
            {
                stringBuffer.append( "<a target=_blank hRef=\"" + ( (ReportiumProvider) webDriver ).getReportiumClient().getReportUrl() + "\" class=\"list-group-item\">Perfecto Reportium Report</a>" );
            }
        }
            
        
        
        stringBuffer.append( "</div></div>" );
        
        
        stringBuffer.append( "</div></div></BODY>" );
        stringBuffer.append( "<script src=\"http://www.xframium.org/output/assets/js/jquery.min.js\"></script><script src=\"http://www.xframium.org/output/assets/js/chart.js\"></script><script src=\"http://www.xframium.org/output/assets/js/tablesorter.min.js\"></script><script src=\"http://www.xframium.org/output/assets/js/toolkit.js\"></script><script src=\"http://www.xframium.org/output/assets/js/application.js\"></script><script>$('.link-tab').click(function() {window.open($(this).attr('href'));});</script>" );
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
