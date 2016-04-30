package game;

import java.awt.Color;

import core.Sprite;
import ecs100.UI;
import game.Collision.Result;
import graphics.Animation;
import graphics.AnimationState;
import graphics.Animator;
import graphics.PixelImage;
import graphics.SpriteSheet;
import input.Keyboard;
import math.AABB;
import math.Mathf;
import math.Predicate;
import math.Transform;
import math.Vec2;
import resources.R;
import util.Log;

public class Player extends Sprite {
	
	private final int MAX_AERIAL_MANEUVERS = 1;
	
	private Controller controller;

	private Animator animator;

	protected Vec2 velocity = new Vec2();
	private Vec2 forces = new Vec2(0, 1600f);
	private float moveSpeed = 120f;
	
	private Level level;
	
	private boolean dead = false;
	
	float respawnCounter = 1f;
	
	private int aerialManeuvers = MAX_AERIAL_MANEUVERS;

	private boolean isGrounded = false;
	
	public Player(Controller controller) {
		super();
		this.controller = controller;
		
		AnimationState idleLeftState = new AnimationState(new Animation(R.sprites.player.idle_left));
		AnimationState idleRightState = new AnimationState(new Animation(R.sprites.player.idle_right));
		AnimationState leftState = new AnimationState(new Animation(R.sprites.player.idle_left));
		AnimationState rightState = new AnimationState(new Animation(R.sprites.player.idle_right));
		
		
		idleLeftState.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return velocity.x < 0;
			}
			
		}, leftState);
		
		idleLeftState.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return velocity.x > 0;
			}
			
		}, rightState);
		
		idleRightState.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return velocity.x > 0;
			}
			
		}, rightState);
		
		idleRightState.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return velocity.x < 0;
			}
			
		}, leftState);
		
		leftState.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return velocity.x >= 0;
			}
			
		}, idleLeftState);
		
		rightState.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return velocity.x <= 0;
			}
			
		}, idleRightState);
		
		this.animator = new Animator(idleRightState);
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	@Override
	public void update(float delta) {
		if(!dead && level.getMap().getCollisionLayer().isOutOfBounds(transform().position)) {
			dead = true;
			velocity.y = -forces.y * 0.3f;
			velocity.x = 0;
		}
		
		if(!dead) {
			Vec2 move = new Vec2();
			if(controller.left()) move.x -= 1;
			if(controller.right()) move.x += 1;
			velocity.x = move.x * moveSpeed;
	
//			Result result = new Result();
//			if(level.getMap().getCollisionLayer().raycast(transform().position, new Vec2(0, 1), result)) {
				if(isGrounded() || aerialManeuvers > 0) {
					if(controller.upOnce()) {
						velocity.y = -forces.y * 0.25f;
						if(!isGrounded()) aerialManeuvers--;
					}
				}
//			}
		}
		
		if(dead) {
			respawnCounter -= delta;
			if(respawnCounter <= 0) {
				level.reset();
			}
		}
		
		Vec2 forces = new Vec2(this.forces);
		if(velocity.y > 0) 
			forces.y *= 1f;
		
		velocity.y = Mathf.min(velocity.y, 300f);
		
		Transform transform = transform();
		
		Vec2 newPosition = transform.position.add(velocity.mul(delta)).add(forces.mul(0.5f).mul(delta*delta));
		velocity = velocity.add(forces.mul(delta));
		
		newPosition = onMove(transform.position, newPosition);
		
		transform.position.set(newPosition);
		
		if(this.animator != null) {
			this.animator.update(delta);
		}
		

		UI.setColor(Color.GREEN);
		UI.drawString(Log.format("Jumps: {}", aerialManeuvers), 100, 100);
		UI.drawString(Log.format("Grounded: {}", isGrounded), 100, 150);
	}

	private Vec2 onMove(Vec2 from, Vec2 to) {
		if(dead) {
			return to;
		}
		
		if(from.equals(to, 1f)) {
			return from;
		}
		
		Collision collision = level.getMap().getCollisionLayer();
		Collision dynamic = level.getMap().getDynamicCollisionLayer();
		
		Result result = new Result();
		if(collision.raycast(from, new Vec2(0, -1), result)) {
			float hitY = result.end.y;
			to.y = Mathf.max(to.y, hitY);
		}
		
		Vec2 delta = to.sub(from);
		Vec2 newPos = new Vec2();
		
		float upOffset = 3;
		
//		if(delta.x > delta.y) {
			//step x first
			
			int sign = Mathf.sign(delta.x);
			result = new Result();
			if(collision.raycast(from.sub(0, upOffset), new Vec2(sign, 0), result)) {
				float hitX = result.end.x;
//				Log.info("HITX: {}", hitX);
				newPos.x = sign < 0 ? Mathf.max(from.x + delta.x, hitX) : Mathf.min(from.x + delta.x, hitX);
			} else {
				newPos.x = to.x;
			}
			
			result = new Result();
			if(collision.raycast(new Vec2(newPos.x, from.y - upOffset), new Vec2(0, 1), result)) {
				float hitY = result.end.y;
//				Log.info("HITY: {}", hitY);
				newPos.y = Mathf.min(from.y, hitY);
			} else {
				newPos.y = to.y;
			}
			
			//step y
			
			result = new Result();
			if(collision.raycast(newPos, new Vec2(0, 1), result)) {
				float hitY = result.end.y;
//				Log.info("HITY: {}", hitY);
				newPos.y = Mathf.min(newPos.y + delta.y, hitY);
				
				if(newPos.y == hitY) {
					if(!isGrounded()) onGround();
					velocity.y = 0;
				} else {
					if(isGrounded()) onLeaveGround();
				}
			} else {
				newPos.y = to.y;
				if(isGrounded()) onLeaveGround();
			}
	//	}
		
		return newPos;
	}
	
	private void onGround() {
		isGrounded = true;
		aerialManeuvers = MAX_AERIAL_MANEUVERS;
	}
	
	private void onLeaveGround() {
		isGrounded = false;
	}
	
	public boolean isGrounded() {
		return isGrounded ;
	}
	
	@Override
	public AABB getAABB() {
		if(animator != null) {
			SpriteSheet.Frame frame = animator.getAnimation().getFrame();
			Vec2 pos = new Vec2(transform().position.x - frame.getWidth() / 2f, 
	                			transform().position.y - frame.getHeight());
//			pos = pos.ceil();
			return new AABB(pos.x, pos.y, pos.x + frame.getWidth(), pos.y + frame.getHeight());
		} else {
			return super.getAABB();
		}
	}

	public AABB getCollisionAABB() {
		AABB aabb = getAABB();
		float qw = aabb.getWidth()/4f;
		float qh = aabb.getHeight()/4f;
		return new AABB(aabb.left + qw, aabb.top + qh, aabb.right - qw, aabb.bottom);
	}
	
	@Override
	public void render(PixelImage canvas, Camera camera) {
		if(animator != null) {
			AABB aabb = getAABB();
			aabb = camera.transform(aabb);
			SpriteSheet.Frame frame = animator.getAnimation().getFrame();
			Vec2 pos = new Vec2(aabb.left, aabb.top);
			canvas.blit(frame.getImage(), pos.x(), pos.y());
		}
//		super.render(canvas, camera);
	}

}
