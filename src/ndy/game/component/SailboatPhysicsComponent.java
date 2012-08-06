package ndy.game.component;

import ndy.game.Game;
import ndy.game.actor.Actor;
import ndy.game.material.WaterOptions;
import ndy.game.math.NDYMath;
import ndy.game.math.Vector3;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import android.util.FloatMath;
import android.util.Log;

public class SailboatPhysicsComponent extends PhysicsComponent {
	public final static String TAG = "NDYComponentCinematicSailboat";
	
	public static int HEADWIND_STARBOARD = 1; // +x -y
	public static int HEADWIND_PORTSIDE = 2; // -x -y
	public static int BACKWIND_STARBOARD = 3; // +x +y
	public static int BACKWIND_PORTSIDE = 4; // -x +y

	public int mWindOrientation;

	public float mMainSailRot = 0.f; // main sail rotation degrees
	public float mJibSailRot = 0.f; // jib sail rotation degrees
	public float mRudderRot = 0.f; // rudder rotation degrees (-90 to 90)
	
	public float kfWater = 0.5f;
	public float kfRudder = 0.05f;
	public float kfMainSail = 1.9f;
	public float kfJibSail = 1.f;
	
	public float koSailMin = 0.1f;
	
	public float MI = 1.f; // moment of inertia kg.m^2
	public float kfRotSpeed = 6.f;
	public float rotSpeedMax = 90.f; // maximum rotation speed degrees.s^-1
	public float rotSpeedMin = 1f;
	
	public float kfRoll = 10f;
	public float kfPitch = 0.1f;
	
	public SailboatPhysicsComponent() {
		super();
	}

	public boolean processMessage(Message msg) {
		super.processMessage(msg);
		
		if( msg.getClass() == UpdateMessage.class ) {
			float dt = ((UpdateMessage)msg).getInterval() / 1000.f;
			Actor boat = parent;
			TransformationComponent trans = (TransformationComponent)boat.findComponent("transformation");
			SailboatMeshComponent mesh = (SailboatMeshComponent)boat.findComponent("mesh");
			
			if( trans != null && mesh != null ) {
				float a_wind = Game.instance.mWeather.mWindRot;
				float a_boat = trans.rot.y;
				float a_boat_wind = NDYMath.clampDegrees(a_boat - a_wind);
				Vector3 v_boat_dir = new Vector3(FloatMath.cos(a_boat*NDYMath.TO_RADIANS),0.f,FloatMath.sin(a_boat*NDYMath.TO_RADIANS));
				Vector3 v_wind = new Vector3(FloatMath.cos(a_wind*NDYMath.TO_RADIANS),0.f,FloatMath.sin(a_wind*NDYMath.TO_RADIANS));
				float w_speed = Game.instance.mWeather.mWindSpeed;
				v_wind.scale(w_speed);
				Vector3 v_speed = new Vector3(v_boat_dir);
				v_speed.scale(speed);
				Vector3 v_awind = new Vector3(v_wind);//.substract(v_speed);
				
				float a=0, b=0;
				
				if( a_boat_wind >= 0.f && a_boat_wind < 90.f ) {
					if( mWindOrientation != BACKWIND_STARBOARD ) {
						mWindOrientation = BACKWIND_STARBOARD;
						Log.i(TAG, "wind orientation: backwind starboard");
					}
					a = -90f;
					b = 90f;
					trans.rot.x = -1f;
				} else if( a_boat_wind >= 90.f && a_boat_wind < 180.f ) {
					if( mWindOrientation != HEADWIND_STARBOARD ) {
						mWindOrientation = HEADWIND_STARBOARD;
						Log.i(TAG, "wind orientation: headwind starboard");
					}
					a = a_boat_wind - 180;
					b = 0f;
					trans.rot.x = -1f;
				} else if( a_boat_wind >= 180.f && a_boat_wind < 270.f ) {
					if( mWindOrientation != HEADWIND_PORTSIDE ) {
						mWindOrientation = HEADWIND_PORTSIDE;
						Log.i(TAG, "wind orientation: headwind portside");
					}					
					a = a_boat_wind - 180;
					b = 0f;
					trans.rot.x = 1f;
				} else if( a_boat_wind >= 270.f && a_boat_wind < 360.f ) {
					if( mWindOrientation != BACKWIND_PORTSIDE ) {
						mWindOrientation = BACKWIND_PORTSIDE;
						Log.i(TAG, "wind orientation: backwind portside");
					}
					a = -90f;
					b = 90f;
					trans.rot.x = 1f;
				}

				mMainSailRot = NDYMath.clamp(mMainSailRot, NDYMath.min(a, b), NDYMath.max(a, b));
				mJibSailRot = NDYMath.clamp(mJibSailRot, NDYMath.min(a, b), NDYMath.max(a, b));
				
				float a_mainsail_wind = mMainSailRot + a_boat - a_wind;
				float a_jibsail_wind = mJibSailRot + a_boat - a_wind;

				float vv = v_awind.squaredLength();
				float ss = speed * speed;

				float koMainSail = NDYMath.abs(FloatMath.sin(a_mainsail_wind*NDYMath.TO_RADIANS));
				if( koMainSail < koSailMin ) koMainSail = 0.f;
				
				float koJibSail = NDYMath.abs(FloatMath.sin(a_jibsail_wind*NDYMath.TO_RADIANS));
				if( koJibSail < koSailMin ) koJibSail = 0.f;
								
				trans.rot.x *= kfRoll * koMainSail;
				
				if( Game.instance.mWater != null ) {
					WaterOptions w = Game.instance.mWater.water;
					float t = Game.instance.mTime/1000.f;
					trans.rot.z = kfPitch*w.amplitude[0]*FloatMath.sin(w.wconst[0]*t);
					trans.pos.y = FloatMath.sin(w.wconst[0]*t)*w.amplitude[0];
				}

				float R = (kfWater + FloatMath.sin(mRudderRot*NDYMath.TO_RADIANS)*kfRudder)*ss;
				float FS = kfMainSail * koMainSail * vv;

				float F = FS - R;

				speed += dt * F/mass;
				if( speed < 0f ) speed = 0f;

				if(mRudderRot < -80.f) mRudderRot = -80.f;
				if(mRudderRot > 80.f) mRudderRot = 80.f;

				rotspeed = FloatMath.sin(mRudderRot*NDYMath.TO_RADIANS)*NDYMath.max(5.f, speed)*kfRotSpeed;

				float rotspeedmax = 90.f;
				rotspeed = NDYMath.clamp(rotspeed, -rotspeedmax, rotspeedmax);
			
				trans.rot.y += rotspeed * dt;
				
				trans.pos.x += v_boat_dir.x*speed*dt;
				trans.pos.z += v_boat_dir.z*speed*dt;
				
				//Log.d(TAG, "mMainSailRot "+mMainSailRot+" a_mainsail_wind "+a_mainsail_wind+" vv "+vv+" ss "+ss+" F "+F+" FS " + FS + " R " + R );
			}
		}
		
		return false;
	}
}
