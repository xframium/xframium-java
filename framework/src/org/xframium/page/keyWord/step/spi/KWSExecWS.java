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
package org.xframium.page.keyWord.step.spi;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSExecJS.
 */
public class KWSExecWS extends AbstractKeyWordStep
{
    //
    // Constants
    //

    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";

    private static final String TOKEN_URL = "url";
    private static final String TOKEN_METHOD = "method";
    private static final String TOKEN_TYPE = "type";
    private static final String TOKEN_PAYLOAD = "payload";
    
    private static final String CONTENT_XML = "xml";
    private static final String CONTENT_JSON = "json";

    private static final String PATH_DIVIDER = "/";
    
    //
    // Class Data
    //

    private XPathFactory xPathFactory = XPathFactory.newInstance();
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
    {
        if ( pageObject == null )
        {
            throw new IllegalStateException( "Page Object was not defined" );
        }

        KeyWordParameter inputSpec = null;
        CallDetails callDetails = null;
        KeyWordParameter outputSpec = null;
        ResponceDetails responceDetails = null;
                        
        if ( getParameterList().size() >= 2 )
        {
            inputSpec = getParameterList().get( 0 );
            callDetails = loadCallDetails( inputSpec );
            if ( !callDetails.valid )
            {
                throwCallDetailsException( callDetails );
            }
            
            outputSpec = getParameterList().get( 1 );

            responceDetails = loadResponceDetails( outputSpec );

            try
            {
                Responce result = makeCall( callDetails );
                
                if ( getContext() != null )
                {
                    processResult( result, responceDetails, contextMap );
                }
            }
            catch( Throwable e )
            {
                throw new IllegalStateException( "KWSExecWS failed with:", e );
            }
        }
        else
        {
            throw new IllegalStateException( "KWSExecWS requires two tokenized parameters" );
        }
		
        return true;
    }
	
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
     */
    public boolean isRecordable()
    {
        return false;
    }

    //
    // Helpers
    //

    private CallDetails loadCallDetails( KeyWordParameter inputSpec )
    {
        CallDetails rtn = new CallDetails();

        Iterator<KeyWordToken> tokens = inputSpec.getTokenList().iterator();
        while( tokens.hasNext() )
        {
            KeyWordToken token = tokens.next();

            switch( token.getName() )
            {
                case TOKEN_URL:
                {
                    rtn.url = token.getValue();
                    break;
                }

                case TOKEN_METHOD:
                {
                    rtn.method = token.getValue();
                    break;
                }

                case TOKEN_TYPE:
                {
                    rtn.type = token.getValue();
                    break;
                }

                case TOKEN_PAYLOAD:
                {
                    rtn.pathToPayload = token.getValue();
                    break;
                }

                default:
                {
                    CallParameter param = new CallParameter();
                    
                    param.name = token.getName();
                    param.value = token.getValue();
                    rtn.parameters.add( param );
                    
                    break;
                }
            }
        }

        rtn.valid = (( rtn.url != null ) &&
                     ( rtn.method != null ) &&
                     ( rtn.type != null ) &&
                     (( !rtn.method.equalsIgnoreCase( HTTP_POST )) ||
                      (( rtn.method.equalsIgnoreCase( HTTP_POST )) && ( rtn.pathToPayload != null ))));
        
        return rtn;
    }

    private void throwCallDetailsException( CallDetails callDetails )
    {
        String errorMsg = null;
        
        if ( callDetails.url == null )
            errorMsg = "URL token is missing";
        else if ( callDetails.method == null )
            errorMsg = "Method token is missing";
        else if ( callDetails.type == null )
            errorMsg = "Call type token is missing";
        else if (( callDetails.method.equalsIgnoreCase( HTTP_POST ) ) &&
                 ( callDetails.pathToPayload == null ))
            errorMsg = "Payload token is missing for a call of type post";                          

        throw new IllegalStateException( errorMsg );
    }

    private ResponceDetails loadResponceDetails( KeyWordParameter outputSpec )
    {
        ResponceDetails rtn = new ResponceDetails();

        Iterator<KeyWordToken> tokens = outputSpec.getTokenList().iterator();
        while( tokens.hasNext() )
        {
            KeyWordToken token = tokens.next();

            if ( TOKEN_TYPE.equalsIgnoreCase( token.getName() ))
            {
                rtn.type = token.getValue();
            }
            else
            {
                ResponceVariable var = new ResponceVariable();

                var.name = token.getName();
                var.path = token.getValue();

                rtn.parameters.add( var );
            }
        }
        
        return rtn;
    }

    private Responce makeCall( CallDetails callDetails )
        throws Exception
    {
        Responce rtn = new Responce();

        HttpURLConnection connection = null;
        String targetURL = buildURL( callDetails );
        
        try
        {
            URL obj = new URL( targetURL );
            
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod( HTTP_GET );
            con.setRequestProperty("User-Agent", "Java Program");
            
            int responseCode = con.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK ) // success
            { 
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                
                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                
                in.close();
                
		rtn.payload = response.toString();
            }
        }        
        finally
        {
            if(connection != null)
            {
                connection.disconnect(); 
            }
        }

        return rtn;
    }

    private static String buildURL( CallDetails callDetails )
    {
        StringBuilder rtn = new StringBuilder();

        rtn.append( callDetails.url );
        rtn.append( PATH_DIVIDER );

        Iterator<CallParameter> params = callDetails.parameters.iterator();
        while( params.hasNext() )
        {
            CallParameter param = params.next();

            rtn.append( param.name );
            rtn.append( PATH_DIVIDER );
            rtn.append( param.value );
        }

        return rtn.toString();
    }

    private void processResult( Responce result, ResponceDetails responceDetails, Map<String, Object> contextMap )
        throws Exception
    {
        if ( CONTENT_XML.equalsIgnoreCase( responceDetails.type ))
        {
            //
            // In this case, result.payload is an XML document and the paths in responceDetails.parameters are XPATH
            // expressions
            //

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse( new ByteArrayInputStream( result.payload.getBytes() ) );
            XPath xPath = xPathFactory.newXPath();

            Iterator<ResponceVariable> params = responceDetails.parameters.iterator();
            while( params.hasNext() )
            {
                ResponceVariable param = params.next();

                Node node = (Node) xPath.evaluate( param.path, document, XPathConstants.NODE );

                String context_name = getContext() + "_" + param.name;
                String value = node.getTextContent();

                if ( log.isDebugEnabled() )
                    log.debug( "Setting Context Data to [" + value + "] for [" + context_name + "]" );

                contextMap.put( context_name, value );
            }
        }
    }

    private class CallDetails
    {
        public String url = null;
        public String method = null;
        public String type = null;
        public ArrayList<CallParameter> parameters = new ArrayList<CallParameter>();
        public String pathToPayload = null;

        public boolean valid = false;
    }

    private class CallParameter
    {
        public String name = null;
        public String value = null;
    }

    private class Responce
    {
        String payload = null;
    }

    private class ResponceDetails
    {
        public String type = null;
        public ArrayList<ResponceVariable> parameters = new ArrayList<ResponceVariable>();
    }

    private class ResponceVariable
    {
        public String name = null;
        public String path = null;
    }
}
