package physics;

import math.Vec2;

public class VelocityParticle extends Particle {

	public VelocityParticle(Vec2 initialPosition, Vec2 forces, float lifetime) {
		super(initialPosition, forces, lifetime);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		position = position.add(velocity.mul(delta)).add(forces.mul(0.5f).mul(delta*delta));
		velocity = velocity.add(forces.mul(delta));
	}

	@Override
	public void impulse(Vec2 direction, float force) {
		velocity = velocity.add(direction.mul(force));
	}

}
