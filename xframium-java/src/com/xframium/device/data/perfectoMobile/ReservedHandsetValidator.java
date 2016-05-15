/*
 * 
 */
package com.xframium.device.data.perfectoMobile;

import org.w3c.dom.Node;
import com.xframium.device.cloud.CloudRegistry;

// TODO: Auto-generated Javadoc
/**
 * The Class ReservedHandsetValidator.
 */
public class ReservedHandsetValidator extends AbstractHandsetValidator implements PerfectoMobileHandsetValidator
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.data.perfectoMobile.PerfectoMobileHandsetValidator#validate(org.w3c.dom.Node)
	 */
	public boolean validate( Node handSet )
	{
		if ( Boolean.parseBoolean( getValue( handSet, "available" ) + "" ) )
		{
			if ( Boolean.parseBoolean( getValue( handSet, "reserved" ) + "" ) )
			{
				return CloudRegistry.instance().getCloud().getUserName().toLowerCase().equals (getValue( handSet, "reservedTo" ) );
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.data.perfectoMobile.PerfectoMobileHandsetValidator#getMessage()
	 */
	public String getMessage()
	{
		return "There were no devices registered to the logged in user running the tests";
	}
}
