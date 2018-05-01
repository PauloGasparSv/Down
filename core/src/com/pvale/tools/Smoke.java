package com.pvale.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pvale.stages.Stage;

public class Smoke
{
    private Animation<TextureRegion> animation;
    private float delta;
    private float alpha;
    private float x;
    private float y;

    public Smoke(Animation<TextureRegion> smoke)
    {
        this.animation = smoke;
        delta = 100f;
        alpha = 0f;
        x = 0f;
        y = 0f;
    }

    public void puff(float x, float y)
    {
        if(alpha <= 0f)
        {
            this.x = x;
            this.y = y;
            delta = 0f;
            alpha = 0.6f;
        }
    }

    public void update(float delta)
    {
        if(!animation.isAnimationFinished(this.delta))
        {
            this.delta += delta;
            if(animation.isAnimationFinished(this.delta)) alpha = 0f;
            alpha -= delta * 1f;
        }
    }

    public void draw()
    {
        if(alpha < 0f) return;
        Stage.batch.setColor(new Color(1, 1, 1, alpha));
        Stage.batch.draw(animation.getKeyFrame(delta,false), x, y, 6, 6);
        Stage.batch.setColor(Color.WHITE);
    }

}