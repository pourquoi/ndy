package ndy.game.actor;

import java.util.Hashtable;

import ndy.game.component.NDYComponent;
import ndy.game.component.NDYComponentCollider;
import ndy.game.component.NDYComponentPhysics;
import ndy.game.message.NDYMessage;

public class NDYActor {
	private static String TAG = "NDYActor";

	protected Hashtable<String, NDYComponent> mComponents = new Hashtable<String, NDYComponent>();
	protected String mName;

	public NDYActor(String name) {
		mName = name;
	}

	public boolean dispatchMessage(NDYMessage msg) {
		if (mComponents != null) {
			for (NDYComponent component : mComponents.values()) {
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
		mComponents.put(name, component);
		if( name == "collider" && NDYGame.instance.hasActor(this) ) {
			NDYGame.instance.colliders.add((NDYComponentCollider)component);
		}
	}

	public void removeComponent(String name) {
		if( name == "collider" ) {
			NDYComponentPhysics c = (NDYComponentPhysics)mComponents.get(name);
			if( c != null && NDYGame.instance.hasActor(this) ) {
				NDYGame.instance.colliders.remove(c);
			}
		}
		mComponents.remove(name);
	}

	public NDYComponent findComponent(String name) {
		return mComponents.get(name);
	}

	public String getName() {
		return mName;
	}
}
