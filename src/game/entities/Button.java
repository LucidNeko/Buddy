package game.entities;

import core.Audio;
import core.Sprite;
import game.Camera;
import game.Interactable;
import graphics.Animation;
import graphics.AnimationState;
import graphics.Animator;
import graphics.PixelImage;
import graphics.SpriteSheet;
import math.AABB;
import math.Mathf;
import math.Predicate;
import math.Transform;
import math.Vec2;
import resources.R;

public class Button extends Sprite implements Interactable {
	
	private Animator animator;
	
	private boolean pressed = false;
	
	private Transform target;
	private Vec2 start;
	private Vec2 end;
	
	private boolean moving;
	float t = 0;
	
	float speed = 1;
	
	public Button(Transform target, float upAmount) {
		this.target = target;
		this.start = new Vec2(target.position);
		this.end = new Vec2(target.position.x, target.position.y - upAmount);
		
		AnimationState up = new AnimationState(new Animation(R.sprites.button.up));
		AnimationState down = new AnimationState(new Animation(R.sprites.button.down));
		
		up.addTransition(new Predicate() {

			@Override
			public boolean test() {
				return pressed;
			}
			
		}, down);
		
		animator = new Animator(up);
	}

	@Override
	public void update(float delta) { 
		animator.update(delta);
		
		if(moving) {
			t += delta * speed;
			
			t = Mathf.clamp(t, 0, 1);
			
			target.position.set(start.sslerp(end, t));
			
			if(t == 1) 
				moving = false;
		}
	}

	@Override
	public void interact(Sprite source) {
		if(!pressed) {
			pressed = true;
			moving = true;
			Audio.play(R.audio.explosion);
		}
	}

	@Override
	public AABB getAABB() {
		SpriteSheet.Frame frame = animator.getAnimation().getFrame();
		Vec2 pos = new Vec2(transform().position.x - frame.getWidth() / 2f, 
                			transform().position.y - frame.getHeight());
		return new AABB(pos.x, pos.y, pos.x + frame.getWidth(), pos.y + frame.getHeight());
	}
	
	@Override
	public void render(PixelImage canvas, Camera camera) {
		SpriteSheet.Frame frame = animator.getAnimation().getFrame();
		AABB aabb = camera.transform(getAABB());
		canvas.blit(frame.getImage(), (int)aabb.left, (int)aabb.top);
	}
	
	@Override
	public void renderIDMask(PixelImage canvas) {
		SpriteSheet.Frame frame = animator.getAnimation().getFrame();
		AABB aabb = getAABB();
		canvas.blit(frame.getImage(), (int)aabb.left, (int)aabb.top, getID());
	}

}
