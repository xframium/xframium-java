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
