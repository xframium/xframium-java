package org.xframium.integrations.alm;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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


public class ALMRESTConnection
{
    private static Log log = LogFactory.getLog( ALMRESTConnection.class );
    private static String fieldTemplate = "--%1$s\r\n" + "Content-Disposition: form-data; name=\"%2$s\" \r\n\r\n" + "%3$s" + "\r\n";
    private static String fileDataPrefixTemplate = "--%1$s\r\n" + "Content-Disposition: form-data; name=\"%2$s\"; filename=\"%3$s\"\r\n" + "Content-Type: %4$s\r\n\r\n";
    private static String boundary = "xALM-Boundary";
    protected Map<String, String> cookies = new HashMap<String, String>( 20 );
    /**
     * This is the URL to the ALM application. For example:
     * http://myhost:8080/qcbin. Make sure that there is no slash at the end.
     */
    protected String serverUrl;
    protected String domain;
    protected String project;

    public static void main( String[] args )
    {
        try
        {
            ALMRESTConnection c = new ALMRESTConnection( "http://alm.perfectomobilelab.net:8080/qcbin", "PROFESSIONAL_SERVICES", "CIBC" );
            boolean loginSuccessful = c.login( "alleng", "Perfecto123!" );

            log.info( "Logged In: " + loginSuccessful );

            ALMDefect d = new ALMDefect();
            d.setDescription( "This is a test defect" );
            d.setStatus( "New" );
            d.setSummary( "Summary Field" );
            d.setSeverity( 3 );
            d.setPriority( 3 );
            d.setDetectedBy( "alleng" );

            d.setAttachments( new ALMAttachment[] { new ALMAttachment( new File( "C:\\Users\\Allen\\git\\fordPass\\fordPass\\fordPass-out\\02-01_14-05-36-048\\artifacts\\state5655463874760842887.png" ), null, "image/png", "test description" ), new ALMAttachment( new File( "C:\\Users\\Allen\\git\\fordPass\\fordPass\\fordPass-out\\02-01_14-05-36-048\\artifacts\\dom-753326596865996462.xml" ), null, "text/xml", "test description" ) } );

            System.out.println( d.toXML() );

            String defectUrl = c.addDefect( d );

            System.out.println( defectUrl );
            // c.entityFields( "defect" );

            if ( loginSuccessful )
                c.logout();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

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

    public String buildEntityCollectionUrl( String entityType )
    {
        return buildUrl( "rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" );
    }

    public String buildProjectUrl( String path )
    {
        return buildUrl( "rest/domains/" + domain + "/projects/" + project + "/" + path );
    }

    /**
     * @param path
     *            on the server to use
     * @return a url on the server for the path parameter
     */
    public String buildUrl( String path )
    {

        return String.format( "%1$s/%2$s", serverUrl, path );
    }

    /**
     * @return the cookies
     */
    public Map<String, String> getCookies()
    {
        return cookies;
    }

    /**
     * @param cookies
     *            the cookies to set
     */
    public void setCookies( Map<String, String> cookies )
    {
        this.cookies = cookies;
    }

    public ALMResponse httpPut( String url, byte[] data, Map<String, String> headers ) throws Exception
    {

        return doHttp( "PUT", url, null, data, headers, cookies );
    }

    public ALMResponse httpPost( String url, byte[] data, Map<String, String> headers ) throws Exception
    {

        return doHttp( "POST", url, null, data, headers, cookies );
    }

    public ALMResponse httpDelete( String url, Map<String, String> headers ) throws Exception
    {

        return doHttp( "DELETE", url, null, null, headers, cookies );
    }

    public ALMResponse httpGet( String url, String queryString, Map<String, String> headers ) throws Exception
    {

        return doHttp( "GET", url, queryString, null, headers, cookies );
    }

    /**
     * @param type
     *            http operation: get post put delete
     * @param url
     *            to work on
     * @param queryString
     * @param data
     *            to write, if a writable operation
     * @param headers
     *            to use in the request
     * @param cookies
     *            to use in the request and update from the response
     * @return http response
     * @throws Exception
     */
    private ALMResponse doHttp( String type, String url, String queryString, byte[] data, Map<String, String> headers, Map<String, String> cookies ) throws Exception
    {

        if ( (queryString != null) && !queryString.isEmpty() )
        {

            url += "?" + queryString;
        }

        if ( log.isInfoEnabled() )
            log.info( type + ": " + url );

        HttpURLConnection con = (HttpURLConnection) new URL( url ).openConnection();

        con.setRequestMethod( type );
        String cookieString = getCookieString();

        log.info( cookieString );

        prepareHttpRequest( con, headers, data, cookieString );
        con.connect();
        ALMResponse ret = retrieveHtmlResponse( con );

        if ( log.isInfoEnabled() )
            log.info( ret );

        updateCookies( ret );

        return ret;
    }

    /**
     * @param con
     *            connection to set the headers and bytes in
     * @param headers
     *            to use in the request, such as content-type
     * @param bytes
     *            the actual data to post in the connection.
     * @param cookieString
     *            the cookies data from clientside, such as lwsso, qcsession,
     *            jsession etc.
     * @throws java.io.IOException
     */
    private void prepareHttpRequest( HttpURLConnection con, Map<String, String> headers, byte[] bytes, String cookieString ) throws IOException
    {

        String contentType = null;

        // attach cookie information if such exists
        if ( (cookieString != null) && !cookieString.isEmpty() )
        {

            con.setRequestProperty( "Cookie", cookieString );
        }

        // send data from headers
        if ( headers != null )
        {

            // Skip the content-type header - should only be sent
            // if you actually have any content to send. see below.
            contentType = headers.remove( "Content-Type" );

            Iterator<Entry<String, String>> headersIterator = headers.entrySet().iterator();
            while ( headersIterator.hasNext() )
            {
                Entry<String, String> header = headersIterator.next();
                con.setRequestProperty( header.getKey(), header.getValue() );
            }
        }

        // If there's data to attach to the request, it's handled here.
        // Note that if data exists, we take into account previously removed
        // content-type.
        if ( (bytes != null) && (bytes.length > 0) )
        {

            con.setDoOutput( true );

            // warning: if you add content-type header then you MUST send
            // information or receive error.
            // so only do so if you're writing information...
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

    /**
     * @param con
     *            that is already connected to its url with an http request, and
     *            that should contain a response for us to retrieve
     * @return a response from the server to the previously submitted http
     *         request
     * @throws Exception
     */
    private ALMResponse retrieveHtmlResponse( HttpURLConnection con ) throws Exception
    {

        ALMResponse ret = new ALMResponse();

        ret.setStatusCode( con.getResponseCode() );
        ret.setResponseHeaders( con.getHeaderFields() );

        InputStream inputStream;
        // select the source of the input bytes, first try 'regular' input
        try
        {
            inputStream = con.getInputStream();
        }

        /*
         * If the connection to the server somehow failed, for example 404 or
         * 500, con.getInputStream() will throw an exception, which we'll keep.
         * We'll also store the body of the exception page, in the response
         * data.
         */
        catch ( Exception e )
        {

            inputStream = con.getErrorStream();
            ret.setFailure( e );
        }

        // This actually takes the data from the previously set stream
        // (error or input) and stores it in a byte[] inside the response
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

    private void updateCookies( ALMResponse response )
    {

        Iterable<String> newCookies = response.getResponseHeaders().get( "Set-Cookie" );
        if ( newCookies != null )
        {

            for ( String cookie : newCookies )
            {
                int equalIndex = cookie.indexOf( '=' );
                int semicolonIndex = cookie.indexOf( ';' );

                String cookieKey = cookie.substring( 0, equalIndex );
                String cookieValue = cookie.substring( equalIndex + 1, semicolonIndex );

                cookies.put( cookieKey, cookieValue );
            }
        }
    }

    public String getCookieString()
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
     * @param username
     * @param password
     * @return true if authenticated at the end of this method.
     * @throws Exception
     *
     *             convenience method used by other examples to do their login
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
     * @param loginUrl
     *            to authenticate at
     * @param username
     * @param password
     * @return true on operation success, false otherwise
     * @throws Exception
     *
     *             Logging in to our system is standard http login (basic
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

    public String addDefect( ALMDefect almDefect ) throws Exception
    {
        String defectUrl = createEntity( buildEntityCollectionUrl( almDefect.getEntityType() ), almDefect.toXML() );

        if ( almDefect.getAttachments() != null )
        {
            for ( ALMAttachment a : almDefect.getAttachments() )
            {
                //String attachmentUrl = attachWithMultipart( defectUrl, a.getFileData() != null ? a.getFileData() : readFile( a.getFileName() ), a.getContentType(), a.getFileName().getName(), a.getDescription() );

                String attachmentUrl = attachWithOctetStream( defectUrl, a.getFileData() != null ? a.getFileData() : readFile( a.getFileName() ), a.getFileName().getName() );
                
                log.info( "Attachment Url: " + attachmentUrl );
            }
        }

        return defectUrl;
    }

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
            e.printStackTrace();
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

    public String entityFields( String entityType ) throws Exception
    {

        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put( "Content-Type", "application/xml" );
        requestHeaders.put( "Accept", "application/xml" );

        // As can be seen in the implementation below, creating an entity
        // is simply posting its xml into the correct collection.
        ALMResponse response = httpGet( buildProjectUrl( "customization/entities/" + entityType + "/fields" ), null, requestHeaders );

        Exception failure = response.getFailure();
        if ( failure != null )
        {
            throw failure;
        }

        return null;
    }

    public String createEntity( String collectionUrl, String postedEntityXml ) throws Exception
    {

        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put( "Content-Type", "application/xml" );
        requestHeaders.put( "Accept", "application/xml" );

        // As can be seen in the implementation below, creating an entity
        // is simply posting its xml into the correct collection.
        ALMResponse response = httpPost( collectionUrl, postedEntityXml.getBytes(), requestHeaders );

        Exception failure = response.getFailure();
        if ( failure != null )
        {
            throw failure;
        }

        /*
         * Note that we also get the xml of the newly created entity. at the
         * same time we get the url where it was created in a location response
         * header.
         */
        String entityUrl = response.getResponseHeaders().get( "Location" ).iterator().next();

        return entityUrl;
    }

    /**
     * @return true if logout successful
     * @throws Exception
     *             close session on server and clean session cookies on client
     */
    public boolean logout() throws Exception
    {

        // note the get operation logs us out by setting authentication cookies
        // to:
        // LWSSO_COOKIE_KEY="" via server response header Set-Cookie
        ALMResponse response = httpGet( buildUrl( "authentication-point/logout" ), null, null );

        return (response.getStatusCode() == HttpURLConnection.HTTP_OK);

    }

    /**
     * @return null if authenticated.<br>
     *         a url to authenticate against if not authenticated.
     * @throws Exception
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
