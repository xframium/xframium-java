package org.xframium.device.capability;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.remote.DesiredCapabilities;

public abstract class AbstractCapabilityBridge implements CapabilityBridge {

	protected Log log = LogFactory.getLog( CapabilityBridge.class );
	
	@Override
	public void addCapabilities(DesiredCapabilities dC) 
	{
		if ( log.isInfoEnabled() )
			log.info( "Adding capabilities via " + getClass().getName() );
		_addCapabilities( dC );
		
		if ( log.isDebugEnabled() )
			log.debug( dC.asMap().toString() );
	}
	
	protected abstract void _addCapabilities( DesiredCapabilities dC );

}
