package physics;

import math.Transform;
import math.Vec2;

public class ParticleEmitter {

	public Transform transform = new Transform();
	
	public Vec2 direction;
	public float force;
	public float lifetime;
	
	private float accumulator = 0;
	private float rate;
	
	private ParticleSystem target = null;
	
	public ParticleEmitter(Vec2 position, Vec2 direction, float force, int rate, float lifetime) {
		transform.position.set(position);
		this.direction = new Vec2(direction);
		this.force = force;
		this.rate = 1f/rate;
		this.lifetime = lifetime;
	}
	
	public void setTargetParticleSystem(ParticleSystem target) {
		this.target = target;
	}
	
	public void update(float delta) {
		accumulator += delta;
		while(accumulator >= rate) {
			accumulator -= rate;
			Particle particle = target.addParticle(transform.position.x, transform.position.y, lifetime);
			particle.impulse(direction, force);
		}
	}
	
}
