package com.xframium.integrations.rest.bean.factory;

import java.io.InputStream;
import com.xframium.integrations.rest.bean.Bean;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Bean objects.
 */
public interface BeanFactory
{
	
	/**
	 * Creates a new Bean object.
	 *
	 * @param returnType the return type
	 * @param inputData the input data
	 * @return the bean
	 * @throws Exception the exception
	 */
	public Bean createBean( Class returnType, String inputData ) throws Exception;
	
	/**
	 * Creates a new Bean object.
	 *
	 * @param returnType the return type
	 * @param inputStream the input stream
	 * @return the bean
	 * @throws Exception the exception
	 */
	public Bean createBean( Class returnType, InputStream inputStream ) throws Exception;
}
