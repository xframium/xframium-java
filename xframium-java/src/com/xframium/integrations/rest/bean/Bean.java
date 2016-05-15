package com.xframium.integrations.rest.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


// TODO: Auto-generated Javadoc
/**
 * The Interface Bean.
 */
public interface Bean
{
	
	/**
	 * The Interface FieldDescriptor.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FieldDescriptor
	{
		
		/**
		 * Field path.
		 *
		 * @return the string
		 */
		String fieldPath() default "";
		
		/**
		 * Text content.
		 *
		 * @return true, if successful
		 */
		boolean textContent() default false;
	}
	
	/**
	 * The Interface FieldCollection.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FieldCollection
	{
		
		/**
		 * Field path.
		 *
		 * @return the string
		 */
		String fieldPath();
		
		/**
		 * Field element.
		 *
		 * @return the class
		 */
		Class fieldElement();
	}
	
	/**
	 * The Interface BeanDescriptor.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface BeanDescriptor
	{
		
		/**
		 * Bean name.
		 *
		 * @return the string
		 */
		String beanName() default "";
	}
	
}
