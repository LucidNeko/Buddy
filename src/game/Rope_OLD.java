package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import core.IRenderable;
import core.IUpdateable;
import graphics.PixelImage;
import math.Transform;
import math.Vec2;
import util.Log;

public class Rope_OLD implements IUpdateable, IRenderable {
	
	private int PARTICLES = 10;
	private Vec2 GRAVITY = new Vec2(0, 1600f);
	
	private Transform a;
	private Transform b;
	private float length;
	
	private Particle[] particles = new Particle[PARTICLES];
	private List<Constraint> constraints = new ArrayList<Constraint>();
	
	public Rope_OLD(Transform a, Transform b) {
		this(a, b, a.position.sub(b.position).length());
	}
		
	public Rope_OLD(Transform a, Transform b, float length) {
		this.a = a;
		this.b = b;
		this.length = length;

		
		
		for(int i = 0; i < PARTICLES; i++) {
			particles[i] = new Particle(a.position.lerp(b.position, i / PARTICLES));
			if(i > 0) {
				Particle p1 = particles[i-1];
				Particle p2 = particles[i];
				constraints.add(new DistanceConstraint(p1, p2, length/PARTICLES));
			}
		}
		

		constraints.add(0, new PinConstraint(particles[0], a));
		constraints.add(0, new PinConstraint(particles[PARTICLES-1], b));
	}

	@Override
	public void update(float delta) {
		for(Particle p : particles) {
			p.update(delta);
		}
		
		for(int i = 0; i < 10; i++) {
			for(Constraint c : constraints) {
				c.solve(delta);
			}
		}
	}

	@Override
	public void render(PixelImage canvas, Camera camera) {
		Graphics2D g = canvas.asBufferedImage().createGraphics();
		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(3));
		
		for(int i = 1; i < particles.length; i++) {
			Particle p1 = particles[i-1];
			Particle p2 = particles[i];
			Vec2 a = camera.transform(p1.position);
			Vec2 b = camera.transform(p2.position);
			g.drawLine(a.x(), a.y(), b.x(), b.y());
		}
		
		g.dispose();
	}
	
	private class Particle implements IUpdateable {
		
		Vec2 position;
		Vec2 oldPosition;
		
		public Particle(Vec2 position) {
			this.position = new Vec2(position);
			this.oldPosition = new Vec2(position);
		}

		@Override
		public void update(float delta) {
			Vec2 velocity = position.sub(oldPosition).mul(delta);
			oldPosition.set(position);
			position = position.add(velocity).add(GRAVITY.mul(0.5f).mul(delta*delta));
		}
		
	}
	
	private interface Constraint {
		public void solve(float delta);
	}

	private class DistanceConstraint implements Constraint {
		
		Particle a;
		Particle b;
		float length;
		
		public DistanceConstraint(Particle a, Particle b, float length) {
			this.a = a;
			this.b = b;
			this.length = length;
		}

		@Override
		public void solve(float delta) {
			Vec2 direction = b.position.sub(a.position);
			float distance = direction.normalize();
			if(distance == 0) { return; }
			Vec2 correction = direction.mul((distance - length)/distance);
			a.position.set(a.position.add(correction));
			b.position.set(b.position.sub(correction));
		}
		
	}
	
	private class PinConstraint implements Constraint {
		
		private Particle particle;
		private Transform transform;

		public PinConstraint(Particle particle, Transform transform) {
			this.particle = particle;
			this.transform = transform;
		}

		@Override
		public void solve(float delta) {
			particle.position.set(transform.position);
		}
		
	}
}
