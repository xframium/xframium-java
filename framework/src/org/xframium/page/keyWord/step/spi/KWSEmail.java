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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.email.EmailProviderFactory;
import org.xframium.email.receive.MessageWrapper;
import org.xframium.email.receive.ReceiveEmailProvider;
import org.xframium.email.receive.filter.MessageFilter;
import org.xframium.email.receive.filter.spi.AgeMessageFilter;
import org.xframium.email.receive.filter.spi.FromMessageFilter;
import org.xframium.email.receive.filter.spi.SubjectMessageFilter;
import org.xframium.email.send.SendEmailProvider;
import org.xframium.exception.ScriptException;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

public class KWSEmail extends AbstractKeyWordStep
{
    private static final String PROVIDER = "PROVIDER";
    private static final String FROM = "FROM";
    private static final String TO = "TO";
    private static final String SUBJECT = "SUBJECT";
    private static final String BODY = "BODY";
    private static final String HOST = "HOST";
    
    private static final String FILTER_AGE = "Filter By Age";
    private static final String FILTER_FROM = "Filter By From";
    private static final String FILTER_SUBJECT = "Filter By Subject";
    
    public KWSEmail()
    {
        kwName = "Send and Receive Email";
        kwDescription = "Allows the script send and check for received emails";
        kwHelp = "https://www.xframium.org/keyword.html#kw-email";
        orMapping = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com
     * .perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map,
     * java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC ) throws Exception
    {
        if ( getName().toUpperCase().equals( "SEND" ) )
        {
            SendEmailProvider sendProvider = null;
            KeyWordParameter providerName = getParameter( PROVIDER );
            if ( providerName != null )
                sendProvider = EmailProviderFactory.instance().getSendProvider( getParameterValue( providerName, contextMap, dataMap ) + "" );
            else
                sendProvider = EmailProviderFactory.instance().getSendProvider();
            
            Map<String,String> propertyMap = new HashMap<String,String>( 20 );
            for ( KeyWordParameter kP : getParameterList() )
            {
                if ( !PROVIDER.equals( kP.getName() ) && !FROM.equals( kP.getName() ) && !TO.equals( kP.getName() ) && !SUBJECT.equals( kP.getName() ) && !BODY.equals( kP.getName() ) )
                    propertyMap.put( kP.getName(), getParameterValue( kP, contextMap, dataMap ) + "" );
            }
            
            return sendProvider.sendEmail( getParameterValue( getParameter( FROM ), contextMap, dataMap ) + "", (getParameterValue( getParameter( TO ), contextMap, dataMap ) + "").split(","), getParameterValue( getParameter( SUBJECT ), contextMap, dataMap ) + "", getParameterValue( getParameter( BODY ), contextMap, dataMap ) + "", propertyMap );
        }
        else if ( getName().toUpperCase().equals( "RECEIVE" ) )
        {
            ReceiveEmailProvider receiveProvider = null;
            KeyWordParameter providerName = getParameter( PROVIDER );
            if ( providerName != null )
                receiveProvider = EmailProviderFactory.instance().getReceiveProvider( getParameterValue( providerName, contextMap, dataMap ) + "" );
            else
                receiveProvider = EmailProviderFactory.instance().getReceiveProvider();
            List<MessageFilter> messageFilter = new ArrayList<MessageFilter>( 10 );
            Map<String,String> propertyMap = new HashMap<String,String>( 20 );
            for ( KeyWordParameter kP : getParameterList() )
            {
                if ( !PROVIDER.equals( kP.getName() ) && !HOST.equals( kP.getName() ) )
                {
                    if ( FILTER_AGE.equals( kP.getName() ) )
                    {
                        messageFilter.add( new AgeMessageFilter( Integer.parseInt( getParameterValue( kP, contextMap, dataMap ) + "" ) ) );
                        continue;
                    }
                    
                    if ( FILTER_FROM.equals( kP.getName() ) )
                    {
                        messageFilter.add( new FromMessageFilter( getParameterValue( kP, contextMap, dataMap ) + "" ) );
                        continue;
                    }
                    
                    if ( FILTER_SUBJECT.equals( kP.getName() ) )
                    {
                        messageFilter.add( new SubjectMessageFilter( getParameterValue( kP, contextMap, dataMap ) + "" ) );
                        continue;
                    }
                    
                    propertyMap.put( kP.getName(), getParameterValue( kP, contextMap, dataMap ) + "" );
                }
            }
                        
            MessageWrapper messageWrapper = receiveProvider.getEmail( getParameterValue( getParameter( HOST ), contextMap, dataMap ) + "", messageFilter.toArray( new MessageFilter[ 0 ] ), propertyMap );
            
            if ( messageWrapper != null )
            {
                contextMap.put( getContext() + "_count", messageWrapper.getMessageCount() + "" );
                contextMap.put( getContext() + "_FROM", messageWrapper.getFrom() );
                contextMap.put( getContext() + "_SUBJECT", messageWrapper.getSubject() );
                contextMap.put( getContext() + "_BODY", messageWrapper.getBody() );
                contextMap.put( getContext() + "_MIMETYPE", messageWrapper.getMimeType() );
                
                if ( !validateData( messageWrapper.getBody() + "" ) )
                    throw new ScriptException( "EMAIL Body Expected a format of [" + getValidationType() + "(" + getValidation() + ") for [" + messageWrapper.getBody() + "]" );
                
            }
            else
            {
                return false;
            }
        }

        return true;
    }



}
