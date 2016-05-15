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
