package ndy.game.component;

import ndy.game.NDYActor;
import ndy.game.NDYWorld;
import ndy.game.math.NDYMath;
import ndy.game.math.Vector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;
import android.util.FloatMath;
import android.util.Log;

public class NDYComponentCinematicSailboat extends NDYComponentCinematic {
	private static String TAG = "NDYComponentCinematicSailboat";
	
	public static int HEADWIND_STARBOARD = 1; // +x -y
	public static int HEADWIND_PORTSIDE = 2; // -x -y
	public static int BACKWIND_STARBOARD = 3; // +x +y
	public static int BACKWIND_PORTSIDE = 4; // -x +y

	private int mWindOrientation;

	private float mMainSailRot = 0.f; // main sail rotation in degrees
	private float mJibSailRot = 0.f; // jib sail rotation in degrees
	
	private float mSpeed = 0.f; // boat speed in m/s
	private float mWeight = 550.f; // in kg
	
	public NDYComponentCinematicSailboat() {
		//NDYWorld.current.getUI().addGraph("boat speed", 1.f, 0.f, 0.f);
	}

	public boolean processMessage(NDYMessage msg) {
		super.processMessage(msg);
		
		if( msg.getClass() == NDYMessageUpdate.class ) {
			float dt = ((NDYMessageUpdate)msg).getInterval();
			NDYActor boat = mParent;
			NDYComponentTransformation trans = (NDYComponentTransformation)boat.findComponent("transformation");
			NDYComponentMeshSailboat mesh = (NDYComponentMeshSailboat)boat.findComponent("mesh");
			
			if( trans != null && mesh != null ) {				
				float a_wind = NDYWorld.current.getWindRot();
				float a_boat = trans.getRot().y;
				float a_mainsail = mMainSailRot + a_boat;
				
				float a_boat_wind = NDYMath.clampDegrees(a_boat - a_wind);
				float a_mainsail_wind = NDYMath.clampDegrees(a_mainsail - a_wind);
				
				Vector3 v_boat_dir = new Vector3(FloatMath.cos(a_boat*NDYMath.TO_RADIANS),0.f,FloatMath.sin(a_boat*NDYMath.TO_RADIANS));
				Vector3 v_wind = new Vector3(FloatMath.cos(a_wind*NDYMath.TO_RADIANS),0.f,FloatMath.sin(a_wind*NDYMath.TO_RADIANS));
				v_wind.scale(NDYWorld.current.getWindSpeed());
				Vector3 v_speed = new Vector3(v_boat_dir);
				v_speed.scale(mSpeed);
				Vector3 v_awind = new Vector3(v_wind).substract(v_speed);

				float vv = v_awind.squaredLength();
				float f = 0.f;
				float r = 0.f;
				float ci = 0.f;
				float kp = 1.f;
				float kr = 0.1f;
				
				ci = NDYMath.abs(FloatMath.sin(a_mainsail_wind*NDYMath.TO_RADIANS));
				
				if( ci < 0.14f ) ci = 0.f; 
				
				if( a_boat_wind >= 0.f && a_boat_wind < 90.f ) {
					if( mWindOrientation != BACKWIND_STARBOARD ) {
						mWindOrientation = BACKWIND_STARBOARD;
						Log.i(TAG, "wind orientation: backwind starboard");
					}
				} else if( a_boat_wind >= 90.f && a_boat_wind < 180.f ) {
					if( mWindOrientation != HEADWIND_STARBOARD ) {
						mWindOrientation = HEADWIND_STARBOARD;
						Log.i(TAG, "wind orientation: headwind starboard");
					}
				} else if( a_boat_wind >= 180.f && a_boat_wind < 270.f ) {
					if( mWindOrientation != HEADWIND_PORTSIDE ) {
						mWindOrientation = HEADWIND_PORTSIDE;
						Log.i(TAG, "wind orientation: headwind portside");
					}
				} else if( a_boat_wind >= 270.f && a_boat_wind < 360.f ) {
					if( mWindOrientation != BACKWIND_PORTSIDE ) {
						mWindOrientation = BACKWIND_PORTSIDE;
						Log.i(TAG, "wind orientation: backwind portside");
					}
				}
				
				r = kr * mSpeed * mSpeed;

				f = kp * ci * vv - r;
				
				//Log.i(TAG, "boat speed:"+mSpeed+", ap. wind speed^2:"+vv+", r:"+r+", ci:"+ci+", f:"+f);
				
				mSpeed += f/mWeight;
				if( mSpeed < 0.f ) mSpeed = 0.f;
				if( mSpeed > 1.5f*NDYWorld.current.getWindSpeed() ) mSpeed = 1.5f*NDYWorld.current.getWindSpeed();
				
				v_speed = new Vector3(v_boat_dir).scale(mSpeed);
				
				//NDYWorld.current.getUI().addGraphPoint("boat speed", mSpeed);
				
				Vector3 pos = trans.getPos();
			//	pos.add(v_speed);
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
	
	public int getWindOrientation() {
		return mWindOrientation;
	}
}
