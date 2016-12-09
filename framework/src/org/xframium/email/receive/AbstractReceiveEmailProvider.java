package org.xframium.email.receive;

import java.util.Map;
import javax.mail.Message;
import org.xframium.email.AbstractEmailProvider;
import org.xframium.email.receive.filter.MessageFilter;

public abstract class AbstractReceiveEmailProvider extends AbstractEmailProvider implements ReceiveEmailProvider
{
    public abstract MessageWrapper _getEmail( String hostName, MessageFilter[] messageFilters, Map<String, String> propertyMap );
    
    @Override
    public MessageWrapper getEmail( String hostName, MessageFilter[] messageFilters, Map<String, String> propertyMap )
    {
        if ( log.isInfoEnabled() )
            log.info( "Attempting to retreive email from " + hostName );
        return _getEmail( hostName, messageFilters, propertyMap );
    }
}
