package com.xframium.page.data;

import com.xframium.page.data.provider.PageDataProvider;

// TODO: Auto-generated Javadoc
/**
 * The singleton containing all loaded page data records.
 */
public class PageDataManager
{
	
	/** The singleton. */
	private static PageDataManager singleton = new PageDataManager();

    /**
     * Instance.
     *
     * @return the page data manager
     */
    public static PageDataManager instance()
    {
        return singleton;
    }

    /**
     * Instantiates a new page data manager.
     */
    private PageDataManager() {}
    
    /** The data provider. */
    private PageDataProvider dataProvider;
    
    /**
     * Sets the page data provider.
     *
     * @param dataProvider the new page data provider
     */
    public void setPageDataProvider( PageDataProvider dataProvider )
    {
    	this.dataProvider = dataProvider;
    	dataProvider.readPageData();
    }
    
    /**
     * Gets the records.
     *
     * @param recordType the record type
     * @return the records
     */
    public PageData[] getRecords( String recordType )
    {
    	return dataProvider.getRecords( recordType );
    }
    
    /**
     * Gets the record i ds.
     *
     * @param recordType the record type
     * @return the record i ds
     */
    public String[] getRecordIDs( String recordType )
    {
    	PageData[] records = getRecords( recordType );
    	String[] ids = new String[ records.length ];
    	
    	for ( int i=0; i<records.length; i++ )
    		ids[ i ] = records[ i ].getName();
    	
    	return ids;
    }
    
    /**
     * Gets the page data.
     *
     * @param recordType the record type
     * @return the page data
     */
    public PageData getPageData( String recordType )
    {
    	return dataProvider.getRecord( recordType );
    }
    
    /**
     * Gets the page data.
     *
     * @param recordType the record type
     * @param recordId the record id
     * @return the page data
     */
    public PageData getPageData( String recordType, String recordId )
    {
    	return dataProvider.getRecord( recordType, recordId );
    }
    
    /**
     * Put page data.
     *
     * @param pageData the page data
     */
    public void putPageData( PageData pageData )
    {
    	dataProvider.putRecord( pageData );
    }
}
