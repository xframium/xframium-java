package com.xframium.integrations.rest.bean.factory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import com.xframium.integrations.perfectoMobile.rest.bean.Execution;
import com.xframium.integrations.perfectoMobile.rest.services.Imaging.ImageFormat;
import com.xframium.integrations.perfectoMobile.rest.services.Imaging.MatchMode;
import com.xframium.integrations.perfectoMobile.rest.services.Imaging.Resolution;
import com.xframium.integrations.perfectoMobile.rest.services.Imaging.Screen;
import com.xframium.integrations.perfectoMobile.rest.services.Repositories.RepositoryType;
import com.xframium.integrations.rest.bean.Bean;

// TODO: Auto-generated Javadoc
/**
 * The Class BeanManager.
 */
public class BeanManager
{
	
	/** The singleton. */
	private static BeanManager singleton = new BeanManager();
	
	/**
	 * Instantiates a new bean manager.
	 */
	private BeanManager()
	{
		
	}
	
	/**
	 * Instance.
	 *
	 * @return the bean manager
	 */
	public static BeanManager instance()
	{
		return singleton;
	}
	
	/** The bean factory. */
	private BeanFactory beanFactory = new XMLBeanFactory();

	/**
	 * Gets the bean factory.
	 *
	 * @return the bean factory
	 */
	public BeanFactory getBeanFactory()
	{
		return beanFactory;
	}

	/**
	 * Sets the bean factory.
	 *
	 * @param beanFactory the new bean factory
	 */
	public void setBeanFactory( BeanFactory beanFactory )
	{
		this.beanFactory = beanFactory;
	}
	
	/**
	 * Creates the bean.
	 *
	 * @param returnType the return type
	 * @param inputData the input data
	 * @return the bean
	 * @throws Exception the exception
	 */
	public Bean createBean( Class returnType, String inputData ) throws Exception
	{
		return beanFactory.createBean( returnType, inputData );
	}
	
	/**
	 * Creates the bean.
	 *
	 * @param returnType the return type
	 * @param inputStream the input stream
	 * @return the bean
	 * @throws Exception the exception
	 */
	public Bean createBean( Class returnType, InputStream inputStream ) throws Exception
	{
		return beanFactory.createBean( returnType, inputStream );
	}
	
	
}
