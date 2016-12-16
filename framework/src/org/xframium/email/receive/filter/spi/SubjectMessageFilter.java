package org.xframium.email.receive.filter.spi;

import javax.mail.Message;
import org.xframium.email.receive.filter.AbstractMessageFilter;

public class SubjectMessageFilter extends AbstractMessageFilter
{
    private String subjectValue;
    
    public SubjectMessageFilter( String subjectValue )
    {
        this.subjectValue = subjectValue;
    }
    
    @Override
    public boolean _acceptMessage( Message currentMessage ) throws Exception
    {
        return currentMessage.getSubject().matches( subjectValue );
    }
}
