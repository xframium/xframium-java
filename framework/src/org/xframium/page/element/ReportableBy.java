package org.xframium.page.element;

import java.util.List;

public interface ReportableBy
{
    public List<ByResult> getResults();
    public void addResult( String locator, int time, boolean success );
}
