package org.xframium.page.keyWord.matrixExtension;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.KeyWordToken.TokenType;

public class MatrixToken
{
    private static Pattern PARAM_PATTERN = Pattern.compile( "(\\w*)\\s*=\\s*(\\w*)\\(\\s*([^)]*)\\s*\\)" );

    private String name;
    private String type;
    private String value;
    
    public MatrixToken( String parameterDefinition )
    {
        Matcher tokenMatcher = PARAM_PATTERN.matcher( parameterDefinition );
        
        if ( tokenMatcher.find() )
        {
            name = tokenMatcher.group( 1 );
            type = tokenMatcher.group( 2 ).toUpperCase();
            value = tokenMatcher.group( 3 );
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
    
    public String getName()
    {
        return name;
    }

    public KeyWordToken getToken()
    {
        return new KeyWordToken( TokenType.valueOf( type ), value, name );
    }
}
