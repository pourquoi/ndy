package ndy.game.component;

import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import ndy.game.system.WorldSystem;

import org.jbox2d.common.Vec3;

public class TransformationComponent extends Component {
	public Vec3 pos = new Vec3();
	public Vec3 rot = new Vec3();
	public Vec3 scale = new Vec3(1f, 1f, 1f);

	public TransformationComponent() {
		super("transformation");

		systems.add(WorldSystem.name);
	}

	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {

		}

		return false;
	}
}
