package ndy.game.math;

import android.util.FloatMath;

//matrice float [] m in column major format:
//m[0]  m[4]  m[8]  m[12]
//m[1]  m[5]  m[9]  m[13]
//m[2]  m[6]  m[10] m[14]
//m[3]  m[7]  m[11] m[15]
//
//where m[12] m[13] m[14] are the translation components

public class Quaternion {
	public float x, y, z, w;
	
	public Quaternion() {
		x = y = z = w = 0f;
	}
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternion(Quaternion q) {
		x = q.x;
		y = q.y;
		z = q.z;
		w = q.w;
	}
	
	public void normalize() {
		float d = FloatMath.sqrt(x*x + y*y + z*z + w*w);
		x /= d;
		y /= d;
		z /= d;
		w /= d;
	}
	
	public void multiply(Quaternion q) {
		float neww = w*q.w - x*q.x - y*q.y - z*q.z;
		float newx = w*q.x + x*q.w + y*q.z - z*q.y;
		float newy = w*q.y - x*q.z + y*q.w + z*q.x;
		float newz = w*q.z + x*q.y - y*q.x + z*q.w;
		x = newx;
		y = newy;
		z = newz;
		w = neww;
	}
	
	public void fromAxis(Vector3 v, float a) {
		float sinAngle;
		a = a * NDYMath.TO_RADIANS * 0.5f;
		Vector3 vn = new Vector3(v);
		vn.normalize();
		sinAngle = FloatMath.sin(a);
		x = vn.x * sinAngle;
		y = vn.y * sinAngle;
		z = vn.z * sinAngle;
		w = FloatMath.cos(a);
	}
	
	/**
	 * Init quaternion from euler angles in degrees.
	 * @param float yaw rotation around Y
	 * @param float pitch rotation around X
	 * @param float roll rotation around Z
	 */
	public void fromEuler(float yaw, float pitch, float roll)
	{	 
		float p = pitch * NDYMath.TO_RADIANS / 2.0f;
		float y = yaw * NDYMath.TO_RADIANS / 2.0f;
		float r = roll * NDYMath.TO_RADIANS / 2.0f;
	 
		float sinp = FloatMath.sin(p);
		float siny = FloatMath.sin(y);
		float sinr = FloatMath.sin(r);
		float cosp = FloatMath.cos(p);
		float cosy = FloatMath.cos(y);
		float cosr = FloatMath.cos(r);
		
		this.w = cosy * cosp * cosr - siny * sinp * sinr;
		this.x = siny * sinp * cosr + cosy * cosp * sinr;
		this.y = siny * cosp * cosr + cosy * sinp * sinr;
		this.z = cosy * sinp * cosr - siny * cosp * sinr;
	 	 
		normalize();
	}

	public void fromRotationMatrix(float [] m) {
		float tr = m[0] + m[5] + m[10];

		if (tr > 0) {
		  float S = FloatMath.sqrt(tr+1.f) * 2f; // S=4*qw 
		  w = 0.25f * S;
		  x = (m[6] - m[9]) / S;
		  y = (m[8] - m[2]) / S;
		  z = (m[1] - m[4]) / S;
		} else if ((m[0] > m[5])&(m[0] > m[10])) { 
		  float S = FloatMath.sqrt(1.0f + m[0] - m[5] - m[10]) * 2f; // S=4*qx 
		  w = (m[6] - m[9]) / S;
		  x = 0.25f * S;
		  y = (m[4] + m[1]) / S; 
		  z = (m[8] + m[3]) / S;
		} else if (m[5] > m[10]) {
		  float S = FloatMath.sqrt(1.0f + m[5] - m[0] - m[10]) * 2f; // S=4*qy
		  w = (m[8] - m[3]) / S;
		  x = (m[4] + m[1]) / S;
		  y = 0.25f * S;
		  z = (m[9] + m[6]) / S;
		} else {
		  float S = FloatMath.sqrt(1.0f + m[10] - m[0] - m[5]) * 2f; // S=4*qz
		  w = (m[1] - m[4]) / S;
		  x = (m[8] + m[3]) / S;
		  y = (m[9] + m[6]) / S;
		  z = 0.25f * S;
		}
	}
	
	public float[] getMatrix() {
		float [] m = new float[16];
		
		float x2 = x * x;
		float y2 = y * y;
		float z2 = z * z;
		float xy = x * y;
		float xz = x * z;
		float yz = y * z;
		float wx = w * x;
		float wy = w * y;
		float wz = w * z;

		m[0] = 1 - 2 * (y2 + z2);
		m[1] = 2 * (xy + wz);
		m[2] = 2 * (xz - wy);
		m[3] = 0;
		
		m[4] = 2 * (xy - wz);
		m[5] = 1 - 2 * (x2 + z2);
		m[6] = 2 * (yz + wx);
		m[7] = 0;
		
		m[8] = 2 * (xz + wy);
		m[9] = 2 * (yz - wx);
		m[10] = 1 - 2 * (x2 + y2);
		m[11] = 0;
		
	    m[12] = 0;
	    m[13] = 0;
	    m[14] = 0;
	    m[15] = 1;
		
		return m;
	}
	
	public static void slerp(Quaternion qm, Quaternion qa, Quaternion qb, float t) {
		float cosHalfTheta = qa.w * qb.w + qa.x * qb.x + qa.y * qb.y + qa.z * qb.z;
		
		if (NDYMath.abs(cosHalfTheta) >= 1.f){
			qm.w = qa.w;qm.x = qa.x;qm.y = qa.y;qm.z = qa.z;
		}
		
		float halfTheta = (float)Math.acos(cosHalfTheta);
		float sinHalfTheta = FloatMath.sqrt(1.f - cosHalfTheta*cosHalfTheta);

		if (NDYMath.abs(sinHalfTheta) < 0.001f) {
			qm.w = (qa.w * 0.5f + qb.w * 0.5f);
			qm.x = (qa.x * 0.5f + qb.x * 0.5f);
			qm.y = (qa.y * 0.5f + qb.y * 0.5f);
			qm.z = (qa.z * 0.5f + qb.z * 0.5f);
		}
		
		float ratioA = FloatMath.sin((1f - t) * halfTheta) / sinHalfTheta;
		float ratioB = FloatMath.sin(t * halfTheta) / sinHalfTheta; 

		qm.w = (qa.w * ratioA + qb.w * ratioB);
		qm.x = (qa.x * ratioA + qb.x * ratioB);
		qm.y = (qa.y * ratioA + qb.y * ratioB);
		qm.z = (qa.z * ratioA + qb.z * ratioB);
	}
	
	public Quaternion getConjugate() {
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Vector3 rotateVector(Vector3 v) {
		Quaternion qv = new Quaternion(v.x, v.y, v.z, 0);
		Quaternion qc = getConjugate();
		Quaternion q = new Quaternion(this);
		qv.multiply(qc);
		q.multiply(qv);
		v.x = q.x;
		v.y = q.y;
		v.z = q.z;
		
		return v;
	}
}
