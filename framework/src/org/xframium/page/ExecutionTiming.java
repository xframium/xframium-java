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
package org.xframium.page;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Holds a reference to the executions of a timed method or event.
 */
public class ExecutionTiming
{
	
	/** The method name. */
	private String methodName;
	
	/** The run list. */
	public List<Long> runList = new ArrayList<Long>( 10 );
	
	/** The execution time. */
	private long executionTime = 0;
	
	/** The minimum run. */
	private long minimumRun = Long.MAX_VALUE;
	
	/** The maximum run. */
	private long maximumRun = 0;
	
	/** The average run. */
	private long averageRun = 0;
	
	/**
	 * Instantiates a new execution timing.
	 *
	 * @param methodName the method name
	 */
	public ExecutionTiming( String methodName )
	{
		this.methodName = methodName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return methodName + "," + executionTime + "," + minimumRun + "," + maximumRun + "," + averageRun + "," + runList.size();
	}

	/**
	 * Adds the run.
	 *
	 * @param runLength the run length
	 */
	public void addRun( long runLength )
	{
		executionTime = System.currentTimeMillis();
		runList.add( runLength );
		
		if ( runLength < minimumRun )
			minimumRun = runLength;
		
		if ( runLength > maximumRun )
			maximumRun = runLength;
		
		long totalLength = 0;
		for ( long listRun : runList )
			totalLength += listRun;
		
		averageRun = totalLength / runList.size();
		
	}
	
	/**
	 * Gets the minimum run.
	 *
	 * @return the minimum run
	 */
	public long getMinimumRun()
	{
		return minimumRun;
	}
	
	/**
	 * Gets the maximum run.
	 *
	 * @return the maximum run
	 */
	public long getMaximumRun()
	{
		return maximumRun;
	}
	
	/**
	 * Gets the average run.
	 *
	 * @return the average run
	 */
	public long getAverageRun()
	{
		return averageRun;
	}
	
	/**
	 * Gets the run count.
	 *
	 * @return the run count
	 */
	public long getRunCount()
	{
		return runList.size();
	}
	
	/**
	 * Gets the method name.
	 *
	 * @return the method name
	 */
	public String getMethodName()
	{
		return methodName;
	}

	/**
	 * Gets the execution time.
	 *
	 * @return the execution time
	 */
	public long getExecutionTime() {
		return executionTime;
	}
	
	
}
