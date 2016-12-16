package org.xframium.email.receive.filter;

import javax.mail.Message;

public interface MessageFilter
{
    public boolean acceptMessage( Message currentMessage ); 
}
