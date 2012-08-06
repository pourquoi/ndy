package ndy.game.component;

import ndy.game.math.Quaternion;
import ndy.game.math.Vector3;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import ndy.game.system.WorldSystem;

public class TransformationComponent extends Component {
	public Vector3 pos = new Vector3();
	public Vector3 rot = new Vector3();
	public Vector3 scale = new Vector3(1, 1, 1);

	public Quaternion rotQ = new Quaternion();

	public TransformationComponent() {
		super("transformation");
		
		systems.add(WorldSystem.name);
	}

	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {
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
