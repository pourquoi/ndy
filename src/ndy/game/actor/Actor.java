package ndy.game.actor;

import java.util.Hashtable;

import ndy.game.Game;
import ndy.game.component.Component;
import ndy.game.message.Message;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Actor {
	private static final String TAG = "NDYActor";

	public Hashtable<String, Component> components = new Hashtable<String, Component>();
	public String name;
	public boolean inGame = false;

	public static Actor load(Element element) {
		String componentClass = element.getAttribute("class");
		Actor actor = null;
		if (!componentClass.equals("")) {
			try {
				Class c = Class.forName(componentClass);
				actor = (Actor) c.getMethod("load", Element.class).invoke(null, element);
			} catch (Exception e) {
				throw new RuntimeException("Error parsing actor DOM:" + e.getMessage());
			}

			actor.parseDOM(element);
		}

		NodeList children = element.getElementsByTagName("component");
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			Component c = Component.load((Element) node);
			c.parseDOM((Element) node);
		}

		return null;
	}

	public Actor() {
	}

	public Actor(String name) {
		this.name = name;
	}

	public void parseDOM(Element element) {

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
		if (c.systems != null) {
			for (String system : c.systems) {
				if (Game.instance.systems.containsKey(system)) {
					Game.instance.systems.get(system).registerComponent(c);
				}
			}
		}
	}

	public void removeComponent(Component c) {
		if (c.systems != null) {
			for (String system : c.systems) {
				if (Game.instance.systems.containsKey(system)) {
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
