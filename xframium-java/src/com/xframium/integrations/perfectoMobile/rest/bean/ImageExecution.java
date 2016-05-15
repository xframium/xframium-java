package com.xframium.integrations.perfectoMobile.rest.bean;

import com.xframium.integrations.rest.bean.AbstractBean;
import com.xframium.integrations.rest.bean.Bean.BeanDescriptor;
import com.xframium.integrations.rest.bean.Bean.FieldDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageExecution.
 */
@BeanDescriptor( beanName="response" )
public class ImageExecution extends AbstractBean
{
	
	/** The width. */
	@FieldDescriptor ( fieldPath="screenAnalysis.resultRegion.width" )
	private String width;
	
	/** The height. */
	@FieldDescriptor ( fieldPath="screenAnalysis.resultRegion.width" )
	private String height;

	/** The grade. */
	@FieldDescriptor ( fieldPath="screenAnalysis.grade" )
	private String grade;
	
	/** The status. */
	@FieldDescriptor ( fieldPath="returnValue" )
	private String status;

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public String getWidth()
	{
		return width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public String getHeight()
	{
		return height;
	}

	/**
	 * Gets the grade.
	 *
	 * @return the grade
	 */
	public String getGrade()
	{
		return grade;
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
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth( String width )
	{
		this.width = width;
	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight( String height )
	{
		this.height = height;
	}

	/**
	 * Sets the grade.
	 *
	 * @param grade the new grade
	 */
	public void setGrade( String grade )
	{
		this.grade = grade;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Imaging [width=" + width + ", height=" + height + ", grade=" + grade + ", status=" + status + "]";
	}

	
	
	
	

}
