package com.xframium.page.element.provider;

import com.xframium.page.ElementDescriptor;
import com.xframium.page.Page;
import com.xframium.page.element.Element;

// TODO: Auto-generated Javadoc
/**
 * The Interface ElementProvider.
 */
public interface ElementProvider
{
	
	/**
	 * Gets the element.
	 *
	 * @param elementDescriptor the element descriptor
	 * @return the element
	 */
	Element getElement( ElementDescriptor elementDescriptor );
}
