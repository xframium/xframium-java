package org.xframium.email.receive.filter.spi;

import javax.mail.Message;
import org.xframium.email.receive.filter.AbstractMessageFilter;

public class FromMessageFilter extends AbstractMessageFilter
{
    private String fromValue;
    
    public FromMessageFilter( String fromValue )
    {
        this.fromValue = fromValue.trim();
    }
    
    @Override
    public boolean _acceptMessage( Message currentMessage ) throws Exception
    {
        return currentMessage.getFrom()[ 0 ].toString().contains( fromValue );
    }
}
