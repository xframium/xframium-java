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
package org.xframium.device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.bean.Handset;
import org.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleDevice.
 */
public class SimpleDevice implements Device
{

	/** The key. */
	private String key;
	
	/** The manufacturer. */
	private String manufacturer;
	
	/** The model. */
	private String model;
	
	/** The os. */
	private String os;
	
	/** The os version. */
	private String osVersion;
	
	/** The browser name. */
	private String browserName;
	
	/** The browser version. */
	private String browserVersion;
	
	/** The available devices. */
	private int availableDevices;
	
	/** The device lock. */
	private Semaphore deviceLock;
	
	/** The driver type. */
	private String driverType;
	
	/** The device name. */
	private String deviceName;
	
	/** The active. */
	private boolean active;
	
	private String resolution;
	private String environment;
	private String cloud;
	private List<String> tagNames = new ArrayList<String>( 5 );
	
	
	
	public String getCloud()
    {
        return cloud;
    }

    public void setCloud( String cloud )
    {
        this.cloud = cloud;
    }

    public String getResolution()
    {
        return resolution;
    }

    public void setResolution( String resolution )
    {
        this.resolution = resolution;
        generateEnv();
    }

    /** The capabilities. */
	private Map<String,Object> capabilities = new HashMap<String,Object>( 10 );
	private List<DeviceCap> capList = new ArrayList<DeviceCap>( 12 );
	
	/* (non-Javadoc)
	 * @see com.morelandLabs.spi.Device#isActive()
	 */
	public boolean isActive()
	{
		return active;
	}

	private void generateEnv()
	{
	    StringBuilder stringBuilder = new StringBuilder();
        if ( model != null )
            stringBuilder.append( " " ).append( model );

	    
	    if ( os != null )
        {
            stringBuilder.append( " / " ).append( os );
            if ( osVersion != null )
                stringBuilder.append( " " ).append( osVersion );
        }
	    
	    if ( browserName != null )
	    {
	        stringBuilder.append( " " ).append( browserName );
	        if ( browserVersion != null )
	            stringBuilder.append( " " ).append( browserVersion );
	    }
	    
	    environment = stringBuilder.toString();
	    
	}
	
	public String getEnvironment()
	{
	    return environment;
	}

	/** The cached string. */
	private String cachedString;
	
	/**
	 * Instantiates a new simple device.
	 *
	 * @param deviceName the device name
	 * @param driverType the driver type
	 */
	public SimpleDevice( String deviceName, String driverType )
	{
		this.deviceName = deviceName;
		key = deviceName;
		availableDevices = 1;
		deviceLock = new Semaphore( availableDevices );
		this.driverType = driverType;
		generateEnv();
	}
	
	/**
	 * Instantiates a new simple device.
	 *
	 * @param key the key
	 * @param manufacturer the manufacturer
	 * @param model the model
	 * @param os the os
	 * @param osVersion the os version
	 * @param browserName the browser name
	 * @param browserVersion the browser version
	 * @param availableDevices the available devices
	 * @param driverType the driver type
	 * @param active the active
	 * @param deviceName the device name
	 */
	public SimpleDevice( String key, String manufacturer, String model, String os, String osVersion, String browserName, String browserVersion, int availableDevices, String driverType, boolean active, String deviceName )
	{
		super();
		this.key = key;
		this.manufacturer = manufacturer;
		this.model = model;
		this.os = os;
		this.osVersion = osVersion;
		this.browserName = browserName;
		this.browserVersion = browserVersion;
		this.availableDevices = availableDevices;
		
		if ( availableDevices < 0)
		{
		    availableDevices = 0;
		    if ( CloudRegistry.instance().getCloud().getProvider() != null && CloudRegistry.instance().getCloud().getProvider().equals( "PERFECTO" ) )
            {
                for ( Handset hs : PerfectoMobile.instance().devices().getDevices( false ).getHandsetList() )
                {
                    if ( !compareValues( manufacturer, hs.getManufacturer() ) )
                        continue;
                    
                    if ( !compareValues( model, hs.getModel() ) )
                        continue;
                    
                    if ( !compareValues( os, hs.getOs() ) )
                        continue;
                    
                    if ( !compareValues( osVersion, hs.getOsVersion() ) )
                        continue;
                    
                    availableDevices++;
                }
            }
		}
		
		this.driverType = driverType;
		this.deviceName = deviceName;
		if ( availableDevices < 1 )
		    this.active = false;
		else
		    this.active = active;
		deviceLock = new Semaphore( availableDevices );
		
		cachedString = manufacturer + " " + model + " [" + key + "]";
		generateEnv();
	}
	
	public void setKey( String key )
    {
        this.key = key;
        generateEnv();
    }

    public void setManufacturer( String manufacturer )
    {
        this.manufacturer = manufacturer;
        generateEnv();
    }

    public void setModel( String model )
    {
        this.model = model;
        generateEnv();
    }

    public void setOs( String os )
    {
        this.os = os;
        generateEnv();
    }

    public void setOsVersion( String osVersion )
    {
        this.osVersion = osVersion;
        generateEnv();
    }

    public void setBrowserName( String browserName )
    {
        this.browserName = browserName;
        generateEnv();
    }

    public void setBrowserVersion( String browserVersion )
    {
        this.browserVersion = browserVersion;
        generateEnv();
    }

    public void setDeviceName( String deviceName )
    {
        this.deviceName = deviceName;
        generateEnv();
    }


    private boolean compareValues( String rootValue, String hsValue )
	{
	    if ( rootValue == null || rootValue.trim().isEmpty() )
	        return true;
	    
	    if ( rootValue.contains( "*" ) )
	    {
	        String useValue = rootValue.replace( "*", "" ).trim();
	        
	        if ( hsValue.contains( useValue ) )
	            return true;
	        else
	            return false;
	    }
	    
	    if ( rootValue.trim().equals( hsValue ) )
	    {
	        return true;
	    }
	    
	    return false;
	    
	}

	/* (non-Javadoc)
	 * @see com.morelandLabs.spi.Device#addCapability(java.lang.String, java.lang.String)
	 */
	public void addCapability( String capabilityName, Object capabilityValue, String classType )
	{
		capabilities.put( capabilityName,capabilityValue);
		capList.add(  new DeviceCap( capabilityName, capabilityValue, classType ) );
	}
	
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getDeviceName()
	 */
	public String getDeviceName()
	{
		return deviceName;
	}
	
	//public String toString()
	//{
	//	return cachedString;
	//}

	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#toShortString()
	 */
	public String toShortString()
	{
		return cachedString;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return manufacturer + " " + model + " (" + key + ")";
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getKey()
	 */
	public String getKey()
	{
		return key;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getDriverType()
	 */
	public String getDriverType()
	{
		return driverType;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getManufacturer()
	 */
	public String getManufacturer()
	{
		return manufacturer;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getModel()
	 */
	public String getModel()
	{
		return model;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getOs()
	 */
	public String getOs()
	{
		return os;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getOsVersion()
	 */
	public String getOsVersion()
	{
		return osVersion;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getBrowserName()
	 */
	public String getBrowserName()
	{
		return browserName;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getBrowserVersion()
	 */
	public String getBrowserVersion()
	{
		return browserVersion;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getAvailableDevices()
	 */
	public int getAvailableDevices()
	{
		return availableDevices;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.Device#getLock()
	 */
	public Semaphore getLock()
	{
		return deviceLock;
	}
	
	/* (non-Javadoc)
	 * @see com.morelandLabs.spi.Device#getCabilities()
	 */
	@Override
	public Map<String,Object> getCapabilities() 
	{
		return capabilities;
	}

    public String[] getTagNames()
    {
        return tagNames.toArray( new String[ 0 ] );
    }

    public void setTagNames( String[] tagNames )
    {
        this.tagNames.addAll( Arrays.asList( tagNames ) );
    }
    
    @Override
    public void addTag( String tagName )
    {
        tagNames.add( tagName );
    }
	
	
}
