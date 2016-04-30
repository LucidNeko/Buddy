package math;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Transform {

	private Transform parent = null;
	private Set<Transform> childern = new HashSet<Transform>();
	
	public Vec2 position = new Vec2();
	
	public Transform() {
		position = new Vec2();
		setParent(null);
	}
	
	public Transform getParent() {
		return parent;
	}
	
	public void setParent(Transform t) {
		if(parent != null) {
			parent.childern.remove(this);
		}
		
		if(t != null) {
			t.childern.add(this);
		}
		
		parent = t;
	}
	
	
	public Vec2 worldPosition() {
		if(parent != null) {
			return parent.worldPosition().add(position);
		}
		return new Vec2(position);
	}

}
