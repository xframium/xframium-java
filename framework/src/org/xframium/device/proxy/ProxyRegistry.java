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
/*
 * 
 */
package org.xframium.device.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class proxyRegistry.
 */
public class ProxyRegistry
{
    private Log log = LogFactory.getLog( ProxyRegistry.class );
    /** The singleton. */
    private static ProxyRegistry singleton = new ProxyRegistry();

    /**
     * Instance.
     *
     * @return the proxy registry
     */
    public static ProxyRegistry instance()
    {
        return singleton;
    }

    /**
     * Instantiates a new proxy registry.
     */
    private ProxyRegistry()
    {

    }

    /** The log. */
    // private Log log = LogFactory.getLog( ProxyRegistry.class );

    private String proxyHost;
    private String proxyPort;
    private String ignoreHost;
    private String proxyUser;
    private String proxyPassword;
    private Class proxyAuthenticator;

    public String getIgnoreHost()
    {
        return ignoreHost;
    }

    public void setIgnoreHost( String ignoreHost )
    {
        this.ignoreHost = ignoreHost;
    }

    public String getProxyHost()
    {
        return proxyHost;
    }

    public void setProxyHost( String proxyHost )
    {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort()
    {
        return proxyPort;
    }

    public void setProxyPort( String proxyPort )
    {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser()
    {
        return proxyUser;
    }

    public void setProxyUser( String proxyUser )
    {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword()
    {
        return proxyPassword;
    }

    public void setProxyPassword( String proxyPassword )
    {
        this.proxyPassword = proxyPassword;
    }

    public Class getProxyAuthenticator()
    {
        return proxyAuthenticator;
    }

    public void setProxyAuthenticator( Class proxyAuthenticator )
    {
        this.proxyAuthenticator = proxyAuthenticator;
    }
    
    public void registerAuthenticator()
    {
        Authenticator newAuth = null;
        if ( proxyAuthenticator != null )
        {
            try
            {
                log.info( "Attempting to create Authenticator as " + proxyAuthenticator.getName() );
                newAuth = (Authenticator) proxyAuthenticator.newInstance();
            }
            catch( Exception e )
            {
                log.error( "Error creating instance of " + proxyAuthenticator.getName() + " using default constructor", e );
            }
        }
        
        if ( newAuth == null )
        {
            if ( proxyUser != null && proxyPassword != null )
            {
                log.info( "Creating default authenticator instance" );
                newAuth = new DefaultAuthenticator();
            }
        }
        
        if ( newAuth != null )
            Authenticator.setDefault( newAuth );
        else
            log.info( "No proxy defined" );
    }
    
    private class DefaultAuthenticator extends Authenticator
    {
        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication( proxyUser, proxyPassword.toCharArray() );
        }
    }

}
