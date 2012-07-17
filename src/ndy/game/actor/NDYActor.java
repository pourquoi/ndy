package ndy.game.actor;

import java.util.Hashtable;

import ndy.game.component.NDYComponent;
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
		mComponents.put(component.getName(), component);
	}

	public void removeComponent(String name) {
		mComponents.remove(name);
	}

	public NDYComponent findComponent(String name) {
		return mComponents.get(name);
	}

	public String getName() {
		return mName;
	}
}
