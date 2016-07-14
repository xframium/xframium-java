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

import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

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
                        
        if ( getParameterList().size() >= 2 )
        {
            inputSpec = getParameterList().get( 0 );
            callDetails = loadCallDetails( inputSpec );
            if ( !callDetails.valid )
            {
                throwCallDetailsException( callDetails );
            }
            
            outputSpec = getParameterList().get( 1 );
        }
        else
        {
            throw new IllegalStateException( "KWSExecWS requires two tokenized parameters" );
        }
		
        if ( getContext() != null )
        {

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
                case "url":
                {
                    rtn.url = token.getValue();
                    break;
                }

                case "method":
                {
                    rtn.method = token.getValue();
                    break;
                }

                case "type":
                {
                    rtn.type = token.getValue();
                    break;
                }

                case "payload":
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

            rtn.valid = (( rtn.url != null ) &&
                         ( rtn.method != null ) &&
                         ( rtn.type != null ) &&
                         (( !rtn.method.equalsIgnoreCase( "POST" )) ||
                          (( rtn.method.equalsIgnoreCase( "POST" )) && ( rtn.pathToPayload != null ))));
        }

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
        else if (( callDetails.method.equalsIgnoreCase( "POST" ) ) &&
                 ( callDetails.pathToPayload == null ))
            errorMsg = "Payload token is missing for a call of type post";                          

        throw new IllegalStateException( errorMsg );
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
