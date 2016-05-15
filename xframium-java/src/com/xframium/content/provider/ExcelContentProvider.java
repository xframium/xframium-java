package com.xframium.content.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.xframium.content.ContentData;
import com.xframium.content.ContentManager;
import com.xframium.content.DefaultContentData;

// TODO: Auto-generated Javadoc
/**
 * The Class CSVElementProvider.
 */
public class ExcelContentProvider extends AbstractContentProvider
{	
	
	/** The file name. */
	private File fileName;
	
	/** The resource name. */
	private String resourceName;
	
	/** The key column. */
	private int keyColumn;
	
	/** The lookup columns. */
	private int[] lookupColumns;
	
	/** The tab name. */
	private String tabName;
	
	
	/**
	 * Instantiates a new Excel element provider.
	 *
	 * @param fileName the file name
	 * @param tabName the tab name
	 * @param keyColumn the key column
	 * @param lookupColumns the lookup columns
	 */
	public ExcelContentProvider( File fileName, String tabName, int keyColumn, int[] lookupColumns )
	{
		this.fileName = fileName;
		this.keyColumn = keyColumn;
		this.lookupColumns = lookupColumns;
		this.tabName = tabName;
	}
	
	/**
	 * Instantiates a new CSV element provider.
	 *
	 * @param resourceName the resource name
	 * @param tabName the tab name
	 * @param keyColumn the key column
	 * @param lookupColumns the lookup columns
	 */
	public ExcelContentProvider( String resourceName, String tabName, int keyColumn, int[] lookupColumns )
	{
		this.resourceName = resourceName;
		this.keyColumn = keyColumn;
		this.lookupColumns = lookupColumns;
		this.tabName = tabName;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.content.provider.ContentProvider#readContent()
	 */
	public void readContent()
	{
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

				String keyName = getCellValue( currentRow.getCell( keyColumn ) );
				
				String[] valueList = new String[ lookupColumns.length ];
				
				for ( int x=0; x<lookupColumns.length; x++ )
				{
					valueList[ x ] = getCellValue( currentRow.getCell( lookupColumns[ x ] ) );
				}
				
				ContentData contentData = new DefaultContentData( keyName, valueList );
				
				ContentManager.instance().addContentData( contentData );	
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
