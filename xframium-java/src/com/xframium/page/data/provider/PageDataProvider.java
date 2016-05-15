package com.xframium.page.data.provider;

import com.xframium.page.data.PageData;

// TODO: Auto-generated Javadoc
/**
 * The Interface PageDataProvider.
 */
public interface PageDataProvider
{
	
	/**
	 * Gets the record.
	 *
	 * @param recordType the record type
	 * @return the record
	 */
	public PageData getRecord( String recordType );
	
	/**
	 * Put record.
	 *
	 * @param pageData the page data
	 */
	public void putRecord( PageData pageData );
	
	/**
	 * Read page data.
	 */
	public void readPageData();
	
	/**
	 * Gets the records.
	 *
	 * @param recordType the record type
	 * @return the records
	 */
	public PageData[] getRecords( String recordType );
	
	/**
	 * Gets the record.
	 *
	 * @param recordType the record type
	 * @param recordId the record id
	 * @return the record
	 */
	public PageData getRecord( String recordType, String recordId );
}
