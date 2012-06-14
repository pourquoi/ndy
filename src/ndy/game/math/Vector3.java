package ndy.game.math;

import android.util.FloatMath;

public class Vector3 {
	public float x, y, z;
	
	public Vector3() {
		x = y = z = 0f;
	}
	
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(Vector3 v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public Vector3 normalize() {
		float d = FloatMath.sqrt(x*x + y*y + z*z);
		if(d!=0.f) {
		x /= d;
		y /= d;
		z /= d;
		}
		
		return this;
	}
	
	public float length() {
		return FloatMath.sqrt(x*x + y*y + z*z);
	}
	
	public float distance(Vector3 v) {
		Vector3 v2 = new Vector3(this);
		return v2.substract(v).length();
	}
	
	public Vector3 add(Vector3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
		
		return this;
	}
	
	public Vector3 substract(Vector3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		
		return this;
	}
	
	public Vector3 scale(float s) {
		x *= s;
		y *= s;
		z *= s;
		
		return this;
	}
	
	public static float dotProduct(Vector3 a, Vector3 b) {
		return a.x*b.x+a.y*b.y+a.z*b.z;
	}
	
	public static Vector3 crossProduct(Vector3 a, Vector3 b) {
		Vector3 v = new Vector3();
		v.x = a.y * b.z - a.z * b.y;
		v.y = a.z * b.x - a.x * b.z;
		v.z = a.x * b.y - a.y * b.x;
		return v;
	}
}
