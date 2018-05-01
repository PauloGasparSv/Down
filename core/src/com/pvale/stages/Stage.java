package com.pvale.stages;

import java.util.LinkedList;
import java.util.List;

import javax.lang.model.util.ElementScanner6;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.pvale.down.MyGame;
import com.pvale.tools.Camera;
import com.pvale.tools.Font;
import com.pvale.tools.In;
import com.pvale.tools.Lava;
import com.pvale.tools.MapPoint;
import com.pvale.tools.Media;
import com.pvale.tools.Wall;
import com.pvale.tools.Sign;

public class Stage extends OrthogonalTiledMapRenderer implements Screen 
{
	public static SpriteBatch batch;
	public static OrthographicCamera camera;
	public static MyGame game;

	public static Animation<TextureRegion> lavaAnimation;
	
	protected List<Lava> lava;
	protected List<Wall> tiles;
	protected List<Rectangle> deathBlocks;
	protected List<Sign> signs;
	protected List<MapPoint> points; 
	protected MapLayers layers;

	private int spriteLayer = 2;
	private int tilesLayer = 4;

	public Stage(String path)
	{
		super(new TmxMapLoader().load(path));

		if(lavaAnimation == null)
		{
			lavaAnimation = new Animation<TextureRegion>(0.4f, Media.getSheetFrames(Media.loadTexture("stage/animated/lava.png"), 1, 3, 32, 32));
		}

		Camera.setBorders(0, (Integer)map.getProperties().get("height") * 16,
			(Integer)map.getProperties().get("width") * 16, 0);

		layers = map.getLayers();
		tiles = new LinkedList<Wall>();	
		points = new LinkedList<MapPoint>();	
		deathBlocks = new LinkedList<Rectangle>();
		signs = new LinkedList<Sign>();	
		lava = new LinkedList<Lava>();	

		Array<RectangleMapObject> tileRects =  map.getLayers().get(tilesLayer).getObjects().getByType(RectangleMapObject.class);
		
		for(RectangleMapObject rect : tileRects)
		{	
			Rectangle temp = rect.getRectangle();
			if(rect.getProperties().get("type").equals("position"))
			{
				points.add(new MapPoint(temp.x, temp.y, rect.getName()));	
				continue;
			}
			else if(rect.getProperties().get("type").equals("wall"))
			{
				tiles.add(new Wall(temp, rect.getName()));
			}
			else if(rect.getProperties().get("type").equals("death"))
			{
				if(rect.getName()!= null && rect.getName().equals("lava"))
				{
					lava.add(new Lava(lavaAnimation,temp.x, temp.y, ((int)(temp.width) / 32) ));
				}
				deathBlocks.add(temp);
			}
			else if(rect.getProperties().get("type").equals("sign"))
			{
				signs.add(new Sign(temp, rect.getName()));
			}
			
		}
		Camera.init();	
	}	

	public void init(){}

	@Override
	public void render(float delta) 
	{
		In.updateController();
		update(delta);
		Camera.shakeHandle(delta);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		setView(camera);
		
		beginRender();
		int currentLayer = 0;
		for(MapLayer layer : layers)
		{
			if(layer.isVisible())
			{
				if(currentLayer == spriteLayer)
				{
					endRender();
					batch.begin();		
					draw();
					for(Lava l : lava)
					{
						l.draw(delta);
					}
					batch.end();
					beginRender();
				}
				if(layer instanceof TiledMapTileLayer)
				{
					renderTileLayer((TiledMapTileLayer) layer);
				}
			}
			currentLayer ++;
		}
		endRender();
		batch.begin();
		if(Camera.draw > 0)
		{	
			switch(Camera.draw)
			{
				case 1:
					Camera.drawFadeIn(delta);
					break;
				case 2:
					Camera.drawBlack();
					break;
				case 3:
					Camera.drawFadeOut(delta);
					break;
			}
		}
		if(Font.drawBig)
		{
			Font.drawBig();
		}
		batch.end();
	}

	@Override
	public void dispose() 
	{
		map.dispose();		
	}

	public void update(float delta){}
	public void draw(){}
	@Override
	public void show() {}
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void hide() {}
}