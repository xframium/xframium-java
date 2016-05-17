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
package org.xframium.device.factory;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class DriverManager.
 */
public class DriverManager
{
	
	/** The singleton. */
	private static DriverManager singleton = new DriverManager();
	
	/**
	 * Instance.
	 *
	 * @return the driver manager
	 */
	public static DriverManager instance()
	{
		return singleton;
	}

	/**
	 * Instantiates a new driver manager.
	 */
	private DriverManager()
	{

	}

	/** The log. */
	private Log log = LogFactory.getLog( DriverManager.class );
	
	/** The driver map. */
	private Map<String,DriverFactory> driverMap = new HashMap<String,DriverFactory>( 20 );
	
	
	/**
	 * Gets the driver factory.
	 *
	 * @param driverName the driver name
	 * @return the driver factory
	 */
	public synchronized DriverFactory getDriverFactory( String driverName )
	{
		if ( log.isDebugEnabled() )
			log.debug( "Getting Driver Factory for " + driverName );
		
		DriverFactory driverFactory = driverMap.get( driverName );
		
		if ( driverFactory == null )
		{
			String className = DriverFactory.class.getPackage().getName() + ".spi." + driverName + "DriverFactory";
			
			if ( log.isInfoEnabled() )
				log.info( "Creating Driver Factory as " + className );
			
			try
			{
				driverFactory = (DriverFactory)Class.forName( className ).newInstance();
				driverMap.put( driverName, driverFactory );
			}
			catch( Exception e )
			{
				log.fatal( "Could not create DriverFactory for " + className, e );
			}
		}
		
		return driverFactory;
	}
}
