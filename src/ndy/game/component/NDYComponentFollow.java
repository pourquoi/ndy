package ndy.game.component;

import android.util.FloatMath;
import ndy.game.actor.NDYActor;
import ndy.game.actor.NDYCamera;
import ndy.game.math.NDYMath;
import ndy.game.math.NDYVector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;


public class NDYComponentFollow extends NDYComponent {
	protected NDYActor mTarget;
	protected float mDistance;
	
	protected float mSpeedFactor = 0.1f;
	
	public NDYComponentFollow(NDYActor target, float distance) {
		super("follow");
		mTarget = target;
		mDistance = distance;
	}
	
	public boolean processMessage(NDYMessage msg) {
		if( msg.getClass() == NDYMessageUpdate.class ) {
			if( mParent.getClass() == NDYCamera.class ) {
				NDYCamera c = (NDYCamera)mParent;
				NDYComponentTransformation transformation = (NDYComponentTransformation)mTarget.findComponent("transformation");
				if( transformation != null ) {
					NDYVector3 cpos = c.mPos;
					NDYVector3 dir = new NDYVector3(FloatMath.cos(transformation.rot.y*NDYMath.TO_RADIANS),0.f,FloatMath.sin(transformation.rot.y*NDYMath.TO_RADIANS));
					NDYVector3 pos = new NDYVector3(transformation.pos).substract(dir.scale(mDistance));
					float r = NDYMath.abs((pos.distance(cpos)-mDistance)/mDistance);
					float rr = r*r*mSpeedFactor;
					float dx = NDYMath.max(mSpeedFactor, rr*NDYMath.abs(pos.x-cpos.x));
					float dz = NDYMath.max(mSpeedFactor, rr*NDYMath.abs(pos.z-cpos.z));
					
					cpos.x += NDYMath.sign(pos.x-cpos.x) * NDYMath.min(NDYMath.abs(pos.x-cpos.x), dx);
					cpos.z += NDYMath.sign(pos.z-cpos.z) * NDYMath.min(NDYMath.abs(pos.z-cpos.z), dz);
				}

				c.setTarget(transformation.pos.x, transformation.pos.y, transformation.pos.z);
			}
		}

		return false;
	}
	
	public NDYActor getTarget() {
		return mTarget;
	}
	
	public void setTarget(NDYActor target) {
		mTarget = target;
	}
	
	public void setDistance(float d) {
		mDistance = d;
	}
	
	public float getDistance() {
		return mDistance;
	}
}
