/*
 * 
 */
package com.xframium.device.data.perfectoMobile;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractHandsetValidator.
 */
public abstract class AbstractHandsetValidator implements PerfectoMobileHandsetValidator
{
	
	/**
	 * Gets the value.
	 *
	 * @param handSet the hand set
	 * @param tagName the tag name
	 * @return the value
	 */
	protected String getValue( Node handSet, String tagName )
	{
		NodeList params = handSet.getChildNodes();
		
		for ( int i=0; i<params.getLength(); i++ )
		{
			if ( params.item( i ).getNodeName().toLowerCase().equals( tagName.toLowerCase() ) )
			{
				return params.item( i ).getTextContent();
			}
		}
		
		return null;
	}
}
