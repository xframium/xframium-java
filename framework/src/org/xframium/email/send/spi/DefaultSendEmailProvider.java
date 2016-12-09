package org.xframium.email.send.spi;

import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.xframium.email.send.AbstractSendEmailProvider;

public class DefaultSendEmailProvider extends AbstractSendEmailProvider
{
    private static final String USER_NAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    public boolean _sendEmail(String fromAddress, String[] toAddress, String subjectLine, String emailBody, final Map<String,String> propertyMap ) 
    {
        Properties mailProps = new Properties();
        
        mailProps.putAll( propertyMap );
        
        try
        {
            Session serverSession = null;
            
            if ( propertyMap.containsKey( USER_NAME ) )
            {
                serverSession = Session.getInstance( mailProps,
                        new javax.mail.Authenticator() {
                          protected PasswordAuthentication getPasswordAuthentication() {
                              return new PasswordAuthentication(propertyMap.get( USER_NAME ), propertyMap.get( PASSWORD ) );
                          }
                        });
            }
            else
                serverSession = Session.getDefaultInstance(mailProps);
            
            MimeMessage newMessage = new MimeMessage( serverSession );
            
            newMessage.setFrom( new InternetAddress( fromAddress ) );
            
            for ( String to : toAddress )
                newMessage.addRecipient(Message.RecipientType.TO, new InternetAddress( to ) );
            
            newMessage.setSubject(subjectLine);
            newMessage.setText(emailBody);
            Transport.send(newMessage);
            return true;
            
        }
        catch( Exception e )
        {
            log.error( "Error sending email message", e );
            return false;
        }
    }


    
    
}
