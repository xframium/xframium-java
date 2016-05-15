package com.xframium.integrations.perfectoMobile.rest.services;

import com.xframium.integrations.perfectoMobile.rest.bean.ItemCollection;
import com.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Repositories.
 */
@ServiceDescriptor( serviceName="repositories" )
public interface Repositories extends PerfectoService
{
	
	/**
	 * The Enum RepositoryType.
	 */
	public enum RepositoryType
	{
		
		/** The media. */
		MEDIA,
		
		/** The datatables. */
		DATATABLES,
		
		/** The scripts. */
		SCRIPTS
	}
	
	
	/**
	 * Gets the repositorys.
	 *
	 * @param rType the r type
	 * @return the repositorys
	 */
	@Operation( operationName="list" )
	public ItemCollection getRepositorys( @ResourceID RepositoryType rType );

	/**
	 * Download.
	 *
	 * @param rType the r type
	 * @param fileKey the file key
	 * @return the byte[]
	 */
	@Operation( operationName="download" )
	public byte[] download( @ResourceID RepositoryType rType, @ResourceID String fileKey );
	
	
	
}
