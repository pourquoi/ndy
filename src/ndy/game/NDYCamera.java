package ndy.game;

import ndy.game.math.Vector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;
import android.opengl.Matrix;
import android.util.Log;


public class NDYCamera extends NDYActor {
	public static int MODE_ORTHO = 1;
	public static int MODE_PERSPECTIVE = 2;

	private static String TAG = "NDYCamera";
	protected Vector3 mPos = new Vector3();
	protected Vector3 mTarget = new Vector3();
	protected float [] mViewMatrix = new float[16];
	protected float [] mProjectionMatrix = new float[16];
	protected int mWidth = 0, mHeight = 0;
	protected int mMode = MODE_PERSPECTIVE;
	
	public NDYCamera() {
		super();
	}
	
	@Override
	public boolean dispatchMessage(NDYMessage msg) {
		if( super.dispatchMessage(msg) ) return true;
		
		if( msg.getClass() == NDYMessageUpdate.class ) {
			if( mMode == MODE_PERSPECTIVE ) {
				Matrix.setLookAtM(mViewMatrix, 0, mPos.x, mPos.y, mPos.z, mTarget.x, mTarget.y, mTarget.z, 0, 1, 0);
			} else if( mMode == MODE_ORTHO ) {
				Matrix.setIdentityM(mViewMatrix, 0);
			}
		}

		return false;
	}
	
	public void resize(int w, int h) {
		Log.d(TAG, "surface changed ("+mWidth+"x"+mHeight+") -> ("+w+"x"+h+")");
		mWidth = w;
		mHeight = h;
		if( mMode == MODE_PERSPECTIVE ) {
			float ratio = (float) w / h;
			Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 1000);
		} else if( mMode == MODE_ORTHO ) {
			Matrix.orthoM(mProjectionMatrix, 0, 0, w, 0, h, -1, 1);
		}
	}
	
	public float [] getProjectionMatrix() {
		return mProjectionMatrix;
	}
	
	public float [] getViewMatrix() {
		return mViewMatrix;
	}
	
	public Vector3 getPos() {
		return mPos;
	}
	
	public void setPos(float x, float y, float z) {
		mPos.x = x;
		mPos.y = y;
		mPos.z = z;
	}
	
	public void setTarget(float x, float y, float z) {
		mTarget.x = x;
		mTarget.y = y;
		mTarget.z = z;
	}
	
	public Vector3 getTarget() {
		return mTarget;
	}
	
	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}
	
	public void setMode(int mode) {
		mMode = mode;
	}
	
	public int getMode() {
		return mMode;
	}
}
