package org.xframium.page.keyWord.matrixExtension;

public abstract class DataArray
{
    private String dataDefinition;
    private String[] dataArray;
    
    public DataArray ( String dataDefinition )
    {
        if ( dataDefinition != null )
        {
            this.dataDefinition = dataDefinition;
            this.dataArray = dataDefinition.split( "\r\n" );
        }
    }

    public String getDataDefinition()
    {
        return dataDefinition;
    }

    public String[] getDataArray()
    {
        return dataArray;
    }
}
