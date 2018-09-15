package core;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import util.Log;

public class Audio {
	
	public static void play(byte[] data) {
		Clip clip = loadClip(data);
		clip.addLineListener(CLOSE_ON_STOP);
		play(clip);
	}
	
	/** must call Audio.close(clip) on the returned clip */
	public static Clip loop(byte[] data) {
		Clip clip = loadClip(data);
		loop(clip);
		return clip;
	}
	
	public static void close(final Clip clip) {
		(new Thread(new Runnable() {
			public void run() {
				if(clip.isOpen()) {
					clip.close();
				}
			}
		})).start();
	}
	
	private static void play(Clip clip) {
		if(clip != null && clip.isOpen()) {
			clip.start();
		}
	}
	
	private static void loop(Clip clip) {
		if(clip != null && clip.isOpen()) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	private static Clip loadClip(byte[] data) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
			DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(ais);
			return clip;
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			Log.error("Failed to create clip: {}", e.getMessage());
			return null;
		}
	}
	
	private static final LineListener CLOSE_ON_STOP = new LineListener() {
		public void update(LineEvent event) {
			Clip clip = (Clip) event.getSource();
			if(event.getType() == LineEvent.Type.STOP) {
				Audio.close(clip);
			}
		}
	};

}
