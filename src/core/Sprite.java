package core;

import java.awt.Color;
import java.awt.Graphics2D;

import graphics.Animator;
import graphics.PixelImage;
import graphics.SpriteSheet;
import math.AABB;
import math.Transform;
import math.Vec2;

public abstract class Sprite implements IUpdateable, IRenderable {
	
	private final int DEFAULT_WIDTH = 50;
	private final int DEFAULT_HEIGHT = 50;

	protected Transform transform = new Transform();
	protected Vec2 velocity = new Vec2();
	protected Vec2 forces = new Vec2(0, 1600f);
	
	protected float moveSpeed = 372.4f;
	
	protected Animator animator;
	
	public Sprite() { 
		
	}
	
	public void setAnimator(Animator animator) {
		this.animator = animator;
	}
	
	public Transform transform() {
		return transform;
	}
	
	public Vec2 renderPosition() {
		if(animator != null) {
			SpriteSheet.Frame frame = animator.getAnimation().getFrame();
			return new Vec2(transform.position.x - frame.getWidth() / 2f, 
			                transform.position.y - frame.getHeight() + 5);
		} else {
			return new Vec2(transform.position.x - DEFAULT_WIDTH / 2f, 
	                		transform.position.y - DEFAULT_HEIGHT);
		}
	}
	
	public AABB getAABB() {
		Vec2 pos = renderPosition();
		if(animator != null) {
			SpriteSheet.Frame frame = animator.getAnimation().getFrame();
			return new AABB(pos.x, pos.y, pos.x + frame.getWidth(), pos.y + frame.getHeight());
		} else {
			return new AABB(pos.x, pos.y, pos.x + DEFAULT_WIDTH, pos.y + DEFAULT_HEIGHT);
		}
	}

	@Override
	public abstract void update(float delta);
	
	@Override
	public void render(PixelImage canvas) {		
		Graphics2D g = canvas.asBufferedImage().createGraphics();
		g.setColor(Color.GREEN);
		g.fillOval(transform.position.x()-2, transform.position.y()-2, 5, 5);
		AABB aabb = getAABB();
		g.drawRect((int)aabb.left, (int)aabb.top, (int)aabb.getWidth(), (int)aabb.getHeight());
		g.dispose();
	}
	
}
