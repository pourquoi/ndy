package ndy.game.math;

public class NDYMath {
	public static float PI = (float) Math.PI;
	public static float HALF_PI = (float) (Math.PI / 2.0);
	public static float TWOPI = (float) (Math.PI * 2.0);
	public static float TO_RADIANS = 0.0174532925f;
	public static float TO_DEGREES = 57.2957795f;

	public static float abs(float v) {
		if (v < 0f)
			return -v;
		return v;
	}

	public static float max(float a, float b) {
		if (a > b)
			return a;
		return b;
	}

	public static float min(float a, float b) {
		if (a < b)
			return a;
		return b;
	}

	public static float sign(float a) {
		return a < 0f ? -1f : 1f;
	}

	public static float clamp(float v, float a, float b) {
		if (v < a)
			v = a;
		if (v > b)
			v = b;
		return v;
	}

	public static float clampDegrees(float v) {
		float a = v / 360.f;

		v -= ((int) a) * 360.f;

		if (v < 0.f)
			v += 360.f;

		return v;
	}
}
