package ndy.game.math;

import org.jbox2d.common.MathUtils;

public class Vec3 extends org.jbox2d.common.Vec3 {
	public Vec3() {
		
	}
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float length() {
		return MathUtils.sqrt(x * x + y * y + z * z);
	}

	public float normalize() {
		float l = length();
		if (l != 0f) {
			x /= l;
			y /= l;
			z /= l;
		}
		return l;
	}
}
