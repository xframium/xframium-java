package org.xframium.page.keyWord.matrixExtension;

import java.util.ArrayList;
import java.util.List;
import org.xframium.page.keyWord.KeyWordParameter;

public class MatrixParameterArray extends DataArray
{
    public MatrixParameterArray( String dataDefinition )
    {
        super( dataDefinition );
    }

    public List<KeyWordParameter> getParameters()
    {
        if ( getDataArray() != null && getDataArray().length > 0 )
        {
            List<KeyWordParameter> parameterList = new ArrayList<KeyWordParameter>( getDataArray().length );

            for ( String param : getDataArray() )
                parameterList.add( new MatrixParameter( param ).getParameter() );

            return parameterList;
        }
        else
            return null;
    }
}
