package com.xframium.integrations.rest.bean.factory;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xframium.integrations.rest.bean.Bean;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating AbstractBean objects.
 */
public abstract class AbstractBeanFactory implements BeanFactory
{
	
	/** The log. */
	protected Log log = LogFactory.getLog( BeanFactory.class );
	
	/**
	 * _create bean.
	 *
	 * @param returnType the return type
	 * @param inputData the input data
	 * @return the bean
	 * @throws Exception the exception
	 */
	protected abstract Bean _createBean( Class returnType, String inputData ) throws Exception;
	
	/**
	 * _create bean.
	 *
	 * @param returnType the return type
	 * @param inputStream the input stream
	 * @return the bean
	 * @throws Exception the exception
	 */
	protected abstract Bean _createBean( Class returnType, InputStream inputStream ) throws Exception;
	
	/* (non-Javadoc)
	 * @see com.morelandLabs.integrations.rest.bean.factory.BeanFactory#createBean(java.lang.Class, java.lang.String)
	 */
	public Bean createBean( Class returnType, String inputData ) throws Exception
	{
		return _createBean( returnType, inputData );
	}

	/* (non-Javadoc)
	 * @see com.morelandLabs.integrations.rest.bean.factory.BeanFactory#createBean(java.lang.Class, java.io.InputStream)
	 */
	public Bean createBean( Class returnType, InputStream inputStream ) throws Exception
	{
		return _createBean( returnType, inputStream );
	}

}
