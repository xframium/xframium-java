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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xframium.integrations.rest.bean.Bean;
import org.xframium.integrations.rest.bean.Bean.BeanDescriptor;
import org.xframium.integrations.rest.bean.Bean.FieldCollection;
import org.xframium.integrations.rest.bean.Bean.FieldDescriptor;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating XMLBean objects.
 */
public class XMLBeanFactory extends AbstractBeanFactory
{

    /** The Constant SLASH. */
    private static final String SLASH = "/";

    /** The Constant DOT. */
    private static final String DOT = ".";

    /** The x path factory. */
    private XPathFactory xPathFactory = XPathFactory.newInstance();

    /** The transformer factory. */
    private TransformerFactory transformerFactory = TransformerFactory.newInstance();

    /*
     * (non-Javadoc)
     * 
     * @see com.morelandLabs.integrations.rest.bean.factory.AbstractBeanFactory#
     * _createBean(java.lang.Class, java.lang.String)
     */
    @Override
    protected Bean _createBean( Class returnType, String inputData ) throws Exception
    {
        ByteArrayInputStream inputStream = new ByteArrayInputStream( inputData.getBytes() );
        return createBean( returnType, inputStream );
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
        if ( log.isDebugEnabled() )
        {
            log.debug( "Parsing XML Document into " + returnType.getName() );
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int bytesRead;

            while ( (bytesRead = inputStream.read( buffer )) > 0 )
            {
                outputStream.write( buffer, 0, bytesRead );
            }

            log.debug( new String( outputStream.toByteArray() ) );

            useStream = new ByteArrayInputStream( outputStream.toByteArray() );
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware( true );
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        XPath xPath = xPathFactory.newXPath();
        Document xmlDocument = dBuilder.parse( useStream );

        String thisPath = ((BeanDescriptor) returnType.getAnnotation( BeanDescriptor.class )).beanName();
        if ( !thisPath.startsWith( SLASH ) )
            thisPath = SLASH + thisPath;

        Node xmlNode = (Node) xPath.evaluate( thisPath, xmlDocument, XPathConstants.NODE );

        Bean returnBean = populateBean( xmlNode, returnType, xPath );

        return returnBean;

    }

    /**
     * Populate bean.
     *
     * @param parentNode
     *            the parent node
     * @param returnType
     *            the return type
     * @param xPath
     *            the x path
     * @return the bean
     * @throws XPathExpressionException
     *             the x path expression exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    private Bean populateBean( Node parentNode, Class<Bean> returnType, XPath xPath ) throws XPathExpressionException, IllegalAccessException, InstantiationException, ClassNotFoundException
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

                    if ( fD.textContent() )
                    {
                        field.set( beanInstance, createValue( field, parentNode.getTextContent() ) );
                    }
                    else
                    {
                        String fieldPath = fD.fieldPath();
                        if ( fieldPath == null || fieldPath.isEmpty() )
                            fieldPath = field.getName();

                        String xPathExpression = DOT + SLASH + fieldPath;

                        if ( log.isDebugEnabled() )
                            log.debug( "Attempting to find field value using [" + xPathExpression + "]" );

                        Node xmlNode = (Node) xPath.evaluate( xPathExpression, parentNode, XPathConstants.NODE );

                        if ( xmlNode != null )
                        {
                            if ( Bean.class.isAssignableFrom( field.getType() ) )
                            {
                                field.set( beanInstance, populateBean( xmlNode, (Class<Bean>) field.getType(), xPath ) );
                            }
                            else
                            {
                                if ( log.isDebugEnabled() )
                                    log.debug( "Attempting to parse [" + xmlNode.getTextContent() + "] into a [" + field.getClass().getSimpleName() + "]" );
                                field.set( beanInstance, createValue( field, xmlNode.getTextContent() ) );
                            }
                        }
                    }
                }
                else if ( field.getAnnotation( Bean.FieldCollection.class ) != null )
                {
                    FieldCollection fC = field.getAnnotation( Bean.FieldCollection.class );

                    String fieldPath = fC.fieldPath();
                    if ( !fieldPath.isEmpty() && !fieldPath.startsWith( SLASH ) )
                        fieldPath = SLASH + fieldPath;

                    BeanDescriptor beanDescriptor = (BeanDescriptor) fC.fieldElement().getAnnotation( BeanDescriptor.class );

                    if ( beanDescriptor == null )
                        throw new IllegalArgumentException( "Bean Descriptor is missing from " + fC.fieldElement().getName() );

                    String xPathExpression = DOT + SLASH + fieldPath + SLASH + beanDescriptor.beanName();

                    if ( log.isDebugEnabled() )
                        log.debug( "Attempting to find field collection nodes using [" + xPathExpression + "]" );

                    NodeList nodeList = (NodeList) xPath.evaluate( xPathExpression, parentNode, XPathConstants.NODESET );

                    List useList = (List) field.get( beanInstance );

                    for ( int i = 0; i < nodeList.getLength(); i++ )
                    {
                        useList.add( populateBean( nodeList.item( i ), fC.fieldElement(), xPath ) );
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
