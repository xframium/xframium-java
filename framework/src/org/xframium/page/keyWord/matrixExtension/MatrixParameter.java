package org.xframium.page.keyWord.matrixExtension;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;

public class MatrixParameter
{
    private static Pattern PARAM_PATTERN = Pattern.compile( "(\\w*)\\(\\s*([^)]*)\\s*\\)" );
    
    private String type;
    private String value;
    
    public MatrixParameter( String parameterDefinition )
    {
        Matcher paramMatcher = PARAM_PATTERN.matcher( parameterDefinition );
        
        if ( paramMatcher.find() )
        {
            type = paramMatcher.group( 1 ).toUpperCase();
            value = paramMatcher.group( 2 );
        }
    }

    public String getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }
    
    public KeyWordParameter getParameter()
    {
        return new KeyWordParameter( ParameterType.valueOf( type ), value, null, null );
    }
}
