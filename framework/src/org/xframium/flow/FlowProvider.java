package org.xframium.flow;

import java.io.File;
import java.io.InputStream;
import org.xframium.flow.xsd.ModuleRegistry;

public interface FlowProvider
{
    ModuleRegistry getFlow( String fileName );
    ModuleRegistry getFlow( File fileName );
    ModuleRegistry getFlow( InputStream inputStream );
}
