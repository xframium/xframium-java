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

import java.util.*;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

import org.xframium.utility.WebServiceClientUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSExecJS.
 */
public class KWSExecWS extends AbstractKeyWordStep
{
    //
    // Constants
    //
    public KWSExecWS()
    {
        kwName = "Invoke WebService";
        kwDescription = "Allows the script to call an existing WebService and analyze the results";
        kwHelp = "https://www.xframium.org/keyword.html#kw-execws";
    }
    
    private static final String HTTP_GET = "GET";
    private static final String HTTP_DELETE = "DELETE";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_PUT = "PUT";

    private static final String TOKEN_URL = "url";
    private static final String TOKEN_METHOD = "method";
    private static final String TOKEN_TYPE = "type";
    private static final String TOKEN_PAYLOAD = "payload";
    private static final String TOKEN_MEDIA_TYPE = "media-type";
    private static final String TOKEN_UNAME = "username";
    private static final String TOKEN_PWD = "password";
    
    private static final String CONTENT_XML = "xml";
    private static final String CONTENT_JSON = "json";

    private static final String INPUT = "input";
    private static final String OUTPUT = "output";

    private static final String PATH_DIVIDER = "/";
    
    //
    // Implementation
    //

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

        WebServiceClientUtil.CallDetails callDetails = null;
        WebServiceClientUtil.ResponceDetails responceDetails = null;
                        
        callDetails = loadCallDetails( getParameterList(), contextMap, dataMap );
        if ( !callDetails.isValid() )
        {
            throwCallDetailsException( callDetails );
        }
        
        responceDetails = loadResponceDetails( getParameterList() );

        try
        {
            Map<String, String> data = WebServiceClientUtil.callWebService( callDetails, responceDetails );

            if (( getContext() != null ) &&
                ( data.size() > 0 ))
            {
                Iterator<String> attrs = data.keySet().iterator();
                while( attrs.hasNext() )
                {
                    String attr = attrs.next();
                    String value = data.get( attr );

                    String context_name = getContext() + "_" + attr;

                    if ( log.isDebugEnabled() )
                        log.debug( "Setting Context Data to [" + value + "] for [" + context_name + "]" );

                    contextMap.put( context_name, value );
                }
            }
        }
        catch( Throwable e )
        {
            e.printStackTrace();
            
            throw new IllegalStateException( "KWSExecWS failed with:", e );
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

    private WebServiceClientUtil.CallDetails loadCallDetails( List<KeyWordParameter> paramList, Map<String, Object> contextMap, Map<String, PageData> dataMap )
    {
        WebServiceClientUtil.CallDetails rtn = new WebServiceClientUtil.CallDetails();

        Iterator<KeyWordParameter> params = paramList.iterator();
        while( params.hasNext() )
        {
            KeyWordParameter param = params.next();

            if (( param.getUsage() == null ) ||
                ( !INPUT.equalsIgnoreCase( param.getUsage() )))
            {
                continue;
            }

            switch( param.getName() )
            {
                case TOKEN_URL:
                {
                    rtn.setUrl( param.getValue() );
                    break;
                }

                case TOKEN_METHOD:
                {
                    rtn.setMethod( param.getValue() );
                    break;
                }

                case TOKEN_TYPE:
                {
                    rtn.setType( param.getValue() );
                    break;
                }

                case TOKEN_MEDIA_TYPE:
                {
                    rtn.setMediaType( param.getValue() );
                    break;
                }

                case TOKEN_UNAME:
                {
                    rtn.setUsername( param.getValue() );
                    break;
                }

                case TOKEN_PWD:
                {
                    rtn.setPassword( param.getValue() );
                    break;
                }

                case TOKEN_PAYLOAD:
                {
                    rtn.setPayload( (String) getParameterValue( param, contextMap, dataMap ));
                    break;
                }

                default:
                {
                    WebServiceClientUtil.CallParameter cparam = new WebServiceClientUtil.CallParameter();
                    
                    cparam.setName( param.getName() );
                    cparam.setValue( param.getValue() );
                    rtn.getParameters().add( cparam );
                    
                    break;
                }
            }
        }

        rtn.setValid((( rtn.getUrl() != null ) &&
                      ( rtn.getMethod() != null ) &&
                      ( rtn.getType() != null ) &&
                      (( !rtn.getMethod().equalsIgnoreCase( HTTP_POST )) ||
                       (( rtn.getMethod().equalsIgnoreCase( HTTP_POST )) && ( rtn.getPayload() != null ) && ( rtn.getMediaType() != null )))));
        
        return rtn;
    }

    private void throwCallDetailsException( WebServiceClientUtil.CallDetails callDetails )
    {
        String errorMsg = null;
        
        if ( callDetails.getUrl() == null )
            errorMsg = "URL token is missing";
        else if ( callDetails.getMethod() == null )
            errorMsg = "Method token is missing";
        else if ( callDetails.getType() == null )
            errorMsg = "Call type token is missing";
        else if (( callDetails.getMethod().equalsIgnoreCase( HTTP_POST ) ) &&
                 (( callDetails.getPayload() == null ) ||
                  ( callDetails.getMediaType() == null )))
            errorMsg = "Payload or media type token is missing for a call of type post";                          

        throw new IllegalStateException( errorMsg );
    }

    private WebServiceClientUtil.ResponceDetails loadResponceDetails( List<KeyWordParameter> paramList )
    {
        WebServiceClientUtil.ResponceDetails rtn = new WebServiceClientUtil.ResponceDetails();

        Iterator<KeyWordParameter> params = paramList.iterator();
        while( params.hasNext() )
        {
            KeyWordParameter param = params.next();

            if (( param.getUsage() == null ) ||
                ( !OUTPUT.equalsIgnoreCase( param.getUsage() )))
            {
                continue;
            }

            if ( TOKEN_TYPE.equalsIgnoreCase( param.getName() ))
            {
                rtn.setType( param.getValue() );
            }
            else
            {
                WebServiceClientUtil.ResponceVariable var = new WebServiceClientUtil.ResponceVariable();

                var.setName( param.getName() );
                var.setPath( param.getValue() );

                rtn.getParameters().add( var );
            }
        }
        
        return rtn;
    }
}
