package math;

import java.util.Random;

import util.Log;


public class Mathf {
	
	public static final float EPSILON = 1.1920928955078125E-7f;
	public static final float PI = (float) Math.PI;
	
	private static final int RANDOM_SEED = 18;
	private static final Random RANDOM = new Random(RANDOM_SEED);
	
	public static float sqrt(float x) {
		return (float)Math.sqrt(x);
	}
	
	public static float sin(float a) {
		return (float)Math.sin(a);
	}
	
	public static float cos(float a) {
		return (float)Math.cos(a);
	}
	
	public static float tan(float a) {
		return (float)Math.tan(a);
	}
	
	public static float asin(float a) {
		return (float)Math.asin(a);
	}
	
	public static float acos(float a) {
		return (float)Math.acos(a);
	}
	
	public static float atan(float a) {
		return (float)Math.atan(a);
	}
	
	public static int sign(float x) {
		return (int)Math.signum(x);
	}
	
	public static float round(float x) {
		return (float)Math.round(x);
	}
	
	public static float random() {
		return RANDOM.nextFloat();
	}
	
	public static float pow(float x, float power) {
		return (float)Math.pow(x, power);
	}
	
	public static float abs(float x) {
		return x < 0 ? -x : x;
	}
	
	public static float min(float x, float y) {
		return x < y ? x : y;
	}
	
	public static float max(float x, float y) {
		return x > y ? x : y;
	}
	
	public static float ceil(float x) {
		return (float) Math.ceil(x);
	}
	
	public static float floor(float x) {
		return (float) Math.floor(x);
	}
	
	public static float clamp(float x, float min, float max) {
		float v = (max < x ? max : x);
		return min > v ? min : v;
	}
	
	public static float lerp(float a, float b, float t) {
		if(t < 0 || t > 1) 
			throw new IllegalArgumentException("t must be (0 >= t <= 1)");
		return (1-t)*a + t*b;
	}
	
	public static float smoothlerp(float a, float b, float t) {
		if(t < 0 || t > 1) {
			Log.error("t must be (0 >= t <= 1) was: {}. Clamping", t);
			t = Mathf.clamp(t, 0, 1);
		}
		
		t = smoothstep(0, 1, t);
		return (1-t)*a + t*b;
	}
	
	public static float smootherlerp(float a, float b, float t) {
		if(t < 0 || t > 1) 
			throw new IllegalArgumentException("t must be (0 >= t <= 1)");
		t = smootherstep(0, 1, t);
		return (1-t)*a + t*b;
	}
	
	public static Vec2 lerp(Vec2 a, Vec2 b, float t) {
		return new Vec2(lerp(a.x, b.x, t), lerp(a.y,b.y, t));
	}
	
	public static float smoothstep(float a, float b, float x) {
		x = clamp((x - a)/(b - a), 0, 1);
		return x*x*(3 - 2*x);
	}

	public static float smootherstep(float a, float b, float x) {
		x = clamp((x - a)/(b - a), 0, 1);
		return x*x*x*(x*(x*6 - 15) + 10);
	}

}
