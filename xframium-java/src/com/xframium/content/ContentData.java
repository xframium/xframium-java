package com.xframium.content;

// TODO: Auto-generated Javadoc
/**
 * Represents a single row in a page data table.
 */
public interface ContentData
{
	
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue();
	
	/**
	 * Gets the value from the underlying implementation using a zero based index.  
	 *
	 * @param matrixIndex the matrix index
	 * @return the value or null of the index is out of boundds
	 */
	public String getValue( int matrixIndex );
	
	/**
	 * Gets the value.
	 *
	 * @param matrixName the matrix name
	 * @return the value or null if the key does not exist
	 */
	public String getValue( String matrixName );
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();
}
