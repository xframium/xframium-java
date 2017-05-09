package org.xframium.device;

public class DeviceVersion
{
    private String minVersion = null;
    private String maxVersion = null;
    private String versionDescriptor;

    public DeviceVersion( String versionDescriptor )
    {
        if ( versionDescriptor.equals( "*" ) )
        {
            minVersion = Double.toString( Double.MIN_VALUE );
            maxVersion = Double.toString( Double.MAX_VALUE );
        }
        else if ( versionDescriptor.contains( "-" ) )
        {
            String[] splitValue = versionDescriptor.split( "-" );

            minVersion = normalize( splitValue[0].trim().split( "\\." ), 5 );

            if ( splitValue[1].trim().equals( "*" ) )
                maxVersion = Double.toString( Double.MAX_VALUE );
            else
                maxVersion = normalize( splitValue[1].trim().split( "\\." ), 5 );
        }
        else
        {
            minVersion = versionDescriptor;
            maxVersion = minVersion;
        }

        this.versionDescriptor = versionDescriptor;
    }

    private String normalize( String[] split, int maxLen )
    {
        StringBuilder sb = new StringBuilder( "" );
        for ( String s : split )
        {
            for ( int i = 0; i < maxLen - s.length(); i++ )
                sb.append( '0' );
            sb.append( s );
        }
        return removeTrailingZeros( sb.toString() );
    }

    private String removeTrailingZeros( String s )
    {
        int i = s.length() - 1;
        int k = s.length() - 1;
        while ( i >= 0 && (s.charAt( i ) == '.' || s.charAt( i ) == '0') )
        {
            if ( s.charAt( i ) == '.' )
                k = i - 1;
            i--;
        }
        return s.substring( 0, k + 1 );
    }

    private int compareVersion( String v1, String v2 )
    {
        //v1 = removeTrailingZeros(v1);
        v2 = removeTrailingZeros(v2);

        String[] splitv1 = v1.split( "\\." );
        String[] splitv2 = v2.split( "\\." );
        int maxLen = 0;
        for ( String str : splitv1 )
            maxLen = Math.max( maxLen, str.length() );
        for ( String str : splitv2 )
            maxLen = Math.max( maxLen, str.length() );
        int cmp = normalize( splitv1, maxLen ).compareTo( normalize( splitv2, maxLen ) );
        return cmp > 0 ? 1 : (cmp < 0 ? -1 : 0);
    }

    public String getMinVersion()
    {
        return minVersion;
    }

    public String getMaxVersion()
    {
        return maxVersion;
    }

    public String getVersionDescriptor()
    {
        return versionDescriptor;
    }



}
