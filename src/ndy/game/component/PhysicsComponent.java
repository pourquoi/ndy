package ndy.game.component;

import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import ndy.game.system.PhysicsSystem;

import org.jbox2d.common.Transform;
import org.jbox2d.dynamics.Body;

public class PhysicsComponent extends Component {
	private static final String TAG = "NDYComponentPhysics";

	protected Body _body;

	public PhysicsComponent(Body body) {
		super("physics");
		systems.add(PhysicsSystem.name);
		_body = body;
	}

	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {
			UpdateMessage m = (UpdateMessage) msg;
			step(m.getInterval() / 1000f);

			TransformationComponent transformationComponent = (TransformationComponent) parent.findComponent("transformation");

			if (_body != null) {
				Transform t = _body.getTransform();
				transformationComponent.pos.x = t.position.x;
				transformationComponent.pos.z = t.position.y;

				transformationComponent.rot.y = t.getAngle();
			}
		}

		return false;
	}

	public void step(float dt) {

	}
}
