package com.xframium.page;

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
