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
package com.xframium.page.keyWord;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyWordToken.
 */
public class KeyWordToken
{

	/**
	 * The Enum ParameterType.
	 */
	public enum TokenType
	{

		/** The context. */
		CONTEXT,
		/** The property. */
		PROPERTY,
		/** The static. */
		STATIC,
		/** The data. */
		
		CONTENT, 
		
		/** The data. */
		DATA;
	}

	/** The type. */
	private TokenType type;
	
	/** The value. */
	private String value;
	
	/** The name. */
	private String name;

	/**
	 * Instantiates a new key word parameter.
	 *
	 * @param type            the type
	 * @param value            the value
	 * @param name the name
	 */
	public KeyWordToken( TokenType type, String value, String name )
	{
		super();
		this.type = type;
		this.value = value;
		this.name = name;
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
	public TokenType getType()
	{
		return type;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
}
