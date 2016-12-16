package org.xframium.email.send;

import java.util.Map;
import org.xframium.email.AbstractEmailProvider;

public abstract class AbstractSendEmailProvider extends AbstractEmailProvider implements SendEmailProvider
{

    public abstract boolean _sendEmail( String fromAddress, String[] toAddress, String subjectLine, String emailBody, Map<String, String> propertyMap );
    
    @Override
    public boolean sendEmail( String fromAddress, String[] toAddress, String subjectLine, String emailBody, Map<String, String> propertyMap )
    {
        if ( log.isInfoEnabled() )
            log.info( "Sending email from " + fromAddress + " as " + subjectLine );
        
        return _sendEmail( fromAddress, toAddress, subjectLine, emailBody, propertyMap );
    }


    
}
