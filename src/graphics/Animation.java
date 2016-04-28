package graphics;

import math.Mathf;

public class Animation {
	
	private SpriteSheet.Frame[] frames;
	private float speed;
	private float timePerFrame;
	
	private int frame = 0;
	private float time = 0;
	
	public Animation(SpriteSheet spriteSheet, float speed) {
		frames = spriteSheet.getFrames();
		setSpeed(speed);
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = Mathf.max(0, speed);
		this.timePerFrame = this.speed / frames.length;
	}
	
	public void reset() {
		frame = 0;
		time = 0;
	}
	
	public void update(float delta) {
		time += delta;
		if(time >= timePerFrame) {
			time -= timePerFrame;
			frame = (frame+1) % frames.length;
		}
	}
	
	public SpriteSheet.Frame getFrame() {
		return frames[frame];
	}

}
