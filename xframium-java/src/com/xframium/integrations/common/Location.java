/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
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
