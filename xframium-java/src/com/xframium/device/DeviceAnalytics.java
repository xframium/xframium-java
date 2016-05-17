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
package com.xframium.device;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceAnalytics.
 */
public class DeviceAnalytics
{
	
	/** The key. */
	private String key;
	
	/** The usage map. */
	private Map<String,Boolean> usageMap = new HashMap<String,Boolean>( 10 );
	
	/**
	 * Instantiates a new device analytics.
	 *
	 * @param deviceKey the device key
	 */
	public DeviceAnalytics( String deviceKey )
	{
		this.key = deviceKey;
	}
	
	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}
	
	/**
	 * Adds the run.
	 *
	 * @param runKey the run key
	 */
	public void addRun( String runKey )
	{
		if ( usageMap.containsKey( runKey ) )
		{
			throw new IllegalArgumentException( "Device " + key + " has already ran " + runKey );
		}
		
		usageMap.put( runKey, Boolean.TRUE );
	}
	
	/**
	 * Checks for run.
	 *
	 * @param runKey the run key
	 * @return true, if successful
	 */
	public boolean hasRun( String runKey )
	{
		Boolean returnValue = usageMap.get( runKey );
		if ( returnValue == null )
			return false;
		else
			return returnValue;
	}
	
	/**
	 * Gets the usage.
	 *
	 * @return the usage
	 */
	public int getUsage()
	{
		return usageMap.size();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append( "Device-" ).append( key ).append( ": " );
		for ( String keyName : usageMap.keySet() )
			sBuilder.append( keyName ).append( ", ");
		
		return sBuilder.toString();
	}
}
