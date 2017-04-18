package org.xframium.imaging.algorithm.match;

public class MatchData
{
    private int x;
    private int y;
    private int confidence;
    private double data;

    public MatchData( int x, int y, int confidence, double data )
    {
        this.x = x;
        this.y = y;
        this.confidence = confidence;
        this.data = data;
    }

    public int getX()
    {
        return x;
    }

    public void setX( int x )
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY( int y )
    {
        this.y = y;
    }

    public int getConfidence()
    {
        return confidence;
    }

    public void setConfidence( int confidence )
    {
        this.confidence = confidence;
    }

    public double getData()
    {
        return data;
    }

    public void setData( double data )
    {
        this.data = data;
    }

}
