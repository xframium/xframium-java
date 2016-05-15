package com.xframium.page;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.xframium.page.Page.TimeMethod;
import com.xframium.page.element.Element;


// TODO: Auto-generated Javadoc
/**
 * The Interface Page.
 *
 * @author ageary
 */
public interface Page
{
	
	/**
	 * A tag indicating that this element is a page object element.  If the name cannot be derived from the site, class and element name
	 * then you may optional specify those.  By default, the system will use the page dat site name, the page object class simple name
	 * and the element variable name to locate the element from your element provider
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ElementDefinition
	{
		
		/**
		 * Site name.
		 *
		 * @return the string
		 */
		String siteName() default "";
		
		/**
		 * Page name.
		 *
		 * @return the string
		 */
		String pageName() default "";
		
		/**
		 * Element name.
		 *
		 * @return the string
		 */
		String elementName() default "";
	}
	
	/**
	 * A flag indicating that this method call should be timed.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface TimeMethod
	{
		
		/**
		 * Key name.
		 *
		 * @return the string
		 */
		String keyName() default "";
	}
	
	/**
	 * A flag indicating if a screen shot should be taken at the end of a method call - pass or fail.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ScreenShot
	{
	}

	
	/**
	 * Gets the element.
	 *
	 * @param elementName the element name
	 * @return the element
	 */
	public Element getElement( String elementName );
    
    /**
     * Gets the element.
     *
     * @param pageName the page name
     * @param elementName the element name
     * @return the element
     */
    public Element getElement( String pageName, String elementName );
    
    /**
     * Initialize page.
     */
    public void initializePage();
    
    /**
     * Sets the driver.
     *
     * @param webDriver the new driver
     */
    public void setDriver( Object webDriver );
}
