package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.sound.sampled.Clip;

import core.Audio;
import core.IRenderable;
import core.IUpdateable;
import core.Sprite;
import ecs100.UI;
import game.GameMap.Layer;
import game.entities.Walker;
import graphics.PixelImage;
import input.Keyboard;
import math.AABB;
import math.Transform;
import math.Vec2;
import resources.R;
import util.Log;

public class Level implements IUpdateable, IRenderable {

	private Player player;
	private Player player2;
	
	private GameMap map = GameMaps.maps[1];
	
	private Camera camera;
	
	private Rope rope;
	
	private boolean reset = false;
	
	private Clip clip = Audio.loop(R.audio.a_bannanas_ages);;
	
	public Level() {
		reset();
	}
	
	public void reset() {
		reset = true;
	}
	
	private void reset(GameMap map) {
		map.init();
		
		player = new Player(Controller.P1);
		player.transform().position.set(3*16, 30*16);
		player.setLevel(this);
		
		player2 = new Player(Controller.P2);
		player2.transform().position.set(6*16, 30*16);
		player2.setLevel(this);
		
		for(Sprite s : map.getEntities()) {
			if(s instanceof Walker) {
				((Walker)s).setLevel(this);
			}
		}
		
		if(camera == null)
			camera = new Camera(new AABB(0, 0, map.getWidth(), map.getHeight()), player, player2);
		else
			camera.setTargets(player, player2);
		
		rope = new Rope(player, player2, 32);
		
//		if(clip != null && clip.isOpen()) {
//			clip.close();
//		}
//
//		clip = Audio.loop(R.audio.a_bannanas_ages);
	}
	
	public GameMap getMap() {
		return map;
	}
	
	public Camera getCamera() {
		return camera;
	}

	@Override
	public void update(float delta) {
		if(Keyboard.isKeyDownOnce(KeyEvent.VK_SPACE)) {
			reset();
		}
		if(reset) {
			reset = false;
			reset(map);
		}
		
		map.getDynamicCollisionLayer().clear();
		
//		map.getDynamicCollisionLayer().add(player);
//		map.getDynamicCollisionLayer().add(player2);
		
		for(Sprite s : map.getEntities()) {
			s.update(delta);
			map.getDynamicCollisionLayer().add(s);
		}
		
		rope.update(delta);		
		player.update(delta);
		player2.update(delta);
		camera.update(delta);
		
//		UI.drawString(Log.format("Pos: {}", player.transform().position.length()), 100, 300);
//		UI.drawString(Log.format("Pos: {}", player.transform().position.round().length()), 100, 350);
//		UI.drawString(Log.format("Pos: {}", player.getAABB()), 100, 400);
//		UI.drawString(Log.format("Pos: {}", player.transform().position), 100, 200);
//		UI.drawString(Log.format("Pos: {}", player2.transform().position), 100, 250);
		
	}

	@Override
	public void render(PixelImage canvas, Camera camera) {
		camera = this.camera;

		Vec2 pos = camera.transform(new Vec2());
		for(Layer layer : map.getBackgroundLayers()) {
			canvas.blit(layer.getImage(), pos.x(), pos.y());
		}

		rope.render(canvas, camera);
		
		pos = camera.transform(new Vec2());
		for(Layer layer : map.getWorldLayers()) {
			canvas.blit(layer.getImage(), pos.x(), pos.y());
		}
		
		pos = camera.transform(new Vec2());
		for(Layer layer : map.getHazardLayers()) {
			canvas.blit(layer.getImage(), pos.x(), pos.y());
		}
		
//		canvas.blit(map.getCollisionLayer().getImage(), 0, 0);
		
		
		player.render(canvas, camera);
		player2.render(canvas, camera);
		
		for(Sprite s : map.getEntities()) {
			s.render(canvas, camera);
		}
		
//		Graphics2D g = canvas.createGraphics();
//		g.setColor(Color.GREEN);
//		AABB box = camera.transform(camera.getAABB());
//		g.drawRect((int)(box.left), (int)(box.top), (int)(box.getWidth()), (int)(box.getHeight()));
//		g.dispose();
		
		pos = camera.transform(new Vec2());
		for(Layer layer : map.getForegroundLayers()) {
			canvas.blit(layer.getImage(), pos.x(), pos.y());
		}
		
//		renderCollisionLayers(canvas, camera);
//		canvas.clear();
//		Vec2 tr = new Vec2();
//		tr = camera.transform(tr);
//		canvas.blit(map.getDynamicCollisionLayer().getImage(), tr.x(), tr.y(), 0xFFFF0000);
	}
	
	private void renderCollisionLayers(PixelImage canvas, Camera camera) {
		Vec2 tr = new Vec2();
		tr = camera.transform(tr);
		canvas.blit(map.getCollisionLayer().getImage(), tr.x(), tr.y());
		
		tr = new Vec2();
		tr = camera.transform(tr);
		canvas.blit(map.getDynamicCollisionLayer().getImage(), tr.x(), tr.y());
		
		AABB aabb = camera.transform(player.getAABB());
		
		if(canvas.testBounds((int)(aabb.left + aabb.getWidth()*0.5f), (int)(aabb.bottom))) 
			canvas.setARGB((int)(aabb.left + aabb.getWidth()*0.5f), (int)(aabb.bottom), 0xFF00FF00);
	}

	public void endLevel() {
		this.reset();
	}

}
