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
package org.xframium.gesture.factory.spi.selenium;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.xframium.gesture.AbstractKeyPressGesture;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyPressGesture.
 */
public class KeyPressGesture extends AbstractKeyPressGesture
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.AbstractGesture#_executeGesture(org.openqa.selenium.WebDriver)
	 */
	@Override
	protected boolean _executeGesture( WebDriver webDriver )
	{
		String[] keyCodes = getKeyCode().split("\\+");
		String keyPressed = null;
		List<CharSequence> charSequence = new ArrayList<CharSequence>();
		
		for (String keyCode : keyCodes) {
			
			try
			{
				charSequence.add(Keys.valueOf(keyCode.toUpperCase()));
			}
			catch( Exception e )
			{
				charSequence.add( keyCode );
			}
			
		}
		
		if (charSequence.size() > 0) {
			Iterable<CharSequence> iterable = charSequence;
			keyPressed = Keys.chord(iterable);
			
			if ( webElement != null )
				new Actions( webDriver ).sendKeys(keyPressed).perform();
			else
				new Actions( webDriver ).sendKeys(keyPressed).perform();
			
			
			//new Actions( webDriver ).moveToElement( webElement ).keyDown( Keys.CONTROL ).sendKeys( "A" ).keyUp( Keys.CONTROL ).perform();
		}
		return true;
	}

}
