package ndy.game.actor;

import ndy.game.math.Vector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;
import android.opengl.Matrix;


public class NDYCamera extends NDYTransformable {
	protected Vector3 mTarget = new Vector3();
	protected float [] mViewMatrix = new float[16];
	protected float [] mProjectionMatrix = new float[16];
	
	public NDYCamera() {
		super();
	}
	
	@Override
	public boolean dispatchMessage(NDYMessage msg) {
		if( super.dispatchMessage(msg) ) return true;
		
		if( msg.getClass() == NDYMessageUpdate.class ) {
			Matrix.setLookAtM(mViewMatrix, 0, mPos.x, mPos.y, mPos.z, mTarget.x, mTarget.y, mTarget.z, 0, 1, 0);
		}

		return false;
	}
	
	public void resize(int w, int h) {
		float ratio = (float) w / h;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 1000);
	}
	
	public float [] getProjectionMatrix() {
		return mProjectionMatrix;
	}
	
	public float [] getViewMatrix() {
		return mViewMatrix;
	}
	
	public void setTarget(float x, float y, float z) {
		mTarget.x = x;
		mTarget.y = y;
		mTarget.z = z;
	}
	
	public Vector3 getTarget() {
		return mTarget;
	}
}
