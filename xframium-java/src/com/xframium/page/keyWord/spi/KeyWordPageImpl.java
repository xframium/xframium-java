package com.xframium.page.keyWord.spi;

import com.xframium.page.AbstractPage;
import com.xframium.page.ElementDescriptor;
import com.xframium.page.PageManager;
import com.xframium.page.element.Element;
import com.xframium.page.keyWord.KeyWordPage;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyWordPageImpl.
 */
public class KeyWordPageImpl extends AbstractPage implements KeyWordPage
{

	/** The _page name. */
	private String _pageName;
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.Page#initializePage()
	 */
	@Override
	public void initializePage()
	{
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.AbstractPage#getElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Element getElement( String siteName, String pageName, String elementName )
    {
    	ElementDescriptor elementDescriptor = new ElementDescriptor( siteName, pageName, elementName );
    	
    	if ( log.isInfoEnabled() )
    		log.info( Thread.currentThread().getName() + ": Attempting to locate element using [" + elementDescriptor.toString() + "]" + " - " + webDriver );
    	
    	Element myElement = PageManager.instance().getElementProvider().getElement( elementDescriptor ).cloneElement();
    	myElement.setDriver( webDriver );
    	return myElement;
    }
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.AbstractPage#getElement(java.lang.String, java.lang.String)
	 */
	public Element getElement( String pageName, String elementName )
    {
    	return getElement( PageManager.instance().getSiteName(), pageName, elementName );
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.AbstractPage#getElement(java.lang.String)
     */
    public Element getElement( String elementName )
    {
    	return getElement( PageManager.instance().getSiteName(), _pageName, elementName );
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.AbstractPage#getPageName()
     */
    @Override
    public String getPageName()
    {
    	return _pageName;
    }
    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.KeyWordPage#setPageName(java.lang.String)
     */
    @Override
    public void setPageName( String pageName )
    {
    	this._pageName = pageName;
    }
    
    

}
