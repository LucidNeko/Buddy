package game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import core.GameLoop;
import graphics.CanvasRenderer;
import graphics.PixelImage;
import input.Keyboard;
import input.Mouse;
import resources.R;
import util.Log;

public class Game extends GameLoop {
	
	private final int WIDTH = 1600;
	private final int HEIGHT = 900;
	
	PixelImage canvas;
	
	Level level;
	
	int scale = 4;
	PixelImage scaledCanvas;
	
	private JFrame frame;
	
	public Game() {
		super(90, 120);
		level = new Level();
		canvas = new PixelImage(WIDTH/scale, HEIGHT/scale);
		scaledCanvas = new PixelImage(canvas.getWidth() * scale, canvas.getHeight() * scale);
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		CanvasRenderer component = new CanvasRenderer(scaledCanvas);
		Keyboard.register(component);
		Mouse.register(component);
		frame.add(component);
		
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	protected void tick(float delta) {
		level.update(delta);
	}

	@Override
	protected void fixedTick(float delta) {
		
	}

	@Override
	protected void render() {
		synchronized(canvas) {
			canvas.clear();
			level.render(canvas, null);
			synchronized(scaledCanvas) {
				scaledCanvas = PixelImage.scale(canvas, scaledCanvas, scale);
			}
		}
		
		frame.repaint();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Game().start();
			}
		});
	}

}
