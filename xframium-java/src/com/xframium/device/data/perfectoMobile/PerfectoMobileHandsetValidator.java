/*
 * 
 */
package com.xframium.device.data.perfectoMobile;

import org.w3c.dom.Node;

// TODO: Auto-generated Javadoc
/**
 * The Interface PerfectoMobileHandsetValidator.
 */
public interface PerfectoMobileHandsetValidator
{
	
	/**
	 * Validate.
	 *
	 * @param handSet the hand set
	 * @return true, if successful
	 */
	public boolean validate( Node handSet );
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage();
}
