package org.xframium.email.receive.filter.spi;

import java.util.concurrent.TimeUnit;
import javax.mail.Message;
import org.xframium.email.receive.filter.AbstractMessageFilter;

public class AgeMessageFilter extends AbstractMessageFilter
{
    private long messageAge;
    
    public AgeMessageFilter( long messageAge, TimeUnit timeUnit )
    {
        
        switch( timeUnit )
        {
            case DAYS:
                this.messageAge = messageAge * 60 * 24;
                break;
                
            case HOURS:
                this.messageAge = messageAge * 60 * 60;
                break;
                
            case MILLISECONDS:
                this.messageAge = messageAge / 1000;
                break;
                
            case MINUTES:
                this.messageAge = messageAge * 60;
                break;
                
            case SECONDS:
                this.messageAge = messageAge;
                break;
            default:
                throw new IllegalArgumentException( "Unsupported Time Unit" );
        }
    }
    
    public AgeMessageFilter( long messageAge )
    {
        this( messageAge, TimeUnit.SECONDS );
    }
    
    @Override
    public boolean _acceptMessage( Message currentMessage ) throws Exception
    {
        return ( ( System.currentTimeMillis() - currentMessage.getReceivedDate().getTime() ) / 1000 <= messageAge );
    }
}
