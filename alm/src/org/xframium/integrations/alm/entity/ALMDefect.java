package org.xframium.integrations.alm.entity;

import java.io.File;
import java.util.Date;

public class ALMDefect extends ALMEntity
{
    @ALMField( PhysicalName="BG_RESPONSIBLE", Name="owner", Label="Assigned To" )
    private String assignedTo;
    
    @ALMField( PhysicalName="BG_DETECTION_DATE", Name="creation-time", Label="Detected on Date" )
    private Date createTime = new Date( System.currentTimeMillis() );
    
    @ALMField( PhysicalName="BG_DESCRIPTION", Name="description", Label="Description" )
    private String description;
    
    @ALMField( PhysicalName="BG_DETECTED_BY", Name="detected-by", Label="Detected By" )
    private String detectedBy;
    
    @ALMField( PhysicalName="BG_DETECTED_IN_RCYC", Name="detected-in-rcyc", Label="Detected in Cycle" )
    private String detectedInCycle;
    
    @ALMField( PhysicalName="BG_DETECTED_IN_REL", Name="detected-in-rel", Label="Detected in Release" )
    private String detectedInRelease;
    
    @ALMField( PhysicalName="BG_DETECTION_VERSION", Name="detection-version", Label="Detected in Version" )
    private String detectedInVersion;
    
    @ALMField( PhysicalName="BG_ENVIRONMENT", Name="environment", Label="Detected on Environment" )
    private String detectedInEnvironment;
    
    @ALMField( PhysicalName="BG_PRIORITY", Name="priority", Label="Priority" )
    private String priority;
    
    @ALMField( PhysicalName="BG_SEVERITY", Name="severity", Label="Severity" )
    private String severity;
    
    @ALMField( PhysicalName="BG_STATUS", Name="status", Label="Status" )
    private String status;
    
    @ALMField( PhysicalName="BG_SUBJECT", Name="subject", Label="Subject" )
    private String subject;
    
    @ALMField( PhysicalName="BG_SUMMARY", Name="name", Label="Summary" )
    private String summary;
    
    private ALMAttachment[] attachments;
    
    @ALMField( PhysicalName="BG_STEP_REFERENCE", Name="step-reference", Label="Step Reference" )
    private String stepLinkId;

    public ALMDefect()
    {
        super( "defect" );
    }
    
    public String getAssignedTo()
    {
        return assignedTo;
    }

    public void setAssignedTo( String assignedTo )
    {
        this.assignedTo = assignedTo;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getDetectedBy()
    {
        return detectedBy;
    }

    public void setDetectedBy( String detectedBy )
    {
        this.detectedBy = detectedBy;
    }

    public String getDetectedInCycle()
    {
        return detectedInCycle;
    }

    public void setDetectedInCycle( String detectedInCycle )
    {
        this.detectedInCycle = detectedInCycle;
    }

    public String getDetectedInRelease()
    {
        return detectedInRelease;
    }

    public void setDetectedInRelease( String detectedInRelease )
    {
        this.detectedInRelease = detectedInRelease;
    }

    public String getDetectedInVersion()
    {
        return detectedInVersion;
    }

    public void setDetectedInVersion( String detectedInVersion )
    {
        this.detectedInVersion = detectedInVersion;
    }

    public String getDetectedInEnvironment()
    {
        return detectedInEnvironment;
    }

    public void setDetectedInEnvironment( String detectedInEnvironment )
    {
        this.detectedInEnvironment = detectedInEnvironment;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority( int priority )
    {
        switch ( priority )
        {
            case 1:
                this.priority = "1-Low";
                break;
                
            case 2:
                this.priority = "2-Medium";
                break;
            case 3:
                this.priority = "3-High";
                break;
            case 4:
                this.priority = "4-Very High";
                break;
            case 5:
                this.priority = "5-Urgent";
                break;
        }
    }
    
    public void setPriority( String priority )
    {
        this.priority = priority;
    }

    public String getSeverity()
    {
        return severity;
    }

    public void setSeverity( int severity )
    {
        switch ( severity )
        {
            case 1:
                this.severity = "1-Low";
                break;
                
            case 2:
                this.severity = "2-Medium";
                break;
            case 3:
                this.severity = "3-High";
                break;
            case 4:
                this.severity = "4-Very High";
                break;
            case 5:
                this.severity = "5-Urgent";
                break;
        }
    }
    
    public void setSeverity( String severity )
    {
        this.severity = severity;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject( String subject )
    {
        this.subject = subject;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary( String summary )
    {
        if ( summary != null && summary.length() > 255 )
            this.summary = summary.substring( 0,  254 );
        else
            this.summary = summary;
    }

    

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime( Date createTime )
    {
        this.createTime = createTime;
    }

    public ALMAttachment[] getAttachments()
    {
        return attachments;
    }

    public void setAttachments( ALMAttachment[] attachments )
    {
        this.attachments = attachments;
    }

    public String getStepLinkId()
    {
        return stepLinkId;
    }

    public void setStepLinkId( String stepLinkId )
    {
        this.stepLinkId = stepLinkId;
    }
    
    
     
}
