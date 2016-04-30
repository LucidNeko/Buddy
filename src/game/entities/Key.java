package game.entities;

import core.Sprite;
import game.Camera;
import game.Pickup;
import graphics.PixelImage;
import graphics.SpriteSheet;
import math.AABB;
import math.Vec2;
import resources.R;

public class Key extends Sprite implements Pickup {
	
	private PixelImage image = R.images.key;
	
	public Key() {
		
	}
	
	@Override
	public void onPickup(Sprite source) {
		
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
	public void renderIDMask(PixelImage canvas, Camera camera) {
		AABB aabb = camera.transform(getAABB());
		canvas.blit(image, (int)aabb.left, (int)aabb.top, getID());
	}

}
