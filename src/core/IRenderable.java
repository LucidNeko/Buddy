package core;

import game.Camera;
import graphics.PixelImage;

public interface IRenderable {
	
	public void render(PixelImage canvas, Camera camera);

}
