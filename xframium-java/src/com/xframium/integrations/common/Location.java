package com.xframium.integrations.common;

// TODO: Auto-generated Javadoc
/**
 * The Class Location.
 */
public class Location
{
	
	/** The longitude. */
	public double longitude;
	
	/** The latitude. */
	public double latitude;

	/**
	 * Instantiates a new location.
	 *
	 * @param longitude the longitude
	 * @param latitude the latitude
	 */
	public Location( double longitude, double latitude )
	{
		this.longitude = longitude;
		this.latitude = latitude;
	}
 
	/**
	 * Instantiates a new location.
	 *
	 * @param longitude the longitude
	 * @param latitude the latitude
	 */
	public Location( String longitude, String latitude )
	{
		this.longitude = Double.parseDouble( longitude );
		this.latitude = Double.parseDouble( latitude );
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object o )
	{
		if (!( o instanceof Location ))
		{
			return false;
		}

		Location other = ( Location ) o;
		return other.longitude == longitude && other.latitude == latitude;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format( "%f,%f", latitude, longitude );
	}
}
