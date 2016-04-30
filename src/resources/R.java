package resources;

import java.awt.Font;
import graphics.SpriteSheet;
import graphics.PixelImage;
import util.IO;

public class R{
	public static class audio{
		public static final byte[] a_bannanas_ages = IO.loadAudio("R/audio/a_bannanas_ages.wav");
		public static final byte[] chirp = IO.loadAudio("R/audio/chirp.wav");
		public static final byte[] coin = IO.loadAudio("R/audio/coin.wav");
		public static final byte[] enemydie = IO.loadAudio("R/audio/enemydie.wav");
		public static final byte[] explosion = IO.loadAudio("R/audio/explosion.wav");
		public static final byte[] hurt = IO.loadAudio("R/audio/hurt.wav");
		public static final byte[] jump = IO.loadAudio("R/audio/jump.wav");
		public static final byte[] jump_alt = IO.loadAudio("R/audio/jump_alt.wav");
		public static final byte[] pulse = IO.loadAudio("R/audio/pulse.wav");
		public static final byte[] sproing = IO.loadAudio("R/audio/sproing.wav");
		public static final byte[] woo = IO.loadAudio("R/audio/woo.wav");
		public static final byte[] zap = IO.loadAudio("R/audio/zap.wav");
	}

	public static class fonts{
		public static final Font kenpixel_mini_square = IO.loadFont("R/fonts/kenpixel_mini_square.ttf");
	}

	public static class images{
		public static class test{
			public static class hero{
				public static final SpriteSheet mami_idle = IO.loadSpriteSheet("R/images/test/hero/mami_idle.450.7.sprite");
				public static final SpriteSheet mami_run_left = IO.loadSpriteSheet("R/images/test/hero/mami_run_left.450.8.sprite");
				public static final SpriteSheet mami_run_right = IO.loadSpriteSheet("R/images/test/hero/mami_run_right.450.8.sprite");
			}

			public static class terrain{
				public static final PixelImage test_ground = IO.loadImage("R/images/test/terrain/test_ground.png");
			}

			public static final PixelImage Pumpkin_C = IO.loadImage("R/images/test/Pumpkin_C.png");
			public static final PixelImage Pumpkin_N = IO.loadImage("R/images/test/Pumpkin_N.png");
			public static final SpriteSheet idlebig = IO.loadSpriteSheet("R/images/test/idlebig.100.6.sprite");
			public static final SpriteSheet idlesmall = IO.loadSpriteSheet("R/images/test/idlesmall.100.6.sprite");
			public static final PixelImage test = IO.loadImage("R/images/test/test.png");
			public static final PixelImage test_diff = IO.loadImage("R/images/test/test_diff.png");
			public static final PixelImage test_diff2 = IO.loadImage("R/images/test/test_diff2.png");
			public static final PixelImage test_diff3 = IO.loadImage("R/images/test/test_diff3.png");
		}

		public static final PixelImage key = IO.loadImage("R/images/key.png");
	}

	public static class levels{
		public static class level000{
			public static final PixelImage Level01_PATH = IO.loadImage("R/levels/level000/Level01_PATH.png");
			public static final PixelImage Level01_bg = IO.loadImage("R/levels/level000/Level01_bg.png");
			public static final PixelImage Level01_decorations = IO.loadImage("R/levels/level000/Level01_decorations.png");
			public static final PixelImage Level01_hazards = IO.loadImage("R/levels/level000/Level01_hazards.png");
			public static final PixelImage Level01_oneside = IO.loadImage("R/levels/level000/Level01_oneside.png");
			public static final PixelImage Level01_preview = IO.loadImage("R/levels/level000/Level01_preview.png");
			public static final PixelImage Level01_world = IO.loadImage("R/levels/level000/Level01_world.png");
			public static final PixelImage background = IO.loadImage("R/levels/level000/background.png");
			public static final PixelImage background2 = IO.loadImage("R/levels/level000/background2.png");
			public static final PixelImage background3 = IO.loadImage("R/levels/level000/background3.png");
			public static final PixelImage background4 = IO.loadImage("R/levels/level000/background4.png");
			public static final PixelImage background5 = IO.loadImage("R/levels/level000/background5.png");
			public static final PixelImage background6 = IO.loadImage("R/levels/level000/background6.png");
			public static final PixelImage background7 = IO.loadImage("R/levels/level000/background7.png");
			public static final PixelImage tiles = IO.loadImage("R/levels/level000/tiles.8.8.sprite.png");
		}

		public static class level001{
			public static final PixelImage Level02_bg = IO.loadImage("R/levels/level001/Level02_bg.png");
			public static final PixelImage Level02_decorations = IO.loadImage("R/levels/level001/Level02_decorations.png");
			public static final PixelImage Level02_hazards = IO.loadImage("R/levels/level001/Level02_hazards.png");
			public static final PixelImage Level02_notes = IO.loadImage("R/levels/level001/Level02_notes.png");
			public static final PixelImage Level02_oneside = IO.loadImage("R/levels/level001/Level02_oneside.png");
			public static final PixelImage Level02_preview = IO.loadImage("R/levels/level001/Level02_preview.png");
			public static final PixelImage Level02_world = IO.loadImage("R/levels/level001/Level02_world.png");
		}

		public static class test{
			public static final PixelImage background = IO.loadImage("R/levels/test/background.png");
			public static final PixelImage collision = IO.loadImage("R/levels/test/collision.png");
			public static final PixelImage foreground = IO.loadImage("R/levels/test/foreground.png");
		}
	}

	public static class sprites{
		public static class enemies{
			public static class walky{
				public static final SpriteSheet idle = IO.loadSpriteSheet("R/sprites/enemies/walky/idle.250.2.sprite");
				public static final SpriteSheet walk = IO.loadSpriteSheet("R/sprites/enemies/walky/walk.250.2.sprite");
			}
		}

		public static class key{
		}

		public static class player{
			public static final SpriteSheet airborn_left = IO.loadSpriteSheet("R/sprites/player/airborn_left.120.1.png");
			public static final SpriteSheet airborn_right = IO.loadSpriteSheet("R/sprites/player/airborn_right.120.1.png");
			public static final SpriteSheet die_left = IO.loadSpriteSheet("R/sprites/player/die_left.120.1.png");
			public static final SpriteSheet die_right = IO.loadSpriteSheet("R/sprites/player/die_right.120.1.png");
			public static final SpriteSheet idle_left = IO.loadSpriteSheet("R/sprites/player/idle_left.120.3.png");
			public static final SpriteSheet idle_right = IO.loadSpriteSheet("R/sprites/player/idle_right.120.3.png");
			public static final SpriteSheet jump_left = IO.loadSpriteSheet("R/sprites/player/jump_left.120.3.png");
			public static final SpriteSheet jump_right = IO.loadSpriteSheet("R/sprites/player/jump_right.120.3.png");
			public static final SpriteSheet walk_right = IO.loadSpriteSheet("R/sprites/player/walk_right.120.3.png");
			public static final SpriteSheet wall_slide = IO.loadSpriteSheet("R/sprites/player/wall_slide.100.1.png");
			public static final SpriteSheet wallcling_left = IO.loadSpriteSheet("R/sprites/player/wallcling_left.120.1.png");
			public static final SpriteSheet wallcling_right = IO.loadSpriteSheet("R/sprites/player/wallcling_right.120.1.png");
			public static final SpriteSheet walljump_left = IO.loadSpriteSheet("R/sprites/player/walljump_left.120.3.png");
			public static final SpriteSheet walljump_right = IO.loadSpriteSheet("R/sprites/player/walljump_right.120.3.png");
		}

		public static class tilesets{
		}
	}
}
