package org.xframium.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractEmailProvider implements EmailProvider
{
    protected Log log = LogFactory.getLog( EmailProvider.class );
    
}
