package ndy.game.component;

import java.util.ArrayList;

import ndy.game.actor.Actor;
import ndy.game.message.Message;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class Component {
	public String name;
	public Actor parent;
	public ArrayList<String> systems = new ArrayList<String>();
	
	public static Component load(Element element) {
		String componentClass = element.getAttribute("class");
		Component component = null;
		if(!componentClass.equals("")) {
			try {
				Class c = Class.forName(componentClass);
				component = (Component)c.getMethod("load", Element.class).invoke(null, element);
			} catch(Exception e) {
				throw new RuntimeException("Error parsing component DOM:" + e.getMessage());
			}
			component.parseDOM(element);
		}
		return component;
	}
	
	public Component(String name) {
		this.name = name;
	}
	
	public void parseDOM(Element element) {
		
	}
	
	public boolean processMessage(Message msg) {
		return false;
	}
	
	public Actor getParent() {
		return parent;
	}
	
	public void setParent(Actor actor) {
		parent = actor;
	}
	
	public String getName() {
		return name;
	}
}
