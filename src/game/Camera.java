package game;

import core.IUpdateable;
import core.Sprite;
import math.AABB;
import math.Mathf;
import math.Transform;
import math.Vec2;

public class Camera implements IUpdateable {
	
	private Transform transform;
	private Sprite[] targets;
	
	private AABB bounds;

	private int width = 400;
	private int height = 225;
	
	public Camera(AABB bounds, Sprite... targets) {
		transform = new Transform();
		this.targets = targets;
		this.bounds = bounds;
	}
	
	public void setTargets(Sprite... targets) {
		this.targets = targets;
	}
	
	public AABB getAABB() {
		return new AABB(transform.position.x - width * 0.5f, 
						transform.position.y - height * 0.5f, 
						transform.position.x + width * 0.5f, 
						transform.position.y + height * 0.5f);
	}
	
	public Vec2 transform(Vec2 position) {
		return position.sub(transform.position.sub(width*0.5f, height*0.5f));
	}
	
	public AABB transform(AABB box) {
		float x = transform.position.x - width * 0.5f;
		float y = transform.position.y - height * 0.5f;
		return new AABB(box.left - x, box.top - y, box.right - x, box.bottom - y);
	}

	@Override
	public void update(float delta) {
		Vec2 sum = new Vec2();
		for(Sprite sprite : targets) {
			sum = sum.add(sprite.transform().position);
		}
		sum = sum.div(targets.length);
		
		transform.position = transform.position.sslerp(sum, Mathf.clamp(0.20f-delta, 0, 1));
		transform.position.x = Mathf.clamp(transform.position.x, bounds.left + width * 0.5f, bounds.right - width * 0.5f);
		transform.position.y = Mathf.clamp(transform.position.y, bounds.top + height * 0.5f, bounds.bottom - height * 0.5f);
	}
	
}
