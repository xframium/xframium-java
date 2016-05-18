package com.xframium.test;
    
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.xframium.content.*;
import org.xframium.device.*;
import org.xframium.device.cloud.*;
import org.xframium.device.data.*;
import org.xframium.page.*;
import org.xframium.page.data.*;
import org.xframium.page.element.*;
import org.xframium.spi.Device;


public class SQLDataProvidersTest
{
    @Test
    public void testSQLCloudProvider()
    {
        CloudDescriptor cloud = CloudRegistry.instance().getCloud( "partners" );

        Assert.assertTrue( cloud != null, "Got a cloud" );
        Assert.assertTrue( "partners.perfectomobile.com".equals(cloud.getHostName()), "Got the right cloud" );
    }

    @Test
    public void testSQLDeviceProvider()
    {
        Device device = DeviceManager.instance().getDevice( "Samsung S6" );

        Assert.assertTrue( device != null, "Got a device" );
        Assert.assertTrue( "Android".equals( device.getOs()), "Got the right device" );
    }

    @Test
    public void testSQLContentProvider()
    {
        ContentData content = ContentManager.instance().getContent( "test" );

        Assert.assertTrue( content != null, "Got some content" );
        Assert.assertTrue( "two".equals( content.getValue( 1 )) , "Got the right content" );
    }

    @Test
    public void testSQLPageDataProvider()
    {
        PageData[] data = PageDataManager.instance().getRecords( "searchData1" );

        Assert.assertTrue( data != null, "Got some data" );
        Assert.assertTrue( data.length > 0, "Got some data" );
        Assert.assertTrue( "perfecto".equals( data[0].getData( "text" )) , "Got the right data" );
    }

    @Test
    public void testSQLPageElementProvider()
    {
        ElementDescriptor elementDescriptor = new ElementDescriptor( "Google",
                                                                     "Home",
                                                                     "SEARCH_FOR" );
        
        Element element = PageManager.instance().getElementProvider().getElement( elementDescriptor );

        Assert.assertTrue( element != null, "Got an element" );
        Assert.assertTrue( "//input[@id='lst-ib']".equals( element.getKey()) , "Got the right element" );
    }

}
