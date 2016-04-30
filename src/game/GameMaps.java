package game;

import resources.R;

public class GameMaps {
	
	public static GameMap[] maps = new GameMap[] {
			new GameMap() {
				@Override
				public void init() {
					background.add(new Layer(R.levels.level000.background7));
					world.add(new Layer(R.levels.level000.Level01_world));
					world.add(new Layer(R.levels.level000.Level01_oneside));
					hazards.add(new Layer(R.levels.level000.Level01_hazards));
					foreground.add(new Layer(R.levels.level000.Level01_decorations));
					collision = new Collision(R.levels.level000.Level01_world);
					collision.add(R.levels.level000.Level01_oneside);
					dynamicCollision = new Collision(collision.getWidth(), collision.getHeight());
				}
			},
			
			new GameMap() {

				@Override
				public void init() {
					R.levels.level001.Level02_bg.fill(0xFF000000);
					background.add(new Layer(R.levels.level001.Level02_bg));
					world.add(new Layer(R.levels.level001.Level02_world));
					world.add(new Layer(R.levels.level001.Level02_oneside));
					hazards.add(new Layer(R.levels.level001.Level02_hazards));
					foreground.add(new Layer(R.levels.level001.Level02_decorations));
					collision = new Collision(R.levels.level001.Level02_world);
					collision.add(R.levels.level001.Level02_oneside);
					dynamicCollision = new Collision(collision.getWidth(), collision.getHeight());
				}
				
			}
	};
	
	public static GameMap getRandomMap() {
		return maps[(int)(Math.random()*maps.length)];
	}

}
