package com.pvale.down.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pvale.down.MyGame;
import javax.swing.JOptionPane;

public class DesktopLauncher 
{
	public static void main (String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		int reply = JOptionPane.showConfirmDialog(null, "Tela cheia?", "Tamanho do jogo", JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) 
		{
			config.fullscreen = true;
        }
		else 
		{
			config.fullscreen = false;
        }

		config.width = 960;
		config.height = 540;
		
		config.resizable = false;

		
		
		new LwjglApplication(new MyGame(), config);
	}
}
