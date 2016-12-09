package org.xframium.email.send;

import java.util.Map;
import org.xframium.email.EmailProvider;

public interface SendEmailProvider extends EmailProvider
{
    public boolean sendEmail(String fromAddress, String[] toAddress, String subjectLine, String emailBody, Map<String,String> propertyMap);
}
