package com.pvale.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.pvale.tools.Media;
import com.pvale.tools.Wall;

import java.util.List;

public class Actor
{
    public static int lastId = 0;
    public static Animation<TextureRegion> exclamation;

    protected static final boolean RIGHT = true;
    protected static final boolean LEFT = false;

    public List<Wall> tiles;
    public int myId;
    public String myName;
    public float x,y;
    public boolean exclamate;
    public float exclamationDelta;
    public float exclamationOffsetY;
    public float exclamationOffsetX;

    public Actor(String name, List<Wall> tiles)
    {
        if(exclamation == null)
        {
            exclamation = new Animation<TextureRegion>(0.015f, Media.getSheetFrames(Media.loadTexture("exclamation.png"), 0, 0, 1, 6, 16, 16));
        }
        lastId ++;
        myId = lastId;
        myName = name;
        x = y = 0;
        exclamate = false;
        exclamationDelta = 0f;
        exclamationOffsetY = 18f;
        exclamationOffsetX = 3f;
        setTiles(tiles);
    }
    
    public void setTiles( List<Wall> tiles)
    {
        this.tiles = tiles;
    }

    public Rectangle myRect()
    {
        return Rectangle.tmp;
    }
    
    public void exclamate()
    {
        if(!exclamate)
        {
            exclamate = true;
        }
    }

    public void update(float delta)
    {
        if(exclamate)
        {
            if(exclamationDelta > 0.015f * 100f)
            {
                exclamate = false;
                exclamationDelta = 0f;
            }
            exclamationDelta += delta;
        }
    }
    public void draw(SpriteBatch batch)
    {
        if(exclamate)
            batch.draw(exclamation.getKeyFrame(exclamationDelta), x + exclamationOffsetX, y + exclamationOffsetY, 12, 12);
    }
}