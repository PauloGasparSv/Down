package com.pvale.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.pvale.actors.Player;
import com.pvale.tools.Camera;
import com.pvale.tools.MapPoint;
import com.pvale.tools.Media;
import com.pvale.tools.Wall;
import com.pvale.down.MyGame;
import com.pvale.tools.Warp;
import com.pvale.tools.Stair;
import com.pvale.actors.Snake;

public class SecondArea extends Stage
{
    private Player player;
    private Texture keyboards;
    private TextureRegion [] keys;
    
    private float upButtonAlpha;
    
    private int state;
    private int stateHelper;

    private boolean willWarp;

    public SecondArea(Player player)
    {   
        super("maps/secondzone.tmx"); 
        
        this.player = player;
        player.setTiles(tiles);
       
        keyboards = Media.loadTexture("keyboard.png");
        keys = Media.getSheetFrames(keyboards, 1, 6, 10, 10);

        for(MapPoint point : points)
        {
            if(point.getName().equals("player"))
            {
                player.setPosition(point.getX(), point.getY());
            }   
        }

        init();
    }


    @Override
    public void init()
    {
        
        for(Snake snake : snakes)
        {
            snake.init(player);
        }

        willWarp = false;
        player.bindCamera();
        player.hasControls();
        state = 0;  
        stateHelper =  0;
        upButtonAlpha = 0f;
        
        Camera.fadeIn(player.state == Player.DEAD || player.state == Player.DYING ? 0.1f : 0.3f);
    }

    @Override
    public void update(float delta)
    {
        player.update(delta);   
        stateHandler(delta);

        for(Snake snake: snakes)
        {
            snake.update(delta);
        }

        for(Warp warp : warps)
        {
            if(player.myRect().overlaps(warp.getRect()))
            {
                if(warp.getName().equals("warp3") && !willWarp )
                {
                    willWarp = true;
                    player.hasNoControls();
                    Camera.fadeOut(2f);
                    game.setScreen(new Test(player));
                }
            }
        }

        for(Stair stair : stairs)
        {
            if(stair.getRect().overlaps(player.myRect()))
            {
                player.stair();
                break;
            }
        }

        if(willWarp && Camera.draw == Camera.BLACK)
        {
            dispose();
            game.setScreen(new SecondArea(player));
            return;
        }

        if(player.state == Player.DEAD)
        {
            player.revive();
            if(player.state != Player.DEAD)
                init();
        }

        if(!player.isDead())
        {
            for(Rectangle death : deathBlocks)
            {
                if(player.myRect().overlaps(death))
                {
                    Camera.fadeOut(1f);
                    player.die();
                    break;
                }
            }
        }
    }

    private void stateHandler(float delta)
    {

        if(player.x > 300f && upButtonAlpha < 1f)
        {
            upButtonAlpha += delta * 0.5f;
        }

    }

    public void draw()
    {
        batch.setColor(new Color(1,1,1, upButtonAlpha));
        batch.draw(keys[2], 474f, 72f);
        batch.setColor(Color.WHITE);
        
        for(Snake snake: snakes)
        {
            snake.draw(batch);
        }


        player.draw(batch);
    }

    @Override
    public void dispose()
    {
        keyboards.dispose();
    }

}