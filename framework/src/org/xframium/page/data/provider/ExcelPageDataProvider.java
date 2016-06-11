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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xframium.page.data.DefaultPageData;
import org.xframium.page.data.PageData;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLPageDataProvider.
 */
public class ExcelPageDataProvider extends AbstractPageDataProvider
{
	
	/** The file name. */
	private File[] fileName;
	
	/** The resource name. */
	private String[] resourceName;
	
	/** The tab names. */
	private String tabNames;

	/**
	 * Instantiates a new XML page data provider.
	 *
	 * @param fileName the file name
	 * @param tabNames the tab names
	 */
	public ExcelPageDataProvider( File fileName, String tabNames )
	{
		this.tabNames = tabNames;
		this.fileName = new File[] { fileName };
	}
	
	public ExcelPageDataProvider( File[] fileName, String tabNames )
    {
        this.tabNames = tabNames;
        this.fileName = fileName;
    }

	/**
	 * Instantiates a new XML page data provider.
	 *
	 * @param resourceName the resource name
	 * @param tabNames the tab names
	 */
	public ExcelPageDataProvider( String resourceName, String tabNames )
	{
		this.tabNames = tabNames;
		this.resourceName = resourceName.split( "," );
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.data.provider.AbstractPageDataProvider#readPageData()
	 */
	@Override
	public void readPageData()
	{
		if (fileName == null)
		{
			for ( String resource : resourceName )
			{
			    if (log.isInfoEnabled())
	                log.info( "Reading from CLASSPATH as " + resource );
			    readElements( getClass().getClassLoader().getResourceAsStream( resource ) );
			}
		}
		else
		{
			try
			{
				
				for ( File currentFile : fileName )
				{
				    if (log.isInfoEnabled())
	                    log.info( "Reading from FILE SYSTEM as [" + currentFile + "]" );
				    readElements( new FileInputStream( currentFile ) );
				}
			}
			catch (FileNotFoundException e)
			{
				log.fatal( "Could not read from " + fileName, e );
			}
		}
		
		populateTrees();
		
		

	}

	/**
	 * Gets the cell value.
	 *
	 * @param cell the cell
	 * @return the cell value
	 */
	private String getCellValue( XSSFCell cell )
	{
		if (cell != null)
		{
			switch (cell.getCellType())
			{
				case XSSFCell.CELL_TYPE_BLANK:
					return null;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					return String.valueOf( cell.getBooleanCellValue() );
				case XSSFCell.CELL_TYPE_NUMERIC:
					return String.valueOf( cell.getNumericCellValue() );
				case XSSFCell.CELL_TYPE_STRING:
					return cell.getRichStringCellValue().toString();
			}
		}
		return null;
	}
	
	
	/**
	 * Read elements.
	 *
	 * @param inputStream the input stream
	 */
	private void readElements( InputStream inputStream )
	{
		
		XSSFWorkbook workbook = null;

		try
		{
			workbook = new XSSFWorkbook( inputStream );
			
			String[] tabs = tabNames.split( "," );
			
			for ( String tabName : tabs )
			{
			    XSSFSheet sheet = workbook.getSheet( tabName );
			    
			    if ( sheet == null )
			        continue;
			    
				addRecordType( tabName, false );
				
				XSSFRow firstRow = sheet.getRow( 0 );
				
				for (int i = 1; i <= sheet.getLastRowNum(); i++)
				{
					XSSFRow currentRow = sheet.getRow( i );
	
					if ( getCellValue( currentRow.getCell( 0 ) ) == null || getCellValue( currentRow.getCell( 0 ) ).isEmpty() )
						break;
					
					DefaultPageData currentRecord = new DefaultPageData( tabName, tabName + "-" + i, true );
					for ( int x=0; x<firstRow.getLastCellNum(); x++ )
					{
					    
					    String currentName = getCellValue( firstRow.getCell( x ) );
		                String currentValue = getCellValue( currentRow.getCell( x ) );
		                
		                if ( currentValue == null )
		                    currentValue = "";
		                
		                if ( currentValue.startsWith( PageData.TREE_MARKER ) && currentValue.endsWith( PageData.TREE_MARKER ) )
		                {
		                    //
		                    // This is a reference to another page data table
		                    //
		                    currentRecord.addPageData( currentName );
		                    currentRecord.addValue( currentName + PageData.DEF, currentValue );
		                    currentRecord.setContainsChildren( true );
		                }
		                else
		                    currentRecord.addValue( currentName, currentValue );
					}
					
					addRecord( currentRecord );
				}
			}

			
		}
		catch (Exception e)
		{
			log.fatal( "Error reading Excel Element File", e );
		}
		finally
		{
			try
			{
				workbook.close();
			}
			catch (Exception e)
			{
			}
		}
	}
}
