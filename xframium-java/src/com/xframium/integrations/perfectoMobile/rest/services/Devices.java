package com.xframium.integrations.perfectoMobile.rest.services;

import com.xframium.integrations.perfectoMobile.rest.bean.Handset;
import com.xframium.integrations.perfectoMobile.rest.bean.HandsetCollection;
import com.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Devices.
 */
@ServiceDescriptor( serviceName="handsets" )
public interface Devices extends PerfectoService
{
	
	/**
	 * Gets the device.
	 *
	 * @param handsetId the handset id
	 * @return the device
	 */
	@Operation( operationName="info" )
	public Handset getDevice( @ResourceID String handsetId );
	
	/**
	 * Gets the devices.
	 *
	 * @return the devices
	 */
	@Operation( operationName="list" )
	public HandsetCollection getDevices();
	
	/**
	 * Gets the devices.
	 *
	 * @param manufacturer the manufacturer
	 * @return the devices
	 */
	@Operation( operationName="list" )
	public HandsetCollection getDevices( @NameOverride( name="manufacturer" ) String manufacturer );
	
	/**
	 * Gets the my devices.
	 *
	 * @param reservedTo the reserved to
	 * @return the my devices
	 */
	@Operation( operationName="list" )
	public HandsetCollection getMyDevices( @NameOverride( name="reservedTo" ) String reservedTo );
	
	/**
	 * Gets the devices.
	 *
	 * @param inUse the in use
	 * @return the devices
	 */
	@Operation( operationName="list" )
	public HandsetCollection getDevices( @NameOverride( name="inUse" ) boolean inUse );
}
