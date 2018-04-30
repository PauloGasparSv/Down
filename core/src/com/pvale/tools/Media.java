package com.pvale.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Media
{
    
    public static Texture loadTexture(String path)
    {
        return new Texture(Gdx.files.internal(path));
    }

    public static TextureRegion [] getSheetFrames(Texture texture, int rows, int cols, int width, int height)
    {
        return getSheetFrames(texture, 0, 0, rows, cols, width, height);
    }

    public static TextureRegion [] getSheetFrames(Texture texture, int startX, int startY, int rows, int cols, int width, int height)
    {
        TextureRegion [] frames = new TextureRegion[cols * rows];
        int current = 0;
        for(int row = 0; row < rows; row ++)
        {
            for(int col = 0; col < cols; col ++)
            {
                frames[current] = new TextureRegion(texture, startX + col * width, startY +  row * height, width, height);
                current ++;
            }
        }
        return frames;
    }
    
}