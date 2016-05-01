package game.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import core.Sprite;
import game.Camera;
import graphics.PixelImage;
import math.AABB;
import math.Vec2;
import resources.R;

public class KeyHole extends Sprite {

	private PixelImage image = R.images.keyhole;
	
	public KeyHole() { 
		
	}

	@Override
	public void update(float delta) { }
	
	@Override
	public AABB getAABB() {
		Vec2 pos = new Vec2(transform().position.x - image.getWidth() / 2f, 
                			transform().position.y - image.getHeight());
		return new AABB(pos.x, pos.y, pos.x + image.getWidth(), pos.y + image.getHeight());
	}
	
	@Override
	public void render(PixelImage canvas, Camera camera) {
		AABB aabb = camera.transform(getAABB());
		canvas.blit(image, (int)aabb.left, (int)aabb.top);
	}
	
	@Override
	public void renderIDMask(PixelImage canvas) {
		AABB aabb = getAABB();
		
//		Graphics2D g = canvas.createGraphics();
//		int id = getID();
//		g.setColor(new Color(id << 16 & 0xFF, id << 8 & 0xFF, id & 0xFF));
//		g.fillRect((int)(aabb.left), (int)(aabb.top-20), (int)(aabb.getWidth()), (int)(aabb.getHeight() + 40));
//		g.dispose();
		canvas.blit(image, (int)aabb.left, (int)aabb.top, getID());
	}

}
