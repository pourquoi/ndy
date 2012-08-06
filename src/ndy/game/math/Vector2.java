package ndy.game.math;

import android.util.FloatMath;

public class Vector2 {
	public float x, y;

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 v) {
		x = v.x;
		y = v.y;
	}

	public Vector2 normalize() {
		float d = FloatMath.sqrt(x * x + y * y);
		if (d != 0.f) {
			x /= d;
			y /= d;
		}

		return this;
	}
}
