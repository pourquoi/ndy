package ndy.game.system;

import ndy.game.component.Component;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class PhysicsSystem extends System {
	public static final String name = "physics";
	
	private World _world;
	
	public PhysicsSystem() {
		super();
	
		//_world = new World(new Vec2(0f,0f),true);
	}

	@Override
	public void dispatchMessage(Message m) {
		if(m.getType() == Message.T_UPDATE) {
			UpdateMessage msg = (UpdateMessage)m;
			for(Component c:components) {
				c.processMessage(m);
			}
			
			//_world.step(msg.getInterval(), 10, 8);
		}
	}
}
