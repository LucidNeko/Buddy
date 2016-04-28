package physics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import math.Transform;
import math.Vec2;

public class ParticleSystem {
	
	private Vec2 gravity = new Vec2(0, 120);

	private Set<Particle> particles = new LinkedHashSet<Particle>();
	
	private Map<Particle, HashSet<Transform>> links = new HashMap<Particle, HashSet<Transform>>();
	
	private Set<ParticleEmitter> emitters = new LinkedHashSet<ParticleEmitter>();
	
	public ParticleSystem() {
		
	}
	
	public Set<Particle> getParticles() {
		return particles;
	}
	
	public void addEmitter(ParticleEmitter emitter) {
		emitter.setTargetParticleSystem(this);
		emitters.add(emitter);
	}
	
	public Particle addParticle(float x, float y, float lifetime) {
		Particle particle = new VelocityParticle(new Vec2(x, y), gravity, lifetime);
		particles.add(particle);
		return particle;
	}
	
	public void link(Particle particle, Transform transform) {
		if(particles.contains(particle)) {
			if(!links.containsKey(particle)) {
				links.put(particle, new HashSet<Transform>());
			}
			links.get(particle).add(transform);
		}
	}
	
	public void update(float delta) {
		for(ParticleEmitter emitter : emitters) {
			emitter.update(delta);
		}
		
		Set<Particle> remove = new HashSet<Particle>();
		for(Particle particle : particles) {
			particle.update(delta);
			if(!particle.isAlive()) {
				remove.add(particle);
			}
		}
		particles.removeAll(remove);
		
		for(Entry<Particle, HashSet<Transform>> entry : links.entrySet()) {
			for(Transform transform : entry.getValue()) {
				transform.position.set(entry.getKey().getPosition());
			}
		}
	}
	
	
}
