package com.pvale.tools;

public class MapPoint
{
    private float x;
    private float y;
    private String name;

    public MapPoint(float x, float y, String name)
    {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

}