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
package org.xframium.content;

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultContentData.
 */
public class DefaultContentData implements ContentData
{
	
	/** The name. */
	private String name;
	
	/** The matrix data. */
	private String[] matrixData;
	private ContentManager contentManager;
	
	/**
	 * Instantiates a new default content data.
	 *
	 * @param name the name
	 * @param matrixData the matrix data
	 */
	public DefaultContentData( String name, String[] matrixData, ContentManager contentManager )
	{
		this.matrixData = matrixData;
		this.name = name;
		this.contentManager = contentManager;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.content.ContentData#getValue()
	 */
	@Override
	public String getValue()
	{
		if ( matrixData != null && matrixData.length > 0 )
			return matrixData[ 0 ];
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.content.ContentData#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.content.ContentData#getValue(java.lang.String)
	 */
	@Override
	public String getValue( String matrixName )
	{
		Integer matrixPosition = contentManager.getMatrixPosition( matrixName );
		if ( matrixPosition != null && matrixData != null && matrixData.length > matrixPosition )
			return matrixData[ matrixPosition ];
		else
			return null;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.content.ContentData#getValue(int)
	 */
	@Override
	public String getValue( int matrixIndex )
	{
		if ( matrixData != null && matrixIndex < matrixData.length )
			return matrixData[ matrixIndex ];
		else
			return null;
	}

}
