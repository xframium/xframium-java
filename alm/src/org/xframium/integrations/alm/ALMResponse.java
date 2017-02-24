/*******************************************************************************
 * xFramium
 *
 * Copyright 2017 by Moreland Labs LTD (http://www.morelandlabs.com)
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
package org.xframium.integrations.alm;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class ALMResponse.
 */
public class ALMResponse 
{

    /** The response headers. */
    private Map<String, ? extends Iterable<String>> responseHeaders = null;
    
    /** The response data. */
    private byte[] responseData = null;
    
    /** The failure. */
    private Exception failure = null;
    
    /** The status code. */
    private int statusCode = 0;

    /**
     * Instantiates a new ALM response.
     *
     * @param responseHeaders the response headers
     * @param responseData the response data
     * @param failure the failure
     * @param statusCode the status code
     */
    public ALMResponse( Map<String, Iterable<String>> responseHeaders, byte[] responseData, Exception failure, int statusCode )
    {
        super();
        this.responseHeaders = responseHeaders;
        this.responseData = responseData;
        this.failure = failure;
        this.statusCode = statusCode;
    }

    /**
     * Instantiates a new ALM response.
     */
    public ALMResponse()
    {
    }

    /**
     * Gets the response headers.
     *
     * @return the responseHeaders
     */
    public Map<String, ? extends Iterable<String>> getResponseHeaders()
    {
        return responseHeaders;
    }

    /**
     * Sets the response headers.
     *
     * @param responseHeaders            the responseHeaders to set
     */
    public void setResponseHeaders( Map<String, ? extends Iterable<String>> responseHeaders )
    {
        this.responseHeaders = responseHeaders;
    }

    /**
     * Gets the response data.
     *
     * @return the responseData
     */
    public byte[] getResponseData()
    {
        return responseData;
    }

    /**
     * Sets the response data.
     *
     * @param responseData            the responseData to set
     */
    public void setResponseData( byte[] responseData )
    {
        this.responseData = responseData;
    }

    /**
     * Gets the failure.
     *
     * @return the failure if the access to the requested URL failed, such as a
     *         404 or 500. If no such failure occured this method returns null.
     */
    public Exception getFailure()
    {
        return failure;
    }

    /**
     * Sets the failure.
     *
     * @param failure            the failure to set
     */
    public void setFailure( Exception failure )
    {
        this.failure = failure;
    }

    /**
     * Gets the status code.
     *
     * @return the statusCode
     */
    public int getStatusCode()
    {
        return statusCode;
    }

    /**
     * Sets the status code.
     *
     * @param statusCode            the statusCode to set
     */
    public void setStatusCode( int statusCode )
    {
        this.statusCode = statusCode;
    }

    /**
     * To string.
     *
     * @return the string
     * @see Object#toString() return the contents of the byte[] data as a
     *      string.
     */
    @Override
    public String toString()
    {

        return new String( this.responseData );
    }

}