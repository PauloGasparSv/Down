package com.pvale.stages;

import com.pvale.actors.Player;
import com.pvale.tools.MapPoint;
import com.pvale.tools.Media;
import com.pvale.tools.Camera;
import com.pvale.tools.Font;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;

public class Intro extends Stage
{
    private Player player;
    private MapLayer lightLayer;

    private Texture keyboards;
    private TextureRegion [] keys;

    private float startX;
    private float startY;
    private float miscDelta;
    private float lightOpacity;
    private float playerColor;
    private float buttonsAlpha;

    private int state;
    private long lastState;

    public Intro()
    {   
        super("maps/introMap.tmx");
        
        player = new Player(tiles);

        for(MapPoint point : points)
        {
            if(point.getName().equals("player"))
            {
                startX = point.getX();
                startY = point.getY();
                player.setPosition(startX, startY);
            }
        }

        Camera.setX(0);
        Camera.setY(344f);
        // Camera.fadeIn(0.18f);
        Camera.black();

        lightLayer = layers.get("fxs");
        lightLayer.setOpacity(0f);

        playerColor = 0.04f;

        keyboards = Media.loadTexture("keyboard.png");
        keys = Media.getSheetFrames(keyboards, 1, 6, 10, 10);

        miscDelta = 0f;
        lightOpacity = 0f;
        buttonsAlpha = 0f;
        state = 0;
        lastState = System.currentTimeMillis();
    }

    @Override
    public void update(float delta)
    {
        player.update(delta);

        states(delta);

        if(state > 6)
        {
            if(buttonsAlpha < 1)
            {
                buttonsAlpha += delta * 0.1f;
            }
            else
            {
                buttonsAlpha = 1f;
            }
        }

        if(player.x > 264 && state != 9)
        {
            state = 8;
            nextState();
            Camera.fadeOut(0.8f);
        }

        if(state == 9)
        {
            player.setPosition(264, player.y);
            if(System.currentTimeMillis() - lastState > 2000)
            {
                dispose();
                game.setScreen(new Test(player));
            }
        }

    
    }

    private void states(float delta)
    {
        if(state == 0)
        {
            nextState();
        }
        else if(state == 1)
        {   
            miscDelta += delta * 0.4f;
            Font.drawBig("Come back", 60, Camera.getY() + 72, miscDelta);
            if(miscDelta >= 1)
            {
                miscDelta = 1;
                nextState();
            }
        }
        else if(state == 2)
        {
            miscDelta -= delta * 0.8f;
            if(miscDelta <= 0)
            {
                Camera.fadeIn(0.7f);
                miscDelta = 0;
                player.setRotation(80);
                nextState();
            }
            Font.drawBig("Come back", 60, Camera.getY() + 72, miscDelta);   
        }
        else if(state == 3)
        {
            playerColor += delta * 0.01f;
            if(System.currentTimeMillis() - lastState > 2500)
            {
                Camera.fadeOut(1.8f);
                miscDelta = 0f;
                nextState();
            }
        }
        else if(state == 4)
        {
            miscDelta += delta * 0.4f;
            Font.drawBig("He will be fine", 40, Camera.getY() + 72, miscDelta);
            if(miscDelta >= 1)
            {
                miscDelta = 1;
                player.setRotation(270f);         
                player.y = 61f;
                Camera.fadeIn(0.2f);
                nextState();
            }
        }
        else if(state == 5)
        {
            player.y = 61f;
            playerColor += delta * 0.6f;
            lightOpacity += delta * 0.6f;
            if(playerColor > 1f)
            {
                playerColor = 1f;   
            }
            if(lightOpacity > 1f)
            {
                lightOpacity = 1f;
            }
            lightLayer.setOpacity(lightOpacity);
            if(playerColor == 1f && lightOpacity == 1f)
            {
                nextState();
            }
        }
        else if(state == 6)
        {
            player.y = 58f;
            player.setRotation(0);
            player.setState(Player.JUMPING);
            player.grounded = false;
            player.gravity = -player.jumpSpeed;
            
            nextState();    
        }
        else if(state == 7)
        {
            player.setPosition(player.x, player.y);
            player.hasControls(); 
            nextState();
        }

        if(state < 3)
        {
            player.y = 61f;
            player.x = startX;
            player.y = startY;
        }

        if(state < 7)
        {
            player.hasNoControls();
        }
    }

    private void nextState()
    {
        state ++;
        lastState = System.currentTimeMillis();
    }

    public void draw()
    {
        batch.setColor(new Color(playerColor,playerColor,playerColor,1f));
        player.draw(batch);
        batch.setColor(new Color(1f,1f,1f,buttonsAlpha));
        batch.draw(keys[1], 112f, 82f);
        batch.draw(keys[0], 94f, 82f);
        batch.setColor(Color.WHITE);
    }

    @Override
    public void dispose()
    {
        keyboards.dispose();
    }

}