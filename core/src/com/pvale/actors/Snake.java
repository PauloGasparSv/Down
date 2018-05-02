package com.pvale.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;

public class Snake extends Actor
{
    private float x;
    private  float y;
    private Animation<TextureRegion> animation;
    private float delta;
    private Player player;
    private boolean alive;
    private int deathState;
    private float squashDelta;

    public Snake(Animation<TextureRegion> snake, float x, float y)
    {
        super("Snake", null);
        this.x = x;
        this.y = y;
        this.delta = 0f;
        this.animation = snake;
    }
    
    public void init(Player player)
    {
        this.player = player;
        alive = true;
        deathState = 0;
        squashDelta = 0f;
    }

    @Override 
    public void update(float delta)
    {
        if(deathState == 0)
        {
            this.delta += delta;
            if(!player.isDead() && alive)
            {
                if(new Rectangle(x + 2,y, 8, 8).overlaps(player.myRect()))
                {
                    if(player.y > y + 4 && player.gravity >= 0)
                    {
                        player.setState(Player.JUMPING);
                        player.grounded = false;
                        player.gravity = -player.jumpSpeed; 
                        deathState = 1;
                    }
                    else
                    {
                        player.die();   
                    }
                }
            }
        }
        

        if(deathState == 1)
        {
            squashDelta += delta * 75.2f;
            if(squashDelta > 10)
            {
                squashDelta = 10f;
                alive = false;
            }
        }
       
    }

    @Override 
    public void draw(SpriteBatch batch)
    {
        if(!alive) return;
        batch.setColor(new Color(1,1,1, 1 - squashDelta / 10f));
        batch.draw(animation.getKeyFrame(delta,true), x, y, 18, 18 -( squashDelta > 0 ? 6 : 0));
        batch.setColor(Color.WHITE);
    }
}