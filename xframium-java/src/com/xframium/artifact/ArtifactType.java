package com.xframium.artifact;



/**
 * The Enum ArtifactType.
 */
public enum ArtifactType
{
    
    /** The execution report. */
    EXECUTION_REPORT ( ArtifactTime.AFTER_TEST ),
    
    /** The execution report pdf. */
    EXECUTION_REPORT_PDF ( ArtifactTime.AFTER_TEST ),
    
    /** The execution report html. */
    EXECUTION_REPORT_HTML ( ArtifactTime.AFTER_TEST ),
    
    /** The execution report csv. */
    EXECUTION_REPORT_CSV ( ArtifactTime.AFTER_TEST ),
    
    /** The execution report xml. */
    EXECUTION_REPORT_XML ( ArtifactTime.AFTER_TEST ),
    
    /** The execution video. */
    EXECUTION_VIDEO( ArtifactTime.AFTER_TEST ),
    
    /** The failure source. */
    FAILURE_SOURCE( ArtifactTime.ON_FAILURE ),
    
    /** The execution report. */
    WCAG_REPORT ( ArtifactTime.AFTER_TEST ),
    
    /** The device log. */
    DEVICE_LOG( ArtifactTime.AFTER_TEST ),
    
    /** The device log. */
    CONSOLE_LOG( ArtifactTime.AFTER_TEST ),
    
    EXECUTION_RECORD_HTML( ArtifactTime.AFTER_TEST ),
    
    EXECUTION_RECORD_CSV( ArtifactTime.AFTER_TEST ),
    
    EXECUTION_RECORD( ArtifactTime.NOOP ),
    
    EXECUTION_TIMING( ArtifactTime.AFTER_TEST ),
    
    EXECUTION_DEFINITION( ArtifactTime.AFTER_TEST);
    
    /** The time. */
    private ArtifactTime time;
    
    /**
     * Instantiates a new artifact type.
     *
     * @param time the time
     */
    ArtifactType( ArtifactTime time )
    {
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

