package com.xframium.page.data;

import java.util.List;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * Represents a single row in a page data table.
 */
public interface PageData
{
	
	/** The selector. */
	public static Pattern SELECTOR = Pattern.compile( "\\|(\\w+):([^\\|]+)\\|" );
	
	/** The values. */
	public static Pattern VALUES = Pattern.compile( "(?:\\[(\\w+)=[']([^']+)[']\\])" );
	
	/** The def. */
	public static String DEF = ".def";
	
	/** The tree marker. */
	public static String TREE_MARKER = "|";
	
	/**
	 * Gets the named field from this page data object.
	 *
	 * @param fieldName the field name
	 * @return The value to return in String format
	 */
	public String getData( String fieldName );
	
	/**
	 * Gets the object record type.
	 *
	 * @return the type
	 */
	public String getType();
	
	/**
	 * Gets the name of this object value.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Checks if the record is active.
	 *
	 * @return true, if is active
	 */

	public boolean isActive();
	
	/**
	 * Gets the page data.
	 *
	 * @param fieldName the field name
	 * @return the page data
	 */
	public List<PageData> getPageData( String fieldName );
	
	/**
	 * Contains children.
	 *
	 * @return true, if successful
	 */
	public boolean containsChildren();
	
	/**
	 * Populate tree structure.
	 */
	public void populateTreeStructure();
}
