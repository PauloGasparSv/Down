package com.pvale.tools;

import com.badlogic.gdx.math.Rectangle;

public class Wall
{
    private Rectangle rect;
    private String name;

    public Wall(Rectangle rect, String name)
    {
        this.rect = rect;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Rectangle getRect()
    {
        return rect;
    }

    public void setWidth(float width)
    {
        rect.width = width;
    }
    public void setHeight(float height)
    {
        rect.height = height;
    }
    public void setX(float x)
    {
        rect.x = x;
    }
    public void setY(float y)
    {
        rect.y = y;
    }

}