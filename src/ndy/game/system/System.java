package ndy.game.system;

import java.util.ArrayList;

import ndy.game.component.Component;
import ndy.game.message.Message;

public class System {	
	public ArrayList<Component> components = new ArrayList<Component>();

	public System() {
		
	}
	
	public void dispatchMessage(Message m) {
		
	}
	
	public void registerComponent(Component c) {
		components.add(c);
	}

	public void unregisterComponent(Component c) {
		if (components.contains(c))
			components.remove(c);
	}
}
