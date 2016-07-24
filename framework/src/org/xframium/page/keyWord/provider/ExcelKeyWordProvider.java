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
package org.xframium.page.keyWord.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.application.ApplicationRegistry;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.matrixExtension.MatrixTest;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLKeyWordProvider.
 */
public class ExcelKeyWordProvider implements KeyWordProvider
{
	

	/** The log. */
	private Log log = LogFactory.getLog( KeyWordTest.class );
	
	/** The file name. */
	private File fileName;
	private File rootFolder;
	
	/** The resource name. */
	private String resourceName;


	/**
	 * Instantiates a new XML key word provider.
	 *
	 * @param fileName
	 *            the file name
	 */
	public ExcelKeyWordProvider( File fileName )
	{
		this.fileName = fileName;
		rootFolder = fileName.getParentFile();
	}

	/**
	 * Instantiates a new XML key word provider.
	 *
	 * @param resourceName
	 *            the resource name
	 */
	public ExcelKeyWordProvider( String resourceName )
	{
		this.resourceName = resourceName;
		this.fileName = new File(".");
		rootFolder = fileName.getParentFile();
	}
	
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.perfectoMobile.page.keyWord.provider.KeyWordProvider#readData()
	 */
	public void readData()
	{
		if (fileName == null)
		{
			if (log.isInfoEnabled())
				log.info( "Reading from CLASSPATH as XMLElementProvider.elementFile" );
			
			readElements( getClass().getClassLoader().getResourceAsStream( resourceName ) );
		}
		else
		{
			try
			{
				if (log.isInfoEnabled())
					log.info( "Reading from FILE SYSTEM as [" + fileName + "]" );
				readElements( new FileInputStream( fileName ) );
			}
			catch (Exception e)
			{
				log.fatal( "Could not read from " + fileName, e );
				System.exit( -1 );
			}
		}
	}

	private void readElements( InputStream inputStream )
    {
        List<MatrixTest> testList = new ArrayList<MatrixTest>( 10 );
        XSSFWorkbook workbook = null;

        try
        {
            workbook = new XSSFWorkbook( inputStream );
            XSSFSheet sheet = workbook.getSheet( "Tests" );

            //
            // Extract the Tests
            //
            for (int i = 1; i <= sheet.getLastRowNum(); i++)
            {
                XSSFRow currentRow = sheet.getRow( i );
                
                List<String> testDefinition = new ArrayList<String>( 10 );
                for ( int j=0; j<currentRow.getLastCellNum(); j++ )
                    testDefinition.add( getCellValue( currentRow.getCell( j ) ) );
                
                MatrixTest currentTest = new MatrixTest( testDefinition.toArray( new String[ 0 ] ) );
                if ( currentTest.getName() != null && !currentTest.getName().isEmpty() && currentTest.isActive() )
                    testList.add( currentTest );
            }
            
            for ( MatrixTest currentTest : testList )
            {
                List<String[]> stepList = new ArrayList<String[]>( 20 );
                sheet = workbook.getSheet( currentTest.getName() );
                if ( sheet != null )
                {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++)
                    {
                        XSSFRow currentRow = sheet.getRow( i );

                        
                        List<String> stepDefinition = new ArrayList<String>( 10 );
                        for ( int j=0; j<currentRow.getLastCellNum(); j++ )
                            stepDefinition.add( getCellValue( currentRow.getCell( j ) ) );
                        
                        stepList.add( stepDefinition.toArray( new String[ 0 ] ) );
                    }
                }
                
                currentTest.setStepDefinition( (String[][]) stepList.toArray( new String[ 0 ][ 0 ] ) );
            }
            
            for ( MatrixTest currentTest : testList )
            {
                if ( currentTest.getType().equals( "function" ) )
                    KeyWordDriver.instance().addFunction( currentTest.createTest() );
                else
                    KeyWordDriver.instance().addTest( currentTest.createTest() );
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
