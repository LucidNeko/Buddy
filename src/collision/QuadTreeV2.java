package collision;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import core.Sprite;
import math.AABB;

public class QuadTreeV2 {
	
	private QuadTreeNode root;
	
	public QuadTreeV2(AABB bounds) {
		root = new QuadTreeNode(bounds);
	}
	
	public Set<Sprite> getSprites(Sprite sprite) {
		Set<Sprite> sprites = getSprites(sprite.getAABB());
		sprites.remove(sprite);
		return sprites;
	}
	
	public Set<Sprite> getSprites(AABB intersects) {
		return null;
	}
	
	private static class QuadTreeNode {
		
		public AABB bounds;
		
		public QuadTreeNode NW;
		public QuadTreeNode NE;
		public QuadTreeNode SW;
		public QuadTreeNode SE;
		
		public Set<Sprite> sprites = new HashSet<Sprite>();
		
		public QuadTreeNode(AABB bounds) {
			bounds = new AABB(bounds);
		}
		
		public boolean isSplit() {
			return NW != null;
		}
		
	}

}
