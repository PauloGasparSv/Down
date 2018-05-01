package com.pvale.tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pvale.stages.Stage;

public class Lava
{
    private Animation<TextureRegion> lava;
    private float delta;

    private float x;
    private float y;
    private int width;

    public Lava(Animation<TextureRegion> lavaAnimation, float x, float y, int width)
    {
        this.lava = lavaAnimation;
        this.width = width;
        this.x = x;
        this.y = y;
    }

    public void draw(float delta)
    {
        if(x + width * 32 < Camera.getX() || x > Camera.getX() + Camera.getWidth()) return;
        this.delta += delta;
        for(int i = 0; i < width; i++)
        {
            Stage.batch.draw(lava.getKeyFrame(this.delta + width, true), x + i * 32, y);
        }
    }
}