package resources;

import java.awt.Font;
import graphics.SpriteSheet;
import graphics.PixelImage;
import util.IO;

public class R{
	public static class audio{
		public static final byte[] bu_a_hunting_road = IO.loadAudio("R/audio/bu-a-hunting-road.wav");
		public static final byte[] jump = IO.loadAudio("R/audio/jump.wav");
	}

	public static class fonts{
		public static final Font kenpixel_mini_square = IO.loadFont("R/fonts/kenpixel_mini_square.ttf");
	}

	public static class images{
		public static class hero{
			public static final SpriteSheet mami_idle = IO.loadSpriteSheet("R/images/hero/mami_idle.7.sprite");
			public static final SpriteSheet mami_run_left = IO.loadSpriteSheet("R/images/hero/mami_run_left.8.sprite");
			public static final SpriteSheet mami_run_right = IO.loadSpriteSheet("R/images/hero/mami_run_right.8.sprite");
		}

		public static class terrain{
			public static final PixelImage test_ground = IO.loadImage("R/images/terrain/test_ground.png");
		}

		public static final PixelImage Pumpkin_C = IO.loadImage("R/images/Pumpkin_C.png");
		public static final PixelImage Pumpkin_N = IO.loadImage("R/images/Pumpkin_N.png");
		public static final PixelImage level_0000_foreground = IO.loadImage("R/images/level_0000_foreground.png");
		public static final PixelImage level_0001_collision = IO.loadImage("R/images/level_0001_collision.png");
		public static final PixelImage level_0002_background = IO.loadImage("R/images/level_0002_background.png");
	}
}
