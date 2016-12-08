package org.xframium.email.receive;

import java.util.Map;
import javax.mail.Message;
import org.xframium.email.receive.filter.MessageFilter;

public interface ReceiveEmailProvider
{
    public MessageWrapper getEmail( String hostName, MessageFilter[] messageFilters, Map<String, String> propertyMap );
}
