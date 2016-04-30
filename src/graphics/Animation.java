package graphics;

import math.Mathf;

public class Animation {
	
	private SpriteSheet.Frame[] frames;
	private float secondsPerFrame;
	
	private int frame = 0;
	private float time = 0;
	
	public Animation(SpriteSheet spriteSheet) {
		this(spriteSheet, spriteSheet.getSecondsPerFrame());
	}
	
	public Animation(SpriteSheet spriteSheet, float secondsPerFrame) {
		frames = spriteSheet.getFrames();
		setSecondsPerFrame(secondsPerFrame);
	}
	
	public float getSecondsPerFrame() {
		return secondsPerFrame;
	}
	
	public void setSecondsPerFrame(float secondsPerFrame) {
		this.secondsPerFrame = Mathf.max(0, secondsPerFrame);
	}
	
	public void reset() {
		frame = 0;
		time = 0;
	}
	
	public void update(float delta) {
		time += delta;
		if(time >= secondsPerFrame) {
			time -= secondsPerFrame;
			frame = (frame+1) % frames.length;
		}
	}
	
	public SpriteSheet.Frame getFrame() {
		return frames[frame];
	}

}
