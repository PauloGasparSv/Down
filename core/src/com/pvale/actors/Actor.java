package com.pvale.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import java.util.List;

public class Actor
{
    public static int lastId = 0;


    protected static final boolean RIGHT = true;
    protected static final boolean LEFT = false;

    public List<Rectangle> tiles;
    public int myId;
    public String myName;
    public float x,y;

    public Actor(String name, List<Rectangle> tiles)
    {
        lastId ++;
        myId = lastId;
        myName = name;
        x = y = 0;
        setTiles(tiles);
    }
    
    public void setTiles( List<Rectangle> tiles)
    {
        this.tiles = tiles;
    }

    public Rectangle myRect()
    {
        return Rectangle.tmp;
    }

    public void update(float delta){}
    public void draw(SpriteBatch batch){}
}