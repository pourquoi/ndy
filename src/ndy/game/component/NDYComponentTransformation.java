package ndy.game.component;

import ndy.game.math.NDYQuaternion;
import ndy.game.math.NDYVector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;

public class NDYComponentTransformation extends NDYComponent {
	public NDYVector3 pos = new NDYVector3();
	public NDYVector3 rot = new NDYVector3();
	public NDYVector3 scale = new NDYVector3(1, 1, 1);

	public NDYQuaternion rotQ = new NDYQuaternion();

	public NDYComponentTransformation() {
		super("transformation");
	}

	public boolean processMessage(NDYMessage msg) {
		if (msg.getClass() == NDYMessageUpdate.class) {
			rotQ.fromEuler(rot.y, rot.z, rot.x);
		}

		return false;
	}

	public void setPos(float x, float y, float z) {
		pos.x = x;
		pos.y = y;
		pos.z = z;
	}

	public void setRot(float x, float y, float z) {
		rot.x = x;
		rot.y = y;
		rot.z = z;
	}

	public void setScale(float x, float y, float z) {
		scale.x = x;
		scale.y = y;
		scale.z = z;
	}
}
