import input.Keyboard;

import java.awt.event.KeyEvent;

import math.Mathf;
import math.Predicate;
import math.Vec2;
import graphics.Animation;
import graphics.AnimationState;
import graphics.Animator;
import graphics.PixelImage;
import resources.R;
import util.Log;
import core.Level;
import core.Sprite;


public class Mami extends Sprite {

	private Animation idle = new Animation(R.images.hero.mami_idle, 0.45f);
	private Animation run_right = new Animation(R.images.hero.mami_run_right, 0.45f);
	private Animation run_left = new Animation(R.images.hero.mami_run_left, 0.45f);
	
	public Mami() {
		super();
		
		AnimationState idleState = new AnimationState(idle);
		AnimationState leftState = new AnimationState(run_left);
		AnimationState rightState = new AnimationState(run_right);
		
		idleState.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return velocity.x < 0;
			}
			
		}, leftState);
		
		idleState.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return velocity.x > 0;
			}
			
		}, rightState);
		
		leftState.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return velocity.x >= 0;
			}
			
		}, idleState);
		
		rightState.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return velocity.x <= 0;
			}
			
		}, idleState);
		
		setAnimator(new Animator(idleState));
	}
	
	@Override
	public void update(float delta) {
		Vec2 move = new Vec2();
		if(Keyboard.isKeyDown(KeyEvent.VK_A)) move.x -= 1;
		if(Keyboard.isKeyDown(KeyEvent.VK_D)) move.x += 1;
		velocity.x = move.x * moveSpeed;
		
		if(Keyboard.isKeyDownOnce(KeyEvent.VK_SPACE)) {
			velocity.y = -forces.y * 0.5f;
		}
		
		Vec2 forces = new Vec2(this.forces);
		if(velocity.y > 0) forces.y *= 2;
		
		Vec2 newPosition = transform.position.add(velocity.mul(delta)).add(forces.mul(0.5f).mul(delta*delta));
		velocity = velocity.add(forces.mul(delta));
		
		newPosition = collisionPass(transform.position, newPosition);
		
		transform.position.set(newPosition);
		
		if(animator != null) {
			animator.update(delta);
		}
	}
	
	private Vec2 collisionPass(Vec2 oldPos, Vec2 newPos) {
		
//		Vec2 move = newPos.sub(oldPos);
//		Vec2 dir = move.normalized();
		
		if(Level.getCurrent().collide(oldPos)) {
			Vec2 out = new Vec2();
			if(Level.getCurrent().raycast(oldPos, new Vec2(0, -1), out, 0)) {
				
			}
			oldPos.y = out.y;
		}
		
		Vec2 out = new Vec2();
		if(Level.getCurrent().raycast(oldPos, new Vec2(0, 1), out, 255)) {
			
		}
		
		Log.info("Ground: {}", out.y);
		
		if(Mathf.min(newPos.y, out.y) == out.y) {
			velocity.y = 0;
		}
		
		return new Vec2(newPos.x, Mathf.min(newPos.y, out.y));
	}
	
	@Override
	public void render(PixelImage canvas) {
		if(animator != null) {
			Vec2 pos = renderPosition();
			PixelImage image = animator.getAnimation().getFrame().getImage();
			canvas.blit(image, pos.x(), pos.y());
		}
		
//		super.render(canvas);
	}
	
}
