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
package org.xframium.gesture;

import org.openqa.selenium.WebElement;
import org.xframium.page.Page;
import org.xframium.page.element.Element;

import io.appium.java_client.touch.offset.PointOption;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractKeyPressGesture.
 */
public abstract class AbstractDragDropGesture extends AbstractGesture
{
	
	/** The show keyboard. */
	private InitialDragDropAction initialAction;
	private Element fromElement;
	private Element toElement;
	
	public enum InitialDragDropAction
	{
	    PRESS,
	    LONG_PRESS;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.Gesture#setParameters(java.lang.Object[])
	 */
	public void setParameters( Object[] parameterArray )
	{
	    fromElement = (Element) parameterArray[ 0 ];
        toElement = (Element) parameterArray[ 1 ];
        initialAction = InitialDragDropAction.valueOf( parameterArray[ 2 ].toString() );
	}

    public InitialDragDropAction getInitialAction()
    {
        return initialAction;
    }

    public void setInitialAction( InitialDragDropAction initialAction )
    {
        this.initialAction = initialAction;
    }

    public Element getFromElement()
    {
        return fromElement;
    }

    public void setFromElement( Element fromElement )
    {
        this.fromElement = fromElement;
    }

    public Element getToElement()
    {
        return toElement;
    }

    public void setToElement( Element toElement )
    {
        this.toElement = toElement;
    }
    
    
}
