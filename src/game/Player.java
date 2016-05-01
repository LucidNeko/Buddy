package game;

import java.awt.Color;
import java.awt.Graphics2D;

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

public class Player extends Sprite implements Killable {
	
	private final int MAX_AERIAL_MANEUVERS = 1;
	
	private Controller controller;

	private Animator animator;

	private Vec2 velocity = new Vec2();
	private Vec2 forces = new Vec2(0, 1600f);
	private float moveSpeed = 120f;
	private float maxFallSpeed = 300f;
	private Level level;
	
	private boolean dead = false;
	
	private boolean killTriggered = false;
	
	float respawnCounter = 2f;
	
	private int aerialManeuvers = MAX_AERIAL_MANEUVERS;

	private boolean isGrounded = false;
	
	private boolean flash = false;

	private boolean isDownDoubleTap = false;
	private TapTimer downDoubleTapTimer = new TapTimer() {

		@Override
		public boolean onDoubleTap() {
			return isDownDoubleTap = true;
		}

		@Override
		public boolean isTap() {
			return controller.downOnce();
		}

		@Override
		public boolean onDoubleTapOff() {
			return isDownDoubleTap = false;
		}
		
	};
	
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
	
	public Level getLevel() {
		return level;
	}
	
	public String getName() {
		return controller == Controller.P1 ? "P1" : "P2";
	}
	
	@Override
	public void update(float delta) {
		if(!dead && (killTriggered || level.getMap().getCollisionLayer().isOutOfBounds(transform().position) || level.getMap().isOnHazard(transform().position, 3))) {
			dead = true;
			velocity.y = -forces.y * 0.3f; // Fake jump
			velocity.x = 0;
			flash = true;
			Audio.play(R.audio.hurt);
		}
		
		if(!dead) {
			Vec2 move = new Vec2();
			if(controller.left()) move.x -= 1;
			if(controller.right()) move.x += 1;
			velocity.x = move.x * moveSpeed;
	
			if(isGrounded() || aerialManeuvers > 0) {
				if(controller.upOnce()) {
					Audio.play(R.audio.jump_alt);
					velocity.y = -forces.y * 0.25f;
					if(!isGrounded()) aerialManeuvers--;
				}
			}
		}
		
		if(dead) {
			respawnCounter -= delta;
			if(respawnCounter <= 0) {
				level.reset();//GameMaps.getRandomMap());
			}
		}
		
		downDoubleTapTimer.update(delta);
		
		Vec2 outUnder = new Vec2();
		if(!dead && level.getMap().isGroundBelow(transform().position, 1) && level.getMap().canFallThrough(transform().position, 8, outUnder)) {
			if(this.isDownDoubleTap) {
				transform().position.set(outUnder);
				this.downDoubleTapTimer.reset();
			}
		}
		
//		if(Mathf.abs(transform().position.length() - transform().position.round().length()) < 0.4f) {
//			transform().position = transform().position.round();
//		}

		Transform transform = transform();
		velocity.y = Mathf.min(velocity.y, maxFallSpeed);
		Vec2 forces = new Vec2(this.forces);
		
		Vec2 newPosition = transform.position.add(velocity.mul(delta)).add(forces.mul(0.5f).mul(delta*delta));
		velocity = velocity.add(forces.mul(delta));
		
		
		
		newPosition = onMove(transform.position, newPosition);
		newPosition = level.getMap().getFreePointAroundIfOOB(newPosition);
		
		transform.position.set(newPosition);
		
		if(this.animator != null) {
			this.animator.update(delta);
		}
		
//		UI.setColor(Color.GREEN);
//		UI.drawString(Log.format("OOB: {}", level.getMap().getCollisionLayer().collide(transform().position)), 100, 100);
//		UI.drawString(Log.format("Grounded: {}", isGrounded), 100, 150);
//
//		UI.drawString(Log.format("Can Fall: {}", level.getMap().canFallThrough(transform().position, 6, new Vec2())), 100, 450);
	}

	private Vec2 onMove(Vec2 from, Vec2 to) {
		if(dead) {
			return to;
		}
		
		if(from.equals(to, 0.6f)) {
			return from;
		}
		
		AABB aabb = getAABB();
		Vec2 newPos = level.getMap().onMove(from, to, new Vec2(aabb.getWidth()*0.5f, aabb.getHeight()*0.5f), this);
		
		boolean ground = level.getMap().isGroundBelow(newPos, 3);
		if(!isGrounded() && ground) {
			onGround();
		} else if(isGrounded() && !ground) {
			onLeaveGround();
		}
		
		
		
		return newPos;
	}
	
	public boolean isDead() {
		return dead;
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
			if(flash)
				canvas.blit(frame.getImage(), pos.x(), pos.y(), 0xFF6A1F4E);
			else
				canvas.blit(frame.getImage(), pos.x(), pos.y());
			
			Graphics2D g = canvas.createGraphics();
			g.setColor(Color.yellow);
			g.setFont(R.fonts.kenpixel_mini_square.deriveFont(8f));
			float hw = g.getFontMetrics().stringWidth(getName()) * 0.5f;
			hw = aabb.getWidth()*0.5f - hw;
			g.drawString(getName(), aabb.left + hw, aabb.top);
			g.dispose();
		}
//		super.render(canvas, camera);
	}
	
	@Override
	public void renderIDMask(PixelImage canvas) {
		if(animator != null) {
			AABB aabb = getAABB();
			SpriteSheet.Frame frame = animator.getAnimation().getFrame();
			Vec2 pos = new Vec2(aabb.left, aabb.top);
			canvas.blit(frame.getImage(), pos.x(), pos.y(), this.getID());
		}
	}

	@Override
	public void kill() {
		killTriggered = true;
	}

}
