package org.xframium.jenkins;

import org.kohsuke.stapler.DataBoundConstructor;
import hudson.model.Action;

public class XFramiumAction implements Action
{
    private String url, text, icon;

    @DataBoundConstructor
    public XFramiumAction( String urlName, String displayName, String iconFileName )
    {
        this.url = urlName;
        this.text = displayName;
        this.icon = iconFileName;
    }

    public String getUrlName()
    {
        return url;
    }

    public String getDisplayName()
    {
        return text;
    }

    public String getIconFileName()
    {
        return icon;
    }
}
