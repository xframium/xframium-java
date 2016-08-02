package org.xframium.browser.capabilities;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.device.factory.DriverFactory;

/**
 * @author user
 * Factory class for creating instances for the browser capabilities.
 */
public class BrowserCapabilityManager {

	private static BrowserCapabilityManager singleton=new BrowserCapabilityManager();
	private Map<String,BrowserCapabilityFactory> browserCapabilityMap = new HashMap<String,BrowserCapabilityFactory>( 20 );

	public static BrowserCapabilityManager instance()
	{
		return singleton;
	}

	private BrowserCapabilityManager()
	{

	}


	private Log log=LogFactory.getLog(BrowserCapabilityManager.class);

	/**Method will create instances for factory.
	 * @param String - factoryName
	 * @return BrowserCapabilityFactory
	 */
	public synchronized BrowserCapabilityFactory getBrowsercapabilityFactory(String factoryName)
	{
		if ( log.isDebugEnabled() )
			log.debug( "Getting Driver Factory for " + factoryName );

		BrowserCapabilityFactory browserCapabilityFactory = (BrowserCapabilityFactory) browserCapabilityMap.get( factoryName );

		if ( browserCapabilityFactory == null )
		{
			String className = BrowserCapabilityFactory.class.getPackage().getName() + "." + factoryName + "Factory";

			if ( log.isInfoEnabled() )
				log.info( "Creating Driver Factory as " + className );

			try
			{
				browserCapabilityFactory = (BrowserCapabilityFactory)Class.forName( className ).newInstance();
				browserCapabilityMap.put( factoryName, browserCapabilityFactory );

			}
			catch( Exception e )
			{
				log.fatal( "Could not create DriverFactory for " + className, e );
			}
		}

		return browserCapabilityFactory;
	}

}


