package game;

import java.awt.Color;
import java.awt.Graphics2D;

import core.Sprite;
import graphics.PixelImage;
import math.AABB;
import math.Vec2;
import util.Log;

public class Collision {
	
	private PixelImage image;
	
	public Collision(int width, int height) {
		this(new PixelImage(width, height));
	}
	
	public Collision(PixelImage image) {
		this.image = image;
		
//		for(int y = 0; y < image.getHeight(); y++) {
//			for(int x = 0; x < image.getWidth(); x++) {//				
//				int left = -1;
//				int right = -1;
//				int up = -1;
//				int upp = -1;
//				int down = -1;
//				
//				if(image.testBounds(x-1, y)) left = image.getAlpha(x-1, y);
//				if(image.testBounds(x+1, y)) right = image.getAlpha(x+1, y);
//				if(image.testBounds(x, y-1)) up = image.getAlpha(x, y-1);
//				if(image.testBounds(x, y+1)) down = image.getAlpha(x, y+1);
//				
//				int count = 0;
//				if(left != 0) count++;
//				if(right != 0) count++;
//				if(up != 0) count++;
//				if(down != 0) count++;
//				
//				if(count >= 3) {
//					image.setARGB(x, y, 0xFF000000);
//				}
//			}
//		}
		
	}
	
	public void add(PixelImage image) {
		this.image.blit(image, 0, 0, 0xFF000000);
	}
	
	public boolean raycast(Vec2 start, Vec2 dir, Result out) {
		dir = dir.normalized();
		
		out.start = new Vec2(start);
		out.direction = new Vec2(dir);
		
		if(dir.x == 0 && dir.y == 0) {
			return false;
		}
		
		float distanceTravelled = 0;
		float distancePerStep = dir.length();
		Vec2 lastPos = new Vec2(start);
		Vec2 pos = new Vec2(start);
		while(image.testBounds(pos.x(), pos.y())) {
			
			
			if(!image.testBounds(pos.x(), pos.y())) {
				return false;
			}
			
			if(image.getAlpha(pos.x(), pos.y()) != 0) {
				out.end = new Vec2(lastPos);
				out.sprite = Sprite.get(image.getARGB(pos.x(), pos.y()));
				out.distanceTravelled = distanceTravelled;
				return true;
			}
			
			lastPos.set(pos);
			pos.set(pos.add(dir));
			distanceTravelled += distancePerStep;
		}
		return false;
	}
	
	public boolean collidePath(Vec2 start, Vec2 end, Result out) {
		Vec2 dir = end.sub(start);
		float maxDistance = dir.normalize();
		
		out.start = new Vec2(start);
		out.direction = new Vec2(dir);
		
		if(dir.x == 0 && dir.y == 0) {
			return false;
		}
		
		float distance = 0f;
		Vec2 lastPos = new Vec2(start);
		Vec2 pos = new Vec2(start);
		while(image.testBounds(pos.x(), pos.y())) {
			
			if(!image.testBounds(pos.x(), pos.y()) || distance > maxDistance) {
				return false;
			}
			
			if(image.getAlpha(pos.x(), pos.y()) != 0) {
				out.end = new Vec2(lastPos);
				out.sprite = Sprite.get(image.getARGB(pos.x(), pos.y()));
				return true;
			}
			
			lastPos.set(pos);
			pos.set(pos.add(dir));
			distance += dir.length();
		}
		return false;
	}
	
	public boolean collideNeighbour(Vec2 start, Vec2 dir, Result out) {
		Vec2 pos = start.add(dir);
		if(image.getAlpha(pos.x(), pos.y()) != 0) {
			out.start = new Vec2(start);
			out.direction = new Vec2(dir);
			out.end = new Vec2(start);
			out.sprite = Sprite.get(image.getARGB(pos.x(), pos.y()));
			return true;
		}
		return false;
	}
	
//	public float testX(float start, float end) {
//		
//	}
	
	public boolean collide(Vec2 pos) {
		if(image.testBounds(pos.x(), pos.y()) && image.getAlpha(pos.x(), pos.y()) != 0) {
			return true;
		}
		return false;
	}
	
	public boolean isOutOfBounds(Vec2 v) {
		return v.x() < 0 || v.y() < 0 || v.x() >= getWidth() || v.y() >= getHeight();
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public void clear() {
		image.fill(0);
	}
	
	public void add(Sprite sprite) {
		AABB box = sprite.getCollisionAABB();
		
		Graphics2D g = image.createGraphics();
		
		int id = sprite.getID();
		Color color = new Color(id >> 16 & 0xFF, id >> 8 & 0xFF, id & 0xFF);
		g.setColor(color);
		g.fillRect((int)(box.left), (int)(box.top), (int)(box.right - box.left), (int)(box.bottom - box.top));
		g.dispose();
	}
	
	
	
	public static class Result {
		public Vec2 start;
		public Vec2 direction;
		public Vec2 end;	
		public Sprite sprite;
		public float distanceTravelled;
		
		@Override
		public String toString() {
			return "Result [start=" + start + ", direction=" + direction + ", end=" + end + ", sprite=" + (sprite != null ? sprite.getID() : sprite) + ", distanceTravelled=" + distanceTravelled + "]";
		}
		
		
	}

	public PixelImage getImage() {
		return image;
	}

}
