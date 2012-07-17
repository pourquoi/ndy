package ndy.game.math;

import android.util.FloatMath;

public class NDYVector2 {
	public float x, y;

	public NDYVector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public NDYVector2(NDYVector2 v) {
		x = v.x;
		y = v.y;
	}

	public NDYVector2 normalize() {
		float d = FloatMath.sqrt(x * x + y * y);
		if (d != 0.f) {
			x /= d;
			y /= d;
		}

		return this;
	}
}
