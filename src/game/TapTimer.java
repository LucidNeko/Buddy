package game;

import core.IUpdateable;
import math.Mathf;

public abstract class TapTimer implements IUpdateable {
	
	public abstract boolean onDoubleTap();
	public abstract boolean onDoubleTapOff();
	
	public abstract boolean isTap();
	
	public void reset() {
		tapped = 0;
		taps = 0;
		window = 0;
		onDoubleTapOff();
	}

	float tapped = 0;
	
	int taps = 0;
	
	float window = 0;
	
	@Override
	public void update(float delta) {
		if(taps == 0) {
			if(isTap()) {
				tapped = 0.2f;
				taps++;
			}
		} else if(taps == 1) {
			if(isTap()) {
				onDoubleTap();
				taps = 0;
				tapped = 0;
				window = 0.2f;
			}
		}
		
		tapped = Mathf.max(0, tapped - delta);
		window = Mathf.max(0, window - delta);
		
		if(tapped == 0) {
			taps = 0;
		}
		
		if(window == 0) {
			onDoubleTapOff();
		}
	}

}
