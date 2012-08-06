package ndy.game.actor;

import java.util.Hashtable;

import ndy.game.Game;
import ndy.game.component.Component;
import ndy.game.component.ComponentCollider;
import ndy.game.component.PhysicsComponent;
import ndy.game.message.Message;
import ndy.game.system.System;

public class Actor {
	private static final String TAG = "NDYActor";

	protected Hashtable<String, Component> components = new Hashtable<String, Component>();
	public String name;
	public boolean inGame = false;

	public Actor(String name) {
		this.name = name;
	}

	public boolean dispatchMessage(Message msg) {
		if (components != null) {
			for (Component component : components.values()) {
				if (component.processMessage(msg)) {
					return true;
				}
			}
		}
		return false;
	}

	public void addComponent(Component c) {
		c.parent = this;
		components.put(c.name, c);
		if(c.systems != null) {
			for(String system:c.systems) {
				if(Game.instance.systems.containsKey(system)) {
					Game.instance.systems.get(system).registerComponent(c);
				}
			}
		}
	}
	
	public void removeComponent(Component c) {
		if(c.systems != null) {
			for(String system:c.systems) {
				if(Game.instance.systems.containsKey(system)) {
					Game.instance.systems.get(system).unregisterComponent(c);
				}
			}
		}
	}

	public Component findComponent(String name) {
		return components.get(name);
	}

	public String getName() {
		return name;
	}
}
