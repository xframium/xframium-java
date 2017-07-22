/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xframium.page.factory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.xframium.exception.ObjectConfigurationException;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.reporting.ExecutionContextTest;




// TODO: Auto-generated Javadoc
/**
 * A factory for creating DefaultPage objects.
 *
 * @author ageary
 */
public class DefaultPageFactory extends LocalPageFactory implements InvocationHandler
{
    private String xFID;
    public DefaultPageFactory( String xFID )
    {
        this.xFID = xFID;
    }
    
    /** The Constant TYPE. */
    private static final String TYPE = "TYPE";

    /* (non-Javadoc)
     * @see com.perfectoMobile.page.factory.LocalPageFactory#_createPage(java.lang.Class, java.lang.Object)
     */
    @Override
    protected Page _createPage(Class<Page> serviceInterface, Object webDriver ) 
    {
    	
    	this.webDriver = webDriver;
    	if ( serviceInterface == null )
    		return null;
    	
    	if ( log.isInfoEnabled() )
    		log.info( "Attempting to create PROXY interface by " + serviceInterface );
    	
    	Page newPage = (Page) Proxy.newProxyInstance( this.getClass().getClassLoader(), new Class[] { serviceInterface, Page.class }, this );
    	
    	return newPage;
    }
    
    /**
     * Find method.
     *
     * @param rootClass the root class
     * @param methodName the method name
     * @param args the args
     * @return the method
     */
    private Method findMethod( Class rootClass, String methodName, Object[] args )
    {
    	Method[] methodArray = rootClass.getMethods();
    	
    	for ( Method currentMethod : methodArray )
		{
			if ( isCorrectMethod(currentMethod, methodName, args) )
			{
				if ( log.isDebugEnabled() )
					log.debug( "Found [" +methodName + "] on " + rootClass.getName() );
				
				if ( log.isDebugEnabled() && args != null )
				{
					StringBuilder pBuilder = new StringBuilder();
					
					pBuilder.append( args.length ).append(" paramters supplied as: \r\n" );
					
					for( Object arg : args )
					{
						pBuilder.append( "\t" );
						if ( arg == null )
							pBuilder.append( "NULL" );
						else
							pBuilder.append( "[" + arg.toString() + "] of type " + arg.getClass().getName() );
						pBuilder.append( "\r\n" );
						
					}
					log.debug( pBuilder.toString() );
				}
				
				return currentMethod;
			}
		}
    	
    	if ( log.isWarnEnabled() )
		{
			StringBuilder pBuilder = new StringBuilder();
			pBuilder.append( "Could not locate ").append( methodName ).append( " with ");
			pBuilder.append( args.length ).append(" paramters supplied as: \r\n" );
			
			for( Object arg : args )
			{
				pBuilder.append( "\t" );
				if ( arg == null )
					pBuilder.append( "NULL" );
				else
					pBuilder.append( "[" + arg.toString() + "] of type " + arg.getClass().getName() );
				pBuilder.append( "\r\n" );
				
			}
			log.warn( pBuilder.toString() );
		}
		return null;
    	
    	
    }
    
	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
	{
		
		Page currentService = PageManager.instance( xFID ).getPageCache().get( proxy.getClass() );
		
		if ( currentService == null )
		{
		    currentService = super._createPage( (Class<Page>) proxy.getClass().getInterfaces()[0], webDriver);
		    PageManager.instance( xFID ).getPageCache().put(  proxy.getClass(), currentService );
		}
		 
		Method methodImplemenation = findMethod( currentService.getClass(), method.getName(), args );
		
		if ( methodImplemenation != null )
		{
			String methodKeyName = PageManager.instance( xFID ).getSiteName() + "." + proxy.getClass().getInterfaces()[0].getSimpleName() + "." + method.getName();
			
			PageManager.instance( xFID ).beforeExecution( methodKeyName );
				
			long startTime = System.currentTimeMillis();
			Object returnValue = methodImplemenation.invoke( currentService, args );
			long runLength = System.currentTimeMillis() - startTime;
			

			PageManager.instance( xFID ).afterExecution( methodKeyName, runLength );
			
			return returnValue;
		}
		
		return null;
	}
	
    /**
     * Checks if is correct method.
     *
     * @param compareMethod the compare method
     * @param methodName the method name
     * @param parameterArray the parameter array
     * @return true, if is correct method
     */
    private boolean isCorrectMethod( Method compareMethod, String methodName, Object[] parameterArray )
    {
        if ( !methodName.equals( compareMethod.getName() ) )
            return false;

        if ( (parameterArray == null || parameterArray.length == 0) && (compareMethod.getParameterTypes() == null || compareMethod.getParameterTypes().length == 0) )
            return true;

        if ( parameterArray == null || compareMethod.getParameterTypes() == null )
            return false;

        Class[] parameterTypes = compareMethod.getParameterTypes();

        if ( parameterTypes.length != parameterArray.length )
        {
            if ( log.isDebugEnabled() ) log.debug( "Paramter Count Mismatch " + parameterTypes.length + " - " + parameterArray.length );
            return false;
        }

        for(int i = 0; i < parameterArray.length; i++)
        {
            if ( log.isDebugEnabled() ) log.debug( parameterTypes[i] + " - " + parameterArray[i] );
            if ( !isInstance( parameterTypes[i], parameterArray[i] ) )
                return false;
        }

        return true;
    }
    
    /**
     * Checks if is instance.
     *
     * @param classType the class type
     * @param value the value
     * @return true, if is instance
     */
    private boolean isInstance( Class classType, Object value )
    {
        try
        {
            if ( classType.isPrimitive() )
            {
                if ( value == null )
                {
                    if ( log.isDebugEnabled() )
                    	log.debug( "Primative value null" );
                    return false;
                }
                else
                {
                    Field typeField = value.getClass().getField( TYPE );
                    return classType.isAssignableFrom( (Class) typeField.get( value ) );
                }
            }
            else
                return (value == null || classType.isInstance( value ));
        }
        catch( Exception e )
        {
        	log.error( "Error getting instance", e );
            return false;
        }
    }

}
