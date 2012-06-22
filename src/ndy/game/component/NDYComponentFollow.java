package ndy.game.component;

import ndy.game.actor.NDYCamera;
import ndy.game.actor.NDYTransformable;
import ndy.game.math.NDYMath;
import ndy.game.math.Vector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;


public class NDYComponentFollow extends NDYComponent {
	protected NDYTransformable mTarget;
	protected float mDistance;
	
	protected float mSpeedFactor = 0.8f;
	
	public NDYComponentFollow(NDYTransformable target, float distance) {
		super("follow");
		mTarget = target;
		mDistance = distance;
	}
	
	public boolean processMessage(NDYMessage msg) {
		if( msg.getClass() == NDYMessageUpdate.class ) {
			if( mParent.getClass() == NDYCamera.class ) {
				NDYCamera c = (NDYCamera)mParent;
				NDYComponentCinematic cinematic = (NDYComponentCinematic)mTarget.findComponent("cinematic");
				if( cinematic != null ) {
					Vector3 cpos = c.getPos();
					Vector3 dir = new Vector3(cinematic.getDir());
					Vector3 pos = new Vector3(mTarget.getPos()).substract(dir.scale(mDistance));
					float r = NDYMath.abs((pos.distance(cpos)-mDistance)/mDistance);
					float rr = r*r*mSpeedFactor;
					float dx = NDYMath.max(1.f, rr*NDYMath.abs(pos.x-cpos.x));
					float dz = NDYMath.max(1.f, rr*NDYMath.abs(pos.z-cpos.z));
					
					cpos.x += NDYMath.sign(pos.x-cpos.x) * NDYMath.min(NDYMath.abs(pos.x-cpos.x), dx);
					cpos.z += NDYMath.sign(pos.z-cpos.z) * NDYMath.min(NDYMath.abs(pos.z-cpos.z), dz);
				}

				c.setTarget(mTarget.getPos().x, mTarget.getPos().y, mTarget.getPos().z);
			}
		}

		return false;
	}
	
	public NDYTransformable getTarget() {
		return mTarget;
	}
	
	public void setTarget(NDYTransformable target) {
		mTarget = target;
	}
	
	public void setDistance(float d) {
		mDistance = d;
	}
	
	public float getDistance() {
		return mDistance;
	}
}
