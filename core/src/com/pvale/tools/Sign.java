package com.pvale.tools;

import com.badlogic.gdx.math.Rectangle;

public class Sign
{
    private Rectangle myRect;
    private String text;

    public Sign(Rectangle rect, String text)
    {
        this.myRect = rect;
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public Rectangle getMyRect()
    {
        return myRect;
    }

}