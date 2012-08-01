package ndy.game.math;

public class NDYBox {
	public NDYVector3 pos;
	public NDYVector3 size;
	
	public NDYBox() {
		
	}
	
	public NDYBox(NDYVector3 pos, NDYVector3 size) {
		this.pos = pos;
		this.size = size;
	}
	
	public NDYBox(float x, float y, float z, float w, float h, float l) {
		pos = new NDYVector3(x,y,z);
		size = new NDYVector3(w,h,l);
	}
}
