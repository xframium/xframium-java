package com.xframium.application;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationRegistry.
 */
public class ApplicationRegistry
{
	
	/** The singleton. */
	private static ApplicationRegistry singleton = new ApplicationRegistry();

	/**
	 * Instance.
	 *
	 * @return the application registry
	 */
	public static ApplicationRegistry instance()
	{
		return singleton;
	}

	/**
	 * Instantiates a new application registry.
	 */
	private ApplicationRegistry()
	{

	}
	
	/** The log. */
	private Log log = LogFactory.getLog( ApplicationRegistry.class );
	
	/** The application map. */
	private Map<String,ApplicationDescriptor> applicationMap = new HashMap<String,ApplicationDescriptor>( 20 );
	
	/** The application provider. */
	private ApplicationProvider applicationProvider;
	
	/** The aut. */
	private ApplicationDescriptor aut = null;
	
	/**
	 * Adds the application descriptor.
	 *
	 * @param applicationDescriptor the application descriptor
	 */
	public void addApplicationDescriptor( ApplicationDescriptor applicationDescriptor )
	{
		applicationMap.put( applicationDescriptor.getName(), applicationDescriptor );
	}
	
	/**
	 * Sets the application provider.
	 *
	 * @param applicationProvider the new application provider
	 */
	public void setApplicationProvider( ApplicationProvider applicationProvider )
	{
		this.applicationProvider = applicationProvider;
		applicationMap.put( "NOOP", new ApplicationDescriptor( "NOOP", "This is the default application type that just opens the phone", "", "", "", "", "", new HashMap<String,Object>( 0 ) ) );
	}
	
	/**
	 * Sets the aut.
	 *
	 * @param appName the new aut
	 */
	public void setAUT( String appName )
	{
		aut = applicationMap.get( appName );
		
		if ( aut == null )
			throw new IllegalArgumentException( "Unknown Application Identifier " + appName );
		
		if ( log.isInfoEnabled() )
			log.info( "Application Under Test set to " + aut );
		
	}
	
	/**
	 * Gets the applications.
	 *
	 * @return the applications
	 */
	public Collection<ApplicationDescriptor> getApplications()
	{
		return applicationMap.values();
	}
	
	/**
	 * Gets the aut.
	 *
	 * @return the aut
	 */
	public ApplicationDescriptor getAUT()
	{
		if ( aut == null && !applicationMap.isEmpty() )
			aut = applicationMap.values().toArray( new ApplicationDescriptor[ 0 ])[ 0 ];
		return aut;
	}
	
	/**
	 * Gets the application.
	 *
	 * @param appName the app name
	 * @return the application
	 */
	public ApplicationDescriptor getApplication( String appName )
	{
		return applicationMap.get( appName );
	}
	
	public void clear()
	{
	    applicationMap.clear();
	}
	
}
