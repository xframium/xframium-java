package org.xframium.driver;

import java.io.File;

public interface SuiteListener
{
    public void beforeSuite( String suiteName, File fileName );
    public void afterSuite( String suiteName, File fileName, File outputFolder );
}
