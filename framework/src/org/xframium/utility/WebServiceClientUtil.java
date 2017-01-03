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
package org.xframium.utility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class WebServiceClientUtil
{
    private static Log log = LogFactory.getLog( WebServiceClientUtil.class );
    //
    // Constants
    //

    private static final String HTTP_GET = "GET";
    private static final String HTTP_DELETE = "DELETE";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_PUT = "PUT";

    private static final String CONTENT_XML = "xml";
    private static final String CONTENT_JSON = "json";

    private static final String PATH_DIVIDER = "/";

    //
    // Class Data
    //

    private static XPathFactory xPathFactory = XPathFactory.newInstance();
    private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    //
    // Behaviors
    //

    public static Map<String, String> callWebService( CallDetails callDetails, ResponceDetails responceDetails )
    {
        Map<String, String> rtn = new HashMap<String, String>();

        try
        {
            Responce result = makeCall( callDetails );
            rtn.put( "_PAYLOAD", result.getPayload() );
            processResult( result, responceDetails, rtn );
        }
        catch ( Throwable e )
        {
            throw new IllegalStateException( "Call failed with:", e );
        }

        return rtn;
    }

    //
    // Data Structures
    //

    public static class CallDetails
    {
        private String url = null;
        private String method = null;
        private String media_type = null;
        private String type = null;
        private String username = null;
        private String password = null;
        private ArrayList<CallParameter> parameters = new ArrayList<CallParameter>();
        private String payload = null;
        private boolean valid = false;
        private HashMap<String, String> headers = new HashMap<String, String>();

        public CallDetails()
        {
        }

        public CallDetails( String url, String method, String media_type, String type, String username, String password, String payload, boolean valid )
        {
            super();
            this.url = url;
            this.method = method;
            this.media_type = media_type;
            this.type = type;
            this.username = username;
            this.password = password;
            this.payload = payload;
            this.valid = valid;
        }

        public String getUrl()
        {
            return url;
        }

        public String getMethod()
        {
            return method;
        }

        public String getMediaType()
        {
            return media_type;
        }

        public String getType()
        {
            return type;
        }

        public String getUsername()
        {
            return username;
        }

        public String getPassword()
        {
            return password;
        }

        public ArrayList<CallParameter> getParameters()
        {
            return parameters;
        }

        public String getPayload()
        {
            return payload;
        }

        public boolean isValid()
        {
            return valid;
        }

        public void setUrl( String url )
        {
            this.url = url;
        }

        public void setMethod( String method )
        {
            this.method = method;
        }

        public void setMediaType( String media_type )
        {
            this.media_type = media_type;
        }

        public void setType( String type )
        {
            this.type = type;
        }

        public void setUsername( String username )
        {
            this.username = username;
        }

        public void setPassword( String password )
        {
            this.password = password;
        }

        public void setParameters( ArrayList<CallParameter> parameters )
        {
            this.parameters = parameters;
        }

        public void setPayload( String payload )
        {
            this.payload = payload;
        }

        public void setValid( boolean valid )
        {
            this.valid = valid;
        }

        public HashMap<String, String> getHeaders()
        {
            return headers;
        }
    }

    public static class CallParameter
    {
        private String name = null;
        private String value = null;

        public CallParameter()
        {
        }

        public CallParameter( String name, String value )
        {
            super();
            this.name = name;
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public String getValue()
        {
            return value;
        }

        public void setName( String name )
        {
            this.name = name;
        }

        public void setValue( String value )
        {
            this.value = value;
        }
    }

    public static class Responce
    {
        private String payload = null;

        public Responce()
        {
        }

        public Responce( String payload )
        {
            super();
            this.payload = payload;
            
            if ( log.isInfoEnabled() )
                log.info( "Return Payload " + payload );
            
        }

        public String getPayload()
        {
            return payload;
        }

        public void setPayload( String payload )
        {
            this.payload = payload;
        }
    }

    public static class ResponceDetails
    {
        private String type = null;
        private ArrayList<ResponceVariable> parameters = new ArrayList<ResponceVariable>();

        public ResponceDetails()
        {
        }

        public ResponceDetails( String type )
        {
            super();
            this.type = type;
        }

        public String getType()
        {
            return type;
        }

        public void setType( String type )
        {
            this.type = type;
        }

        public ArrayList<ResponceVariable> getParameters()
        {
            return parameters;
        }
    }

    public static class ResponceVariable
    {
        private String name = null;
        private String path = null;

        public ResponceVariable()
        {
        }

        public ResponceVariable( String name, String path )
        {
            super();
            this.name = name;
            this.path = path;
        }

        public String getName()
        {
            return name;
        }

        public String getPath()
        {
            return path;
        }

        public void setName( String name )
        {
            this.name = name;
        }

        public void setPath( String path )
        {
            this.path = path;
        }

    }

    //
    // Helpers
    //

    private static Responce makeCall( CallDetails callDetails ) throws Exception
    {
        Responce rtn = null;

        String targetURL = buildURL( callDetails );

        HttpURLConnection con = null;

        try
        {
            if ( log.isInfoEnabled() )
                log.info( "Executing " + callDetails.method + " WebService call to " + targetURL );
            
            URL obj = new URL( targetURL );

            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod( callDetails.method );
            con.setRequestProperty( "User-Agent", "xFramium" );

            if ( callDetails.headers.size() > 0 )
            {
                Iterator<String> names = callDetails.headers.keySet().iterator();
                while ( names.hasNext() )
                {
                    String name = names.next();
                    String value = callDetails.headers.get( name );

                    con.setRequestProperty( name, value );
                }
            }

            setBasicAuthIfRequested( con, callDetails );

            if ( (HTTP_GET.equalsIgnoreCase( callDetails.method )) || (HTTP_DELETE.equalsIgnoreCase( callDetails.method )) )
            {
                int responseCode = con.getResponseCode();

                if ( responseCode == HttpURLConnection.HTTP_OK ) // success
                {
                    BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ( (inputLine = in.readLine()) != null )
                    {
                        response.append( inputLine );
                    }

                    in.close();

                    rtn = new Responce( response.toString() );
                }
            }
            else if ( (HTTP_POST.equals( callDetails.method )) || (HTTP_PUT.equals( callDetails.method )) )
            {
                con.setDoOutput( true );
                con.setRequestProperty( "Content-Type", callDetails.media_type );

                if ( log.isInfoEnabled() )
                    log.info( "Payload is " + callDetails.payload );
                
                OutputStream os = con.getOutputStream();
                os.write( callDetails.payload.getBytes() );
                os.flush();

                int responseCode = con.getResponseCode();

                if ( responseCode == HttpURLConnection.HTTP_OK ) // success
                {
                    BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ( (inputLine = in.readLine()) != null )
                    {
                        response.append( inputLine ).append( "\n" );
                    }

                    in.close();
                    os.close();

                    rtn = new Responce( response.toString() );
                }
                else
                {
                    BufferedReader in = new BufferedReader( new InputStreamReader( con.getErrorStream() ) );
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ( (inputLine = in.readLine()) != null )
                    {
                        response.append( inputLine ).append( "\n" );
                    }

                    in.close();
                    os.close();

                    throw new ScriptException( response.toString() );
                }
            }
        }
        finally
        {
            if ( con != null )
            {
                con.disconnect();
            }
        }

        return rtn;
    }

    private static String buildURL( CallDetails callDetails )
    {
        StringBuilder rtn = new StringBuilder();

        rtn.append( callDetails.url );

        if ( callDetails.parameters.size() > 0 )
        {
            rtn.append( PATH_DIVIDER );

            Iterator<CallParameter> params = callDetails.parameters.iterator();
            while ( params.hasNext() )
            {
                CallParameter param = params.next();

                rtn.append( param.name );
                rtn.append( PATH_DIVIDER );
                rtn.append( param.value );
            }
        }

        return rtn.toString();
    }

    private static void processResult( Responce result, ResponceDetails responceDetails, Map<String, String> contextMap ) throws Exception
    {
        if ( CONTENT_XML.equalsIgnoreCase( responceDetails.type ) )
        {
            //
            // In this case, result.payload is an XML document and the paths in
            // responceDetails.parameters are XPATH
            // expressions
            //

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse( new ByteArrayInputStream( result.payload.getBytes() ) );
            XPath xPath = xPathFactory.newXPath();

            Iterator<ResponceVariable> params = responceDetails.parameters.iterator();
            while ( params.hasNext() )
            {
                ResponceVariable param = params.next();

                Node node = (Node) xPath.evaluate( param.path, document, XPathConstants.NODE );

                if ( node == null )
                {
                    throw new ScriptConfigurationException( "No result found. Check xpath expression " + param.path );
                }

                String value = node.getTextContent();

                contextMap.put( param.name, value );
            }
        }
        else if ( CONTENT_JSON.equalsIgnoreCase( responceDetails.type ) )
        {
            //
            // In this case, result.payload is an JSON document and the paths in
            // responceDetails.parameters are JASONPATH
            // (https://github.com/jayway/JsonPath) expressions
            //

            DocumentContext ctx = JsonPath.parse( result.payload );

            Iterator<ResponceVariable> params = responceDetails.parameters.iterator();
            while ( params.hasNext() )
            {
                ResponceVariable param = params.next();

               /* String value = ctx.read( param.path );

                contextMap.put( param.name, value );*/
                
                JSONArray value = (JSONArray)ctx.read( param.path );
                
                contextMap.put( param.name, value.toJSONString() );
            }
        }
    }

    private static void setBasicAuthIfRequested( HttpURLConnection con, CallDetails callDetails )
    {
        if ( (callDetails.username != null) && (callDetails.password != null) )
        {
            String userpass = callDetails.username + ":" + callDetails.password;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary( userpass.getBytes() );

            con.setRequestProperty( "Authorization", basicAuth );
        }
    }
}
