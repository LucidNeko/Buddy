package game;

import java.awt.Color;

import core.Audio;
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

	private Vec2 velocity = new Vec2();
	private Vec2 forces = new Vec2(0, 1600f);
	private float moveSpeed = 120f;
	private float maxFallSpeed = 300f;
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
			velocity.y = -forces.y * 0.3f; // Fake jump
			velocity.x = 0;
		}
		
		if(!dead) {
			Vec2 move = new Vec2();
			if(controller.left()) move.x -= 1;
			if(controller.right()) move.x += 1;
			velocity.x = move.x * moveSpeed;
	
			if(isGrounded() || aerialManeuvers > 0) {
				if(controller.upOnce()) {
//					Audio.play(R.audio.jump_alt);
					velocity.y = -forces.y * 0.25f;
					if(!isGrounded()) aerialManeuvers--;
				}
			}
		}
		
		if(dead) {
			respawnCounter -= delta;
			if(respawnCounter <= 0) {
				level.reset();
			}
		}
		
//		if(Mathf.abs(transform().position.length() - transform().position.round().length()) < 0.2f) {
//			transform().position = transform().position.round();
//		}

		Transform transform = transform();
		velocity.y = Mathf.min(velocity.y, maxFallSpeed);
		Vec2 forces = new Vec2(this.forces);
		
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
		
		if(from.equals(to, 0.6f)) {
			return from;
		}
		
		AABB aabb = getAABB();
		Vec2 newPos = level.getMap().onMove(from, to, new Vec2(aabb.getWidth(), aabb.getHeight()));
		
		boolean ground = level.getMap().isGroundBelow(newPos);
		if(!isGrounded() && ground) {
			onGround();
		} else if(isGrounded() && !ground) {
			onLeaveGround();
		}
		
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
			pos = pos.ceil();
			return new AABB(pos.x, pos.y, pos.x + (int)frame.getWidth(), pos.y + (int)frame.getHeight());
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
	
	@Override
	public void renderIDMask(PixelImage canvas, Camera camera) {
		if(animator != null) {
			AABB aabb = getAABB();
			aabb = camera.transform(aabb);
			SpriteSheet.Frame frame = animator.getAnimation().getFrame();
			Vec2 pos = new Vec2(aabb.left, aabb.top);
			canvas.blit(frame.getImage(), pos.x(), pos.y(), this.getID());
		}
	}

}
