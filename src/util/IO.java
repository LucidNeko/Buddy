package util;

import graphics.PixelImage;
import graphics.SpriteSheet;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class IO {
	
	public static PixelImage loadImage(String fpath) {
		try {
			return new PixelImage(ImageIO.read(new File(fpath)));
		} catch (IOException e) {
			Log.error("Failed loading {} with error: {}", fpath, e.getMessage());
			return null;
		}
	}
	
	/**
	 * Format: "name.numFrames.sprite" => "mami_run.ms_per_frame.8.sprite"
	 */
	public static SpriteSheet loadSpriteSheet(String fpath) {
		String[] data = fpath.substring(fpath.lastIndexOf("/") + 1).split("\\.");
		String name = data[0];
		int ms = Integer.parseInt(data[1]);
		int numFrames = Integer.parseInt(data[2]);
		PixelImage image = loadImage(fpath);
		
		if(image != null) {
			return new SpriteSheet(image, numFrames, ms / 1000f);
		} else {
			Log.error("Failed loading {} with error: Failed loading image", fpath);
			return null;
		}
	}
	
	public static Font loadFont(String fpath) {
		try {
			if(!getExtension(fpath).equals("ttf")) {
				Log.warn("Font {} doesn't have extension {}", fpath, "ttf");
			}
			
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fpath)).deriveFont(12f);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			return font;
		} catch (FontFormatException | IOException e) {
			Log.error("Failed loading {} with error: {}", fpath, e.getMessage());
			return null;
		}
	}
	
	public static Clip loadClip(String fpath) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fpath));
			clip.open(ais);
			return clip;
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			Log.error("Failed loading {} with error: {}", fpath, e.getMessage());
			return null;
		}
	}
	
	public static byte[] loadAudio(String fpath) {
		try {
			return Files.readAllBytes(Paths.get(fpath));
		} catch (IOException e) {
			Log.error("Failed loading {} with error: {}", fpath, e.getMessage());
			return null;
		}
	}
	
	public static void writeFile(String text, String fpath) {
		try {
			PrintStream out = new PrintStream(new FileOutputStream(fpath));
			out.print(text);
			out.close();
		} catch (FileNotFoundException e) {
			Log.error("Failed writing to file {} with error: {}", fpath, e.getMessage());
		}
	}
	
//	public static Audio loadAudio(String fpath) {
//		
//	}
	
	private static String getExtension(String s) {
		return s.substring(s.lastIndexOf(".") + 1);
	}
	
	public static void main(String[] args) {		
//		Log.info("{}", getExtension("starship.png"));
//		Log.info("{}", getExtension("star.ship.png"));
//		Log.info("{}", getExtension("st.ar.sh.ip.png"));
//		Log.info("{}", getExtension("starshippng"));
//		Log.info("{}", getExtension(""));
	}

}
