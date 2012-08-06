package ndy.game.math;

public class Box {
	public Vector3 pos;
	public Vector3 size;
	
	public Box() {
		
	}
	
	public Box(Vector3 pos, Vector3 size) {
		this.pos = pos;
		this.size = size;
	}
	
	public Box(float x, float y, float z, float w, float h, float l) {
		pos = new Vector3(x,y,z);
		size = new Vector3(w,h,l);
	}
}
