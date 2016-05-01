package game.entities;

import core.Audio;
import core.Sprite;
import game.Camera;
import game.Interactable;
import game.Player;
import graphics.PixelImage;
import graphics.SpriteSheet;
import math.AABB;
import math.Transform;
import math.Vec2;
import resources.R;

public class Key extends Sprite implements Interactable {
	
	private PixelImage image = R.images.key;
	
	private Sprite owner;
	
	private Transform target;
	
	public Key(Transform target) {
		this.target = target;
	}
	
	@Override
	public void interact(Sprite source) {
		owner = source;
		Audio.play(R.audio.coin);
	}

	@Override
	public void update(float delta) { 
		if(owner != null) {
			transform().position = owner.transform().position.add(14, 0); 
		}
		
		if(owner != null && this.transform().position.sub(target.position).length() < 8) {
			((Player)owner).getLevel().endLevel();
		}
	}
	
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
		if(owner == null) {
			AABB aabb = getAABB();
			canvas.blit(image, (int)aabb.left, (int)aabb.top, getID());
		}
	}

}
