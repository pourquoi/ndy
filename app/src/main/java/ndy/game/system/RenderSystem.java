package ndy.game.system;

import ndy.game.component.Component;
import ndy.game.message.Message;


public class RenderSystem extends System {
	public static final String name = "render";
	
	@Override
	public void dispatchMessage(Message m) {
		if(m.getType() == Message.T_RENDER) {
			for(Component c:components) {
				c.processMessage(m);
			}
		}
	}
}
