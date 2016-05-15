package com.xframium.page.element.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xframium.page.ElementDescriptor;
import com.xframium.page.Page;
import com.xframium.page.element.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractElementProvider.
 */
public abstract class AbstractElementProvider implements ElementProvider 
{
	
	/** The log. */
	protected Log log = LogFactory.getLog(ElementProvider.class);
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.provider.ElementProvider#getElement(com.perfectoMobile.page.ElementDescriptor)
	 */
	@Override
	public Element getElement( ElementDescriptor elementDescriptor )
	{
		return _getElement( elementDescriptor );
	}
	
	/**
	 * _get element.
	 *
	 * @param elementDescriptor the element descriptor
	 * @return the element
	 */
	protected abstract Element _getElement( ElementDescriptor elementDescriptor	 );

}
