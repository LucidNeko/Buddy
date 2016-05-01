package game.entities;

import core.Sprite;
import game.Camera;
import graphics.PixelImage;

public class Timer extends Sprite {

	float time;
	
	public Timer() {
		time = 0;
	}
	
	@Override
	public void update(float delta) {
		time += delta;
	}
	
	@Override
	public void render(PixelImage canvas, Camera camera) {
		
	}

}
