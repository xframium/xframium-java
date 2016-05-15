package com.xframium.page.keyWord;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyWordParameter.
 */
public class KeyWordParameter
{
	
	/**
	 * The Enum ParameterType.
	 */
	public enum ParameterType
	{
		
		/** The context. */
		CONTEXT, 
 /** The property. */
 PROPERTY, 
 /** The static. */
 STATIC, 
 /** The data. */
 DATA,
 
 /** The content. */
 CONTENT;
	}
	
	/** The type. */
	private ParameterType type;
	
	/** The value. */
	private String value;

	
	/**
	 * Instantiates a new key word parameter.
	 *
	 * @param type the type
	 * @param value the value
	 */
	public KeyWordParameter( ParameterType type, String value )
	{
		super();
		this.type = type;
		this.value = value;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public ParameterType getType()
	{
		return type;
	}
}
