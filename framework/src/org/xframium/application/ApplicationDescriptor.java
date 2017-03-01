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
package org.xframium.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.xframium.page.keyWord.step.spi.KWSBrowser.SwitchType;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationDescriptor.
 */
public class ApplicationDescriptor
{
	
    public enum AppType
    {
        NATIVE( 1, "NATIVE", "A native IOS, Android or Windows application"),
        HYBRID( 2, "HYBRID", "A mix of native and web applications"),
        UNKNOWN( 3, "UNKNOWN", "An unknown application type"),
        WEB( 4, "WEB", "A web application or web site");
        
        private AppType( int id, String name, String description )
        {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        private int id;
        private String name;
        private String description;
        
        public List<AppType> getSupported()
        {
            List<AppType> supportedList = new ArrayList<AppType>( 10 );
            supportedList.add( AppType.NATIVE );
            supportedList.add( AppType.HYBRID );
            supportedList.add( AppType.UNKNOWN );
            supportedList.add( AppType.WEB );
            return supportedList;
        }
    }
    
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

	private AppType appType;
	
	private double version;
	
	private String environment;
	
	private boolean autoStart = true;
	
	
	
	public boolean isAutoStart()
    {
        return autoStart;
    }

    public void setAutoStart( boolean autoStart )
    {
        this.autoStart = autoStart;
    }

    public String getEnvironment()
    {
        return environment;
    }

    public void setEnvironment( String environment )
    {
        this.environment = environment;
    }

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


	public double getVersion()
    {
        return version;
    }

    public void setVersion( double version )
    {
        this.version = version;
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
		_setAppType();
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
		_setAppType();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
	    return name + " (" + appType.name() + ")";
	}
	
	private void _setAppType()
	{
	    if ( url != null && !url.isEmpty() )
        {
            if ( (androidIdentifier != null && !androidIdentifier.isEmpty() ) || (appleIdentifier != null && !appleIdentifier.isEmpty() ) )
                appType = AppType.HYBRID;
            else
                appType = AppType.WEB;
        }
        else
        {
            if ( (androidIdentifier != null && !androidIdentifier.isEmpty() ) || (appleIdentifier != null && !appleIdentifier.isEmpty() ) )
                appType = AppType.NATIVE;
            else
                appType = AppType.UNKNOWN;
        }
	}
	
	public boolean isWeb()
	{
	    return url != null && !url.trim().isEmpty();
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
	public ApplicationDescriptor( String name, String description, String androidIdentifier, String appleIdentifier, String url, String iosInstallation, String androidInstallation, Map<String,Object> capabilities, double appVersion, String environment, boolean autoStart )
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
		this.version = appVersion;
		this.environment = environment;
		this.autoStart = autoStart;
		
		_setAppType();
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
		_setAppType();
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
		_setAppType();
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
		_setAppType();
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
