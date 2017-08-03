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

import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.integrations.alm.entity.ALMAttachment;
import org.xframium.integrations.alm.entity.ALMDefect;


// TODO: Auto-generated Javadoc
/**
 * Represents a connection to an ALM Server
 */
public class ALMRESTConnection
{
    
    /** The log. */
    private static Log log = LogFactory.getLog( ALMRESTConnection.class );
    
    /** The field template. */
    private static String fieldTemplate = "--%1$s\r\n" + "Content-Disposition: form-data; name=\"%2$s\" \r\n\r\n" + "%3$s" + "\r\n";
    
    /** The file data prefix template. */
    private static String fileDataPrefixTemplate = "--%1$s\r\n" + "Content-Disposition: form-data; name=\"%2$s\"; filename=\"%3$s\"\r\n" + "Content-Type: %4$s\r\n\r\n";
    
    /** The boundary. */
    private static String boundary = "xALM-Boundary";
    
    /** The cookies. */
    protected Map<String, String> cookies = new HashMap<String, String>( 20 );
    /**
     * This is the URL to the ALM application. Make sure that there is no slash at the end.
     */
    protected String serverUrl;
    
    /** The domain. */
    protected String domain;
    
    /** The project. */
    protected String project;


    /**
     * Instantiates a new ALM REST connection.
     *
     * @param serverUrl The URL of the ALM Server instance
     * @param domain The domain 
     * @param project the project
     */
    public ALMRESTConnection( String serverUrl, String domain, String project )
    {
        this.serverUrl = serverUrl;
        this.domain = domain;
        this.project = project;
    }

    private String attachWithOctetStream( String entityUrl, byte[] fileData, String filename ) throws Exception
    {

        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put( "Slug", filename );
        requestHeaders.put( "Content-Type", "application/octet-stream" );

        ALMResponse response = httpPost( entityUrl + "/attachments", fileData, requestHeaders );

        if ( response.getStatusCode() != HttpURLConnection.HTTP_CREATED )
        {
            throw new Exception( response.toString() );
        }

        return response.getResponseHeaders().get( "Location" ).iterator().next();
    }

    private String attachWithMultipart( String entityUrl, byte[] fileData, String contentType, String filename, String description ) throws Exception
    {

        // Note the order - extremely important:
        // Filename and description before file data.
        // Name of file in file part and filename part value MUST MATCH.
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write( String.format( fieldTemplate, boundary, "filename", filename ).getBytes() );
        bytes.write( String.format( fieldTemplate, boundary, "description", description ).getBytes() );
        bytes.write( ("\r\n--" + boundary + "--").getBytes() );
        bytes.write( fileData );
        bytes.write( String.format( fileDataPrefixTemplate, boundary, "file", filename, contentType ).getBytes() );
        bytes.close();

        Map<String, String> requestHeaders = new HashMap<String, String>();

        requestHeaders.put( "Content-Type", "multipart/form-data; boundary=" + boundary );

        ALMResponse response = httpPost( entityUrl + "/attachments", bytes.toByteArray(), requestHeaders );

        if ( response.getStatusCode() != HttpURLConnection.HTTP_CREATED )
        {
            throw new Exception( response.toString() );
        }

        return response.getResponseHeaders().get( "Location" ).iterator().next();
    }

    /**
     * Builds the entity collection url.
     *
     * @param entityType the entity type
     * @return the string
     */
    public String buildEntityCollectionUrl( String entityType )
    {
        return buildUrl( "rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" );
    }

    /**
     * Builds the project url.
     *
     * @param path the path
     * @return the string
     */
    public String buildProjectUrl( String path )
    {
        return buildUrl( "rest/domains/" + domain + "/projects/" + project + "/" + path );
    }

    /**
     * Builds the base server url.
     *
     * @param path            on the server to use
     * @return a url on the server for the path parameter
     */
    public String buildUrl( String path )
    {

        return String.format( "%1$s/%2$s", serverUrl, path );
    }

    /**
     * Performs an HTTP Requests using PUT
     *
     * @param url the url
     * @param data the data
     * @param headers the headers
     * @return the ALM response
     * @throws Exception the exception
     */
    public ALMResponse httpPut( String url, byte[] data, Map<String, String> headers ) throws Exception
    {

        return doHttp( "PUT", url, null, data, headers, cookies );
    }

    /**
     * Performs an HTTP Requests using POST
     *
     * @param url the url
     * @param data the data
     * @param headers the headers
     * @return the ALM response
     * @throws Exception the exception
     */
    public ALMResponse httpPost( String url, byte[] data, Map<String, String> headers ) throws Exception
    {

        return doHttp( "POST", url, null, data, headers, cookies );
    }

    /**
     * Performs an HTTP Requests using DELETE
     *
     * @param url the url
     * @param headers the headers
     * @return the ALM response
     * @throws Exception the exception
     */
    public ALMResponse httpDelete( String url, Map<String, String> headers ) throws Exception
    {

        return doHttp( "DELETE", url, null, null, headers, cookies );
    }

    /**
     * Performs an HTTP Requests using GET
     *
     * @param url the url
     * @param queryString the query string
     * @param headers the headers
     * @return the ALM response
     * @throws Exception the exception
     */
    public ALMResponse httpGet( String url, String queryString, Map<String, String> headers ) throws Exception
    {

        return doHttp( "GET", url, queryString, null, headers, cookies );
    }

    private ALMResponse doHttp( String type, String url, String queryString, byte[] data, Map<String, String> headers, Map<String, String> cookies ) throws Exception
    {
        log.warn( "1.0.4" );

        if ( (queryString != null) && !queryString.isEmpty() )
            url += "?" + queryString;

        if ( log.isInfoEnabled() )
            log.info( "Executing " + type + ": to " + url );

        HttpURLConnection con = null;
        
        if ( Boolean.parseBoolean( System.getProperty( "alm.bypassProxy", "false" ) ) )
            con = (HttpURLConnection) new URL( url ).openConnection( Proxy.NO_PROXY );
        else
            con = (HttpURLConnection) new URL( url ).openConnection();

        con.setRequestMethod( type );
        String cookieString = getCookieString();

        if ( log.isInfoEnabled() )
            log.info( "Cookies: " + cookieString );

        prepareHttpRequest( con, headers, data, cookieString );
        con.connect();
        ALMResponse ret = retrieveHtmlResponse( con );

        if ( log.isInfoEnabled() )
            log.info( "Return Value: " + ret );

        updateCookies( ret );

        return ret;
    }

    /**
     * Prepare http request.
     *
     * @param con            connection to set the headers and bytes in
     * @param headers            to use in the request, such as content-type
     * @param bytes            the actual data to post in the connection.
     * @param cookieString            the cookies data from clientside, such as lwsso, qcsession,
     *            jsession etc.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void prepareHttpRequest( HttpURLConnection con, Map<String, String> headers, byte[] bytes, String cookieString ) throws IOException
    {

        String contentType = null;

        // attach cookie information if such exists
        if ( (cookieString != null) && !cookieString.isEmpty() )
            con.setRequestProperty( "Cookie", cookieString );

        // send data from headers
        if ( headers != null )
        {

            contentType = headers.remove( "Content-Type" );

            Iterator<Entry<String, String>> headersIterator = headers.entrySet().iterator();
            while ( headersIterator.hasNext() )
            {
                Entry<String, String> header = headersIterator.next();
                con.setRequestProperty( header.getKey(), header.getValue() );
            }
        }


        if ( (bytes != null) && (bytes.length > 0) )
        {

            con.setDoOutput( true );

            if ( contentType != null )
            {
                con.setRequestProperty( "Content-Type", contentType );
            }

            OutputStream out = con.getOutputStream();
            out.write( bytes );
            out.flush();
            out.close();
        }
    }

    private ALMResponse retrieveHtmlResponse( HttpURLConnection con ) throws Exception
    {

        ALMResponse ret = new ALMResponse();

        ret.setStatusCode( con.getResponseCode() );
        ret.setResponseHeaders( con.getHeaderFields() );

        InputStream inputStream;

        try
        {
            inputStream = con.getInputStream();
        }

        catch ( Exception e )
        {
            inputStream = con.getErrorStream();
            ret.setFailure( e );
        }

        ByteArrayOutputStream container = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int read;
        while ( (read = inputStream.read( buf, 0, 1024 )) > 0 )
        {
            container.write( buf, 0, read );
        }

        ret.setResponseData( container.toByteArray() );

        return ret;
    }

    /**
     * Update cookies.
     *
     * @param response the response
     */
    private void updateCookies( ALMResponse response )
    {

        Iterable<String> newCookies = response.getResponseHeaders().get( "Set-Cookie" );
        if ( newCookies != null )
        {

            for ( String cookie : newCookies )
            {
                if ( cookie == null || cookie.isEmpty() )
                    continue;
                
                int equalIndex = cookie.indexOf( '=' );
                int semicolonIndex = cookie.indexOf( ';' );

                if ( equalIndex < 0 || semicolonIndex < 0 )
                    continue;
                
                String cookieKey = cookie.substring( 0, equalIndex );
                String cookieValue = cookie.substring( equalIndex + 1, semicolonIndex );

                cookies.put( cookieKey, cookieValue );
            }
        }
    }

    private String getCookieString()
    {

        StringBuilder sb = new StringBuilder();

        if ( !cookies.isEmpty() )
        {

            Set<Entry<String, String>> cookieEntries = cookies.entrySet();
            for ( Entry<String, String> entry : cookieEntries )
            {
                sb.append( entry.getKey() ).append( "=" ).append( entry.getValue() ).append( ";" );
            }
        }

        String ret = sb.toString();

        return ret;
    }

    /**
     * Allows the user to login to ALM by first determining the authentication point
     *
     * @param username the username
     * @param password the password
     * @return true if authenticated at the end of this method.
     * @throws Exception             convenience method used by other examples to do their login
     */
    public boolean login( String username, String password ) throws Exception
    {

        String authenticationPoint = this.isAuthenticated();
        if ( authenticationPoint != null )
        {
            return this.login( authenticationPoint, username, password );
        }
        return true;
    }

    /**
     * Allows the user to login to ALM using the specified authentication point
     *
     * @param loginUrl            to authenticate at
     * @param username the username
     * @param password the password
     * @return true on operation success, false otherwise
     * @throws Exception             Logging in to our system is standard http login (basic
     *             authentication), where one must store the returned cookies
     *             for further use.
     */
    private boolean login( String loginUrl, String username, String password ) throws Exception
    {

        // create a string that lookes like:
        // "Basic ((username:password)<as bytes>)<64encoded>"
        byte[] credBytes = (username + ":" + password).getBytes();
        String credEncodedString = "Basic " + new String( Base64.getEncoder().encode( credBytes ) );

        Map<String, String> map = new HashMap<String, String>();
        map.put( "Authorization", credEncodedString );

        log.info( map );

        ALMResponse response = httpGet( loginUrl, null, map );

        boolean ret = response.getStatusCode() == HttpURLConnection.HTTP_OK;

        if ( ret )
        {
            Map<String, String> requestHeaders = new HashMap<String, String>();

            // As can be seen in the implementation below, creating an entity
            // is simply posting its xml into the correct collection.
            response = httpPost( buildUrl( "rest/site-session" ), null, requestHeaders );
        }

        return ret;
    }

    /**
     * Adds a defect to ALM
     *
     * @param almDefect the alm defect
     * @return the string
     * @throws Exception the exception
     */
    public String addDefect( ALMDefect almDefect ) throws Exception
    {
        String defectUrl = createEntity( buildEntityCollectionUrl( almDefect.getEntityType() ), almDefect.toXML() );

        if ( almDefect.getAttachments() != null )
        {
            for ( ALMAttachment a : almDefect.getAttachments() )
            {
                byte[] attachmentData = a.getFileData() != null ? a.getFileData() : readFile( a.getFileName() );
                if ( attachmentData != null )
                    attachWithOctetStream( defectUrl, attachmentData, a.getFileName().getName() );
            }
        }

        return defectUrl;
    }

    /**
     * Read file.
     *
     * @param currentFile the current file
     * @return the byte[]
     */
    private byte[] readFile( File currentFile )
    {
        byte[] buffer = new byte[512];
        int bytesRead = 0;

        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try
        {
            inputStream = new BufferedInputStream( new FileInputStream( currentFile ) );
            while ( (bytesRead = inputStream.read( buffer )) > 0 )
            {
                outputStream.write( buffer, 0, bytesRead );
            }

            return outputStream.toByteArray();
        }
        catch ( Exception e )
        {
            log.warn( "Could not read " + currentFile.getAbsolutePath() + " - " + e.getMessage() );
            return null;
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch ( Exception e )
            {
            }
        }
    }

    /**
     * Given an entity, list all of the possible fields.
     *
     * @param entityType the entity type
     * @return the string
     * @throws Exception the exception
     */
    public String entityFields( String entityType ) throws Exception
    {

        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put( "Content-Type", "application/xml" );
        requestHeaders.put( "Accept", "application/xml" );

        ALMResponse response = httpGet( buildProjectUrl( "customization/entities/" + entityType + "/fields" ), null, requestHeaders );

        Exception failure = response.getFailure();
        if ( failure != null )
        {
            throw failure;
        }

        return null;
    }

    private String createEntity( String collectionUrl, String postedEntityXml ) throws Exception
    {

        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put( "Content-Type", "application/xml" );
        requestHeaders.put( "Accept", "application/xml" );


        ALMResponse response = httpPost( collectionUrl, postedEntityXml.getBytes(), requestHeaders );

        Exception failure = response.getFailure();
        if ( failure != null )
        {
            throw failure;
        }

        String entityUrl = response.getResponseHeaders().get( "Location" ).iterator().next();

        return entityUrl;
    }

    /**
     * Logout of ALM
     *
     * @return true if logout successful
     * @throws Exception             close session on server and clean session cookies on client
     */
    public boolean logout() throws Exception
    {
        ALMResponse response = httpGet( buildUrl( "authentication-point/logout" ), null, null );
        cookies.clear();

        return (response.getStatusCode() == HttpURLConnection.HTTP_OK);

    }

    /**
     * Checks if is authenticated.
     *
     * @return null if authenticated.<br>
     *         a url to authenticate against if not authenticated.
     * @throws Exception the exception
     */
    public String isAuthenticated() throws Exception
    {

        String isAuthenticateUrl = buildUrl( "rest/is-authenticated" );
        String ret;

        ALMResponse response = httpGet( isAuthenticateUrl, null, null );
        int responseCode = response.getStatusCode();

        // if already authenticated
        if ( responseCode == HttpURLConnection.HTTP_OK )
        {

            ret = null;
        }

        // if not authenticated - get the address where to authenticate
        // via WWW-Authenticate
        else if ( responseCode == HttpURLConnection.HTTP_UNAUTHORIZED )
        {

            Iterable<String> authenticationHeader = response.getResponseHeaders().get( "WWW-Authenticate" );

            String newUrl = authenticationHeader.iterator().next().split( "=" )[1];
            newUrl = newUrl.replace( "\"", "" );
            newUrl += "/authenticate";
            ret = newUrl;
        }

        // Not ok, not unauthorized. An error, such as 404, or 500
        else
        {

            throw response.getFailure();
        }

        return ret;
    }

}
