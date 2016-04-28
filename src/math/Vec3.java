package math;

public class Vec3 {

	public float x;
	public float y;
	public float z;
	
	// Constructor
	public Vec3() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vec3(Vec3 source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
	}
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//Conversion
	public Vec3(Vec2 source) {
		this.x = source.x;
		this.y = source.y;
		this.z = 0;
	}
	
	// Addition
	public Vec3 add(Vec3 o) {
		return add(o.x, o.y, o.z);
	}
	
	public Vec3 add(float x, float y, float z) {
		return new Vec3(this.x + x, this.y + y, this.z + z);
	}
	
	// Subtraction
	public Vec3 sub(Vec3 o) {
		return sub(o.x, o.y, o.z);
	}
	
	public Vec3 sub(float x, float y, float z) {
		return new Vec3(this.x - x, this.y - y, this.z - z);
	}
	
	// Multiplication
	public Vec3 mul(Vec3 o) {
		return mul(o.x, o.y, o.z);
	}
	
	public Vec3 mul(float x, float y, float z) {
		return new Vec3(this.x * x, this.y * y, this.z * z);
	}
	
	public Vec3 mul(float factor) {
		return new Vec3(x * factor, y * factor, z * factor);
	}
	
	// Utility
	public Vec3 abs() {
		return new Vec3(Mathf.abs(x), Mathf.abs(y), Mathf.abs(z));
	}
	
	public float lengthSquared() {
		return x*x + y*y + z*z;
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
		this.z *= invLength;
		return length;
	}
	
	public Vec3 normalized() {
		float length = length();
		if(length < Mathf.EPSILON) { 
			return new Vec3(); 
		}
		
		float invLength = 1f/length;
		return new Vec3(x *= invLength, y *= invLength, z *= invLength);
	}
	
	public float dot(Vec3 other) {
		return this.x * other.x + 
			   this.y * other.y + 
			   this.z + other.z;
	}

	@Override
	public String toString() {
		return "Vec3 [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
}
