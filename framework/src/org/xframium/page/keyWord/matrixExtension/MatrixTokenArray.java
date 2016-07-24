package org.xframium.page.keyWord.matrixExtension;

import java.util.ArrayList;
import java.util.List;
import org.xframium.page.keyWord.KeyWordToken;

public class MatrixTokenArray extends DataArray
{
    public MatrixTokenArray( String dataDefinition )
    {
        super( dataDefinition );
    }

    public List<KeyWordToken> getTokens()
    {
        if ( getDataArray() != null && getDataArray().length > 0 )
        {
            List<KeyWordToken> parameterList = new ArrayList<KeyWordToken>( getDataArray().length );

            for ( String param : getDataArray() )
                parameterList.add( new MatrixToken( param ).getToken() );

            return parameterList;
        }
        else
            return null;
    }
}
