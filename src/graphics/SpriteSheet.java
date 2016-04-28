package graphics;

public class SpriteSheet {
	
	private PixelImage image;
	private Frame[] frames;
	
	public SpriteSheet(PixelImage image, int numFrames) {
		this.image = image;
		this.frames = new Frame[numFrames];
		
		populate();
	}
	
	private void populate() {
		int w = image.getWidth() / frames.length;
		int h = image.getHeight();
		for(int i = 0; i < frames.length; i++) {
			frames[i] = new Frame(this, i*w, 0, w, h);
		}
	}
	
	public PixelImage getImage() {
		return image;
	}
	
	public int getNumFrames() {
		return frames.length;
	}
	
	public Frame[] getFrames() {
		return frames;
	}
	
	public Frame getFrame(int i) {
		if(i >= 0 && i < frames.length) {
			return frames[i];
		}
		throw new IndexOutOfBoundsException();
	}
	
	public static class Frame {
		private SpriteSheet spriteSheet;
		private int x;
		private int y;
		private int width;
		private int height;
		
		public Frame(SpriteSheet spriteSheet, int x, int y, int width, int height) {
			this.spriteSheet = spriteSheet;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		public int getWidth() {
			return width;
		}
		
		public int getHeight() {
			return height;
		}
		
		/** More expensive than blit, but cleaner. Makes a new PixelImage */
		public PixelImage getImage() {
			return spriteSheet.image.getSubImage(x, y, width, height);
		}
		
		public void blit(PixelImage canvas, int x, int y) {
			for(int srcY = this.y; srcY < this.y + height; srcY++) {
				for(int srcX = this.x; srcX < this.x + width; srcX++) {
					int dstX = x + srcX - this.x;
					int dstY = y + srcY - this.y;
					if(dstX >= 0 && dstX < canvas.getWidth() && dstY >= 0 && dstY < canvas.getHeight()) {
						if(spriteSheet.image.getAlpha(srcX, srcY) != 0) {
							canvas.setARGB(dstX, dstY, spriteSheet.image.getARGB(srcX, srcY));
						}
					}
				}
			}
		}
	}

}
