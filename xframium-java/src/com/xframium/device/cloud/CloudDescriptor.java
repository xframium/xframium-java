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
/*
 * 
 */
package com.xframium.device.cloud;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// TODO: Auto-generated Javadoc
/**
 * The Class CloudDescriptor.
 */
public class CloudDescriptor
{
	
	/** The name. */
	private String name;
	
	/** The user name. */
	private String userName;
	
	/** The password. */
	private String password;
	
	/** The host name. */
	private String hostName;
	
	/** The proxy host. */
	private String proxyHost;
	
	/** The proxy port. */
	private String proxyPort;
	
	/** The description. */
	private String description;
	
	/** The grid instance. */
	private String gridInstance;
	
	/**
	 * Instantiates a new cloud descriptor.
	 */
	public CloudDescriptor()
	{
		
	}
	
	/**
	 * Instantiates a new cloud descriptor.
	 *
	 * @param name the name
	 * @param userName the user name
	 * @param password the password
	 * @param hostName the host name
	 * @param proxyHost the proxy host
	 * @param proxyPort the proxy port
	 * @param description the description
	 * @param gridInstance the grid instance
	 */
	public CloudDescriptor( String name, String userName, String password, String hostName, String proxyHost, String proxyPort, String description, String gridInstance )
	{
		this.name = name;
		this.userName = userName;
		this.password = password;
		this.hostName = hostName;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.description = description;
		this.gridInstance = gridInstance;
	}
	
	/**
	 * Gets the cloud url.
	 *
	 * @return the cloud url
	 */
	public String getCloudUrl()
	{
		
		try
		{
			return "https://" + URLEncoder.encode( getUserName(), "UTF-8" ) + ":" + URLEncoder.encode( getPassword(), "UTF-8" ) + "@" + getHostName() + "/nexperience/wd/hub";
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return name + " (" + hostName + ")";
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
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Gets the host name.
	 *
	 * @return the host name
	 */
	public String getHostName()
	{
		return hostName;
	}

	/**
	 * Gets the proxy host.
	 *
	 * @return the proxy host
	 */
	public String getProxyHost()
	{
		return proxyHost;
	}

	/**
	 * Gets the proxy port.
	 *
	 * @return the proxy port
	 */
	public String getProxyPort()
	{
		return proxyPort;
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
	 * Gets the grid instance.
	 *
	 * @return the grid instance
	 */
	public String getGridInstance()
	{
		return gridInstance;
	}
	
	
}
