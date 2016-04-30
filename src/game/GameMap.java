package game;

import java.util.ArrayList;
import java.util.List;

import core.IRenderable;
import core.Sprite;
import graphics.PixelImage;
import resources.R;

public class GameMap {

	private List<Layer> background = new ArrayList<Layer>();
	private List<Layer> foreground = new ArrayList<Layer>();
	
	private Collision collision;
	private Collision dynamicCollision;
	
	public GameMap() {
		background.add(new Layer(R.levels.level000.background7));
		background.add(new Layer(R.levels.level000.Level01_world));
		foreground.add(new Layer(R.levels.level000.Level01_decorations));
		collision = new Collision(R.levels.level000.Level01_world);
		dynamicCollision = new Collision(collision.getWidth(), collision.getHeight());
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
