package ndy.game.math;

public class Sphere {
	Vector3 c;
	float r;
	
	public Sphere(float x, float y, float z, float r) {
		c = new Vector3(x,y,z);
		this.r = r;
	}
}
