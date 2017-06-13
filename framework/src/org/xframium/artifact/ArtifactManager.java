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
package org.xframium.artifact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.artifact.spi.ALMDefectArtifact;
import org.xframium.artifact.spi.CSVDataArtifact;
import org.xframium.artifact.spi.ConsoleLogArtifact;
import org.xframium.artifact.spi.DefaultHistoryReportingArtifact;
import org.xframium.artifact.spi.DefaultReportingArtifact;
import org.xframium.artifact.spi.DefaultSuiteReportingArtifact;
import org.xframium.artifact.spi.DeviceLogArtifact;
import org.xframium.artifact.spi.HTMLGridArtifact;
import org.xframium.artifact.spi.HTMLSourceArtifact;
import org.xframium.artifact.spi.ImagingArtifact;
import org.xframium.artifact.spi.JSONArtifact;
import org.xframium.artifact.spi.JSONGridArtifact;
import org.xframium.artifact.spi.JSONHistoryArtifact;
import org.xframium.artifact.spi.JSONSuiteArtifact;
import org.xframium.artifact.spi.PerfectoCSVReport;
import org.xframium.artifact.spi.PerfectoHTMLReport;
import org.xframium.artifact.spi.PerfectoPDFReport;
import org.xframium.artifact.spi.PerfectoReportingServices;
import org.xframium.artifact.spi.PerfectoWindTunnel;
import org.xframium.artifact.spi.PerfectoXMLReport;
import org.xframium.artifact.spi.SauceLabsReportingServices;
import org.xframium.artifact.spi.TimingArtifact;
import org.xframium.artifact.spi.VitalsArtifact;
import org.xframium.artifact.spi.XMLSourceArtifact;
import org.xframium.device.factory.DeviceWebDriver;

public class ArtifactManager
{
    /** The singleton. */
    private static ArtifactManager singleton = new ArtifactManager();

    private String displayArtifact = null; 

    public String getDisplayArtifact()
    {
        return displayArtifact;
    }

    public void setDisplayArtifact( String displayArtifact )
    {
        if ( displayArtifact != null && displayArtifact.equals( "true" ) )
            this.displayArtifact = ArtifactType.EXECUTION_SUITE_HTML.name();
        else
            this.displayArtifact = displayArtifact;
    }

    /**
     * Instance.
     *
     * @return the RunDetails
     */
    public static ArtifactManager instance()
    {
        return singleton;
    }
    
    private Log log = LogFactory.getLog( ArtifactManager.class );

    /**
     * Instantiates a RunDetails.
     */
    private ArtifactManager()
    {
        //
        // Register the default artifacts
        //
        registerArtifact( ArtifactTime.ON_FAILURE, ArtifactType.FAILURE_SOURCE.name(), XMLSourceArtifact.class );
        registerArtifact( ArtifactTime.ON_FAILURE, ArtifactType.FAILURE_SOURCE_HTML.name(), HTMLSourceArtifact.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.CONSOLE_LOG.name(), ConsoleLogArtifact.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.STATISTICS.name(), VitalsArtifact.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.DEVICE_LOG.name(), DeviceLogArtifact.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.IMAGING_ANALYSIS.name(), ImagingArtifact.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.ADD_TO_CSV.name(), CSVDataArtifact.class );
        registerArtifact( ArtifactTime.AFTER_ARTIFACTS, ArtifactType.EXECUTION_RECORD_JSON.name(), JSONArtifact.class );
        registerArtifact( ArtifactTime.AFTER_ARTIFACTS, ArtifactType.EXECUTION_RECORD_HTML.name(), DefaultReportingArtifact.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.EXECUTION_REPORT_XML.name(), PerfectoXMLReport.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.EXECUTION_REPORT_HTML.name(), PerfectoHTMLReport.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.EXECUTION_REPORT_PDF.name(), PerfectoPDFReport.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.EXECUTION_REPORT.name(), PerfectoPDFReport.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.EXECUTION_REPORT_CSV.name(), PerfectoCSVReport.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.TIMING_HTML.name(), TimingArtifact.class );
        
        registerArtifact( ArtifactTime.BEFORE_SUITE_ARTIFACTS, ArtifactType.GRID_REPORT.name(), JSONGridArtifact.class );
        registerArtifact( ArtifactTime.BEFORE_SUITE_ARTIFACTS, ArtifactType.GRID_HTML.name(), HTMLGridArtifact.class );
        registerArtifact( ArtifactTime.AFTER_SUITE, ArtifactType.EXECUTION_SUITE_HTML.name(), DefaultSuiteReportingArtifact.class );
        registerArtifact( ArtifactTime.AFTER_SUITE, ArtifactType.EXECUTION_SUITE_JSON.name(), JSONSuiteArtifact.class );
        registerArtifact( ArtifactTime.AFTER_SUITE_ARTIFACTS, ArtifactType.EXECUTION_HISTORY_HTML.name(), DefaultHistoryReportingArtifact.class );
        registerArtifact( ArtifactTime.AFTER_SUITE_ARTIFACTS, ArtifactType.EXECUTION_HISTORY_JSON.name(), JSONHistoryArtifact.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.REPORTIUM.name(), PerfectoReportingServices.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.SAUCE_LABS.name(), SauceLabsReportingServices.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.WIND_TUNNEL.name(), PerfectoWindTunnel.class );
        registerArtifact( ArtifactTime.AFTER_TEST, ArtifactType.ALM_DEFECT.name(), ALMDefectArtifact.class );
        registerArtifact( ArtifactTime.AFTER_ARTIFACTS, ArtifactType.EXECUTION_TEST_JSON.name(), JSONArtifact.class );
        registerArtifact( ArtifactTime.AFTER_ARTIFACTS, ArtifactType.EXECUTION_TEST_HTML.name(), DefaultReportingArtifact.class );
    }
    
    public void registerArtifact( ArtifactTime artifactTime, String aType, Class artifactImplementation )
    {
        log.info( "Registering artifact " + aType );
        
        artifactMap.put( aType, artifactImplementation );
        classTimeMap.put( artifactImplementation, artifactTime );
        
        List<Class> timeList = timeMap.get( artifactTime );
        if ( timeList == null )
        {
            timeList = new ArrayList<Class>( 10 );
            timeMap.put( artifactTime, timeList );
        }
        
        timeList.add( artifactImplementation );
    }
    
    private Map<ArtifactTime,List<Class>> timeMap = new HashMap<ArtifactTime,List<Class>>( 10 );
    private Map<Class,ArtifactTime> classTimeMap = new HashMap<Class,ArtifactTime>( 10 );
    private Map<String,Class> artifactMap = new HashMap<String,Class>( 10 );
    
    private Map<ArtifactTime,List<String>> enabledArtifactMap = new HashMap<ArtifactTime,List<String>>( 10 );
    private List<String> enabledArtifacts = new ArrayList<String>( 10 );
    
    public boolean isTime( String artifactName, ArtifactTime aTime )
    {
        try
        {
            return classTimeMap.get( artifactMap.get( artifactName ) ).equals( aTime );
        }
        catch( Exception e )
        {
            return false;
        }
    }
    
    public boolean isArtifactEnabled( String artifactName )
    {
        return enabledArtifacts.contains( artifactName );
    }
    
    private static final String[] DEFAULT_ARTIFACTS = new String[] { "EXECUTION_SUITE_HTML", "EXECUTION_SUITE_JSON", "EXECUTION_HISTORY_JSON", "EXECUTION_HISTORY_HTML", "EXECUTION_TEST_JSON", "EXECUTION_TEST_HTML" };
    
    public void enableArtifact( String artifactName )
    {
        log.info( "Enabling artifact " + artifactName );
        
        if  (ArtifactType.EXECUTION_RECORD_HTML.name().equals( artifactName ) )
        {
            for ( String defaultArtifact : DEFAULT_ARTIFACTS )
            {
                enableArtifact( defaultArtifact );
            }
        }
        else
        {
            try
            {
                Class artifactClass = artifactMap.get( artifactName ); 
                ArtifactTime aTime = classTimeMap.get( artifactClass );
                List<String> classList = enabledArtifactMap.get( aTime );
                if ( classList == null )
                {
                    classList = new ArrayList<String>( 10 );
                    enabledArtifactMap.put( aTime, classList );
                }
                
                if ( !classList.contains( artifactName ) )
                    classList.add( artifactName );
                
                if ( !enabledArtifacts.contains( artifactName ) )
                    enabledArtifacts.add( artifactName );
            }
            catch( Exception e )
            {
                log.warn( "Error enabling artifact " + artifactName, e );
            }
        }
    }
    
    public List<String> getEnabledArtifacts( ArtifactTime aTime )
    {
        return enabledArtifactMap.get( aTime );
    }
    
    public Artifact generateArtifact( String artifactType, String rootFolder, DeviceWebDriver webDriver )
    {
        Class artifactImpl = artifactMap.get( artifactType );
        
        if ( artifactImpl == null )
        {
            log.warn( "Invalid artifact specified [" + artifactType );
            return null;
        }
        
        try
        {
            Artifact artifact = (Artifact)artifactImpl.newInstance();
            if ( artifact.generateArtifact( rootFolder, webDriver ) != null )
                return artifact;
            else
                return null;
                
        }
        catch( Exception e )
        {
            log.warn( "Error generating artifact for " + artifactType );
        }
        
        return null;
    }
        
    
}
