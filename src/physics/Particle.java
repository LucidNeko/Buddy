package physics;

import math.Mathf;
import math.Vec2;

public abstract class Particle {
	
	protected Vec2 oldPosition;
	protected Vec2 position;
	protected Vec2 velocity;
	protected Vec2 forces;
	
	private float life;
	private float lifetime;
	
	public Particle(Vec2 initialPosition, Vec2 forces, float lifetime) {
		position = new Vec2(initialPosition);
		oldPosition = new Vec2(initialPosition);
		velocity = new Vec2();
		this.forces = new Vec2(forces);
		this.life = lifetime;
		this.lifetime = lifetime;
	}
	
	public boolean isAlive() {
		return lifetime > 0f;
	}
	
	public float getLife() {
		if(lifetime <= 0) return 0;
		return Mathf.clamp(lifetime/life, 0, 1);
	}
	
	public Vec2 getPosition() {
		return new Vec2(position);
	}
	
	public abstract void impulse(Vec2 direction, float force);
	
	public void update(float delta) {
		this.lifetime -= delta;
	}

}
