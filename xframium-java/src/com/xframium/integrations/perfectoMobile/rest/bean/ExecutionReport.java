/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package com.xframium.integrations.perfectoMobile.rest.bean;

import java.util.ArrayList;
import java.util.List;
import com.xframium.integrations.rest.bean.AbstractBean;
import com.xframium.integrations.rest.bean.Bean.BeanDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class ExecutionReport.
 */
@BeanDescriptor ( beanName = "execution")
public class ExecutionReport extends AbstractBean
{
	
	/** The name. */
	@FieldDescriptor ( fieldPath = "info/name")
	private String name;
	
	/** The start time. */
	@FieldDescriptor ( fieldPath = "info/times/flowTimes/start/millis")
	private Long startTime;
	
	/** The end time. */
	@FieldDescriptor ( fieldPath = "info/times/flowTimes/end/millis")
	private Long endTime;

	/** The action list. */
	@FieldCollection ( fieldElement = Action.class, fieldPath = "flow/")
	private List<Action> actionList = new ArrayList<Action>( 10 );
	
	/** The script list. */
	@FieldCollection ( fieldElement = Script.class, fieldPath = "flow/")
	private List<Script> scriptList = new ArrayList<Script>( 10 );

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
	 * Gets the run time.
	 *
	 * @return the run time
	 */
	public long getRunTime()
	{
		return ( endTime - startTime );
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
	 * Gets the action list.
	 *
	 * @return the action list
	 */
	public List<Action> getActionList()
	{
		return actionList;
	}

	/**
	 * Sets the action list.
	 *
	 * @param actionList the new action list
	 */
	public void setActionList( List<Action> actionList )
	{
		this.actionList = actionList;
	}

	/**
	 * Gets the script list.
	 *
	 * @return the script list
	 */
	public List<Script> getScriptList()
	{
		return scriptList;
	}

	/**
	 * Sets the script list.
	 *
	 * @param scriptList the new script list
	 */
	public void setScriptList( List<Script> scriptList )
	{
		this.scriptList = scriptList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ExecutionReport [name=" + name + ", startTime=" + startTime + ", endTime=" + endTime + ", actionList=" + actionList + ", scriptList=" + scriptList + "]";
	}

	

}
