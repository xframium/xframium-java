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
package org.xframium.page.data.provider;

import org.xframium.page.data.PageData;

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
	
	public void addRecordType( String typeName, boolean lockRecords );
	public void addRecord( PageData pageData );
}
