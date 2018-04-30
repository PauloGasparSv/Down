package com.pvale.stages;

import com.badlogic.gdx.math.Rectangle;
import com.pvale.actors.Player;
import com.pvale.tools.Camera;
import com.pvale.tools.MapPoint;

public class Test extends Stage
{
    private Player player;

    public Test(Player player)
    {   
        super("maps/testMap.tmx"); 
        
        this.player = player;
        player.setTiles(tiles);

        for(MapPoint point : points)
        {
            if(point.getName().equals("player"))
            {
                player.setPosition(point.getX(), point.getY());
            }   
        }
        Camera.fadeIn();
    }

    @Override
    public void update(float delta)
    {
        player.update(delta);   
        if(player.x < 0) player.x = 0;  

        if(!player.isDead())
        {
            for(Rectangle death : deathBlocks)
            {
                if(player.myRect().overlaps(death))
                {
                    player.die();
                    break;
                }
            }
        }
    
    }

    public void draw()
    {
        player.draw(batch);
    }

}