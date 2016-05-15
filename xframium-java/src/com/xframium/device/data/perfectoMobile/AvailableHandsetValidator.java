/*
 * 
 */
package com.xframium.device.data.perfectoMobile;

import org.w3c.dom.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class AvailableHandsetValidator.
 */
public class AvailableHandsetValidator extends AbstractHandsetValidator implements PerfectoMobileHandsetValidator
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.data.perfectoMobile.PerfectoMobileHandsetValidator#validate(org.w3c.dom.Node)
	 */
	public boolean validate( Node handSet )
	{
		return Boolean.parseBoolean( getValue( handSet, "available" ) + "" );

	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.data.perfectoMobile.PerfectoMobileHandsetValidator#getMessage()
	 */
	public String getMessage()
	{
		return "There were no available devices in your cloud at this time";
	}
}
