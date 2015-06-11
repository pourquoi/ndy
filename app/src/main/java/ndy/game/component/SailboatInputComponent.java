package ndy.game.component;

import ndy.game.message.InputMessage;
import ndy.game.message.Message;
import ndy.game.system.InputSystem;

public class SailboatInputComponent extends Component {
	public SailboatInputComponent() {
		super("input");
		
		systems.add(InputSystem.name);
	}

	public boolean processMessage(Message msg) {
		if (msg.getType() == Message.T_INPUT) {
			InputMessage m = (InputMessage) msg;
			SailboatPhysicsComponent sailboatPhysics = (SailboatPhysicsComponent) parent.findComponent("physics");

			switch (m.action) {
			case InputMessage.RUDDER:
				if (sailboatPhysics != null) {
					sailboatPhysics.mRudderRot = sailboatPhysics.mRudderRot + m.dx;
				}
				break;
			case InputMessage.MAINSAIL:
				if (sailboatPhysics != null) {
					sailboatPhysics.mMainSailRot = sailboatPhysics.mMainSailRot + m.dx;
				}
				break;
			case InputMessage.JIBSAIL:
				if (sailboatPhysics != null) {
					sailboatPhysics.mJibSailRot = sailboatPhysics.mJibSailRot + m.dx;
				}
				break;
			}
		}
		return false;
	}
}
