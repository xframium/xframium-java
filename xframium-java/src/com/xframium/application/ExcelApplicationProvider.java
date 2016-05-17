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
package com.xframium.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// TODO: Auto-generated Javadoc
/**
 * The Class CSVApplicationProvider.
 */
public class ExcelApplicationProvider extends AbstractApplicationProvider
{
	
	/** The file name. */
	private File fileName;
	
	/** The resource name. */
	private String resourceName;
	
	/** The tab name. */
	private String tabName;
	
	/**
	 * Instantiates a new CSV application provider.
	 *
	 * @param fileName the file name
	 * @param tabName the tab name
	 */
	public ExcelApplicationProvider( File fileName, String tabName )
	{
		this.fileName = fileName;
		this.tabName = tabName;
		readData();
	}
	
	/**
	 * Instantiates a new CSV application provider.
	 *
	 * @param resourceName the resource name
	 * @param tabName the tab name
	 */
	public ExcelApplicationProvider( String resourceName, String tabName )
	{
		this.resourceName = resourceName;
		this.tabName = tabName;
		readData();
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.application.ApplicationProvider#readData()
	 */
	public void readData()
	{
	    ApplicationRegistry.instance().clear();
		if ( fileName == null )
		{
			if ( log.isInfoEnabled() )
				log.info( "Reading from CLASSPATH as " + resourceName );
			readElements( getClass().getClassLoader().getResourceAsStream( resourceName ) );
		}
		else
		{
			try
			{
				if ( log.isInfoEnabled() )
					log.info( "Reading from FILE SYSTEM as [" + fileName + "]" );
				readElements( new FileInputStream( fileName ) );
			}
			catch( FileNotFoundException e )
			{
				log.fatal( "Could not read from " + fileName, e );
			}
		}
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
			XSSFSheet sheet = workbook.getSheet( tabName );

			for (int i = 1; i <= sheet.getLastRowNum(); i++)
			{
				XSSFRow currentRow = sheet.getRow( i );

				if ( getCellValue( currentRow.getCell( 0 ) ) == null || getCellValue( currentRow.getCell( 0 ) ).isEmpty() )
					break;
				
				ApplicationRegistry.instance().addApplicationDescriptor( new ApplicationDescriptor( getCellValue( currentRow.getCell( 0 ) ), getCellValue( currentRow.getCell( 4 ) ), getCellValue( currentRow.getCell( 1 ) ), getCellValue( currentRow.getCell( 2 ) ), getCellValue( currentRow.getCell( 3 ) ), getCellValue( currentRow.getCell( 5 ) ), getCellValue( currentRow.getCell( 6 ) ), new HashMap<String,Object>( 0 ) ) );
				
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
