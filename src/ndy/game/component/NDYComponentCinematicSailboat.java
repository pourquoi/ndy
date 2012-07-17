package ndy.game.component;

import ndy.game.actor.NDYActor;
import ndy.game.actor.NDYGame;
import ndy.game.math.NDYMath;
import ndy.game.math.NDYVector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;
import android.util.FloatMath;
import android.util.Log;

public class NDYComponentCinematicSailboat extends NDYComponentCinematic {
	public static String TAG = "NDYComponentCinematicSailboat";
	
	public static int HEADWIND_STARBOARD = 1; // +x -y
	public static int HEADWIND_PORTSIDE = 2; // -x -y
	public static int BACKWIND_STARBOARD = 3; // +x +y
	public static int BACKWIND_PORTSIDE = 4; // -x +y

	public int mWindOrientation;

	public float mMainSailRot = 0.f; // main sail rotation in degrees
	public float mJibSailRot = 0.f; // jib sail rotation in degrees
	
	public float mSpeed = 0.f; // boat speed in m/s
	public float mWeight = 1.f; // in kg
	
	public NDYComponentCinematicSailboat() {
	}

	public boolean processMessage(NDYMessage msg) {
		super.processMessage(msg);
		
		if( msg.getClass() == NDYMessageUpdate.class ) {
			float dt = ((NDYMessageUpdate)msg).getInterval() / 1000.f;
			NDYActor boat = mParent;
			NDYComponentTransformation trans = (NDYComponentTransformation)boat.findComponent("transformation");
			NDYComponentMeshSailboat mesh = (NDYComponentMeshSailboat)boat.findComponent("mesh");
			
			if( trans != null && mesh != null ) {
				float a_wind = NDYGame.instance.mWeather.mWindRot;
				float a_boat = trans.rot.y;
				float a_boat_wind = NDYMath.clampDegrees(a_boat - a_wind);
				
				float a=0, b=0;
				
				if( a_boat_wind >= 0.f && a_boat_wind < 90.f ) {
					if( mWindOrientation != BACKWIND_STARBOARD ) {
						mWindOrientation = BACKWIND_STARBOARD;
						Log.i(TAG, "wind orientation: backwind starboard");
					}
					a = -90f;
					b = 90f;
				} else if( a_boat_wind >= 90.f && a_boat_wind < 180.f ) {
					if( mWindOrientation != HEADWIND_STARBOARD ) {
						mWindOrientation = HEADWIND_STARBOARD;
						Log.i(TAG, "wind orientation: headwind starboard");
					}
					a = a_boat_wind - 180;
					b = 0f;
				} else if( a_boat_wind >= 180.f && a_boat_wind < 270.f ) {
					if( mWindOrientation != HEADWIND_PORTSIDE ) {
						mWindOrientation = HEADWIND_PORTSIDE;
						Log.i(TAG, "wind orientation: headwind portside");
					}					
					a = a_boat_wind - 180;
					b = 0f;
				} else if( a_boat_wind >= 270.f && a_boat_wind < 360.f ) {
					if( mWindOrientation != BACKWIND_PORTSIDE ) {
						mWindOrientation = BACKWIND_PORTSIDE;
						Log.i(TAG, "wind orientation: backwind portside");
					}
					a = -90f;
					b = 90f;
				}
				
				mMainSailRot = NDYMath.clamp(mMainSailRot, NDYMath.min(a, b), NDYMath.max(a, b));
				
			
				float a_mainsail = mMainSailRot + a_boat;
				float w_speed = NDYGame.instance.mWeather.mWindSpeed;
				float a_mainsail_wind = NDYMath.clampDegrees(a_mainsail - a_wind);
				
				NDYVector3 v_boat_dir = new NDYVector3(FloatMath.cos(a_boat*NDYMath.TO_RADIANS),0.f,FloatMath.sin(a_boat*NDYMath.TO_RADIANS));
				NDYVector3 v_wind = new NDYVector3(FloatMath.cos(a_wind*NDYMath.TO_RADIANS),0.f,FloatMath.sin(a_wind*NDYMath.TO_RADIANS));
				v_wind.scale(w_speed);
				NDYVector3 v_speed = new NDYVector3(v_boat_dir);
				v_speed.scale(mSpeed);
				NDYVector3 v_awind = new NDYVector3(v_wind).substract(v_speed);

				float vv = v_awind.squaredLength();
				float f = 0.f;
				float r = 0.f;
				float ci = 0.f;
				float kp = 100.f;
				float kr = 0.1f;
				float rs = mWeight/100.f;
				
				ci = NDYMath.abs(FloatMath.sin(a_mainsail_wind*NDYMath.TO_RADIANS));
				
				if( ci < 0.4f ) ci = 0.f;
				
				r = kr * mSpeed * mSpeed + rs;
				float fw = kp * ci * vv;

				f = fw - r;
				
			//	Log.i(TAG, "boat speed:"+mSpeed+", ap. wind speed^2:"+vv+", r:"+r+", ci:"+ci+", f:"+f);
				
				mSpeed += dt * f/mWeight;
				if( mSpeed < 0.f ) mSpeed = 0.f;
				if( mSpeed > 1.5f*w_speed ) mSpeed = 1.5f*w_speed;
				
				v_speed = new NDYVector3(v_boat_dir).scale(mSpeed);
				
			//	NDYGame.instance.getInterface().addGraphPoint("wind", w_speed*w_speed);
			//	NDYGame.instance.getInterface().addGraphPoint("relative wind", vv);
				NDYVector3 pos = trans.pos;
			//	pos.x = NDYMath.clamp(pos.x+v_speed.x*dt, -5000.f, 5000.f);
			//	pos.z = NDYMath.clamp(pos.z+v_speed.z*dt, -5000.f, 5000.f);
			}
		}
		
		return false;
	}
}
