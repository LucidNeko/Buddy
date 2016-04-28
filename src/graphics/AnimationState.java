package graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import core.IUpdateable;
import math.Predicate;

public class AnimationState implements IUpdateable {
	
	private Map<Predicate, AnimationState> transitions = new HashMap<Predicate, AnimationState>();
	
	private Animation animation;
	
	public AnimationState(Animation animation) {
		this.animation = animation;
	}
	
	public void addTransition(Predicate predicate, AnimationState state) {
		transitions.put(predicate, state);
	}
	
	public AnimationState transition() {
		for(Entry<Predicate, AnimationState> entry : transitions.entrySet()) {
			if(entry.getKey().test()) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	public void onEnter() {
		animation.reset();
	}
	
	@Override
	public void update(float delta) {
		animation.update(delta);
	}
	
	public void onExit() {
		animation.reset();
	}

}
