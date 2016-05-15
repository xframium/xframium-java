/*
 * 
 */
package com.xframium.device.factory;

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
