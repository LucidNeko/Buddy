package game.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import core.Audio;
import core.Sprite;
import ecs100.UI;
import game.Camera;
import game.Collision.Result;
import game.Enemy;
import game.Killable;
import game.Level;
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

public class Walker extends Sprite implements Killable, Enemy {

	private Animator animator;

	private Vec2 velocity = new Vec2();
	private Vec2 forces = new Vec2(0, 1600f);
	private float moveSpeed = 60f;
	private float maxFallSpeed = 300f;
	private Level level;
	
	private boolean dead = false;
	private boolean isGrounded = false;
	private boolean flash = false;
	
	private boolean killTriggered = false;
	
	public Walker() {
		super();
		AnimationState idleLeftState = new AnimationState(new Animation(R.sprites.enemies.walky.idle_left));
		AnimationState idleRightState = new AnimationState(new Animation(R.sprites.enemies.walky.idle_right));
		AnimationState leftState = new AnimationState(new Animation(R.sprites.enemies.walky.walk_left));
		AnimationState rightState = new AnimationState(new Animation(R.sprites.enemies.walky.walk_right));
		
		velocity.x = moveSpeed;
		
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
		return "Walky";
	}
	
	@Override
	public void update(float delta) {
		if(!dead && (killTriggered || (level.getMap().getCollisionLayer().isOutOfBounds(transform().position)))) {
			dead = true;
			velocity.y = -forces.y * 0.3f; // Fake jump
			velocity.x = 0;
			flash = true;
			Audio.play(R.audio.enemydie);
		}

		Transform transform = transform();
		velocity.y = Mathf.min(velocity.y, maxFallSpeed);
		
		int sign = Mathf.sign(velocity.x);
		velocity.x = sign * moveSpeed;
		Result result = new Result();
		if(level.getMap().getCollisionLayer().raycast(transform.position, new Vec2(sign, 0), result)) {
			if(result.distanceTravelled < 2) {
				velocity.x = -sign * moveSpeed;
			}
		}
		
		
		Vec2 forces = new Vec2(this.forces);
		
		Vec2 newPosition = transform.position.add(velocity.mul(delta)).add(forces.mul(0.5f).mul(delta*delta));
		velocity = velocity.add(forces.mul(delta));
		
		Vec2 dir = newPosition.sub(transform.position);
		if(dir.x <= 0 && velocity.x > 0) {
			velocity.x = -moveSpeed;
		} else if(dir.x >= 0 && velocity.x < 0) {
			velocity.x = moveSpeed;
		}
		
		newPosition = onMove(transform.position, newPosition);
		newPosition = level.getMap().getFreePointAroundIfOOB(newPosition);
		
		transform.position.set(newPosition);
		
		if(this.animator != null) {
			this.animator.update(delta);
		}
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
			
//			Graphics2D g = canvas.createGraphics();
//			g.setColor(Color.yellow);
//			g.setFont(R.fonts.kenpixel_mini_square.deriveFont(8f));
//			float hw = g.getFontMetrics().stringWidth(getName()) * 0.5f;
//			hw = aabb.getWidth()*0.5f - hw;
//			g.drawString(getName(), aabb.left + hw, aabb.top);
//			g.dispose();
		}
//		super.render(canvas, camera);
	}
	
	@Override
	public void renderIDMask(PixelImage canvas) {
		if(animator != null && !dead) {
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
