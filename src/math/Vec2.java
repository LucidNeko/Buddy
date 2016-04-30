package math;


public class Vec2 {
	
	public float x;
	public float y;
	
	// Constructor
	public Vec2() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vec2(Vec2 source) {
		this.x = source.x;
		this.y = source.y;
	}
	
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	// Set
	public void setZero() {
		this.x = 0;
		this.y = 0;
	}
	
	public void set(Vec2 source) {
		this.x = source.x;
		this.y = source.y;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	// Get Convenience
	public int x() {
		return (int) x;
	}
	
	public int y() {
		return (int) y;
	}
	
	// Addition
	public Vec2 add(Vec2 o) {
		return add(o.x, o.y);
	}
	
	public Vec2 add(float x, float y) {
		return new Vec2(this.x + x, this.y + y);
	}
	
	// Subtraction
	public Vec2 sub(Vec2 o) {
		return sub(o.x, o.y);
	}
	
	public Vec2 sub(float x, float y) {
		return new Vec2(this.x - x, this.y - y);
	}
	
	// Multiplication
	public Vec2 mul(Vec2 o) {
		return mul(o.x, o.y);
	}
	
	public Vec2 mul(float x, float y) {
		return new Vec2(this.x * x, this.y * y);
	}
	
	public Vec2 mul(float factor) {
		return new Vec2(x * factor, y * factor);
	}
	
	// Division
	public Vec2 div(Vec2 o) {
		return div(o.x, o.y);
	}
	
	public Vec2 div(float x, float y) {
		return new Vec2(this.x / x, this.y / y);
	}
	
	public Vec2 div(float factor) {
		return new Vec2(x / factor, y / factor);
	}
	
	// Utility
	public Vec2 ceil() {
		return new Vec2(Mathf.ceil(x), Mathf.ceil(y));
	}
	
	public Vec2 floor() {
		return new Vec2(Mathf.floor(x), Mathf.floor(y));
	}
	
	public Vec2 round() {
		return new Vec2(Mathf.round(x), Mathf.round(y));
	}
	
	public Vec2 abs() {
		return new Vec2(Mathf.abs(x), Mathf.abs(y));
	}
	
	public float lengthSquared() {
		return x*x + y*y;
	}
	
	public float length() {
		return Mathf.sqrt(lengthSquared());
	}
	
	public float normalize() {
		float length = length();
		if(length < Mathf.EPSILON) { 
			return 0f; 
		}
		
		float invLength = 1f/length;
		this.x *= invLength;
		this.y *= invLength;
		return length;
	}
	
	public Vec2 normalized() {
		float length = length();
		if(length < Mathf.EPSILON) { 
			return new Vec2(); 
		}
		
		float invLength = 1f/length;
		return new Vec2(x *= invLength, y *= invLength);
	}
	
	public float dot(Vec2 other) {
		return this.x * other.x + this.y * other.y;
	}
	
	public Vec2 lerp(Vec2 dest, float t) {
		return new Vec2(Mathf.lerp(x, dest.x, t), Mathf.lerp(y, dest.y, t));
	}
	
	public Vec2 slerp(Vec2 dest, float t) {
		return new Vec2(Mathf.smoothlerp(x, dest.x, t), Mathf.smoothlerp(y, dest.y, t));
	}
	
	public Vec2 sslerp(Vec2 dest, float t) {
		return new Vec2(Mathf.smootherlerp(x, dest.x, t), Mathf.smootherlerp(y, dest.y, t));
	}
	
	@Override
	public Vec2 clone() {
		return new Vec2(x, y);
	}

	public boolean epsilonEquals(Vec2 o) {
		return Mathf.abs(x - o.x) < Mathf.EPSILON && Mathf.abs(y - o.y) < Mathf.EPSILON;
	}
	
	public boolean equals(Vec2 o, float threshold) {
		return Mathf.abs(x - o.x) < threshold&& Mathf.abs(y - o.y) < threshold;
	}

	@Override
	public String toString() {
		return String.format("Vec2 [x=%.5f, y=%.5f]", x, y);
	}

}
