package game;

import java.util.ArrayList;
import java.util.List;

import core.IRenderable;
import core.Sprite;
import game.Collision.Result;
import graphics.PixelImage;
import math.Mathf;
import math.Vec2;
import resources.R;

public abstract class GameMap {

	protected List<Layer> background = new ArrayList<Layer>();
	protected List<Layer> world = new ArrayList<Layer>();
	protected List<Layer> hazards = new ArrayList<Layer>();
	protected List<Layer> foreground = new ArrayList<Layer>();
	
	protected Collision collision;
	protected Collision dynamicCollision;
	
	public GameMap() { 
		init();
	}
	
	public abstract void init();
	
	public Vec2 onMove(Vec2 from, Vec2 to, Vec2 halfSize) {
		Vec2 pos = onMove(from, to, halfSize, collision);
		
		
		
		return pos;
	}
	
	private Vec2 onMove(Vec2 from, Vec2 to, Vec2 halfSize, Collision collision) {		
		Result result = new Result();
		if(collision.raycast(from, new Vec2(0, -1), result)) {
			float hitY = result.end.y;
			to.y = Mathf.max(to.y, hitY);
		}
		
		Vec2 delta = to.sub(from);
		Vec2 newPos = new Vec2();
		
		float upOffset = 3;
		
		boolean goingUp = false;
		
		if(!isGapAbove(from, 6)) {
			result = new Result();
			if(collision.raycast(from, new Vec2(0, -1), result)) {
				float diff = from.y - result.end.y;
				upOffset = Mathf.min(upOffset, diff);
			}
		} else {
			goingUp = true;
		}
		
		
		
		//step x first
		
		int sign = Mathf.sign(delta.x);
		result = new Result();
		if(collision.raycast(from.sub(0, upOffset), new Vec2(sign, 0), result)) {
			float hitX = result.end.x;
			newPos.x = sign < 0 ? Mathf.max(from.x + delta.x, hitX) : Mathf.min(from.x + delta.x, hitX);
		} else {
			newPos.x = to.x;
		}
		
		result = new Result();
		if(collision.raycast(new Vec2(newPos.x, from.y - upOffset), new Vec2(0, 1), result)) {
			float hitY = result.end.y;
			newPos.y = Mathf.min(from.y, hitY);
		} else {
			newPos.y = to.y;
		}
		
		//step y
		
		result = new Result();
		if(collision.raycast(newPos, new Vec2(0, 1), result)) {
			float hitY = result.end.y;
			newPos.y = Mathf.min(newPos.y + delta.y, hitY);
		} else {
			newPos.y = to.y;
		}
		
		if(!goingUp && collision.collide(newPos)) {
			return from;
		}
		
		return newPos;
	}
	
	public boolean isGroundBelow(Vec2 pos, int pixels) {
		boolean result = false;
		for(int y = 1; y <= pixels; y++) {
			if(collision.getImage().testBounds(pos.x(), pos.y() + y)) {
				if(collision.getImage().getAlpha(pos.x(), pos.y() + y) != 0) {
					result |= true;
				}
			}
		}
		return result;
	}
	
	public boolean isGroundBeside(Vec2 pos) {
		int x = pos.x();
		int y = pos.y();
		if((collision.getImage().testBounds(x-1, y) && collision.getImage().getAlpha(x-1, y) != 0) ||
		   (collision.getImage().testBounds(x+1, y) && collision.getImage().getAlpha(x+1, y) != 0))
		{
			return true;
		}
		return false;
		
	}
	
	public boolean isGapAbove(Vec2 pos, int pixels) {
		for(int y = 3; y <= pixels; y++) {
			if(collision.getImage().testBounds(pos.x(), pos.y() - y)) {
				if(collision.getImage().getAlpha(pos.x(), pos.y() - y) == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isOnHazard(Vec2 pos, int pixels) {
		boolean result = false;
		for(int y = 1; y <= pixels; y++) {
			for(Layer hazard : hazards) {
				if(hazard.getImage().testBounds(pos.x(), pos.y() + y)) {
					if(hazard.getImage().getAlpha(pos.x(), pos.y() + y) != 0) {
						result |= true;
					}
				}
			}
		}
		return result;
	}
	
	public boolean canFallThrough(Vec2 pos, int pixels, Vec2 outUnderPos) {
		boolean result = false;
		for(int y = 1; y <= pixels; y++) {
			Vec2 test = pos.add(0, y);
			if(collision.getImage().testBounds(test.x(), test.y())) {
				if(collision.getImage().getAlpha(test.x(), test.y()) == 0) {
					outUnderPos.set(test);
					result |= true;
				}
			}
		}
		return result;
	}
	
	public int getWidth() {
		return collision.getWidth();
	}
	
	public int getHeight() {
		return collision.getHeight();
	}
	
	public Collision getCollisionLayer() {
		return collision;
	}
	
	public Collision getDynamicCollisionLayer() {
		return dynamicCollision;
	}
	
	public List<Layer> getBackgroundLayers() {
		return background;
	}
	
	public List<Layer> getWorldLayers() {
		return world;
	}
	
	public List<Layer> getHazardLayers() {
		return hazards;
	}
	
	public List<Layer> getForegroundLayers() {
		return foreground;
	}

	public static class Layer {
		private PixelImage image;
		public Layer(PixelImage image) {
			this.image = image;
		}
		
		public PixelImage getImage() {
			return image;
		}
	}

}
