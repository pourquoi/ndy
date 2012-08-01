package ndy.game.math;

public class NDYSphere {
	NDYVector3 c;
	float r;
	
	public NDYSphere(float x, float y, float z, float r) {
		c = new NDYVector3(x,y,z);
		this.r = r;
	}
}
