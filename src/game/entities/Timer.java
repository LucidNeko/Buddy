package game.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import core.Sprite;
import game.Camera;
import graphics.PixelImage;
import math.Vec2;
import resources.R;
import util.Log;

public class Timer extends Sprite {

	float time;
	
	public Timer() {
		time = 0;
	}
	
	public void start() {
		time = 0;
	}
	
	@Override
	public void update(float delta) {
		time += delta;
	}
	
	@Override
	public void render(PixelImage canvas, Camera camera) {
		Vec2 pos = transform().position;
//		pos = camera.transform(pos);
		
		Graphics2D g = canvas.createGraphics();
		g.setFont(R.fonts.kenpixel_mini_square.deriveFont(12f));
		g.setColor(Color.YELLOW);
		g.drawString(String.format("Time: %.2f", time), pos.x(), pos.y());
		g.dispose();
		
		
	}

}
