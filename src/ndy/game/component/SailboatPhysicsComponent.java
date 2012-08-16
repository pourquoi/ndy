package ndy.game.component;

import ndy.game.Game;
import ndy.game.actor.Actor;
import ndy.game.material.WaterOptions;
import ndy.game.math.NDYMath;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

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
	
	private Vec2 boatDir = new Vec2();
	private Vec2 windDir = new Vec2();
	
	public SailboatPhysicsComponent(Body body) {
		super(body);
	}
	
	@Override
	public void step(float dt) {
		Actor boat = parent;
		TransformationComponent trans = (TransformationComponent)boat.findComponent("transformation");
		SailboatMeshComponent mesh = (SailboatMeshComponent)boat.findComponent("mesh");
		
		if( trans != null && mesh != null ) {
			float a_wind = Game.instance.world.weather.mWindRot;
			float a_boat = trans.rot.y;
			float a_boat_wind = NDYMath.clampDegrees(a_boat - a_wind);
			
			boatDir.x = MathUtils.cos(a_boat);
			boatDir.y = MathUtils.sin(a_boat);
			
			windDir.x = MathUtils.cos(a_wind);
			windDir.y = MathUtils.sin(a_wind);
			
			float w_speed = Game.instance.world.weather.mWindSpeed;
			windDir.mul(w_speed);
			
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

			float s = _body.getLinearVelocity().length();
			float vv = windDir.lengthSquared();
			float ss = s*s;

			float koMainSail = NDYMath.abs(FloatMath.sin(a_mainsail_wind*NDYMath.TO_RADIANS));
			if( koMainSail < koSailMin ) koMainSail = 0.f;
			
			float koJibSail = NDYMath.abs(FloatMath.sin(a_jibsail_wind*NDYMath.TO_RADIANS));
			if( koJibSail < koSailMin ) koJibSail = 0.f;

			trans.rot.x *= kfRoll * koMainSail;
			
			if( Game.instance.mWater != null ) {
				WaterOptions w = Game.instance.mWater.water;
				float t = Game.instance.time/1000.f;
				trans.rot.z = kfPitch*w.amplitude[0]*MathUtils.sin(w.wconst[0]*t);
				trans.pos.y = MathUtils.sin(w.wconst[0]*t)*w.amplitude[0];
			}

			float FS = kfMainSail * koMainSail * vv;
			
			_body.applyForce(boatDir.mul(FS), _body.getWorldCenter());

			if(mRudderRot < -80.f) mRudderRot = -80.f;
			if(mRudderRot > 80.f) mRudderRot = 80.f;

			float rotspeed = FloatMath.sin(mRudderRot)*MathUtils.max(5.f, s)*kfRotSpeed;
			
			float rotspeedmax = MathUtils.HALF_PI;
			rotspeed = MathUtils.clamp(rotspeed, -rotspeedmax, rotspeedmax);
			
			_body.applyAngularImpulse(rotspeed);
		}
	}
}
