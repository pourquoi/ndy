package ndy.game.component;

import ndy.game.actor.Camera;
import ndy.game.actor.World;
import ndy.game.message.InputMessage;
import ndy.game.message.Message;
import ndy.game.system.InputSystem;

public class CameraInputComponent extends Component {
	public CameraInputComponent() {
		super("input");
		systems.add(InputSystem.name);
	}
	
	public boolean processMessage(Message msg) {
		if (msg.getType() == Message.T_INPUT) {
			InputMessage m = (InputMessage) msg;
			if(m.action == InputMessage.CAMERA) {
				Camera c = ((World)parent).camera;
				if (c != null)
					c.mPos.y += m.dy;
				if (c.mPos.y < 5.f)
					c.mPos.y = 5.f;
				if (c.mPos.y > 30.f)
					c.mPos.y = 30.f;
			}
		}
		
		return false;
	}
}
