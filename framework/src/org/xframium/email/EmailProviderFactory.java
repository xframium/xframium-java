package org.xframium.email;

import java.util.HashMap;
import java.util.Map;
import org.xframium.email.receive.ReceiveEmailProvider;
import org.xframium.email.receive.spi.DefaultReceiveEmailProvider;
import org.xframium.email.send.SendEmailProvider;
import org.xframium.email.send.spi.DefaultSendEmailProvider;

public class EmailProviderFactory
{
    private static final String DEFAULT = "DEFAULT";
    private static final EmailProviderFactory singleton = new EmailProviderFactory();
    
    private EmailProviderFactory()
    {
        registerSendProvider( DEFAULT, new DefaultSendEmailProvider() );
        registerReceiveProvider( DEFAULT, new DefaultReceiveEmailProvider() );
    }
    
    public static EmailProviderFactory instance()
    {
        return singleton;
    }

    private Map<String,SendEmailProvider> sendMap = new HashMap<String,SendEmailProvider>( 10 );
    private Map<String,ReceiveEmailProvider> receiveMap = new HashMap<String,ReceiveEmailProvider>( 10 );
    
    public SendEmailProvider getSendProvider()
    {
        return getSendProvider( DEFAULT );
    }
    
    public ReceiveEmailProvider getReceiveProvider()
    {
        return getReceiveProvider( DEFAULT );
    }
    
    public SendEmailProvider getSendProvider( String providerName )
    {
        return sendMap.get( providerName );
    }
    
    public ReceiveEmailProvider getReceiveProvider( String providerName )
    {
        return receiveMap.get( providerName );
    }
    
    public void registerSendProvider( String providerName, SendEmailProvider sendProvider )
    {
        sendMap.put( providerName, sendProvider );
    }
    
    public void registerReceiveProvider( String providerName, ReceiveEmailProvider receiveProvider )
    {
        receiveMap.put( providerName, receiveProvider );
    }
    
}
