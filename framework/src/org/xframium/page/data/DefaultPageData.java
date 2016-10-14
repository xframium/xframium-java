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
package org.xframium.page.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.xframium.page.data.provider.PageDataProvider;

// TODO: Auto-generated Javadoc
/**
 * The default page data instance.
 */
public class DefaultPageData implements PageData
{
	
	/** The record map. */
	public Map<String, Object> recordMap = new HashMap<String, Object>( 10 );

	/** The type name. */
	private String typeName;
	
	/** The record name. */
	private String recordName;
	
	/** The active. */
	private boolean active;
	
	/** The contains children. */
	private boolean containsChildren;

	/**
	 * Instantiates a new default page data.
	 *
	 * @param typeName the type name
	 * @param recordName the record name
	 * @param active the active
	 */
	public DefaultPageData( String typeName, String recordName, boolean active )
	{
		this.typeName = typeName;
		this.recordName = recordName;
		this.active = active;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.data.PageData#getName()
	 */
	public String getName()
	{
		return recordName;
	}

	@Override
	public String[] getFieldNames()
	{
	    List<String> fieldNames = new ArrayList<String>( 20 );
	    fieldNames.add( "typeName" );
	    fieldNames.add( "recordName" );
	    fieldNames.add( "active" );
	    for ( String name : recordMap.keySet() )
	        fieldNames.add( name );
	    
	    return fieldNames.toArray( new String[ 0 ]);
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.data.PageData#getData(java.lang.String)
	 */
	@Override
	public String getData( String fieldName )
	{
		return recordMap.get( fieldName ) + "";
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.data.PageData#getType()
	 */
	@Override
	public String getType()
	{
		return typeName;
	}
	
	@Override
	public Object get( String fieldName )
	{
	    if ( fieldName.contains( "." ) )
	    {
	        String[] fieldArray = fieldName.split( "\\." );
	        
	        List<PageData> dataList = (List<PageData>) recordMap.get( fieldArray[ 0 ] );
	        
	        for ( PageData p : dataList )
	        {
	            if ( p.getName().equals( fieldArray[ 1 ] ) )
	            {
	                String newName = fieldName.substring( fieldName.indexOf( "." ) + 1 );
	                newName = newName.substring( newName.indexOf( "." ) + 1 );
	                
	                if ( newName.trim().isEmpty() )
	                    return p;
	                else
	                    return p.get( newName );
	            }
	        }
	        
	        return null;
	    }
	    
	    List<PageData> dataList = getPageData( fieldName );
	    if ( dataList.size() > 0 )
	        return dataList.get( 0 );
	    else
	        return null;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.data.PageData#populateTreeStructure()
	 */
	public void populateTreeStructure( PageDataProvider dataProvider )
	{
	    if ( containsChildren )
	    {
	        for ( String keyName : recordMap.keySet() )
	        {
	            if ( keyName.endsWith( DEF ) )
	            {
	                //
	                // Set the actual page data name
	                //
	                String useKey = keyName.substring( 0, keyName.length() - DEF.length() );
	                
	                
	                String lookupValue = (String) recordMap.get( keyName );
	                
	                Matcher selectorMatcher = SELECTOR.matcher( lookupValue );
	                boolean matchFound = false;
	                while( selectorMatcher.find() )
	                {
	                    matchFound = true;
	                    String recordType = selectorMatcher.group( 1 );
	                    
	                    Map<String,String> criteriaMap = new HashMap<String,String>( 5 );
	                    
	                    Matcher valueMatcher = VALUES.matcher( selectorMatcher.group( 2 ) );
	                    while( valueMatcher.find() )
	                    {
	                        criteriaMap.put( valueMatcher.group( 1 ), valueMatcher.group( 2 ) );
	                    }
	                    
	                    PageData[] dataArray = dataProvider.getRecords( recordType );
	                    
	                    for ( PageData pageData : dataArray )
	                    {
	                        boolean addData = true;
	                        for ( String criteriaField : criteriaMap.keySet() )
	                        {
	                            String compareTo = pageData.getData( criteriaField );
	                            if ( compareTo == null || !compareTo.equals( criteriaMap.get( criteriaField ) ) )
	                            {
	                                addData = false;
	                                break;
	                            }
	                        }
	                        
	                        if ( addData )
	                            addPageData( useKey, pageData );
	                    }
	                }
	                
	                if ( !matchFound )
	                {
	                    PageData[] dataArray = dataProvider.getRecords( lookupValue.replace( "|", "" ).trim() );
	                    if ( dataArray != null )
	                    {
	                        for ( PageData pageData : dataArray )
	                            addPageData( useKey, pageData );
	                    }
	                            
	                }
	            }
	        }
	    }
	}

	/**
	 * Adds the value.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 */
	public void addValue( String fieldName, String value )
	{
	    if ( fieldName == null || value == null )
	        return;
	    if ( fieldName.equals( "name" ) )
	        recordName = value;
	    else if ( fieldName.equals( "active" ) )
	        active = Boolean.parseBoolean( value );
		recordMap.put( fieldName, value );
	}
	
	/**
	 * Adds the page data.
	 *
	 * @param fieldName the field name
	 * @param pageData the page data
	 */
	public void addPageData( String fieldName, PageData pageData )
	{
	    List<PageData> dataList = (List<PageData>) recordMap.get( fieldName );
	    
	    if ( dataList == null )
	    {
	        dataList = new ArrayList<PageData>( 10 );
	        recordMap.put( fieldName, dataList );
	    }
	    
	    dataList.add( pageData );
	}
	
	/**
	 * Adds the page data.
	 *
	 * @param fieldName the field name
	 */
	public void addPageData( String fieldName )
    {
        List<PageData> dataList = (List<PageData>) recordMap.get( fieldName );
        
        if ( dataList == null )
        {
            dataList = new ArrayList<PageData>( 10 );
            recordMap.put( fieldName, dataList );
        }
    }
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.data.PageData#isActive()
	 */
	@Override
	public boolean isActive()
	{
		return active;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.data.PageData#containsChildren()
	 */
	public boolean containsChildren()
	{
	    return containsChildren;
	}
	
	/**
	 * Sets the contains children.
	 *
	 * @param containsChildren the new contains children
	 */
	public void setContainsChildren( boolean containsChildren )
	{
	    this.containsChildren = containsChildren;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.data.PageData#getPageData(java.lang.String)
	 */
	public List<PageData> getPageData( String fieldName )
	{
	    return (List<PageData>) recordMap.get( fieldName );
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
	    StringBuilder stringBuilder = new StringBuilder();
	    stringBuilder.append( "Name: " ).append( recordName ).append( "\r\n" );
	    stringBuilder.append( "Type: " ).append( typeName ).append( "\r\n" );
	    for ( String keyName : recordMap.keySet() )
	    {
	        stringBuilder.append( "[" ).append( keyName ).append( "='" ).append( recordMap.get( keyName ) ).append( "']\r\n" );
	    }
	    
	    return stringBuilder.toString();
	}

}
