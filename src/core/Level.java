package core;

import math.Vec2;
import resources.R;
import graphics.PixelImage;

public class Level {
	
	public PixelImage background;
	public PixelImage foreground;
	public PixelImage collision;
	
	public Level(PixelImage background, PixelImage foreground, PixelImage collision) {
		this.background = background;
		this.foreground = foreground;
		this.collision = collision;
	}
	
	public boolean raycast(Vec2 start, Vec2 dir, Vec2 out, int testValue) {
		dir = dir.normalized();
		
		if(dir.x == 0 && dir.y == 0) {
			return false;
		}
		
		Vec2 pos = new Vec2(start);
		while(collision.testBounds(pos.x(), pos.y())) {
			if(!collision.testBounds(pos.x(), pos.y())) {
				return false;
			}
			
			if(collision.getAlpha(pos.x(), pos.y()) == testValue) {
				out.set(pos);
				return true;
			}
			
			pos.set(pos.add(dir));
		}
		return false;
	}
	
	public boolean collide(Vec2 pos) {
		if(collision.testBounds(pos.x(), pos.y()) && collision.getAlpha(pos.x(), pos.y()) != 0) {
			return true;
		}
		return false;
	}
	
	private static Level current = new Level(R.images.level_0002_background, R.images.level_0000_foreground, R.images.level_0001_collision);
	
	public static Level getCurrent() {
		return current;
	}
	
}
