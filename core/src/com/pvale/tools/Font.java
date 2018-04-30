package com.pvale.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.pvale.stages.Stage;

public class Font
{
    public static BitmapFont font12;
    public static BitmapFont font6;

    public static String text;
    public static boolean drawBig;
    public static float x;
    public static float y;
    public static float alpha;

    public static void init()
    {
        text = "";

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/8-BIT WONDER.TTF"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12;
        font12 = generator.generateFont(parameter);
        parameter.size = 6;
        font6 = generator.generateFont(parameter);
    }

    public static void draw(String text_, float x_, float y_, float alpha)
    {
        font6.setColor(new Color(1, 1, 1, alpha));
        font6.draw(Stage.batch, text_, x_, y_);
    }

    public static void drawBig(String text_, float x_, float y_, float alpha_)
    {
        text = text_;
        x = x_;
        y = y_;
        alpha = alpha_;
        drawBig = true;
    }

    public static void drawBig()
    {
        font12.setColor(new Color(1, 1, 1, alpha));
        font12.draw(Stage.batch, text, x, y);
        drawBig = false;
    }

}