package ndy.game.component;

import ndy.game.actor.Camera;
import ndy.game.actor.GameWorld;
import ndy.game.message.InputMessage;
import ndy.game.message.Message;
import ndy.game.system.InputSystem;

public class WorldInputComponent extends Component {
	public WorldInputComponent() {
		super("input");
		systems.add(InputSystem.name);
	}
	
	public boolean processMessage(Message msg) {
		if (msg.getType() == Message.T_INPUT) {
			InputMessage m = (InputMessage) msg;
			if(m.action == InputMessage.CAMERA) {
				Camera c = ((GameWorld)parent).camera;
				if (c != null)
					c.pos.y += m.dy;
				if (c.pos.y < 5.f)
					c.pos.y = 5.f;
				if (c.pos.y > 30.f)
					c.pos.y = 30.f;
			}
		}
		
		return false;
	}
}
