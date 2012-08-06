package ndy.game.component;

import ndy.game.Game;
import ndy.game.actor.Camera;
import ndy.game.math.NDYMath;
import ndy.game.math.Vector3;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import ndy.game.system.WorldSystem;
import android.util.FloatMath;

public class CameraFocusComponent extends Component {
	protected float mDistance;

	protected float mSpeedFactor = 0.1f;

	public CameraFocusComponent(float distance) {
		super("follow");
		mDistance = distance;
		systems.add(WorldSystem.name);
	}

	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {
			Camera c = (Camera) Game.instance.mCamera;
			TransformationComponent transformation = (TransformationComponent) parent.findComponent("transformation");
			if (transformation != null) {
				Vector3 cpos = c.mPos;
				Vector3 dir = new Vector3(FloatMath.cos(transformation.rot.y * NDYMath.TO_RADIANS), 0.f, FloatMath.sin(transformation.rot.y
						* NDYMath.TO_RADIANS));
				Vector3 pos = new Vector3(transformation.pos).substract(dir.scale(mDistance));
				float r = NDYMath.abs((pos.distance(cpos) - mDistance) / mDistance);
				float rr = r * r * mSpeedFactor;
				float dx = NDYMath.max(mSpeedFactor, rr * NDYMath.abs(pos.x - cpos.x));
				float dz = NDYMath.max(mSpeedFactor, rr * NDYMath.abs(pos.z - cpos.z));

				cpos.x += NDYMath.sign(pos.x - cpos.x) * NDYMath.min(NDYMath.abs(pos.x - cpos.x), dx);
				cpos.z += NDYMath.sign(pos.z - cpos.z) * NDYMath.min(NDYMath.abs(pos.z - cpos.z), dz);
			}

			c.setTarget(transformation.pos.x, transformation.pos.y, transformation.pos.z);
		}

		return false;
	}

	public void setDistance(float d) {
		mDistance = d;
	}

	public float getDistance() {
		return mDistance;
	}
}
