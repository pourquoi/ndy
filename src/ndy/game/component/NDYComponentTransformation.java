package ndy.game.component;

import ndy.game.math.Quaternion;
import ndy.game.math.Vector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;

public class NDYComponentTransformation extends NDYComponent {
	protected Vector3 mPos = new Vector3();
	protected Vector3 mRot = new Vector3();
	protected Vector3 mScale = new Vector3(1,1,1);

	protected Quaternion mRotQ = new Quaternion();
	
	public NDYComponentTransformation() {
		super("transformation");
	}
	
	public boolean processMessage(NDYMessage msg) {
		if( msg.getClass() == NDYMessageUpdate.class ) {
			mRotQ.fromEuler(mRot.y, mRot.z, mRot.x);
		}

		return false;
	}
	
	public Vector3 getPos() {
		return mPos;
	}
	
	public void setPos(float x, float y, float z) {
		mPos.x = x;
		mPos.y = y;
		mPos.z = z;
	}
	
	public Vector3 getRot() {
		return mRot;
	}
	
	public void setRot(float x, float y, float z) {
		mRot.x = x;
		mRot.y = y;
		mRot.z = z;
	}
	
	public Vector3 getScale() {
		return mScale;
	}
	
	public void setScale(float x, float y, float z) {
		mScale.x = x;
		mScale.y = y;
		mScale.z = z;
	}
	
	public Quaternion getRotQ() {
		return mRotQ;
	}

}