package com.pvale.down.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pvale.down.MyGame;

public class DesktopLauncher 
{
	public static void main (String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 960;
		config.height = 540;
		config.fullscreen = false;
		config.resizable = false;
		
		new LwjglApplication(new MyGame(), config);
	}
}
