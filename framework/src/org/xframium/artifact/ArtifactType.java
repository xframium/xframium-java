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
import java.util.List;
import org.xframium.page.keyWord.step.spi.KWSBrowser.SwitchType;

/**
 * The Enum ArtifactType.
 */
public enum ArtifactType
{
    
    /** The execution report. */
    EXECUTION_REPORT ( ArtifactTime.AFTER_TEST, 1, "EXECUTION_REPORT", "Perfecto PDF execution report" ),
    
    /** The execution report pdf. */
    EXECUTION_REPORT_PDF ( ArtifactTime.AFTER_TEST, 2, "EXECUTION_REPORT_PDF", "Perfecto PDF execution report" ),
    
    /** The execution report html. */
    EXECUTION_REPORT_HTML ( ArtifactTime.AFTER_TEST, 3, "EXECUTION_REPORT_HTML", "Perfecto HTML execution report" ),
    
    /** The execution report csv. */
    EXECUTION_REPORT_CSV ( ArtifactTime.AFTER_TEST, 4, "EXECUTION_REPORT_CSV", "Perfecto CSV execution report" ),
    
    /** The execution report xml. */
    EXECUTION_REPORT_XML ( ArtifactTime.AFTER_TEST, 5, "EXECUTION_REPORT_XML", "Perfecto XML execution report" ),
    
    /** The execution video. */
    EXECUTION_VIDEO( ArtifactTime.AFTER_TEST, 6, "EXECUTION_VIDEO", "Perfecto execution video" ),
    
    /** The failure source. */
    FAILURE_SOURCE( ArtifactTime.ON_FAILURE, 7, "FAILURE_SOURCE", "Extract device state on failure" ),
    
    /** The failure source. */
    FAILURE_SOURCE_HTML( ArtifactTime.ON_FAILURE, 8, "FAILURE_SOURCE_HTML", "Extract device state on failure and convert to HTML" ),
    
    /** The execution report. */
    WCAG_REPORT ( ArtifactTime.AFTER_TEST, 9, "WCAG_REPORT", "WCAG Analysis report" ),
    
    /** The device log. */
    DEVICE_LOG( ArtifactTime.AFTER_TEST, 10, "DEVICE_LOG", "Extract platform logs for debugging" ),
    
    /** The device log. */
    CONSOLE_LOG( ArtifactTime.AFTER_TEST, 11, "CONSOLE_LOG", "Extract the console log of the test runner" ),
    
    EXECUTION_RECORD_HTML( ArtifactTime.NOOP, 12, "EXECUTION_RECORD_HTML", "xFramium HTML Report" ),
    
    EXECUTION_RECORD_CSV( ArtifactTime.AFTER_TEST, 13, "EXECUTION_RECORD_CSV", "xFramium CSV Report" ),
    
    EXECUTION_RECORD( ArtifactTime.NOOP, 14, "EXECUTION_RECORD", "xFramium Report" ),
    
    EXECUTION_TIMING( ArtifactTime.AFTER_TEST, 15, "EXECUTION_TIMING", "Extract execution timings" ),
    
    EXECUTION_DEFINITION( ArtifactTime.AFTER_TEST, 16, "EXECUTION_DEFINITION", "xFramium execution definition report"),
    
    DEBUGGER( ArtifactTime.NOOP, 17, "DEBUGGER", "xFramium integrated web-based debugger" ),
    
    SAUCE_LABS( ArtifactTime.NOOP, 18, "SAUCE_LABS", "SauceLabs reporting integrations" ),
    
    REPORTIUM( ArtifactTime.NOOP, 19, "REPORTIUM", "Perfeto reporting integrations" ),
    
    EXECUTION_RECORD_JSON( ArtifactTime.NOOP, 20, "EXECUTION_RECORD_HTML", "xFramium JSON Report" ),
    ;
    
    /** The time. */
    private ArtifactTime time;
    private String name;
    private String description;
    private int id;
    
    public List<ArtifactType> getSupported()
    {
        List<ArtifactType> supportedList = new ArrayList<ArtifactType>( 10 );
        supportedList.add( ArtifactType.EXECUTION_REPORT_PDF );
        supportedList.add( ArtifactType.EXECUTION_REPORT_HTML );
        supportedList.add( ArtifactType.EXECUTION_REPORT_CSV );
        supportedList.add( ArtifactType.EXECUTION_REPORT_XML );
        supportedList.add( ArtifactType.FAILURE_SOURCE );
        supportedList.add( ArtifactType.FAILURE_SOURCE_HTML );
        supportedList.add( ArtifactType.WCAG_REPORT );
        supportedList.add( ArtifactType.DEVICE_LOG );
        supportedList.add( ArtifactType.CONSOLE_LOG );
        supportedList.add( ArtifactType.EXECUTION_RECORD_HTML );
        supportedList.add( ArtifactType.EXECUTION_RECORD_CSV );
        supportedList.add( ArtifactType.EXECUTION_RECORD );
        supportedList.add( ArtifactType.EXECUTION_TIMING );
        supportedList.add( ArtifactType.EXECUTION_DEFINITION );
        supportedList.add( ArtifactType.DEBUGGER );
        supportedList.add( ArtifactType.SAUCE_LABS );
        supportedList.add( ArtifactType.REPORTIUM );

        return supportedList;
    }
    
    
    /**
     * Instantiates a new artifact type.
     *
     * @param time the time
     */
    ArtifactType( ArtifactTime time, int id, String name, String description )
    {
        this.id = id;
        this.name= name;
        this.description = description;
        this.time = time;
    }
    
    /**
     * Gets the time.
     *
     * @return the time
     */
    public ArtifactTime getTime()
    {
        return time;
    }
}

