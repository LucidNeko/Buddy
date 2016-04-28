package physics;

import math.Vec2;

public class VerletParticle extends Particle {

	public VerletParticle(Vec2 initialPosition, Vec2 forces, float lifetime) {
		super(initialPosition, forces, lifetime);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		velocity = position.sub(oldPosition).mul(delta);
		oldPosition.set(position);
		position = position.add(velocity).add(forces.mul(0.5f).mul(delta*delta));
	}

	@Override
	public void impulse(Vec2 direction, float force) {
		position = position.add(direction.mul(force));
	}

}
