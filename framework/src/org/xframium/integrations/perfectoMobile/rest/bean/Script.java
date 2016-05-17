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
package org.xframium.integrations.perfectoMobile.rest.bean;

import java.util.ArrayList;
import java.util.List;
import org.xframium.integrations.rest.bean.AbstractBean;
import org.xframium.integrations.rest.bean.Bean.BeanDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class Action.
 */
@BeanDescriptor( beanName="script" )
public class Script extends AbstractBean
{
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Script [name=" + name + ", startTime=" + startTime + ", endTime=" + endTime + ", success=" + success + ", failureDescription=" + failureDescription + ", timerList=" + timerList + "]";
	}


	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName( String name )
	{
		this.name = name;
	}


	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Long getStartTime()
	{
		return startTime;
	}


	/**
	 * Sets the start time.
	 *
	 * @param startTime the new start time
	 */
	public void setStartTime( Long startTime )
	{
		this.startTime = startTime;
	}


	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public Long getEndTime()
	{
		return endTime;
	}


	/**
	 * Sets the end time.
	 *
	 * @param endTime the new end time
	 */
	public void setEndTime( Long endTime )
	{
		this.endTime = endTime;
	}


	/**
	 * Gets the success.
	 *
	 * @return the success
	 */
	public Boolean getSuccess()
	{
		return success;
	}


	/**
	 * Sets the success.
	 *
	 * @param success the new success
	 */
	public void setSuccess( Boolean success )
	{
		this.success = success;
	}


	/**
	 * Gets the failure description.
	 *
	 * @return the failure description
	 */
	public String getFailureDescription()
	{
		return failureDescription;
	}


	/**
	 * Sets the failure description.
	 *
	 * @param failureDescription the new failure description
	 */
	public void setFailureDescription( String failureDescription )
	{
		this.failureDescription = failureDescription;
	}


	/**
	 * Gets the timer list.
	 *
	 * @return the timer list
	 */
	public List<Timer> getTimerList()
	{
		return timerList;
	}


	/**
	 * Sets the timer list.
	 *
	 * @param timerList the new timer list
	 */
	public void setTimerList( List<Timer> timerList )
	{
		this.timerList = timerList;
	}


	/** The name. */
	@FieldDescriptor ( fieldPath = "info/name")
	private String name;
	
	/** The start time. */
	@FieldDescriptor ( fieldPath = "info/times/flowTimes/start/millis")
	private Long startTime;
	
	/** The end time. */
	@FieldDescriptor ( fieldPath = "info/times/flowTimes/end/millis")
	private Long endTime;

	/** The success. */
	@FieldDescriptor ( fieldPath = "output/status/success")
	private Boolean success;
	
	/** The failure description. */
	@FieldDescriptor ( fieldPath = "output/status/description")
	private String failureDescription;

	
	/** The timer list. */
	@FieldCollection ( fieldElement = Timer.class, fieldPath = "output/timers")
	private List<Timer> timerList = new ArrayList<Timer>( 10 );

}
