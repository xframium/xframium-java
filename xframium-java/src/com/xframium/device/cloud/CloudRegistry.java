/*
 * 
 */
package com.xframium.device.cloud;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class cloudRegistry.
 */
public class CloudRegistry
{
	
	/** The singleton. */
	private static CloudRegistry singleton = new CloudRegistry();

	/**
	 * Instance.
	 *
	 * @return the cloud registry
	 */
	public static CloudRegistry instance()
	{
		return singleton;
	}

	/**
	 * Instantiates a new cloud registry.
	 */
	private CloudRegistry()
	{

	}
	
	/** The log. */
	private Log log = LogFactory.getLog( CloudRegistry.class );
	
	/** The cloud map. */
	private Map<String,CloudDescriptor> cloudMap = new HashMap<String,CloudDescriptor>( 20 );
	
	/** The cloud provider. */
	private CloudProvider cloudProvider;
	
	/** The cut. */
	private CloudDescriptor cut = null;
	
	/**
	 * Adds the cloud descriptor.
	 *
	 * @param cloudDescriptor the cloud descriptor
	 */
	public void addCloudDescriptor( CloudDescriptor cloudDescriptor )
	{
		cloudMap.put( cloudDescriptor.getName(), cloudDescriptor );
	}
	
	/**
	 * Sets the cloud provider.
	 *
	 * @param cloudProvider the new cloud provider
	 */
	public void setCloudProvider( CloudProvider cloudProvider )
	{
		this.cloudProvider = cloudProvider;
	}
	
	
	/**
	 * Gets the cloud descriptors.
	 *
	 * @return the cloud descriptors
	 */
	public Collection<CloudDescriptor> getCloudDescriptors()
	{
		return Collections.unmodifiableCollection( cloudMap.values() );
		
	}
	
	/**
	 * Sets the cut.
	 *
	 * @param cloudName the new cloud
	 */
	public void setCloud( String cloudName )
	{
		cut = cloudMap.get( cloudName );
		
		if ( cut == null )
			throw new IllegalArgumentException( "Unknown cloud Identifier " + cloudName );
		
		if ( log.isInfoEnabled() )
			log.info( "cloud Under Test set to " + cut );
		
		System.setProperty( "__cloudUrl", "https://" + cut.getHostName() );
		System.setProperty( "__userName", cut.getUserName() );
		System.setProperty( "__password", cut.getPassword() );
	}
	
	/**
	 * Gets the aut.
	 *
	 * @param cloudName the cloud name
	 * @return the aut
	 */
	public CloudDescriptor getCloud( String cloudName )
	{
		return cloudMap.get( cloudName );
	}
	
	/**
	 * Gets the aut.
	 *
	 * @return the aut
	 */
	public CloudDescriptor getCloud()
	{
		if ( cut == null && !cloudMap.isEmpty() )
			cut = cloudMap.values().toArray( new CloudDescriptor[ 0 ] )[ 0 ];
		return cut;
	}
	
	public void clear()
	{
	    cloudMap.clear();
	}
}
