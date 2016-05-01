package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import ecs100.UI;
import game.Camera;
import graphics.PixelImage;
import math.AABB;
import math.Transform;
import math.Vec2;
import util.Log;

public abstract class Sprite implements IUpdateable, IRenderable {
	
	private final int DEFAULT_WIDTH = 50;
	private final int DEFAULT_HEIGHT = 50;

	private Transform transform = new Transform();
	
	private int id;
	{
		id = NEXT_ID;
		sprites.put(NEXT_ID++, this);
	}
	
	public Sprite() {
		Log.info("ID: {}", Integer.toHexString(getID()));
	}
	
	public int getID() {
		return id;
	}
	
	public Transform transform() {
		return transform;
	}
	
	public AABB getAABB() {
		Vec2 pos = new Vec2(transform.position.x - DEFAULT_WIDTH / 2f, 
        		 			transform.position.y - DEFAULT_HEIGHT);
		return new AABB(pos.x, pos.y, pos.x + DEFAULT_WIDTH, pos.y + DEFAULT_HEIGHT);
	}
	
	public AABB getCollisionAABB() {
		return getAABB();
	}
	
	@Override
	public void render(PixelImage canvas, Camera camera) {		
		if(Settings.DEBUG) {
			Graphics2D g = canvas.asBufferedImage().createGraphics();
			g.setColor(Color.GREEN);
			AABB aabb = getAABB();
			aabb = camera.transform(aabb);
			g.drawRect((int)aabb.left, (int)aabb.top, (int)aabb.getWidth(), (int)aabb.getHeight());
//			g.drawLine((int)aabb.left, (int)aabb.bottom, (int)aabb.right, (int)aabb.bottom);
			g.dispose();
		}
	}
	
	public void renderIDMask(PixelImage canvas) { }
	
	private static int NEXT_ID = 0xFF000000;
	private static Map<Integer, Sprite> sprites = new HashMap<Integer, Sprite>();
	
	public static Sprite get(int ID) {
		return sprites.get(ID);
	}
	
}
