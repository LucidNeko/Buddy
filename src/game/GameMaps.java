package game;

import core.Sprite;
import game.entities.Button;
import game.entities.Door;
import game.entities.Key;
import game.entities.KeyHole;
import game.entities.Walker;
import math.Vec2;
import resources.R;

public class GameMaps {
	
	public static GameMap[] maps = new GameMap[] {
			new GameMap() {
				@Override
				public void init() {
					clear();
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
					clear();
					background.add(new Layer(R.levels.level001.background_wide_6));
					world.add(new Layer(R.levels.level001.Level02_world));
					world.add(new Layer(R.levels.level001.Level02_oneside));
					hazards.add(new Layer(R.levels.level001.Level02_hazards));
					foreground.add(new Layer(R.levels.level001.Level02_decorations));
					collision = new Collision(R.levels.level001.Level02_world);
					collision.add(R.levels.level001.Level02_oneside);
					dynamicCollision = new Collision(collision.getWidth(), collision.getHeight());
					
					Vec2[] walkies = new Vec2[] {
							new Vec2(36*16, 24*16),
							new Vec2(38*16, 16*16),
							new Vec2(41*16, 10*16)
					};
					
					for(Vec2 pos : walkies) {
						Sprite walky = new Walker();
						walky.transform().position.set(pos);
						entities.add(walky);
					}
					
					Sprite keyhole = new KeyHole();
					keyhole.transform().position.set(59*16, 8*16);
					entities.add(keyhole);
					
					Sprite key = new Key(keyhole.transform());
					key.transform().position.set(21*16, 21*16);
					entities.add(key);

					Sprite door = new Door();
					door.transform().position.set(7*16, 9*16);
					entities.add(door);
					
					Sprite button = new Button(door.transform(), 64);
					button.transform().position.set(2*16, 11*16);
					entities.add(button);
					
				}
				
			}
	};
	
	public static GameMap getRandomMap() {
		return maps[(int)(Math.random()*maps.length)];
	}

}
