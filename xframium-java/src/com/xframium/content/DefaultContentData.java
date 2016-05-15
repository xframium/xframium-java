package com.xframium.content;

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
	
	/**
	 * Instantiates a new default content data.
	 *
	 * @param name the name
	 * @param matrixData the matrix data
	 */
	public DefaultContentData( String name, String[] matrixData )
	{
		this.matrixData = matrixData;
		this.name = name;
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
		Integer matrixPosition = ContentManager.instance().getMatrixPosition( matrixName );
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
