package ndy.game.component;

import ndy.game.actor.GameWorld;
import ndy.game.actor.Player;
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
				CameraComponent c = ((GameWorld)parent).camera;
				if (c != null)
					c.pos.y += m.dy;
				if (c.pos.y < -30.f)
					c.pos.y = -30.f;
				if (c.pos.y > 30.f)
					c.pos.y = 30.f;
				
				Player p = ((GameWorld)parent).player;
				TransformationComponent T = (TransformationComponent)p.findComponent("transformation");
				
				T.rot.y += m.dx / 30;
			}
		}
		
		return false;
	}
}
