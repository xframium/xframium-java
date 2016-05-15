package com.xframium.integrations.common;

// TODO: Auto-generated Javadoc
/**
 * The Class PercentagePoint.
 */
public class PercentagePoint
{
	
	/** The x. */
	public int x;
	
	/** The y. */
	public int y;
	
	/** The include percent. */
	boolean includePercent = true;

	/**
	 * Instantiates a new percentage point.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public PercentagePoint( int x, int y )
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Instantiates a new percentage point.
	 *
	 * @param x the x
	 * @param y the y
	 * @param includePercent the include percent
	 */
	public PercentagePoint( int x, int y, boolean includePercent )
	{
		this.x = x;
		this.y = y;
		this.includePercent = includePercent;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY()
	{
		return y;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object o )
	{
		if (!( o instanceof PercentagePoint ))
		{
			return false;
		}

		PercentagePoint other = ( PercentagePoint ) o;
		return other.x == x && other.y == y;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return x << 12 + y;
	}

	/**
	 * Move.
	 *
	 * @param newX the new x
	 * @param newY the new y
	 */
	public void move( int newX, int newY )
	{
		x = newX;
		y = newY;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if ( includePercent )
			return String.format( "%d%%, %d%%", x, y );
		else
			return String.format( "%d, %d", x, y );
	}
}
