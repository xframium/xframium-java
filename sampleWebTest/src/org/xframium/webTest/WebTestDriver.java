package org.xframium.webTest;

import org.xframium.driver.TestDriver;

public class WebTestDriver extends TestDriver
{
    public static void main( String[] args )
    {
        TestDriver.main( new String[] { "resources\\Web Test.xml" } );
    }
}
