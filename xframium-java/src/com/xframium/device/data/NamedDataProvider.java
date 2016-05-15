/*
 * 
 */
package com.xframium.device.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xframium.device.DeviceManager;
import com.xframium.device.SimpleDevice;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import com.xframium.integrations.perfectoMobile.rest.bean.Handset;
import com.xframium.spi.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class CSVDataProvider.
 */
public class NamedDataProvider implements DataProvider
{
	
	/** The log. */
	private Log log = LogFactory.getLog( NamedDataProvider.class );
	
	/** The named resources. */
	private String[] namedResources = null; 
	
	/** The driver type. */
	private DriverType driverType;
	
	/**
	 * Instantiates a new named data provider.
	 *
	 * @param resourceName A comma separated list of resource names
	 * @param driverType the driver type
	 */
	public NamedDataProvider( String resourceName, DriverType driverType )
	{
		namedResources = resourceName.split( "," );
		this.driverType = driverType;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.device.data.DataProvider#readData()
	 */
    public void readData()
    {
        for ( String device : namedResources )
        {
            DeviceManager.instance().registerDevice( lookupDeviceById( device, driverType ));
        }
    }

    public static Device lookupDeviceById( String device, DriverType driverType )
    {
        Handset handset = PerfectoMobile.instance().devices().getDevice( device );
			
        String driverName = "";
        switch( driverType )
        {
            case APPIUM:
                if ( handset.getOs().equals( "iOS" ) )
                    driverName = "IOS";
                else if ( handset.getOs().equals( "Android" ) )
                    driverName = "ANDROID";
                else
                    throw new IllegalArgumentException( "Appium is not supported on the following OS " + handset.getOs() );
                break;
                    
            case PERFECTO:
                driverName = "PERFECTO";
                break;
                    
            case WEB:
                driverName = "WEB";
                break;
        }
			
        return new SimpleDevice( device,
                                 handset.getManufacturer(),
                                 handset.getModel(),
                                 handset.getOs(),
                                 handset.getOsVersion(),
                                 null,
                                 null,
                                 1,
                                 driverName,
                                 true,
                                 device );
    }
    
}
