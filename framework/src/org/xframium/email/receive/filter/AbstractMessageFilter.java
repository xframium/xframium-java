package org.xframium.email.receive.filter;

import javax.mail.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractMessageFilter implements MessageFilter
{
    protected Log log = LogFactory.getLog( MessageFilter.class );
    
    public abstract boolean _acceptMessage( Message currentMessage ) throws Exception;
    
    @Override
    public boolean acceptMessage( Message currentMessage )
    {
        try
        {
            if ( log.isInfoEnabled() )
                log.info( "Applying " + getClass().getSimpleName() + " filter to message from " + currentMessage.getFrom()[ 0 ] );
            
            boolean returnValue = _acceptMessage( currentMessage );
            
            if ( !returnValue )
            {
                if ( log.isInfoEnabled() )
                    log.info( "Message [" + currentMessage.getSubject() + "] from " + currentMessage.getFrom()[ 0 ] + " has been removed by " + getClass().getSimpleName() );
            }
        
            return returnValue;
        }
        catch( Exception e )
        {
            log.error( "Error applying filter", e );
            return true;
        }
    }

}
