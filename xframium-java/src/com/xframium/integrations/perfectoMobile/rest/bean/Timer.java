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
package com.xframium.integrations.perfectoMobile.rest.bean;

import com.xframium.integrations.rest.bean.AbstractBean;
import com.xframium.integrations.rest.bean.Bean.BeanDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class Action.
 */
@BeanDescriptor( beanName="timer" )
public class Timer extends AbstractBean
{

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Timer [name=" + name + ", elapsed=" + elapsed + ", device=" + device + ", system=" + system + ", ux=" + ux + "]";
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
	 * Gets the elapsed.
	 *
	 * @return the elapsed
	 */
	public Double getElapsed()
	{
		return elapsed;
	}

	/**
	 * Sets the elapsed.
	 *
	 * @param elapsed the new elapsed
	 */
	public void setElapsed( Double elapsed )
	{
		this.elapsed = elapsed;
	}

	/**
	 * Gets the device.
	 *
	 * @return the device
	 */
	public Double getDevice()
	{
		return device;
	}

	/**
	 * Sets the device.
	 *
	 * @param device the new device
	 */
	public void setDevice( Double device )
	{
		this.device = device;
	}

	/**
	 * Gets the system.
	 *
	 * @return the system
	 */
	public Boolean getSystem()
	{
		return system;
	}

	/**
	 * Sets the system.
	 *
	 * @param system the new system
	 */
	public void setSystem( Boolean system )
	{
		this.system = system;
	}

	/**
	 * Gets the ux.
	 *
	 * @return the ux
	 */
	public String getUx()
	{
		return ux;
	}

	/**
	 * Sets the ux.
	 *
	 * @param ux the new ux
	 */
	public void setUx( String ux )
	{
		this.ux = ux;
	}

	/** The name. */
	@FieldDescriptor ( fieldPath = "@id")
	private String name;
	
	/** The elapsed. */
	@FieldDescriptor ( fieldPath = "time[@label='elapsed']")
	private Double elapsed;
	
	/** The device. */
	@FieldDescriptor ( fieldPath = "time[@label='device']")
	private Double device;

	/** The system. */
	@FieldDescriptor ( fieldPath = "time[@label='system']")
	private Boolean system;
	
	/** The ux. */
	@FieldDescriptor ( fieldPath = "time[@label='ux']")
	private String ux;

	

}
