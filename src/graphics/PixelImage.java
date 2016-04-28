package graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;


public class PixelImage implements Cloneable {
	
	public static final int DEFAULT_CLEAR_COLOR = -1;
	
	private int width;
	private int height;

	private int[] data;
	
	private BufferedImage image;
	
	public PixelImage(int width, int height) {
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.width = width;
		this.height = height;
		
		this.data = ((DataBufferInt)(image.getRaster().getDataBuffer())).getData();
	}
	
	public PixelImage(BufferedImage src) {
		this(src.getWidth(), src.getHeight());
		
		//draw in the src image
		this.image.createGraphics().drawImage(src, null, 0, 0);
	}
	
	public boolean testBounds(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
	public void clear() {
		fill(DEFAULT_CLEAR_COLOR);
	}
	
	public void fill(int rgb) {
		Arrays.fill(data, rgb);
	}
	
	public int getARGB(int x, int y) {
		return data[y*width + x];
	}
	
	public void setARGB(int x, int y, int rgb) {
		data[y*width + x] = rgb;
	}
	
	public int getAlpha(int x, int y) {
		return data[y*width + x] >> 24 & 0xFF;
	}
	
	public int getRed(int x, int y) {
		return data[y*width + x] >> 16 & 0xFF;
	}
	
	public int getGreen(int x, int y) {
		return data[y*width + x] >>  8 & 0xFF;
	}
	
	public int getBlue(int x, int y) {
		return data[y*width + x]       & 0xFF;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public BufferedImage asBufferedImage() {
		return image;
	}
	
	public void blit(PixelImage img, int x, int y) {
		if(x + img.getWidth() < 0 || y + img.getHeight() < 0 || x >= width || y >= height) {
			return;
		}
		
		for(int srcY = 0; srcY < img.getHeight(); srcY++) {
			for(int srcX = 0; srcX < img.getWidth(); srcX++) {
				int dstX = x + srcX;
				int dstY = y + srcY;
				if(dstX >= 0 && dstX < this.getWidth() && dstY >= 0 && dstY < this.getHeight()) {
					int alpha = img.getARGB(srcX, srcY) >> 24 & 0xFF;
					if(alpha != 0) {
						this.setARGB(dstX, dstY, img.getARGB(srcX, srcY));
					}
				}
			}
		}
	}
	
	public PixelImage getSubImage(int offsetX, int offsetY, int width, int height) {
		PixelImage out = new PixelImage(width, height);
		
		for(int y = 0; y < out.height; y++) {
			for(int x = 0; x < out.width; x++) {
				int srcX = offsetX + x;
				int srcY = offsetY + y;
				if(srcX >= 0 && srcX < this.getWidth() && srcY >= 0 && srcY < this.getHeight()) {
					out.setARGB(x, y, this.getARGB(srcX, srcY));
				}
			}
		}
		
		return out;
	}
	
	public PixelImage clone() {
		PixelImage out = new PixelImage(width, height);
		
		for(int i = 0; i < this.data.length; i++) {
			out.data[i] = this.data[i];
		}
		
		return out;
	}
	
	// Static
	public static PixelImage flipHorizontal(PixelImage img) {
		PixelImage out = new PixelImage(img.getWidth(), img.getHeight());
		for(int x = 0; x < img.getWidth()/2; x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				out.setARGB(x, y, img.getARGB(img.getWidth()-1-x, y));
				out.setARGB(img.getWidth()-1-x, y, img.getARGB(x, y));
			}
		}
		return out;
	}
	
	public static PixelImage scale(PixelImage img, int n) {
		if(n <= 0) { return new PixelImage(0, 0); }
		if(n == 1) { return img.clone(); }
		
		PixelImage out = new PixelImage((int)(img.width * n), (int)(img.height * n));
		
		for(int y = 0; y < out.height; y++) {
			for(int x = 0; x < out.width; x++) {
				out.setARGB(x, y, img.getARGB(x/n, y/n));
			}
		}
		
		return out;
	}
	
	public static class SubPixelImage {
		
		PixelImage image;
		int x;
		int y;
		int width;
		int height;
		
		public SubPixelImage(PixelImage image, int x, int y, int width, int height) {
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

}
