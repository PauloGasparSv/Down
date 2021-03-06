package com.pvale.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.pvale.tools.Camera;
import com.pvale.tools.In;
import com.pvale.tools.Media;
import com.pvale.tools.Smoke;
import com.pvale.tools.Wall;

import java.util.List;


public class Player extends Actor
{
    public static final int IDDLING = 0;
    public static final int WALKING = 1;
    public static final int PREPARING = 2;
    public static final int JUMPING = 3;
    public static final int FALLING = 4;
    public static final int LANDING = 5;
    public static final int DYING = 6;
    public static final int DEAD = 7;
    public static final int CLIMBING = 8;

    private Texture texture;
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> iddling;
    private Animation<TextureRegion> walking;
    private Animation<TextureRegion> preparing;
    private Animation<TextureRegion> jumping;
    private Animation<TextureRegion> falling;
    private Animation<TextureRegion> landing;
    private Animation<TextureRegion> dying;
    private Animation<TextureRegion> climbing;

    private Smoke smoke;

    private boolean stair;
    public float gravity;
    public float jumpSpeed;
    private float startX;
    private float startY;
    private float animationDelta;
    private float speed;
    private float rotation;
    private long deadTimer;
    public int state;

    private boolean cameraControl;
    public boolean grounded;
    private boolean hasControls;
    private boolean direction;

    public Player(List<Wall> tiles)
    {
        super("Player", tiles);

        texture = Media.loadTexture("characters.png");
        iddling = new Animation<TextureRegion>(1.2f, Media.getSheetFrames(texture, 576, 24, 1, 2, 24, 24));
        walking = new Animation<TextureRegion>(0.18f, Media.getSheetFrames(texture, 0, 24, 1, 4, 24, 24));
        preparing = new Animation<TextureRegion>(0, Media.getSheetFrames(texture, 96, 24, 1, 1, 24, 24)); 
        jumping = new Animation<TextureRegion>(0.2f, Media.getSheetFrames(texture, 120, 24, 1, 2, 24, 24));
        falling = new Animation<TextureRegion>(0f, Media.getSheetFrames(texture, 168, 24, 1, 1, 24, 24));
        landing = new Animation<TextureRegion>(0.3f, Media.getSheetFrames(texture, 192, 24, 1, 1, 24, 24));
        dying = new Animation<TextureRegion>(0.08f, Media.getSheetFrames(texture, 624, 24, 1, 9, 24, 24));
        smoke = new Smoke(new Animation<TextureRegion>(0.1f, Media.getSheetFrames(Media.loadTexture("smoke.png"), 1, 4, 16, 16)));
        climbing = new Animation<TextureRegion>(0.2f, Media.getSheetFrames(texture, 456, 24, 1, 4, 24, 24));

        setState(IDDLING, true);
        direction = RIGHT;

        x = 20f;
        y = 64f;
        animationDelta = 0f;
        gravity = 0f;
        speed = 48.5f * 1f;
        jumpSpeed = 96f * 1f;
        rotation = 0f;
        grounded = true;
        hasControls = true;
        cameraControl = true;
    }
    
    @Override
    public void update(float delta)
    {
        super.update(delta);
        if(state != CLIMBING)
            animationDelta += delta;
        if(hasControls) controls(delta);
        mapCollision(delta);
        if(cameraControl) cameraHandle(delta);

        if(state == LANDING && currentAnimation.isAnimationFinished(animationDelta))
        {
            setState(IDDLING);
        }

        if(state == CLIMBING)
        {
            if(grounded)
                setState(IDDLING);
            else if(!stair)
            {
                grounded = false;
                setState(FALLING);
            }
        }

        if(!grounded && state != DEAD && state != DYING && state != CLIMBING)
        {
            if(gravity < 140f)
                gravity += delta * 275f;
            else
                gravity = 140f;
            y -= gravity * delta;
        }

        if(state == DYING && dying.isAnimationFinished(animationDelta))
        {
            setState(DEAD);
        }
      
        smoke.update(delta);
        stair = false;
    }

    public void stair()
    {
        stair = true;
    }

    public void revive()
    {
        if(state != DEAD) return;
        if(System.currentTimeMillis() - deadTimer < 1000) return;
        setState(IDDLING);
        x = startX;
        y = startY;
        hasControls();
    }
    
    private void cameraHandle( float delta)
    {
        if(x > Camera.getX() + Camera.getWidth() - 96f) 
        {
            Camera.move(x - Camera.getX() - Camera.getWidth() + 96f , 0);
        }
        else if(x < Camera.getX() + 72f) 
        {
            Camera.move(x - Camera.getX() - 72f, 0);
        }

        if(y < Camera.getY() + 32f)
        {
            Camera.move(0, y - Camera.getY() - 32f);
        }

        if(y > Camera.getY() + Camera.getHeight() - 72f)
        {
            Camera.move(0, y - Camera.getY() - Camera.getHeight() + 72f);
        }

    }

    private void mapCollision(float delta)
    {
        if(isDead()) return;
        boolean newGrounded = false;
        for(Wall wall : tiles)
        {
            Rectangle tile = wall.getRect();
            Rectangle myRect = myRect();
            if(myRect.overlaps(tile))
            {
                if(myRect.y > tile.y + tile.height - 4 && gravity > -4f)
                {
                    newGrounded = true;
                    groundMe(tile.y + tile.height);
                }
                else
                {
                    if(myRect.x + myRect.width > tile.x && direction == RIGHT)
                    {
                        x = tile.x - myRect.width - 4;
                    }
                    else if(myRect.x < tile.x + tile.width  && direction == LEFT)
                    {
                        x = tile.x + tile.width - 4;
                    }
                }
            }
        }
        if(!newGrounded && grounded)
        {
            grounded = false;
            setState(FALLING);
        }
    }

    private void controls(float delta)
    {
        if(isDead()) return;
        if(In.jump() && state != CLIMBING)
        {
            if(grounded && state != PREPARING)
            {
                setState(PREPARING);
            }
        }
        else if(state == PREPARING && animationDelta > 0.2f)
        {
            setState(JUMPING);
            y += 4;
            grounded = false;
            gravity = -jumpSpeed; 
        }

        if(In.up() && stair)
        {
            if(state != CLIMBING)
            {
                gravity = 0f;
                grounded = false;
                y += 2f;
                setState(CLIMBING);
            }
            else
            {
                animationDelta += delta;
            }
            y += speed * delta * 0.35f;
        }

        if(In.down() && stair && state == CLIMBING)
        {
            y -= speed * delta * 0.5f;
        }

        if(In.right())
        {
            if(state != PREPARING && state != LANDING)
            {
                x += speed * delta;
            }
            direction = RIGHT;
            if(state == IDDLING)
            {
                setState(WALKING);
            }
        }
        else if(In.left())
        {
            if(state != PREPARING && state != LANDING)
            {
                x -= speed * delta;   
            }
            direction = LEFT;
            if(state == IDDLING)
            {
                setState(WALKING);
            } 
        }
        else if(state == WALKING)
        {
            setState(IDDLING);
        }


    }

    public void unBindCamera()
    {
        cameraControl = false;
    }

    public void bindCamera()
    {
        cameraControl = true;
    }

    public void setState(int state, boolean force)
    {
        if(this.state == state && !force) return;
        this.state = state;
        animationDelta = 0f;
        switch(state)
        {
            case IDDLING:
                currentAnimation = iddling;
                break;
            case WALKING:
                animationDelta = 0.2f;
                currentAnimation = walking;
                break;
            case PREPARING:
                currentAnimation = preparing;
                break;
            case JUMPING:
                smoke.puff(x + (direction == RIGHT ? 0 : 10) , y - 2);
                currentAnimation = jumping;
                break;
            case FALLING:
                currentAnimation = falling;
                break;
            case LANDING:
                currentAnimation = landing;
                smoke.puff(x + (direction == RIGHT ? 0 : 10), y - 2);
                break;
            case DYING:
                currentAnimation = dying;
                break;
            case DEAD:
                deadTimer = System.currentTimeMillis();
                animationDelta = 100f;
                currentAnimation = dying;
                break;
            case CLIMBING:
                currentAnimation = climbing;
                break;
        }
    }

    public boolean isDead()
    {
        return state == DEAD || state == DYING;
    }

    public void die()
    {
        if(isDead()) return;
        gravity = 0f;
        hasNoControls();
        setState(DYING);
    }

    public void setRotation(float rotation)
    {
        this.rotation = rotation;
    }

    public void groundMe()
    {
        groundMe(this.y);
    }

    public void groundMe(float y)
    {
        if(isDead()) return;
        this.y = (int) y;

        if(!grounded)
        {
            if( (state == FALLING || state == JUMPING) )
                setState(gravity > 40f ? LANDING : IDDLING);
            else
                setState(LANDING);
        }
       
        gravity = 0f;
        grounded = true;
    }

    public void setState(int state)
    {
       setState(state, false);
    }    

    public void hasControls()
    {
        hasControls = true;
    }

    public void hasNoControls()
    {
        hasControls = false;
    }

    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
        setCheckpoint(x, y);
    }

    public void setCheckpoint(float x, float y)
    {
        startX = x;
        startY = y;
    }

    @Override 
    public Rectangle myRect()
    {
        if(isDead()) return new Rectangle(0,0,0,0);
        return new Rectangle(x + 4, y - 1, 9, 15);
    }

    @Override 
    public void draw(SpriteBatch batch)
    {
        super.draw(batch);
        TextureRegion frame = currentAnimation.getKeyFrame(animationDelta, state != JUMPING && state != FALLING && state != DYING && state != DEAD);
        if(frame.isFlipX() != !direction)
        {
            frame.flip(true, false);
        }
        batch.draw(frame, x, y, 0, 0, 18, 18, 1, 1, rotation);
        smoke.draw();
    }
}