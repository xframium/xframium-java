package com.xframium.driver;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import com.xframium.device.DeviceManager;
import com.xframium.device.ng.AbstractSeleniumTest;
import com.xframium.page.PageManager;
import com.xframium.page.keyWord.KeyWordDriver;
import com.xframium.page.keyWord.KeyWordTest;


public class XMLTestDriver extends AbstractSeleniumTest
{
	@Test( dataProvider = "deviceManager")
	public void testDriver( TestName testName ) throws Throwable
	{
		String deviceOs = getDevice().getOs();
		
		KeyWordTest test = KeyWordDriver.instance().getTest( testName.getTestName() );
		if ( test == null )
			throw new IllegalArgumentException( "The Test Name " + testName + " does not exist" );
		
		if ( test.getOs() != null && deviceOs != null )
		{
			if ( !deviceOs.toUpperCase().equals(  test.getOs().toUpperCase() ) )
				throw new SkipException( "This test is not designed to work on a device with [" + deviceOs + "]  It needs [" + test.getOs() + "]" );
		}
		
		if ( DeviceManager.instance().isDryRun() )
		{
			log.info( "This would have executed " +  testName.getTestName() );
			return;
		}
		
		boolean returnValue = KeyWordDriver.instance().executeTest( testName.getTestName(), getWebDriver() );
		
		
		if ( returnValue )
			return;
		
		if ( PageManager.instance().getThrowable() != null )
			throw PageManager.instance().getThrowable();
		
		
		Assert.assertTrue( returnValue );
	} 

}
