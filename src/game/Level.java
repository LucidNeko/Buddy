package game;

import java.awt.Color;
import java.awt.Graphics2D;

import core.IRenderable;
import core.IUpdateable;
import game.GameMap.Layer;
import graphics.PixelImage;
import math.AABB;
import math.Transform;
import math.Vec2;

public class Level implements IUpdateable, IRenderable {

	private Player player;
	private Player player2;
	
	private GameMap map = new GameMap();
	
	private Camera camera;
	
	public Level() {
		reset();
	}
	
	public void reset() {
		player = new Player(Controller.P1);
		player.transform().position.set(100, 100);
		player.setLevel(this);
		
		player2 = new Player(Controller.P2);
		player2.transform().position.set(150, 100);
		player2.setLevel(this);
		
		camera = new Camera(new AABB(0, 0, map.getWidth(), map.getHeight()), player, player2);
	}
	
	public GameMap getMap() {
		return map;
	}
	
	public Camera getCamera() {
		return camera;
	}

	@Override
	public void update(float delta) {
		player.update(delta);
		player2.update(delta);
		camera.update(delta);
	}

	@Override
	public void render(PixelImage canvas, Camera camera) {
		camera = this.camera; 
		
		Vec2 pos = camera.transform(new Vec2());
		for(Layer layer : map.getBackgroundLayers()) {
			canvas.blit(layer.getImage(), pos.x(), pos.y());
		}
		
//		canvas.blit(map.getCollisionLayer().getImage(), 0, 0);
		
		Vec2 a = player.getAABB().getCenter();
		Vec2 b = player.getAABB().getCenter();
		
		
		player.render(canvas, camera);
		player2.render(canvas, camera);
		
//		Graphics2D g = canvas.createGraphics();
//		g.setColor(Color.GREEN);
//		AABB box = camera.transform(camera.getAABB());
//		g.drawRect((int)(box.left), (int)(box.top), (int)(box.getWidth()), (int)(box.getHeight()));
//		g.dispose();
		
		pos = camera.transform(new Vec2());
		for(Layer layer : map.getForegroundLayers()) {
			canvas.blit(layer.getImage(), pos.x(), pos.y());
		}
	}

}
