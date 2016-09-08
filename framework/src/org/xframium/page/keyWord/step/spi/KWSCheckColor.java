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
package org.xframium.page.keyWord.step.spi;

import java.awt.image.BufferedImage;
import java.util.Map;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.xframium.artifact.ArtifactManager;
import org.xframium.artifact.ArtifactType;
import org.xframium.integrations.perfectoMobile.rest.services.Imaging.Resolution;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.wcag.WCAGRecord;
import org.xframium.wcag.WCAGRecord.WCAGType;

// TODO: Auto-generated Javadoc
/**
 * <b>Keyword(s):</b> <code>VERIFY_COLOR</code><br>
 * The verify color keyword allow you to select a point from a named element, extract the color and compare it to a know color.  
 * If the deviation falls outside of a predefined threshold then the step fails.  The determine the deviation, the color is broken 
 * down into 3 bands: red, green and blue.  Each band is compare as a percentage difference against 255.  The 3 differences are then
 * average out and compared to the deviation value specified.  Note that PageManager can be configured to dump these images to disk 
 * for analysis purposes.<br><br>
 * <b>Parameters:</b> Parameters can be supplied in as either a a set of 2 parameter or a set of 4.  The set of 2 parameters is usually used 
 * in conjunction with the context attribute to store the color for later use as it does no comparison<br>
 * <i>Extraction Only</i><br>
 * <ul>
 * <li>Resolution: high, med or low.  The specifies the screenshot resolution that will be used.</li>
 * <li>Reference Point: The specifies the point on the extracted element that you want to have the color extracted from.  This point 
 * is absolute to the image starting from 1,1</li>
 * </ul>
 * <i>Extraction and Comparison</i><br>
 * <ul>
 * <li>Resolution: high, med or low.  The specifies the screenshot resolution that will be used.</li>
 * <li>Reference Point: The specifies the point on the extracted element that you want to have the color extracted from.  This point
 * is absolute to the image starting from 1,1</li>
 * <li>Color: This specifies the color to compare to.  This can be specified as an HTML color (#FFEE44) and integer represented all 3 bands or
 * a comma separated list specifying red, green and blue</li> 
 * <li>Percent Deviation: 0 for no deviation and 100 for maximum deviation.  If the calculated deviation is great than this value then the step fails</li>

 * </ul>
 * <br><b>Example(s): </b><ul>
 * <li> This example will find the LOGO element and store the color at the 3,3 position in a context variable named useColor<br>
 * {@literal <step name="LOGO" type="VERIFY_COLOR" page="TEST_PAGE" context="useColor"> }<br>
 * &nbsp;&nbsp;&nbsp;{@literal  <parameter type="static" value="high" /> }<br>
 * &nbsp;&nbsp;&nbsp;{@literal  <parameter type="static" value="3,3" /> }<br>
 * {@literal </step> }
 * </li>
 *  <li> This example will find the LOGO element and analyze the color at the 3,3 position.  If the color is 5% different from white then it will fail<br>
 * {@literal <step name="LOGO" type="VERIFY_COLOR" page="TEST_PAGE" > }<br>
 * &nbsp;&nbsp;&nbsp;{@literal  <parameter type="static" value="high" /> }<br>
 * &nbsp;&nbsp;&nbsp;{@literal  <parameter type="static" value="3,3" /> }<br>
 * &nbsp;&nbsp;&nbsp;{@literal  <parameter type="static" value="#FFFFFF" /> }<br>
 * &nbsp;&nbsp;&nbsp;{@literal  <parameter type="static" value="5" /> }<br>
 * {@literal </step> }
 * </li>
 * </ul>
 */
public class KWSCheckColor extends AbstractKeyWordStep
{
    public KWSCheckColor()
    {
        kwName = "Color Verification";
        kwDescription = "Allows the script to validate the color at a point in the named element";
        kwHelp = "https://www.xframium.org/keyword.html#kw-checkcolor";
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
		if ( pageObject == null )
			throw new IllegalStateException( "Page Object was not defined" );

		if (getParameterList().size() < 2 )
			throw new IllegalArgumentException( "Verify Color must have 3 parameters - Resolution, location(x, y) and color" );
		
		long fileKey = System.currentTimeMillis();
		Resolution resolution = Resolution.valueOf( ( getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "" ).toLowerCase() );
		Point location = createPoint( getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "" );
		int percentDeviation = 0;
		String colorCode = null;
		
		if ( getParameterList().size() >= 3 )
		{
			if ( log.isInfoEnabled() )
				log.info( "Extracted information for color comparison" );
			colorCode = getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) + "";
			percentDeviation = Integer.parseInt( getParameterValue( getParameterList().get( 3 ), contextMap, dataMap ) + "" );
		}
		
		BufferedImage elementValue = (BufferedImage)getElement( pageObject, contextMap, webDriver, dataMap ).getImage( resolution );
		String imagePath = null;
		if ( elementValue != null )
			imagePath = PageManager.instance().writeImage( elementValue, fileKey + "-" + getName() + ".png" );
		
		
		int elementColor = elementValue.getRGB( location.getX(), location.getY() );
		
		if ( getContext() != null )
		{
			if ( log.isInfoEnabled() )
				log.info( "Setting Context Data to [" + elementColor + "] for [" + getContext() + "]" );
			contextMap.put( getContext(), elementColor + "" );
		}
		
		if ( colorCode != null )
		{
			int[] expectedColors = extractColors( colorCode );
			int[] extractColors = new int[] { ( (elementColor & 0x00ff0000) >> 16 ), ( (elementColor & 0x0000ff00) >> 8 ), ( (elementColor & 0x000000ff) )} ;
			
			int redChange = compareColor( expectedColors[0], extractColors[0] );
			int greenChange = compareColor( expectedColors[1], extractColors[1] );
			int blueChange = compareColor( expectedColors[2], extractColors[2] );
			
			if ( ( ( redChange + greenChange + blueChange ) / 3 ) > percentDeviation )
			{
			    
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append( "The COLOR value of [#" + Integer.toHexString( elementColor ) +  "] for [" + getName() + "] was off by " + ( ( redChange + greenChange + blueChange ) / 3 ) + "% - Here is the break down - " );
				if ( redChange > 0 )
					stringBuilder.append( "The RED channel was off by " + redChange + "% " );
				if ( greenChange > 0 )
					stringBuilder.append( "The GREEN channel was off by " + greenChange + "% " );
				if ( blueChange > 0 )
					stringBuilder.append( "The BLUE channel was off by " + blueChange + "% " );
				
				ArtifactManager.instance().notifyArtifactListeners( ArtifactType.WCAG_REPORT, new WCAGRecord( getPageName(), getName(), WCAGType.ColorVerification, System.currentTimeMillis(), System.currentTimeMillis() - fileKey, false, imagePath, colorCode, Integer.toHexString( elementColor ), stringBuilder.toString() ) );
				
				throw new IllegalArgumentException( stringBuilder.toString() );
			}
			else
			    ArtifactManager.instance().notifyArtifactListeners( ArtifactType.WCAG_REPORT, new WCAGRecord( getPageName(), getName(), WCAGType.ColorVerification, System.currentTimeMillis(), System.currentTimeMillis() - fileKey, true, imagePath, colorCode, Integer.toHexString( elementColor ), "OK" ) );
		}
		
		return true;
	}
	/**
	 * Compare color.
	 *
	 * @param colorOne the color one
	 * @param colorTwo the color two
	 * @return the int
	 */
	private static int compareColor( int colorOne, int colorTwo )
	{
		double difference = 0;
		
		colorOne++;
		colorTwo++;
		
		if ( colorOne > colorTwo )
			difference = (double) ((double)colorTwo / (double)colorOne);
		else
			difference = (double) ((double)colorOne / (double)colorTwo);
		
		if ( difference == 0 )
			return 0;
		
		difference = 1 - difference;
		
		return (int) (100*difference);
		
	}

	
	/**
	 * Extract colors.
	 *
	 * @param colorValue the color value
	 * @return the int[]
	 */
	public int[] extractColors( String colorValue )
	{
		int colorData = 0;
		
		if ( colorValue.startsWith( "#" ) )
			colorData = Integer.parseInt( colorValue.substring( 1 ).trim(), 16 );
		else if ( colorValue.indexOf( "," ) > 0 )
		{
			String[] colors = colorValue.split( "," );
			if ( colors.length == 3 )
				return new int[] { Integer.parseInt( colors[ 0 ] ), Integer.parseInt( colors[ 1 ] ), Integer.parseInt( colors[ 2 ] ) };
		}
		else
			colorData = Integer.parseInt( colorValue );
		
		return new int[] { (int) ( (colorData & 0x00ff0000) >> 16 ), (int) ( (colorData & 0x0000ff00) >> 8 ), (int) ( (colorData & 0x000000ff) )} ;

	}

}
