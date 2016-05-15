package com.xframium.gesture.factory.spi.perfecto;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import com.xframium.gesture.AbstractTwoFingerGesture;
import com.xframium.integrations.common.PercentagePoint;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;

// TODO: Auto-generated Javadoc
/**
 * The Class TwoFingerGesture.
 */
public class TwoFingerGesture extends AbstractTwoFingerGesture
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.AbstractGesture#_executeGesture(org.openqa.selenium.WebDriver)
	 */
	@Override
	protected boolean _executeGesture( WebDriver webDriver )
	{
		
		String executionId = getExecutionId( webDriver );
		String deviceName = getDeviceName( webDriver );
		if ( executionId != null && deviceName != null )
		{
			
			if ( webElement != null )
			{
				if ( webElement.getLocation() != null && webElement.getSize() != null && webElement.getSize().getWidth() > 0 && webElement.getSize().getHeight() > 0 )
				{
					int x = getStartOne().getX() * webElement.getSize().getWidth() + webElement.getLocation().getX();
					int y = getStartOne().getY() * webElement.getSize().getHeight() + webElement.getLocation().getY();
					Point swipeStart = new Point( x, y );
					
					x = getEndOne().getX() * webElement.getSize().getWidth() + webElement.getLocation().getX();
					y = getEndOne().getY() * webElement.getSize().getHeight() + webElement.getLocation().getY();
					Point swipeEnd = new Point( x, y );
					
					PerfectoMobile.instance().gestures().pinch( executionId, deviceName, new PercentagePoint( swipeStart.getX(), swipeStart.getY(), false ), new PercentagePoint( swipeEnd.getX(), swipeEnd.getY(), false ) );
					return true;
				}
				else
				{
					log.warn( "A relative elements was specified however no size could be determined" );
					return false;
				}
			}

			PerfectoMobile.instance().gestures().pinch( executionId, deviceName, new PercentagePoint( getStartOne().getX(), getStartOne().getY() ), new PercentagePoint( getEndOne().getX(), getEndOne().getY() ) );
			return true;
		}
		else
			return false;
	}

}
