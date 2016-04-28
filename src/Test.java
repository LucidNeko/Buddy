import input.Keyboard;
import input.Mouse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ecs100.UI;
import graphics.Animation;
import graphics.CanvasRenderer;
import graphics.PixelImage;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import collision.QuadTree;
import core.Audio;
import core.GameLoop;
import core.Level;
import core.Sprite;
import physics.Particle;
import physics.ParticleEmitter;
import physics.ParticleSystem;
import resources.R;
import util.Log;
import math.Mathf;
import math.Vec2;
import math.Vec3;


public class Test {
	
	private Animation idle = new Animation(R.images.hero.mami_idle, 0.65f);
	private Animation run_right = new Animation(R.images.hero.mami_run_right, 0.65f);
	private Animation run_left = new Animation(R.images.hero.mami_run_left, 0.65f);
	private Animation anim = idle;
	private Vec2 position = new Vec2();
	private Vec2 velocity = new Vec2();
	private float movespeed = 172.4f;
	private PixelImage ground = PixelImage.scale(R.images.terrain.test_ground, 4);
	
	private ParticleSystem ps = new ParticleSystem();
	private ParticleEmitter emitter = new ParticleEmitter(new Vec2(100, 100), new Vec2(1, 0).normalized(), 200, 2, 4);
	
	private QuadTree qt;
	
	private int clipIndex = 0;
	

	Set<Clip> clips = new HashSet<Clip>();
	
	public Test() {
//		final PixelImage canvas = UI.getCanvas();
		final PixelImage canvas = new PixelImage(UI.getCanvasWidth(), UI.getCanvasHeight());
		
		qt = new QuadTree(0, 0, canvas.getWidth(), canvas.getHeight());
		
		ps.addEmitter(emitter);
		
		final JPanel sliders = new JPanel(new FlowLayout());
		
		final JLabel moveLabel = new JLabel("000.000");
		JSlider move = new JSlider(JSlider.VERTICAL, 0, 100000, 0);
		move.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
				movespeed = slider.getValue()/100f;
				moveLabel.setText("" + movespeed);
			}
			
		});
		move.setValue((int) (movespeed * 100));
		move.setFocusable(false);
		sliders.add(moveLabel);
		sliders.add(move);
		
		final JLabel speedLabel = new JLabel("000.000");
		JSlider speed = new JSlider(JSlider.VERTICAL, 0, 1000, 0);
		speed.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
				anim.setSpeed(slider.getValue()/100f);
				speedLabel.setText("" + anim.getSpeed());
			}
			
		});
		speed.setValue((int) (anim.getSpeed()*100));
		speed.setFocusable(false);
		sliders.add(speedLabel);
		sliders.add(speed);
		
		UI.getFrame().add(sliders, BorderLayout.EAST);
		UI.getFrame().pack();
		
		UI.setFont(R.fonts.kenpixel_mini_square);
//		UI.setFontSize(30);
		
		final Sprite sprite = new Mami();

		
		sprite.transform().position.y = 10;
		final List<Sprite> sprites = new ArrayList<Sprite>();
		
		(new GameLoop(70, 100) {

			float acc = 0;
			
			float time = 0;
			
			@Override
			protected void tick(float delta) {
//				anim.update(delta);
				
				sprite.update(delta);
				
				acc += delta;
				time += delta;
				
				if(time > 1) {
					time -= 1;
//					Log.info("{} sprites. {} running slowly.", sprites.size(), this.isRunningSlow() ? "Is" : "Not");
				}
				
				if(acc > 0.25f) {
					acc -= 0.25f;
//					Sprite s = new Sprite();
//					s.transform().position.x = Mathf.random() * canvas.getWidth();
//					s.transform().position.y = Mathf.random() * 50;
//					sprites.add(s);
				}
				
//				qt.clear();
				

				ps.update(delta);
				
//				qt.add(sprite);
				for(Sprite s : sprites) {
					s.update(delta);
//					qt.add(s);
				}
			}

			@Override
			protected void fixedTick(float delta) {		
				
				
				

				
				
//				Vec2 acc = new Vec2(0, 0);
//				
//				position = position.add(velocity.mul(delta)).add(acc.mul(0.5f).mul(delta*delta));
//				velocity = velocity.add(acc.mul(delta));
//				
//				if(position.x > canvas.getWidth()) {
//					position.x = 0;
//				} else if(position.x < 0) {
//					position.x = canvas.getWidth();
//				}
//				
//				int x = 0;
//				if(Keyboard.isKeyDown(KeyEvent.VK_A)) x -= 1;
//				if(Keyboard.isKeyDown(KeyEvent.VK_D)) x += 1;
//				velocity.x = movespeed * x;
//				
//				if(velocity.x < 0) {
//					anim = run_left;
//				} else if(velocity.x > 0) {
//					anim = run_right;
//				} else {
//					anim = idle;
//				}
				
//				Clip[] clips = {
//						R.audio.explosion,
//						R.audio.jump,
//						R.audio.land_hard,
//						R.audio.land_soft,
//						R.audio.lose,
//						R.audio.win
//				};
				
				
				if(Keyboard.isKeyDownOnce(KeyEvent.VK_T)) {
					clips.add(Audio.loop(R.audio.jump));
//					Audio.play(clips[clipIndex]);
//					clipIndex = clipIndex + 1;
//					clipIndex = clipIndex % clips.length;
				}
				
				if(Keyboard.isKeyDownOnce(KeyEvent.VK_G)) {
					for(Clip clip : clips) {
						Audio.close(clip);
					}
					clips.clear();
				}
			}

			@Override
			protected void render() {
//				synchronized(canvas) {
					canvas.clear();
//					canvas.blit(ground, 0, 107);
//					anim.getFrame().blit(canvas, position.x(), position.y());
					
					canvas.blit(Level.getCurrent().background, 0, 0);
					sprite.render(canvas);
					for(Sprite s : sprites) s.render(canvas);
					canvas.blit(Level.getCurrent().foreground, 0, 0);
					
					Graphics2D g = canvas.asBufferedImage().createGraphics();
					for(Particle p : ps.getParticles()) {
						Vec2 v = p.getPosition();
						g.setColor(new Color(Mathf.lerp(0, 1, p.getLife()), 0f, 0f));
						g.fillOval(v.x() - 3, v.y() - 3, 7, 7);
						anim.getFrame().blit(canvas, v.x(), v.y());
					}
					g.dispose();
					
					qt.render(canvas);
					
					PixelImage color = R.images.Pumpkin_C;
					PixelImage normal = R.images.Pumpkin_N;
					
					Vec2 pos = new Vec2(500, 100);
					Vec2 mouse = new Vec2(Mouse.getX(), Mouse.getY());
					
					Vec2 dir = mouse.sub(pos);
					
					Vec3 light = new Vec3(dir.x, -dir.y, 0);
					light.normalize();
					
					float attenuation = 1f;
					Vec3 intensities = new Vec3(0.5f, 0.5f, 0.5f);
					float ambientCoefficient = 0.26f;
					
					Vec3 spotIntensities = new Vec3(2, 2, 2);
					float spotAttenuation = 0.0025f;
					float spotAmbientCoefficient = 0f;
					
					intensities = spotIntensities;
					attenuation = spotAttenuation;
					ambientCoefficient = 0.26f;
					
					PixelImage out = new PixelImage(color.getWidth(), color.getHeight());
					
					boolean ambi = false;
					
					for(int y = 0; y < out.getHeight(); y++) {
						for(int x = 0; x < out.getWidth(); x++) {
							if(color.getAlpha(x, y) == 0) {
								out.setARGB(x, y, color.getARGB(x, y));
							} else {
								Vec3 surfaceToLight;
								if(ambi) {
									surfaceToLight = light;
									attenuation = 1;
								} else {
									surfaceToLight = new Vec3(mouse.sub(pos.add(new Vec2(x, y).mul(2))));
									float dist = surfaceToLight.normalize();
									attenuation = 1f/ (1f + spotAttenuation * Mathf.pow(dist, 2));
								}
								
								Vec3 rgb = new Vec3(color.getRed(x, y)/255f, color.getGreen(x, y)/255f, color.getBlue(x, y)/255f);
								
								Vec3 ambient = rgb.mul(ambientCoefficient).mul(intensities);
								
								Vec3 N = new Vec3(normal.getRed(x, y)/255f, normal.getGreen(x, y)/255f, normal.getBlue(x, y)/255f);
								N = N.mul(2).sub(1, 1, 1);
								
								float diffuseCoefficient = Mathf.max(0f, N.dot(surfaceToLight));
								Vec3 diffuse = rgb.mul(diffuseCoefficient).mul(intensities);
								
								float specularCoefficient = 0;
								if(diffuseCoefficient > 0) {
//									I - 2.0 * dot(N, I) * N
									Vec3 I = surfaceToLight.mul(-1);
									Vec3 reflect = I.sub(N.mul(2 * N.dot(I)));
									specularCoefficient = Mathf.pow(Mathf.max(0, new Vec3(1,1,-1).normalized().dot(reflect)), 0.1f);
								}
								Vec3 specular = rgb.mul(0.4f).mul(specularCoefficient).mul(spotIntensities);
								
								Vec3 frag = ambient.add(diffuse.add(specular).mul(attenuation));
								frag.x = Mathf.clamp(frag.x, 0, 1);
								frag.y = Mathf.clamp(frag.y, 0, 1);
								frag.z = Mathf.clamp(frag.z, 0, 1);
								
								int fragColor = 0xFF << 24 | (int)(frag.x*255) << 16 | (int)(frag.y*255) << 8 | (int)(frag.z*255);

								out.setARGB(x, y, fragColor);
							}
						}
					}
					
					canvas.blit(PixelImage.scale(out, 2), pos.x(), pos.y());
					
					UI.clearGraphics();
					UI.drawImage(canvas, 0, 0);
					UI.drawString("The quick brown fox jumped over the lazy dog", 0, 100);
//				}
				UI.repaint();
			}
			
		}).start();
		
		
		
		
//		final JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		
//		final PixelImage canvas = new PixelImage(800, 600);
//		
//		frame.setLayout(new FlowLayout());
//		
//		ps.addEmitter(emitter);
//		
//		CanvasRenderer renderer = new CanvasRenderer(canvas);
//		Keyboard.register(renderer);
//		Mouse.register(renderer);
//		frame.add(renderer);
//		
//		final JLabel moveLabel = new JLabel("000.000");
//		JSlider move = new JSlider(JSlider.VERTICAL, 0, 100000, 0);
//		move.addChangeListener(new ChangeListener() {
//
//			@Override
//			public void stateChanged(ChangeEvent e) {
//				JSlider slider = (JSlider) e.getSource();
//				movespeed = slider.getValue()/100f;
//				moveLabel.setText("" + movespeed);
//			}
//			
//		});
//		move.setValue((int) (movespeed * 100));
//		move.setFocusable(false);
//		frame.add(moveLabel);
//		frame.add(move);
//		
//		final JLabel speedLabel = new JLabel("000.000");
//		JSlider speed = new JSlider(JSlider.VERTICAL, 0, 1000, 0);
//		speed.addChangeListener(new ChangeListener() {
//
//			@Override
//			public void stateChanged(ChangeEvent e) {
//				JSlider slider = (JSlider) e.getSource();
//				anim.setSpeed(slider.getValue()/100f);
//				speedLabel.setText("" + anim.getSpeed());
//			}
//			
//		});
//		speed.setValue((int) (anim.getSpeed()*100));
//		speed.setFocusable(false);
//		frame.add(speedLabel);
//		frame.add(speed);
//		
//		frame.pack();
//		frame.setVisible(true);
//		
//		(new GameLoop(90, 125) {
//
//			@Override
//			protected void tick(float delta) {
//				anim.update(delta);
//				
//				
//			}
//
//			@Override
//			protected void fixedTick(float delta) {		
//				ps.update(delta);
//				
//				Vec2 acc = new Vec2(0, 0);
//				
//				position = position.add(velocity.mul(delta)).add(acc.mul(0.5f).mul(delta*delta));
//				velocity = velocity.add(acc.mul(delta));
//				
//				if(position.x > canvas.getWidth()) {
//					position.x = 0;
//				} else if(position.x < 0) {
//					position.x = canvas.getWidth();
//				}
//				
//				int x = 0;
//				if(Keyboard.isKeyDown(KeyEvent.VK_A)) x -= 1;
//				if(Keyboard.isKeyDown(KeyEvent.VK_D)) x += 1;
//				velocity.x = movespeed * x;
//				
//				if(velocity.x < 0) {
//					anim = run_left;
//				} else if(velocity.x > 0) {
//					anim = run_right;
//				} else {
//					anim = idle;
//				}
//			}
//
//			@Override
//			protected void render() {
//				synchronized(canvas) {
//					canvas.clear();
//					canvas.blit(ground, 0, 107);
//					anim.getFrame().blit(canvas, position.x(), position.y());
//					
//					Graphics2D g = canvas.asBufferedImage().createGraphics();
//					for(Particle p : ps.getParticles()) {
//						Vec2 v = p.getPosition();
//						g.setColor(new Color(Mathf.lerp(0, 1, p.getLife()), 0f, 0f));
//						g.fillOval(v.x() - 3, v.y() - 3, 7, 7);
//						anim.getFrame().blit(canvas, v.x(), v.y());
//					}
//					g.dispose();
//				}
//				frame.repaint();
//			}
//			
//		}).start();
	}
	
	public static void main(String[] args) {
		new Test();
	}

}
