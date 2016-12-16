package org.xframium.email.receive.spi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import org.xframium.email.receive.AbstractReceiveEmailProvider;
import org.xframium.email.receive.MessageWrapper;
import org.xframium.email.receive.filter.MessageFilter;
import org.xframium.exception.ScriptException;

public class DefaultReceiveEmailProvider extends AbstractReceiveEmailProvider
{
    private static final String USER_NAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String PROTOCOL = "PROTOCOL";
    private static final String FOLDER_NAME = "FOLDER_NAME";
    private static final String DEFAULT_FOLDER_NAME = "INBOX";
    private static final int MAX_MESSAGES = 50;

    public static void main( String[] args )
    {
        DefaultReceiveEmailProvider x = new DefaultReceiveEmailProvider();

        Map<String, String> propertyMap = new HashMap<String, String>( 20 );

        propertyMap.put( "mail.pop3.host", "pop.gmail.com" );
        propertyMap.put( "mail.pop3.port", "995" );
        propertyMap.put( "mail.pop3.starttls.enable", "true" );
        propertyMap.put( USER_NAME, "nolan.a.geary@gmail.com" );
        propertyMap.put( PASSWORD, "bD046879" );
        propertyMap.put( PROTOCOL, "pop3s" );

        x.getEmail( "pop.gmail.com", null, propertyMap );
    }

    public MessageWrapper _getEmail( String hostName, MessageFilter[] messageFilters, Map<String, String> propertyMap )
    {
        Properties mailProps = new Properties();
        mailProps.putAll( propertyMap );

        List<Message> messageList = new ArrayList<Message>( 10 );
        MessageWrapper messageWrapper = null;

        try
        {
            Session emailSession = Session.getDefaultInstance( mailProps );
            Store store = emailSession.getStore( propertyMap.get( PROTOCOL ) );
            store.connect( hostName, propertyMap.get( USER_NAME ), propertyMap.get( PASSWORD ) );

            Folder emailFolder = store.getFolder( propertyMap.get( FOLDER_NAME ) != null ? propertyMap.get( FOLDER_NAME ) : DEFAULT_FOLDER_NAME );
            emailFolder.open( Folder.READ_WRITE );

            Message[] messages = emailFolder.getMessages();

            int messageCount = emailFolder.getMessageCount();
            if ( messageCount > MAX_MESSAGES )
                messageCount = MAX_MESSAGES;

            if ( log.isInfoEnabled() )
                log.info( "Processing " + messageCount + " messages" );

            for ( int i = 0; i < messageCount; i++ )
            {
                System.out.println( messages[i].getSubject() );
                if ( applyFilters( messages[i], messageFilters ) )
                {
                    messageList.add( messages[i] );
                }
            }

            if ( messageList.size() == 0 )
                throw new ScriptException( "Failed to find any email messages that met the criteria" );
                
            Collections.sort( messageList, new DateComparator() );

            String messageContent = null;
            String contentType = messageList.get( 0 ).getContentType();

            if ( contentType.startsWith( "text/" ) )
                messageContent = messageList.get( 0 ).getContent().toString();
            else if ( messageList.get( 0 ).isMimeType( "multipart/*" ) )
                messageContent = getTextFromMimeMultipart( (MimeMultipart) messageList.get( 0 ).getContent() );
            else
                messageContent = messageList.get( 0 ).getContent().getClass().getName();

            messageWrapper = new MessageWrapper( messageList.size(), messageList.get( 0 ).getFrom()[0].toString(), messageList.get( 0 ).getSubject(), messageContent, contentType );

            emailFolder.close( false );
            store.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new ScriptException( "Failed to find email" );
        }

        return messageWrapper;
    }

    private String getTextFromMimeMultipart( MimeMultipart mimeMultipart ) throws IOException, MessagingException
    {

        int count = mimeMultipart.getCount();
        if ( count == 0 )
            throw new MessagingException( "Multipart with no body parts not supported." );

        boolean multipartAlt = new ContentType( mimeMultipart.getContentType() ).match( "multipart/alternative" );
        if ( multipartAlt )
            return getTextFromBodyPart( mimeMultipart.getBodyPart( count - 1 ) );

        String result = "";
        for ( int i = 0; i < count; i++ )
        {
            BodyPart bodyPart = mimeMultipart.getBodyPart( i );
            result += getTextFromBodyPart( bodyPart );
        }
        return result;
    }

    private String getTextFromBodyPart( BodyPart bodyPart ) throws IOException, MessagingException
    {

        String result = "";
        if ( bodyPart.getContentType().startsWith( "text/" ) )
        {
            result = (String) bodyPart.getContent();
        }
        else if ( bodyPart.getContent() instanceof MimeMultipart )
        {
            result = getTextFromMimeMultipart( (MimeMultipart) bodyPart.getContent() );
        }

        return result;
    }

    private class DateComparator implements Comparator<Message>
    {
        @Override
        public int compare( Message o1, Message o2 )
        {
            try
            {
                if ( o1.getReceivedDate() != null && o2.getReceivedDate() != null )
                {
                    if ( o1.getReceivedDate().getTime() > o2.getReceivedDate().getTime() )
                        return -1;
                    else if ( o1.getReceivedDate().getTime() < o2.getReceivedDate().getTime() )
                        return 1;
                }
                else
                {
                    if ( o1.getSentDate().getTime() > o2.getSentDate().getTime() )
                        return -1;
                    else if ( o1.getSentDate().getTime() < o2.getSentDate().getTime() )
                        return 1;
                }

                return 0;
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                return 0;
            }
        }
    }

    private boolean applyFilters( Message currentMessage, MessageFilter[] filterList )
    {
        if ( filterList == null || filterList.length == 0 )
            return true;

        for ( MessageFilter mFilter : filterList )
        {
            if ( !mFilter.acceptMessage( currentMessage ) )
                return false;

        }

        return true;
    }
}
