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
package com.xframium.application;

import java.util.Map;
import com.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationDescriptor.
 */
public class ApplicationDescriptor
{
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The android identifier. */
	private String androidIdentifier;
	
	/** The apple identifier. */
	private String appleIdentifier;
	
	/** The url. */
	private String url;
	
	/** The ios installation. */
	private String iosInstallation;
	
	/** The android installation. */
	private String androidInstallation;
	
	/** The capabilities. */
	private Map<String,Object> capabilities;


	/**
	 * Gets the capabilities.
	 *
	 * @return the capabilities
	 */
	public Map<String, Object> getCapabilities() {
		return capabilities;
	}

	/**
	 * Sets the capabilities.
	 *
	 * @param capabilities the capabilities
	 */
	public void setCapabilities(Map<String, Object> capabilities) {
		this.capabilities = capabilities;
	}

	/**
	 * Gets the ios installation.
	 *
	 * @return the ios installation
	 */
	public String getIosInstallation()
	{
		return iosInstallation;
	}

	/**
	 * Sets the ios installation.
	 *
	 * @param iosInstallation the new ios installation
	 */
	public void setIosInstallation( String iosInstallation )
	{
		this.iosInstallation = iosInstallation;
	}

	/**
	 * Gets the android installation.
	 *
	 * @return the android installation
	 */
	public String getAndroidInstallation()
	{
		return androidInstallation;
	}

	/**
	 * Sets the android installation.
	 *
	 * @param androidInstallation the new android installation
	 */
	public void setAndroidInstallation( String androidInstallation )
	{
		this.androidInstallation = androidInstallation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if ( url != null && !url.isEmpty() )
		{
			if ( (androidIdentifier != null && !androidIdentifier.isEmpty() ) || (appleIdentifier != null && !appleIdentifier.isEmpty() ) )
				return name + " (HYBRID)";
			else
				return name + " (WEB)";
		}
		else
		{
			if ( (androidIdentifier != null && !androidIdentifier.isEmpty() ) || (appleIdentifier != null && !appleIdentifier.isEmpty() ) )
				return name + " (NATIVE)";
			else
				return name + " (UNKNOWN)";
		}
	}
	
	public boolean isWeb()
	{
	    return url != null && !url.isEmpty();
	}
	
	/**
	 * Instantiates a new application descriptor.
	 *
	 * @param name the name
	 * @param description the description
	 * @param androidIdentifier the android identifier
	 * @param appleIdentifier the apple identifier
	 * @param url the url
	 * @param iosInstallation the ios installation
	 * @param androidInstallation the android installation
	 * @param capabilities the capabilities
	 */
	public ApplicationDescriptor( String name, String description, String androidIdentifier, String appleIdentifier, String url, String iosInstallation, String androidInstallation, Map<String,Object> capabilities )
	{
		super();
		this.name = name;
		this.description = description;
		this.androidIdentifier = androidIdentifier;
		this.appleIdentifier = appleIdentifier;
		this.url = url;
		this.iosInstallation = iosInstallation;
		this.androidInstallation = androidInstallation;
		this.capabilities = capabilities;
	}
	
	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}
	
	

	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl( String url )
	{
		this.url = url;
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
	 * Gets the android identifier.
	 *
	 * @return the android identifier
	 */
	public String getAndroidIdentifier()
	{
		return androidIdentifier;
	}
	
	/**
	 * Sets the android identifier.
	 *
	 * @param androidIdentifier the new android identifier
	 */
	public void setAndroidIdentifier( String androidIdentifier )
	{
		this.androidIdentifier = androidIdentifier;
	}
	
	/**
	 * Gets the apple identifier.
	 *
	 * @return the apple identifier
	 */
	public String getAppleIdentifier()
	{
		return appleIdentifier;
	}
	
	/**
	 * Sets the apple identifier.
	 *
	 * @param appleIdentifier the new apple identifier
	 */
	public void setAppleIdentifier( String appleIdentifier )
	{
		this.appleIdentifier = appleIdentifier;
	}
	
	/**
	 * Gets the identifier.
	 *
	 * @param deviceInformation the device information
	 * @return the identifier
	 */
	public String getIdentifier( Device deviceInformation )
	{
		switch( deviceInformation.getOs().toLowerCase() )
		{
			case "ios":
				return getAppleIdentifier();
				
			case "android":
				return getAndroidIdentifier();
				
			default:
				return null;
		}
		
	}
}
