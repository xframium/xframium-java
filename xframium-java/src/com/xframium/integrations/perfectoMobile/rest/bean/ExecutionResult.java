package com.xframium.integrations.perfectoMobile.rest.bean;

import com.xframium.integrations.rest.bean.AbstractBean;
import com.xframium.integrations.rest.bean.Bean.BeanDescriptor;
import com.xframium.integrations.rest.bean.Bean.FieldDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class ExecutionResult.
 */
@BeanDescriptor( beanName="response" )
public class ExecutionResult extends AbstractBean
{
	
	/** The execution id. */
	@FieldDescriptor ( )
	private String executionId;
	
	/** The report key. */
	@FieldDescriptor ( )
	private String reportKey;

	/** The status. */
	@FieldDescriptor ( )
	private String status;
	
	/** The failed validations. */
	@FieldDescriptor ( )
	private Integer failedValidations;
	
	/** The description. */
	@FieldDescriptor ( )
	private String description;
	
	/** The failed actions. */
	@FieldDescriptor ( )
	private Integer failedActions;
	
	/** The progress percentage. */
	@FieldDescriptor ( )
	private Double progressPercentage;
	
	/** The user. */
	@FieldDescriptor ( )
	private String user;
	
	/** The completed. */
	@FieldDescriptor ( )
	private Boolean completed;

	/**
	 * Gets the execution id.
	 *
	 * @return the execution id
	 */
	public String getExecutionId()
	{
		return executionId;
	}

	/**
	 * Sets the execution id.
	 *
	 * @param executionId the new execution id
	 */
	public void setExecutionId( String executionId )
	{
		this.executionId = executionId;
	}

	/**
	 * Gets the report key.
	 *
	 * @return the report key
	 */
	public String getReportKey()
	{
		return reportKey;
	}

	/**
	 * Sets the report key.
	 *
	 * @param reportKey the new report key
	 */
	public void setReportKey( String reportKey )
	{
		this.reportKey = reportKey;
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
	 * Gets the failed validations.
	 *
	 * @return the failed validations
	 */
	public Integer getFailedValidations()
	{
		if ( failedValidations == 0 )
			failedValidations = 0;
		return failedValidations;
	}

	/**
	 * Sets the failed validations.
	 *
	 * @param failedValidations the new failed validations
	 */
	public void setFailedValidations( Integer failedValidations )
	{
		this.failedValidations = failedValidations;
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
	 * Gets the failed actions.
	 *
	 * @return the failed actions
	 */
	public Integer getFailedActions()
	{
		if ( failedActions == null )
			failedActions = 0;
		return failedActions;
	}

	/**
	 * Sets the failed actions.
	 *
	 * @param failedActions the new failed actions
	 */
	public void setFailedActions( Integer failedActions )
	{
		this.failedActions = failedActions;
	}

	/**
	 * Gets the progress percentage.
	 *
	 * @return the progress percentage
	 */
	public Double getProgressPercentage()
	{
		if ( progressPercentage == null )
			progressPercentage = 0.0;
		return progressPercentage;
	}

	/**
	 * Sets the progress percentage.
	 *
	 * @param progressPercentage the new progress percentage
	 */
	public void setProgressPercentage( Double progressPercentage )
	{
		this.progressPercentage = progressPercentage;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser( String user )
	{
		this.user = user;
	}

	/**
	 * Gets the completed.
	 *
	 * @return the completed
	 */
	public Boolean getCompleted()
	{
		if ( completed == null )
			completed = false;
		return completed;
	}

	/**
	 * Sets the completed.
	 *
	 * @param completed the new completed
	 */
	public void setCompleted( Boolean completed )
	{
		this.completed = completed;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ExecutionResult [executionId=" + executionId + ", reportKey=" + reportKey + ", status=" + status + ", description=" + description + ", user=" + user + ", completed=" + completed + "]";
	}


}
