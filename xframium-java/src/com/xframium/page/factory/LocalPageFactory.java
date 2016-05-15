/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xframium.page.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.xframium.page.AbstractPage;
import com.xframium.page.ElementDescriptor;
import com.xframium.page.Page;
import com.xframium.page.PageManager;
import com.xframium.page.element.Element;



// TODO: Auto-generated Javadoc
/**
 * The local page factory is used to create the local instance using the interface name and 
 * a derived implementation name using a predefined structure.  The FQGN of the interface name 
 * appending .spi to the package name and Impl to the class name
 * @author ageary
 */
public class LocalPageFactory extends AbstractPageFactory
{

    /** The Constant DOT. */
    private static final char DOT = '.';
    
    /** The Constant SPI. */
    private static final String SPI = ".spi";
    
    /** The Constant IMPL. */
    private static final String IMPL = "Impl";
    
    /** The Constant GET. */
    private static final String GET = "get";
    
    /**
     * Instantiates a new local page factory.
     */
    public LocalPageFactory()
    {
    	
    }

    
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.factory.AbstractPageFactory#_createPage(java.lang.Class, java.lang.Object)
     */
    @Override
    protected Page _createPage(Class<Page> pageInterface, Object webDriver )
    {
    	this.webDriver = webDriver;
        String serviceName = pageInterface.getPackage().getName() + SPI + DOT + pageInterface.getSimpleName() + IMPL; 
        
        if ( log.isInfoEnabled() )
    		log.info( "Creating page implementation as " + serviceName );
        
        try
        {
        	Page currentPage = (Page) Class.forName( serviceName ).newInstance();
        	currentPage.initializePage();
        	currentPage.setDriver( webDriver );

        	
        	Method[] methodArray = currentPage.getClass().getInterfaces()[0].getMethods();
        	
        	for ( Method currentMethod : methodArray )
            {
        		if ( currentMethod.getAnnotation( Page.ElementDefinition.class ) != null )
            	{
            		if ( log.isDebugEnabled() )
                		log.debug( " Analyzing Method " + currentMethod.getName() );

            		//
            		// Strip off a 'get' if it exists for the elementName
            		//
            		String elementName = currentMethod.getName();
            		if ( elementName.startsWith( GET ) )
            			elementName = elementName.substring( 3 );

            		try
            		{
            			ElementDescriptor elementDescriptor = new ElementDescriptor( PageManager.instance().getSiteName(), currentPage.getClass().getInterfaces()[0].getSimpleName(), elementName );
                		Element currentElement = PageManager.instance().getElementProvider().getElement( elementDescriptor );
            			( (AbstractPage) currentPage ).registerElement( elementDescriptor, currentElement );
            		}
            		catch( Exception e )
            		{
            			e.printStackTrace();
            		}
            	}
            }
        	
        	Field[] fieldArray = currentPage.getClass().getFields();
        	for ( Field currentField : fieldArray )
            {
        		if ( currentField.getAnnotation( Page.ElementDefinition.class ) != null )
            	{
            		if ( log.isDebugEnabled() )
                		log.debug( " Analyzing Field " + currentField.getName() );
 
            		String fieldValue = currentField.get( currentPage ) + "";
            		
            		try
            		{
            			ElementDescriptor elementDescriptor = new ElementDescriptor( PageManager.instance().getSiteName(), currentPage.getClass().getInterfaces()[0].getSimpleName(), fieldValue );
                		Element currentElement = PageManager.instance().getElementProvider().getElement( elementDescriptor );
                		
                		currentElement.setTimed(  currentField.getAnnotation( Page.TimeMethod.class ) != null );
                		
            			( (AbstractPage) currentPage ).registerElement( elementDescriptor, currentElement );
            		}
            		catch( Exception e )
            		{
            			e.printStackTrace();
            		}
            	}
            }

        	
        	return currentPage;
        }
        catch( Exception e )
        {
            log.error( "Could not create a service for " + serviceName, e );
            return null;
        }
    }

    /**
     * Page created.
     *
     * @param serviceImpl the service impl
     */
    protected void pageCreated( Page serviceImpl )
    {
        serviceImpl.initializePage();
    }

}
