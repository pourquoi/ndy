package ndy.game.system;

import ndy.game.component.Component;
import ndy.game.message.Message;


public class CollisionSystem extends System {
	public static final String name = "collision";
	
	@Override
	public void dispatchMessage(Message m) {
		if(m.getType() == Message.T_UPDATE) {
			for(Component c:components) {
				c.processMessage(m);
			}
		}
	}
}
