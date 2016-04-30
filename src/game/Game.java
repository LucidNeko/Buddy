package game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import core.GameLoop;
import ecs100.UI;
import graphics.CanvasRenderer;
import graphics.PixelImage;
import input.Keyboard;
import input.Mouse;
import resources.R;
import util.Log;

public class Game extends GameLoop {
	
	PixelImage canvas;
	
	Level level;
	
	int scale = 4;
	PixelImage scaledCanvas;
	
	private JFrame frame;
	
	public Game() {
		super(90, 120);
		level = new Level();
		canvas = new PixelImage(UI.getCanvasWidth()/scale, UI.getCanvasHeight()/scale);
		scaledCanvas = new PixelImage(canvas.getWidth() * scale, canvas.getHeight() * scale);
		
		UI.setFont(R.fonts.kenpixel_mini_square);
		UI.setFontSize(30);
		
//		frame = new JFrame();
//		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		
//		CanvasRenderer component = new CanvasRenderer(scaledCanvas);
//		Keyboard.register(component);
//		Mouse.register(component);
//		frame.add(component);
		
//		frame.pack();
//		frame.setVisible(true);
	}

	@Override
	protected void tick(float delta) {
		level.update(delta);
	}

	@Override
	protected void fixedTick(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void render() {
//		canvas.clear();

		synchronized(canvas) {
//			canvas.fill(0xFF000000);
			canvas.clear();
			level.render(canvas, null);
			synchronized(scaledCanvas) {
				scaledCanvas = PixelImage.scale(canvas, scaledCanvas, scale);
			}
		}
		
//		frame.repaint();
		
		//WHY??WHY??WHY??
			
//		synchronized(canvas) {
//			synchronized(scaledCanvas) {
//				PixelImage.scale(canvas, scaledCanvas, 4);
				UI.clearGraphics();
				UI.drawImage(scaledCanvas, 0, 0);
				UI.repaint();
//			}
//		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Game().start();
			}
		});
	}

}
