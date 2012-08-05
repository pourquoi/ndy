package ndy.game.actor;

import java.util.Hashtable;

import ndy.game.NDYGame;
import ndy.game.component.NDYComponent;
import ndy.game.component.NDYComponentCollider;
import ndy.game.component.NDYComponentPhysics;
import ndy.game.message.NDYMessage;

public class NDYActor {
	private static final String TAG = "NDYActor";

	protected Hashtable<String, NDYComponent> components = new Hashtable<String, NDYComponent>();
	protected String name;

	public NDYActor(String name) {
		this.name = name;
	}

	public boolean dispatchMessage(NDYMessage msg) {
		if (components != null) {
			for (NDYComponent component : components.values()) {
				if (component.processMessage(msg)) {
					return true;
				}
			}
		}
		return false;
	}

	public void addComponent(NDYComponent component) {
		component.setParent(this);
		String name = component.getName();
		components.put(name, component);
		if( name == "collider" && NDYGame.instance.hasActor(this) ) {
			NDYGame.instance.colliders.add((NDYComponentCollider)component);
		}
	}

	public void removeComponent(String name) {
		if( name == "collider" ) {
			NDYComponentPhysics c = (NDYComponentPhysics)components.get(name);
			if( c != null && NDYGame.instance.hasActor(this) ) {
				NDYGame.instance.colliders.remove(c);
			}
		}
		components.remove(name);
	}

	public NDYComponent findComponent(String name) {
		return components.get(name);
	}

	public String getName() {
		return name;
	}
}
