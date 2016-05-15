package com.xframium.integrations.perfectoMobile.rest.services;

import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractService.
 */
public abstract class AbstractService implements PerfectoService
{
	
	/** The perfecto mobile. */
	private PerfectoMobile perfectoMobile;
	
	/**
	 * Instantiates a new abstract service.
	 *
	 * @param perfectoMobile the perfecto mobile
	 */
	public AbstractService( PerfectoMobile perfectoMobile )
	{
		this.perfectoMobile = perfectoMobile;
	}
	
	/**
	 * Gets the base url.
	 *
	 * @return the base url
	 */
	protected String getBaseUrl()
	{
		return perfectoMobile.getBaseUrl();
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	protected String getUserName()
	{
		return perfectoMobile.getUserName();
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	protected String getPassword()
	{
		return perfectoMobile.getPassword();
	}

	
}
