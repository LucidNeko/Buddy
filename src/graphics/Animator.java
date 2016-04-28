package graphics;

import core.IUpdateable;

public class Animator implements IUpdateable {
	
	private AnimationState state;
	
	public Animator(AnimationState initialState) {
		this.state = initialState;
		this.state.onEnter();
	}
	
	public Animation getAnimation() {
		return state.getAnimation();
	}

	@Override
	public void update(float delta) {
		state.update(delta);
		
		AnimationState transition = state.transition();
		if(transition != null) {
			state.onExit();
			state = transition;
			state.onEnter();
		}
	}

}
