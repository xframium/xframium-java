package org.xframium.device;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class XFramiumLock extends ReentrantLock
{

    /**
     * 
     */
    private static final long serialVersionUID = -3654406108189956530L;
    
    
    @Override
    public boolean tryLock()
    {
        
        return super.tryLock();
    }
    
    @Override
    public void lock()
    {
        if ( isLocked() )
        {
            System.err.println( "LOCKED BY " + getOwner() );
            StackTraceElement[] x = getOwner().getStackTrace();
            for (StackTraceElement f : x )
                System.err.println( f.toString() );
        }
        super.lock();
        if ( isLocked() )
        {
            System.err.println( "NOW LOCKED BY " + getOwner() );
        }
    }
    
    @Override
    public void unlock()
    {
        if ( isLocked() )
        {
            System.err.println( "UNLOCKING BY " + getOwner() );
        }
        super.unlock();
        if ( isLocked() )
        {
            System.err.println( "NOW UNLOCKED" );
        }
    }
    
    @Override
    public boolean tryLock( long timeout, TimeUnit unit ) throws InterruptedException
    {
        // TODO Auto-generated method stub
        return super.tryLock( timeout, unit );
    }
    

}
