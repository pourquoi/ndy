package ndy.game.component;

import ndy.game.NDYActor;
import ndy.game.math.Vector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;


public class NDYComponentCinematic extends NDYComponent {
	protected Vector3 mDir;
	
	public NDYComponentCinematic() {
		super("cinematic");
		
		mDir = new Vector3(1f,0f,0f);
	}
	
	public boolean processMessage(NDYMessage msg) {
		if( msg.getClass() == NDYMessageUpdate.class ) {
			NDYActor r = mParent;
			NDYComponentTransformation trans = (NDYComponentTransformation)r.findComponent("transformation");
			if( trans != null ) {
				Vector3 dir = new Vector3(1f,0f,0f);
				mDir = trans.getRotQ().rotateVector(dir);
			}
		}
		
		return false;
	}
	
	public Vector3 getDir() {
		return mDir;
	}
	
	public void setDir(float x, float y, float z) {
		mDir.x = x;
		mDir.y = y;
		mDir.z = z;
	}
}
