package math;

public class AABB {
	
	public float left;
	public float top;
	public float right;
	public float bottom;
	
	public AABB() {
		this(0, 0, 0, 0);
	}
	
	public AABB(AABB source) {
		this(source.left, source.top, source.right, source.bottom);
	}
	
	public AABB(float left, float top, float right, float bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		assert(left <= right && top <= bottom);
	}
	
	public AABB translate(Vec2 delta) {
		return new AABB(
			this.left + delta.x,
			this.right + delta.x,
			this.top + delta.y,
			this.bottom + delta.y
		);
	}
	
	public Vec2 getCenter() {
		return new Vec2(left + getWidth()/2, top + getHeight()/2);
	}
	
	public float getWidth() {
		return right - left;
	}
	
	public float getHeight() {
		return bottom - top;
	}
	
	public boolean contains(Vec2 point) {
		return contains(point.x, point.y);
	}
	
	public boolean contains(float x, float y) {
		return x >= left && x < right && y >= top && y < bottom;
	}
	
	public boolean intersects(AABB other) {
		Vec2 v = other.getCenter().sub(this.getCenter());
		float cw = this.getWidth()/2 + other.getWidth()/2;
		float ch = this.getHeight()/2 + other.getHeight()/2;
		return Mathf.abs(v.x) < cw && Mathf.abs(v.y) < ch;
	}
	
	@Override
	public AABB clone() {
		return new AABB(this);
	}

	@Override
	public String toString() {
		return "AABB [left=" + left + ", top=" + top + ", right=" + right + ", bottom=" + bottom + "]";
	}
	
}
