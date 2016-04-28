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
	
	@Override
	public Vec2 clone() {
		return new Vec2(x, y);
	}

	@Override
	public String toString() {
		return "Vec2 [x=" + x + ", y=" + y + "]";
	}

}
