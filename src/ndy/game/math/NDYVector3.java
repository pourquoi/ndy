package ndy.game.math;

import android.util.FloatMath;

public class NDYVector3 {
	public float x, y, z;
	
	public NDYVector3() {
		x = y = z = 0f;
	}
	
	public NDYVector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public NDYVector3(NDYVector3 v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public NDYVector3 normalize() {
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
	
	public float squaredLength() {
		return x*x + y*y + z*z;
	}
	
	public float distance(NDYVector3 v) {
		NDYVector3 v2 = new NDYVector3(this);
		return v2.substract(v).length();
	}
	
	public NDYVector3 add(NDYVector3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
		
		return this;
	}
	
	public NDYVector3 substract(NDYVector3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		
		return this;
	}
	
	public NDYVector3 scale(float s) {
		x *= s;
		y *= s;
		z *= s;
		
		return this;
	}
	
	public String toString() {
		return "["+x+","+y+","+z+"]";
	}
	
	public static float dotProduct(NDYVector3 a, NDYVector3 b) {
		return a.x*b.x+a.y*b.y+a.z*b.z;
	}
	
	public static void crossProduct(NDYVector3 a, NDYVector3 b, NDYVector3 r) {
		r.x = a.y * b.z - a.z * b.y;
		r.y = a.z * b.x - a.x * b.z;
		r.z = a.x * b.y - a.y * b.x;
	}
}
