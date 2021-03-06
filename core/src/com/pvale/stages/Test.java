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
import com.pvale.tools.Font;
import com.pvale.tools.Stair;
import com.pvale.actors.Snake;

public class Test extends Stage
{
    private Player player;
    private Texture keyboards;
    private TextureRegion [] keys;
    private Wall bridge;
    
    private float movingButtonsAlpha;
    private float jumpButtonAlpha;

    private Texture pillar;
    private float pillarX;
    private float pillarY;

    private Texture bridgeImg;
    private long stateTimer;

    private float [] bridgeY;
    private boolean [] bridgeFall;

    private Wall dragonPillar;
    
    private int state;
    private int stateHelper;

    private boolean []willWarp;

    private Texture grokSheet;
    private Animation<TextureRegion> gork;
    private float grokX, grokY;
    private float grokDelta;

    public Test(Player player)
    {   
        super("maps/testMap.tmx"); 
        
        this.player = player;
        player.setTiles(tiles);

        bridgeImg = Media.loadTexture("stage/bridge.png");
        keyboards = Media.loadTexture("keyboard.png");
        pillar = Media.loadTexture("stage/dragonPillar.png");

        grokSheet = Media.loadTexture("gork-NESW.png");
        gork = new Animation<TextureRegion>(0.2f, Media.getSheetFrames(grokSheet, 0, 32, 1, 3, 32, 32));

        keys = Media.getSheetFrames(keyboards, 1, 6, 10, 10);


        for(Wall wall : tiles)
        {
            if(wall.getName() != null)
            {
                if(wall.getName().equals("bridge"))
                {
                    bridge = wall;
                }
                else if(wall.getName().equals("pillar"))
                {
                    dragonPillar = wall;
                }
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

        
        player.hasControls();
        willWarp = new boolean[2];
        boolean visited = false;
        if(MyGame.prefs.contains("visitedfirst"))
        {
            System.out.println("CONTAINS");
            visited = MyGame.prefs.getBoolean("visitedfirst");
        }

        bridgeY = new float[8];
        bridgeFall = new boolean[8];
        player.bindCamera();
        Camera.left = 0f;
        Camera.right = 1600f;

        if(!visited)
        {
            stateHelper = 0;
            pillarX = 432f;
            pillarY = 280f;
            dragonPillar.setWidth(0);
            bridge.setWidth(368f); 
            state = 0;  
            for(int i = 0; i < 8; i ++)
            {
                bridgeY[i] = 38f;
                bridgeFall[i] = false;
            }
    
            for(MapPoint point : points)
            {
                if(point.getName().equals("player"))
                {
                    player.setPosition(point.getX(), point.getY());
                }   
            }
        }
        else
        {
            grokX = grokY  = -1000f;
            bridge.setWidth(240f);
            pillarY = 16f;
            pillarX  = 448f;
            stateHelper =  1;
            dragonPillar.setWidth(128f);
            state = 12;
            
            for(MapPoint point : points)
            {
                if(point.getName().equals("player2"))
                {
                    player.setPosition(point.getX(), point.getY());
                }   
            }
            for(int i = 0; i < 8; i ++)
            {
                bridgeY[i] = -90f;
                bridgeFall[i] = true;
            }
        }
        
        jumpButtonAlpha = movingButtonsAlpha = 0f;
        
        Camera.fadeIn(player.state == Player.DEAD || player.state == Player.DYING ? 0.1f : 0.3f);
    }

    @Override
    public void update(float delta)
    {
        player.update(delta);   
        if(player.x < -8f) player.x = -8f;

        stateHandler(delta);

        for(Snake snake : snakes)
        {
            snake.update(delta);
        }

        for(Warp warp : warps)
        {
            if(player.myRect().overlaps(warp.getRect()))
            {
                if(warp.getName().equals("warp1") && !willWarp[0] )
                {
                    MyGame.prefs.putBoolean("visitedfirst", true);
                    MyGame.prefs.flush();
                    willWarp[0] = true;
                    player.hasNoControls();
                    Camera.fadeOut(2f);
                }
                if(warp.getName().equals("warp4") && !willWarp[1] )
                {
                    willWarp[1] = true;
                    player.hasNoControls();
                    Camera.fadeOut(0.8f);
                }
            }
        }

        if(willWarp[0] && Camera.draw == Camera.BLACK)
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

        for(Stair stair : stairs)
        {
            if(stair.getRect().overlaps(player.myRect()))
            {
                player.stair();
                break;
            }
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

        if(movingButtonsAlpha < 1f)
        {
            movingButtonsAlpha += delta * 0.4f;
        }

        if(player.x > 428f && state == 0)
        {
            state = 1;
            stateTimer = System.currentTimeMillis();
        }

        if(state == 1)
        {
            player.setCheckpoint(428f, 64f);
            if(System.currentTimeMillis() - stateTimer > 400)
            {
                Rectangle bridgeRect = bridge.getRect();
                bridgeRect.width -= 32;
                
                for(int i = 7; i > -1; i--)
                {
                    if(!bridgeFall[i])
                    {
                        bridgeFall[i] = true;
                        break;
                    }
                }     

                if(bridgeRect.width < 240 && bridgeFall[0])
                {
                    bridgeRect.width = 240;
                    state = 2;
                    
                }
                bridge.setWidth(bridgeRect.width);  
                stateTimer = System.currentTimeMillis();
            }
        }
        else if(state == 2)
        {
            if(jumpButtonAlpha < 1f)
            {
                jumpButtonAlpha += delta * 0.6f;
            }
            else
            {
                state = 3;
                stateTimer = System.currentTimeMillis();
            }
        }
        else if (state == 3)
        {
            if(System.currentTimeMillis() - stateTimer > 1200 && System.currentTimeMillis() - stateTimer < 2200)
            {
                player.exclamate();
                Camera.shake(35);
                player.hasNoControls();
            }
                

            if(System.currentTimeMillis() - stateTimer > 3000)
            {
                pillarY -= delta * 200f;
                if(pillarY < 82f && stateHelper == 0)
                {
                    Camera.fadeOut(10f);
                    stateHelper ++;
                }
                if(pillarY < 64f)
                {
                    stateTimer = System.currentTimeMillis();
                    state ++;
                }
            }
        }
        else if(state == 4)
        {
            if(System.currentTimeMillis() - stateTimer > 2400)
            {
                player.hasControls();
                state ++;
                Camera.fadeIn(0.8f);
                dragonPillar.setWidth(128f);
                pillarY = 16f;
                pillarX  = 448f;
            }
        }
        else if(state == 5)
        {
            if(player.x > 756f)
            {
                stateTimer = System.currentTimeMillis();
                state ++;
                //0f
                Camera.left = Camera.getX();
                //1600f
                Camera.right = Camera.getX() + Camera.getWidth();
                player.unBindCamera();
            }
        }
        else if(state == 6)
        {
            if(player.x  > Camera.getX() + Camera.getWidth() - 10)
            {
                player.x = Camera.getX() + Camera.getWidth() - 10;
            }
            if(System.currentTimeMillis() - stateTimer > 2000)
            {
                state ++;
                stateTimer = System.currentTimeMillis();
                grokY = 164f;
                grokX = 638f;
                grokDelta = 0f;
            }
        }
        if(state == 7)
        {
            if(player.x  > Camera.getX() + Camera.getWidth() - 10)
            {
                player.x = Camera.getX() + Camera.getWidth() - 10;
            }

            if(grokY > 32f)
            {
                grokY -= 200 * delta;
            }
            else
            {
                player.exclamate();
                Camera.shake(8);
                grokY = 32f;
                stateTimer = System.currentTimeMillis();
                state ++;
            }
        }
        else if(state == 8)
        {
            if(player.x  > Camera.getX() + Camera.getWidth() - 10)
            {
                player.x = Camera.getX() + Camera.getWidth() - 10;
            }
            grokDelta += delta * 0.2f;
            if(System.currentTimeMillis() - stateTimer > 1800)
            {
                stateTimer = System.currentTimeMillis();
                state ++;
            }
            
            Camera.left = 0;
            Camera.right = 1600f;

        }
        else if(state == 9)
        {
            grokX += delta * 40f;
            grokDelta += delta;
            Camera.move(delta * 40f, 0);
            if(grokX > 1480f)
            {
                player.bindCamera();
                grokX = 1480f;
                state ++;
            }
        }


        if(state == 9 || state == 10)
        {
            if(player.x < grokX + 56f)
            {
                player.die();
            }
            if(player.x > 1600f)
            {
                System.out.println("HI");
              
                state  = 11;
            }
        }
        
        if(state < 4)
        {
            for(int i = 0; i < 8; i ++)
            {
                if(bridgeFall[i])
                    bridgeY[i] -= delta * 42f;
            }
        }
    }

    public void draw()
    {
        batch.setColor(new Color(1,1,1, movingButtonsAlpha));
        batch.draw(keys[1], 24f, 72f);
        batch.setColor(Color.WHITE);

        batch.setColor(new Color(1,1,1,jumpButtonAlpha));
        batch.draw(keys[4], 464f, 72f);
        batch.setColor(Color.WHITE);

        for(Snake snake : snakes)
        {
            snake.draw(batch);
        }
        
        if(state > 6)
        {
            batch.setColor(new Color(0.5f,0.5f,0.5f,1f));
            batch.draw(gork.getKeyFrame(grokDelta, true), grokX, grokY, 72f, 72f);
            batch.setColor(Color.WHITE);
        }

        player.draw(batch);

        if(state < 3)
        {
            for(int i = 0; i < 8; i ++)
            {
                batch.draw(bridgeImg, 304 + 16 * i, bridgeY[i]);
            }
        }
        else
        {
            if(state == 3)
                batch.draw(pillar, pillarX, pillarY, 0, 0, 152, 32, 1f, 1f, -18f, 0, 0, 152, 32, false, false);
            else
                batch.draw(pillar, pillarX, pillarY, 0, 0, 152, 32, 1f, 1f, 0f, 0, 0, 152, 32, false, false);
        }

        if(willWarp[1] && Camera.draw == Camera.BLACK)
        {
            Font.drawBig("Thanks for playing!", Camera.getX() + 20, Camera.getY() + 72, 1f);
        }

    }

    @Override
    public void dispose()
    {
        keyboards.dispose();
        pillar.dispose();
        grokSheet.dispose();
        bridgeImg.dispose();
    }

}