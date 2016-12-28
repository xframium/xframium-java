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
	    applicationMap.put( "NOOP", new ApplicationDescriptor( "NOOP", "This is the default application type that just opens the phone", "", "", "", "", "", new HashMap<String,Object>( 0 ), 0 ) );
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
		
		for ( ApplicationDescriptor a : applicationProvider.readData() )
		    addApplicationDescriptor( a );
	}
	
	/**
	 * Sets the aut.
	 *
	 * @param appName the new aut
	 */
	public void setAUT( String appName )
	{
	    if ( appName == null )
	    {
	        aut = null;
	        return;
	    }
	    
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
