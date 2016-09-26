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
package org.xframium.integrations.rest.bean.factory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xframium.integrations.rest.bean.Bean;
import org.xframium.integrations.rest.bean.Bean.BeanDescriptor;
import org.xframium.integrations.rest.bean.Bean.FieldCollection;
import org.xframium.integrations.rest.bean.Bean.FieldDescriptor;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating XMLBean objects.
 */
public class JSONBeanFactory extends AbstractBeanFactory
{

    /*
     * (non-Javadoc)
     * 
     * @see com.morelandLabs.integrations.rest.bean.factory.AbstractBeanFactory#
     * _createBean(java.lang.Class, java.lang.String)
     */
    @Override
    protected Bean _createBean( Class returnType, String inputData ) throws Exception
    {
        System.out.println( inputData );
        DocumentContext ctx = JsonPath.parse( inputData );
        
        return populateBean( ctx, returnType, "" );
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.morelandLabs.integrations.rest.bean.factory.AbstractBeanFactory#
     * _createBean(java.lang.Class, java.io.InputStream)
     */
    @Override
    protected Bean _createBean( Class returnType, InputStream inputStream ) throws Exception
    {
        
        InputStream useStream = inputStream;

        log.debug( "Parsing XML Document into " + returnType.getName() );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int bytesRead;

        while ( (bytesRead = inputStream.read( buffer )) > 0 )
        {
            outputStream.write( buffer, 0, bytesRead );
        }

        return _createBean( returnType, new String( outputStream.toByteArray() ) );

    }
    
    private Bean populateBean( DocumentContext ctx, Class<Bean> returnType, String jsonPath ) throws IllegalAccessException, InstantiationException, ClassNotFoundException
    {

        Field[] fieldArray = returnType.getDeclaredFields();
        if ( log.isDebugEnabled() )
            log.debug( "Analyzing " + fieldArray.length + " fields on " + returnType.getSimpleName() );

        Bean beanInstance = returnType.newInstance();
        for ( Field field : fieldArray )
        {
            field.setAccessible( true );

            try
            {

                if ( log.isDebugEnabled() )
                    log.debug( "Processing Field [" + field.getName() + "]" );

                if ( field.getAnnotation( Bean.FieldDescriptor.class ) != null )
                {
                    FieldDescriptor fD = field.getAnnotation( Bean.FieldDescriptor.class );

                    String fieldPath = fD.fieldPath();
                    if ( fieldPath == null || fieldPath.isEmpty() )
                        fieldPath = field.getName();

                    
                    
                    String jPathExpression = jsonPath;
                    if ( jPathExpression.trim().isEmpty() )
                        jPathExpression = "$.";
                    else
                        jPathExpression = jPathExpression + ".";
                    jPathExpression = jPathExpression + fieldPath;

                    System.out.println( jPathExpression );
                    
                    if ( log.isDebugEnabled() )
                        log.debug( "Attempting to find field value using [" + jPathExpression + "]" );

                    Object returnValue = ctx.read( jPathExpression );
                    
                    if ( returnValue != null )
                    {
                        if ( Bean.class.isAssignableFrom( field.getType() ) )
                        {
                            field.set( beanInstance, populateBean( ctx, (Class<Bean>) field.getType(), jPathExpression ) );
                        }
                        else
                        {
                            if ( log.isDebugEnabled() )
                                log.debug( "Attempting to parse [" + returnValue + "] into a [" + field.getClass().getSimpleName() + "]" );
                            field.set( beanInstance, createValue( field, returnValue + "" ) );
                        }
                    }
                }
                else if ( field.getAnnotation( Bean.FieldCollection.class ) != null )
                {
                    FieldCollection fC = field.getAnnotation( Bean.FieldCollection.class );

                    String fieldPath = fC.fieldPath();
                    if ( fieldPath == null || fieldPath.isEmpty() )
                        fieldPath = field.getName();

                    String jPathExpression = jsonPath;
                    if ( jPathExpression.trim().isEmpty() )
                        jPathExpression = "$.";
                    jPathExpression = jPathExpression + fieldPath;

                    System.out.println( jPathExpression );
                    if ( log.isDebugEnabled() )
                        log.debug( "Attempting to find field collection using [" + jPathExpression + "]" );

                    List returnValue = ctx.read( jPathExpression );

                    List useList = (List) field.get( beanInstance );

                    for ( int i = 0; i < returnValue.size(); i++ )
                    {
                        useList.add( populateBean( ctx, fC.fieldElement(), jPathExpression + "[" + i + "]" ) );
                    }
                }

            }
            catch ( Exception e )
            {
                log.warn( "Error populating bean object", e );
            }
        }

        return beanInstance;
    }

    /**
     * Creates a new XMLBean object.
     *
     * @param field
     *            the field
     * @param value
     *            the value
     * @return the object
     */
    private Object createValue( Field field, String value )
    {
        try
        {
            if ( field.getType().equals( String.class ) )
                return value;
            else if ( field.getType().equals( Integer.class ) )
                return Integer.parseInt( value );
            else if ( field.getType().equals( Double.class ) )
                return Double.parseDouble( value );
            else if ( field.getType().equals( Long.class ) )
                return Long.parseLong( value );
            else if ( field.getType().equals( Float.class ) )
                return Float.parseFloat( value );
            else if ( field.getType().equals( Short.class ) )
                return Short.parseShort( value );
            else if ( field.getType().equals( Boolean.class ) )
            {
                if ( value == null )
                    return false;
                else
                    return Boolean.parseBoolean( value );
            }
            else
            {
                log.warn( "Unknown Type" );
                return null;
            }
        }
        catch ( Exception e )
        {
            log.warn( "Error parsing " + value, e );
            return null;
        }

    }
}
