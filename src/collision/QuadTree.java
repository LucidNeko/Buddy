package collision;

import graphics.PixelImage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import core.IRenderable;
import core.Sprite;
import game.Camera;

public class QuadTree implements IRenderable {

	private static final int MAX_NODE_SIZE = 4;
	
	private QuadTreeNode root;
	
	public QuadTree(int left, int top, int right, int bottom) {
		root = new QuadTreeNode(left, top, right, bottom);
	}
	
	public boolean add(Sprite sprite) {
		return root.add(sprite);
	}

	@Override
	public void render(PixelImage canvas, Camera camera) {
		root.render(canvas, camera);
	}
	
	public void clear() {
		root.clear();
	}
	
	private static class QuadTreeNode implements IRenderable {
		public int left;
		public int top;
		public int right;
		public int bottom;
		
		public boolean split = false;
		
		public QuadTreeNode NW;
		public QuadTreeNode NE;
		public QuadTreeNode SW;
		public QuadTreeNode SE;
		
		public List<Sprite> sprites = new ArrayList<Sprite>(MAX_NODE_SIZE + 1);
		
		public QuadTreeNode(int left, int top, int right, int bottom) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
		
		public boolean contains(float x, float y) {
			return x >= left && x < right && y >= top && y < bottom;
		}
		
		public boolean add(Sprite sprite) {
			float x = sprite.transform().position.x;
			float y = sprite.transform().position.y;
			
			if(!contains(x, y)) {
				return false;
			}
			
			if(split) {
				if(NW.contains(x, y)) return NW.add(sprite);
				if(NE.contains(x, y)) return NE.add(sprite);
				if(SW.contains(x, y)) return SW.add(sprite);
				if(SE.contains(x, y)) return SE.add(sprite);
			} else {
				sprites.add(sprite);
				
				if(sprites.size() == MAX_NODE_SIZE) {
					split();
				}
				
				return true;
			}
			return false;
		}
		
		public void split() {
			int halfwidth  = (right - left) / 2;
			int halfheight = (bottom - top) / 2;
			
			NW = new QuadTreeNode(left,             top,              left + halfwidth , top + halfheight);
			NE = new QuadTreeNode(left + halfwidth, top,              right,             top + halfheight);
			SW = new QuadTreeNode(left,             top + halfheight, left + halfwidth,  bottom);
			SE = new QuadTreeNode(left + halfwidth, top + halfheight, right,             bottom);
			
			for(Sprite sprite : sprites) {
				if(NW.add(sprite)) continue;
				if(NE.add(sprite)) continue;
				if(SW.add(sprite)) continue;
				if(SE.add(sprite)) continue;
			}
			
			if(NW.size() != sprites.size() &&
			   NE.size() != sprites.size() &&
			   SW.size() != sprites.size() &&
			   SE.size() != sprites.size()) 
			{
				sprites.clear();
				split = true;
			} else {
				NW = NE = SW = SE = null;
			}
		}
		
		public void clear() {
			sprites.clear();
			split = false;
			NW = null;
			NE = null;
			SW = null;
			SE = null;
		}
		
		public int size() {
			return sprites.size();
		}

		@Override
		public void render(PixelImage canvas, Camera camera) {
			if(split) {
				NW.render(canvas, camera);
				NE.render(canvas, camera);
				SW.render(canvas, camera);
				SE.render(canvas, camera);
			}
			
			Graphics2D g = canvas.asBufferedImage().createGraphics();
			g.setColor(Color.GREEN);
			g.drawRect(left, top, right-left, bottom-top);
			g.dispose();
		}
		
		
	}
	
}
