/*******************************************************************************
 * xFramium
 *
 * Copyright 2017 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package org.xframium.integrations.alm.entity;

import java.io.File;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class ALMDefect.
 */
public class ALMDefect extends ALMEntity
{
    
    /** The assigned to. */
    @ALMField( PhysicalName="BG_RESPONSIBLE", Name="owner", Label="Assigned To" )
    private String assignedTo;
    
    /** The create time. */
    @ALMField( PhysicalName="BG_DETECTION_DATE", Name="creation-time", Label="Detected on Date" )
    private Date createTime = new Date( System.currentTimeMillis() );
    
    /** The description. */
    @ALMField( PhysicalName="BG_DESCRIPTION", Name="description", Label="Description" )
    private String description;
    
    /** The detected by. */
    @ALMField( PhysicalName="BG_DETECTED_BY", Name="detected-by", Label="Detected By" )
    private String detectedBy;
    
    /** The detected in cycle. */
    @ALMField( PhysicalName="BG_DETECTED_IN_RCYC", Name="detected-in-rcyc", Label="Detected in Cycle" )
    private String detectedInCycle;
    
    /** The detected in release. */
    @ALMField( PhysicalName="BG_DETECTED_IN_REL", Name="detected-in-rel", Label="Detected in Release" )
    private String detectedInRelease;
    
    /** The detected in version. */
    @ALMField( PhysicalName="BG_DETECTION_VERSION", Name="detection-version", Label="Detected in Version" )
    private String detectedInVersion;
    
    /** The detected in environment. */
    @ALMField( PhysicalName="BG_ENVIRONMENT", Name="environment", Label="Detected on Environment" )
    private String detectedInEnvironment;
    
    /** The priority. */
    @ALMField( PhysicalName="BG_PRIORITY", Name="priority", Label="Priority" )
    private String priority;
    
    /** The severity. */
    @ALMField( PhysicalName="BG_SEVERITY", Name="severity", Label="Severity" )
    private String severity;
    
    /** The status. */
    @ALMField( PhysicalName="BG_STATUS", Name="status", Label="Status" )
    private String status;
    
    /** The subject. */
    @ALMField( PhysicalName="BG_SUBJECT", Name="subject", Label="Subject" )
    private String subject;
    
    /** The summary. */
    @ALMField( PhysicalName="BG_SUMMARY", Name="name", Label="Summary" )
    private String summary;
    
    /** The attachments. */
    private ALMAttachment[] attachments;
    
    /** The step link id. */
    @ALMField( PhysicalName="BG_STEP_REFERENCE", Name="step-reference", Label="Step Reference" )
    private String stepLinkId;

    /**
     * Instantiates a new ALM defect.
     */
    public ALMDefect()
    {
        super( "defect" );
    }
    
    /**
     * Gets the assigned to.
     *
     * @return the assigned to
     */
    public String getAssignedTo()
    {
        return assignedTo;
    }

    /**
     * Sets the assigned to.
     *
     * @param assignedTo the new assigned to
     */
    public void setAssignedTo( String assignedTo )
    {
        this.assignedTo = assignedTo;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription( String description )
    {
        this.description = description;
    }

    /**
     * Gets the detected by.
     *
     * @return the detected by
     */
    public String getDetectedBy()
    {
        return detectedBy;
    }

    /**
     * Sets the detected by.
     *
     * @param detectedBy the new detected by
     */
    public void setDetectedBy( String detectedBy )
    {
        this.detectedBy = detectedBy;
    }

    /**
     * Gets the detected in cycle.
     *
     * @return the detected in cycle
     */
    public String getDetectedInCycle()
    {
        return detectedInCycle;
    }

    /**
     * Sets the detected in cycle.
     *
     * @param detectedInCycle the new detected in cycle
     */
    public void setDetectedInCycle( String detectedInCycle )
    {
        this.detectedInCycle = detectedInCycle;
    }

    /**
     * Gets the detected in release.
     *
     * @return the detected in release
     */
    public String getDetectedInRelease()
    {
        return detectedInRelease;
    }

    /**
     * Sets the detected in release.
     *
     * @param detectedInRelease the new detected in release
     */
    public void setDetectedInRelease( String detectedInRelease )
    {
        this.detectedInRelease = detectedInRelease;
    }

    /**
     * Gets the detected in version.
     *
     * @return the detected in version
     */
    public String getDetectedInVersion()
    {
        return detectedInVersion;
    }

    /**
     * Sets the detected in version.
     *
     * @param detectedInVersion the new detected in version
     */
    public void setDetectedInVersion( String detectedInVersion )
    {
        this.detectedInVersion = detectedInVersion;
    }

    /**
     * Gets the detected in environment.
     *
     * @return the detected in environment
     */
    public String getDetectedInEnvironment()
    {
        return detectedInEnvironment;
    }

    /**
     * Sets the detected in environment.
     *
     * @param detectedInEnvironment the new detected in environment
     */
    public void setDetectedInEnvironment( String detectedInEnvironment )
    {
        this.detectedInEnvironment = detectedInEnvironment;
    }

    /**
     * Gets the priority.
     *
     * @return the priority
     */
    public String getPriority()
    {
        return priority;
    }

    /**
     * Sets the priority.
     *
     * @param priority the new priority
     */
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
    
    /**
     * Sets the priority.
     *
     * @param priority the new priority
     */
    public void setPriority( String priority )
    {
        this.priority = priority;
    }

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public String getSeverity()
    {
        return severity;
    }

    /**
     * Sets the severity.
     *
     * @param severity the new severity
     */
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
    
    /**
     * Sets the severity.
     *
     * @param severity the new severity
     */
    public void setSeverity( String severity )
    {
        this.severity = severity;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus( String status )
    {
        this.status = status;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject()
    {
        return subject;
    }

    /**
     * Sets the subject.
     *
     * @param subject the new subject
     */
    public void setSubject( String subject )
    {
        this.subject = subject;
    }

    /**
     * Gets the summary.
     *
     * @return the summary
     */
    public String getSummary()
    {
        return summary;
    }

    /**
     * Sets the summary.
     *
     * @param summary the new summary
     */
    public void setSummary( String summary )
    {
        if ( summary != null && summary.length() > 255 )
            this.summary = summary.substring( 0,  254 );
        else
            this.summary = summary;
    }

    

    /**
     * Gets the creates the time.
     *
     * @return the creates the time
     */
    public Date getCreateTime()
    {
        return createTime;
    }

    /**
     * Sets the creates the time.
     *
     * @param createTime the new creates the time
     */
    public void setCreateTime( Date createTime )
    {
        this.createTime = createTime;
    }

    /**
     * Gets the attachments.
     *
     * @return the attachments
     */
    public ALMAttachment[] getAttachments()
    {
        return attachments;
    }

    /**
     * Sets the attachments.
     *
     * @param attachments the new attachments
     */
    public void setAttachments( ALMAttachment[] attachments )
    {
        this.attachments = attachments;
    }

    /**
     * Gets the step link id.
     *
     * @return the step link id
     */
    public String getStepLinkId()
    {
        return stepLinkId;
    }

    /**
     * Sets the step link id.
     *
     * @param stepLinkId the new step link id
     */
    public void setStepLinkId( String stepLinkId )
    {
        this.stepLinkId = stepLinkId;
    }
    
    
     
}
