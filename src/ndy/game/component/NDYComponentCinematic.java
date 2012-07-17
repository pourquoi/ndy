package ndy.game.component;

import ndy.game.actor.NDYActor;
import ndy.game.math.NDYVector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;

public class NDYComponentCinematic extends NDYComponent {
	public NDYVector3 mDir;
	private NDYVector3 mXDir = new NDYVector3(1f, 0f, 0f);

	public NDYComponentCinematic() {
		super("cinematic");

		mDir = new NDYVector3(1f, 0f, 0f);
	}

	public boolean processMessage(NDYMessage msg) {
		if (msg.getClass() == NDYMessageUpdate.class) {
			NDYActor r = mParent;
			NDYComponentTransformation trans = (NDYComponentTransformation) r
					.findComponent("transformation");
			if (trans != null) {
				mXDir.x = 1f;
				mXDir.y = 0f;
				mXDir.z = 0f;
				mDir = trans.rotQ.rotateVector(mXDir);
			}
		}

		return false;
	}
}
