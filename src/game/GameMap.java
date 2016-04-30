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

public class GameMap {

	private List<Layer> background = new ArrayList<Layer>();
	private List<Layer> world = new ArrayList<Layer>();
	private List<Layer> foreground = new ArrayList<Layer>();
	
	private Collision collision;
	private Collision dynamicCollision;
	
	public GameMap() {
		background.add(new Layer(R.levels.level000.background7));
		world.add(new Layer(R.levels.level000.Level01_world));
		world.add(new Layer(R.levels.level000.Level01_hazards));
		world.add(new Layer(R.levels.level000.Level01_oneside));
		foreground.add(new Layer(R.levels.level000.Level01_decorations));
		collision = new Collision(R.levels.level000.Level01_world);
		dynamicCollision = new Collision(collision.getWidth(), collision.getHeight());
	}
	
	public Vec2 onMove(Vec2 from, Vec2 to, Vec2 halfSize) {
		Collision collision = this.collision;
		Collision dynamic = this.dynamicCollision;
		
		Result result = new Result();
		if(collision.raycast(from, new Vec2(0, -1), result)) {
			float hitY = result.end.y;
			to.y = Mathf.max(to.y, hitY);
		}
		
		Vec2 delta = to.sub(from);
		Vec2 newPos = new Vec2();
		
		float upOffset = 3;
		
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
		
		return newPos;
	}
	
	public boolean isGroundBelow(Vec2 pos) {
		int x = pos.x();
		int y = pos.y() + 1;
		if(collision.getImage().testBounds(x, y)) {
			if(collision.getImage().getAlpha(x, y) != 0) {
				return true;
			}
		}
		return false;
		
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
