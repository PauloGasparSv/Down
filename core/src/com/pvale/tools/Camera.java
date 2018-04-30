package com.pvale.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.pvale.stages.Stage;

public class Camera
{
    public static float top = 435f;
    public static float bottom = 0f;
    public static float left = 0;
    public static float right = 240f;

    public static int draw = 0;
    public static float alpha = 1f;
    public static float speed = 0.8f;
    
    public static Texture primitive;

    public static void setBorders(float a, float b, float c, float d)
    {   
        left = a;
        top = b;
        right = c;
        bottom = d;
    }

    public static float getWidth()
    {
        return 240f;
    }

    public static float getHeight()
    {
        return 135f;
    }

    public static float getX()
    {
        return Stage.camera.position.x - 120f;
    }

    public static float getY()
    {
        return Stage.camera.position.y - 67.5f;
    }

    public static void setX(float x)
    {
        Stage.camera.position.x = x + 120f;
    }

    public static void setY(float y)
    {
        Stage.camera.position.y = y + 67.5f;
    }

    public static void move(float x, float y)
    {
        Stage.camera.position.x += x;
        Stage.camera.position.y += y;

        if(getX() < left) setX(left);
        else if(getX() + getWidth() > right) setX(right - getWidth());
        if(getY() < bottom) setY(bottom);
        else if(getY() + getHeight() > top) setY(top - getHeight());
    }

    public static void init()
    {
        Stage.camera.position.x = 120f;
        Stage.camera.position.y = 67.5f;
        primitive = Media.loadTexture("primitive.png");
    }

    public static void drawBlack()
    {
        Stage.batch.setColor(Color.BLACK);
        Stage.batch.draw(primitive, getX(), getY(), getWidth(), getHeight());
        Stage.batch.setColor(Color.WHITE);
    }

    public static void drawFadeIn(float delta)
    {
        alpha -= delta * speed;
        if(alpha < 0)
        {
            alpha = 0f;
            draw = 0;
            return;
        }
        Stage.batch.setColor(new Color(0, 0, 0, alpha));
        Stage.batch.draw(primitive, getX(), getY(), getWidth(), getHeight());
        Stage.batch.setColor(Color.WHITE);
    }

    public static void drawFadeOut(float delta)
    {
        alpha += delta * speed;
        if(alpha > 1f)
        {
            alpha = 1f;
            draw = 2;
        }
        Stage.batch.setColor(new Color(0, 0, 0, alpha));
        Stage.batch.draw(primitive, getX(), getY(), getWidth(), getHeight());
        Stage.batch.setColor(Color.WHITE);
    }

    public static void fadeIn()
    {
        fadeIn(speed);
    }

    public static void black()
    {
        draw = 2;
    }

    public static void fadeOut()
    {
        fadeOut(speed);
    }

    public static void transparent()
    {
        draw = 0;
    }

    public static void fadeOut(float newSpeed)
    {
        if(draw != 3)
        {
            draw = 3;
            alpha = 0f;
            if(newSpeed > 0f) speed = newSpeed;
            else speed = 0.3f;
        }
    }

    public static void fadeIn(float newSpeed)
    {
        if(draw != 1)
        {
            draw = 1;
            alpha = 1f;
            if(newSpeed > 0f) speed = newSpeed;
            else speed = 0.3f;
        }
    }

}