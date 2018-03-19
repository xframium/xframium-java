package org.xframium.page.keyWord.step.transform;

public class DefaultTransformation implements ValueTransformation
{
    @Override
    public String transformValue(String value)
    {
        return value;
    }
    
    @Override
    public String wrapValue(String value)
    {
        return value;
    }
}
