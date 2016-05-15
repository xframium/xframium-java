package com.xframium.integrations.perfectoMobile.rest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import com.xframium.integrations.perfectoMobile.rest.services.Application;
import com.xframium.integrations.perfectoMobile.rest.services.Device;
import com.xframium.integrations.perfectoMobile.rest.services.Devices;
import com.xframium.integrations.perfectoMobile.rest.services.Executions;
import com.xframium.integrations.perfectoMobile.rest.services.Gestures;
import com.xframium.integrations.perfectoMobile.rest.services.Imaging;
import com.xframium.integrations.perfectoMobile.rest.services.RESTInvocationHandler;
import com.xframium.integrations.perfectoMobile.rest.services.Reports;
import com.xframium.integrations.perfectoMobile.rest.services.Repositories;
import com.xframium.integrations.perfectoMobile.rest.services.WindTunnel;

// TODO: Auto-generated Javadoc
/**
 * The Class PerfectoMobile.
 */
public class PerfectoMobile
{
	
	/** The invocation handler. */
	private RESTInvocationHandler invocationHandler = new RESTInvocationHandler();
	
	/** The base url. */
	private String baseUrl;
	
	/** The user name. */
	private String userName;
	
	/** The password. */
	private String password;
	
	/** The response method. */
	private String responseMethod = "xml";
	
	/** The singleton. */
	private static PerfectoMobile singleton = new PerfectoMobile();
	
	/**
	 * Instantiates a new perfecto mobile.
	 */
	private PerfectoMobile()
	{
		
	}
	
	/**
	 * Instance.
	 *
	 * @return the perfecto mobile
	 */
	public static PerfectoMobile instance()
	{
		return singleton;
	}
	
	/**
	 * Application.
	 *
	 * @return the application
	 */
	public Application application()
	{
		return (Application) Proxy.newProxyInstance( this.getClass().getClassLoader(), new Class[] { Application.class }, (InvocationHandler)invocationHandler );
	}
	
	/**
	 * Reports.
	 *
	 * @return the reports
	 */
	public Reports reports()
	{
		return (Reports) Proxy.newProxyInstance( this.getClass().getClassLoader(), new Class[] { Reports.class }, (InvocationHandler)invocationHandler );
	}
	
	/**
	 * Devices.
	 *
	 * @return the devices
	 */
	public Devices devices()
	{
		return (Devices) Proxy.newProxyInstance( this.getClass().getClassLoader(), new Class[] { Devices.class }, (InvocationHandler)invocationHandler );
	}
	
	/**
	 * Imaging.
	 *
	 * @return the imaging
	 */
	public Imaging imaging()
	{
		return (Imaging) Proxy.newProxyInstance( this.getClass().getClassLoader(), new Class[] { Imaging.class }, (InvocationHandler)invocationHandler );
	}
	
	/**
	 * Imaging.
	 *
	 * @return the imaging
	 */
	public WindTunnel windTunnel()
	{
		return (WindTunnel) Proxy.newProxyInstance( this.getClass().getClassLoader(), new Class[] { WindTunnel.class }, (InvocationHandler)invocationHandler );
	}
	
	/**
	 * Executions.
	 *
	 * @return the executions
	 */
	public Executions executions()
	{
		return (Executions) Proxy.newProxyInstance( this.getClass().getClassLoader(), new Class[] { Executions.class }, (InvocationHandler)invocationHandler );
	}
	
	/**
	 * Repositories.
	 *
	 * @return the repositories
	 */
	public Repositories repositories()
	{
		return (Repositories) Proxy.newProxyInstance( this.getClass().getClassLoader(), new Class[] { Repositories.class }, (InvocationHandler)invocationHandler );
	}
	
	/**
	 * Device.
	 *
	 * @return the device
	 */
	public Device device()
	{
		return (Device) Proxy.newProxyInstance( this.getClass().getClassLoader(), new Class[] { Device.class }, (InvocationHandler)invocationHandler );
	}
	
	/**
	 * Gestures.
	 *
	 * @return the gestures
	 */
	public Gestures gestures()
	{
		return (Gestures) Proxy.newProxyInstance( this.getClass().getClassLoader(), new Class[] { Gestures.class }, (InvocationHandler)invocationHandler );
	}
	
	/**
	 * Gets the base url.
	 *
	 * @return the base url
	 */
	public String getBaseUrl()
	{
		if ( baseUrl == null )
			baseUrl = System.getProperty( "__cloudUrl" );
		return baseUrl;
	}
	
	/**
	 * Sets the base url.
	 *
	 * @param baseUrl the new base url
	 */
	public void setBaseUrl( String baseUrl )
	{
		this.baseUrl = baseUrl;
	}
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName()
	{
		if ( userName == null )
			userName = System.getProperty( "__userName" );
		return userName;
	}
	
	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName( String userName )
	{
		this.userName = userName;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword()
	{
		if ( password == null )
			password = System.getProperty( "__password" );
		return password;
	}
	
	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword( String password )
	{
		this.password = password;
	}

	/**
	 * Gets the response method.
	 *
	 * @return the response method
	 */
	public String getResponseMethod()
	{
		return responseMethod;
	}

	/**
	 * Sets the response method.
	 *
	 * @param responseMethod the new response method
	 */
	public void setResponseMethod( String responseMethod )
	{
		this.responseMethod = responseMethod;
	}
	
	
}
