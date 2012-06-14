package ndy.game.component;

import ndy.game.NDYWorld;
import ndy.game.actor.NDYTransformable;
import ndy.game.math.NDYMath;
import ndy.game.math.Vector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;
import android.util.FloatMath;
import android.util.Log;

public class NDYComponentCinematicSailboat extends NDYComponentCinematic {
	private static String TAG = "NDYComponentCinematicSailboat";
	
	private static int HEADWIND_STARBOARD = 1; // +x -y
	private static int HEADWIND_PORTSIDE = 2; // -x -y
	private static int BACKWIND_STARBOARD = 3; // +x +y
	private static int BACKWIND_PORTSIDE = 4; // -x +y
	
	private int mWindOrientation;
	
	private float mMainSailRot = 0; // main sail rotation in degrees
	private float mJibSailRot = 0; // jib sail rotation in degrees
	
	public boolean processMessage(NDYMessage msg) {
		super.processMessage(msg);
		
		if( msg.getClass() == NDYMessageUpdate.class ) {
			NDYTransformable r = (NDYTransformable)mParent;
			NDYComponentMeshSailboat b = (NDYComponentMeshSailboat)r.findComponent("mesh");
			if( b != null ) {
				float a = mMainSailRot;

				Vector3 w = NDYWorld.current.getWindDir();
				Vector3 v = new Vector3(-FloatMath.sin(a*NDYMath.TO_RADIANS),0.f,-FloatMath.cos(a*NDYMath.TO_RADIANS));
				r.getRotQ().rotateVector(v);
				v.normalize();
				Vector3 F = Vector3.crossProduct(v, w);
				
				float wv = Vector3.dotProduct(v, w);
				float wd = Vector3.dotProduct(mDir, w);
				
				float det = Vector3.det(w, mDir);
				
				if( wd < 0 ) {
					if( det < 0 ) {
						if( mWindOrientation != HEADWIND_PORTSIDE ) {
							mWindOrientation = HEADWIND_PORTSIDE;
							Log.i(TAG, "wind orientation: headwind portside");
						}
					} else {
						if( mWindOrientation != HEADWIND_STARBOARD ) {
							mWindOrientation = HEADWIND_STARBOARD;
							Log.i(TAG, "wind orientation: headwind starboard");
						}
					}
				} else {
					if( det < 0 ) {
						if( mWindOrientation != BACKWIND_PORTSIDE ) {
							mWindOrientation = BACKWIND_PORTSIDE;
							Log.i(TAG, "wind orientation: backwind portside");
						}
					} else {
						if( mWindOrientation != BACKWIND_STARBOARD ) {
							mWindOrientation = BACKWIND_STARBOARD;
							Log.i(TAG, "wind orientation: backwind starboard");
						}
					}
				}
				
				Vector3 delta = new Vector3(mDir);
				delta.scale(F.length()/10.f);
				//r.getPos().add(delta);
			}
		}
		
		return false;
	}

	public void setMainSailRot(float r) {
		if( r > 80 ) r = 80;
		else if( r < -80 ) r = -80;
		
		mMainSailRot = r;
	}
	
	public float getMainSailRot() {
		return mMainSailRot;
	}
	
	public void setJibSailRot(float r) {
		mJibSailRot = r;
	}
	
	public float getJibSailRot() {
		return mJibSailRot;
	}
}
