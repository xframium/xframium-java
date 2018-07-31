package org.xframium.device.capability;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.xframium.Initializable;

public class ExampleCapabilityBridge extends AbstractCapabilityBridge 
{
	@Override
	protected void _addCapabilities(DesiredCapabilities dC) 
	{
		dC.setCapability( "xFramium Version", Initializable.VERSION );
	}
}
