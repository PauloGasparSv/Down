package com.pvale.down;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvale.actors.Player;
import com.pvale.stages.*;
import com.pvale.tools.Camera;
import com.pvale.tools.Font;

// import com.badlogic.gdx.Gdx;
// import com.badlogic.gdx.graphics.GL20;

public class MyGame extends Game 
{	
	public static Preferences prefs;

	@Override
	public void create () 
	{   
		prefs = Gdx.app.getPreferences("pvaledown");

		Font.init();

		Stage.camera = new OrthographicCamera();
		Stage.camera.setToOrtho(false, Camera.getWidth(), Camera.getHeight());
		Stage.batch = new SpriteBatch();
		Stage.game = this;

		setScreen(new Intro());
		// setScreen(new Test(new Player(null)));
	}

	@Override
	public void render () 
	{
		super.render();	
	}
	
	@Override
	public void dispose () 
	{
		super.dispose();
	}
}
