package ndy.game.component;

import java.util.ArrayList;

import ndy.game.actor.Actor;
import ndy.game.message.Message;

import org.xmlpull.v1.XmlPullParser;


public class Component {
	public String name;
	public Actor parent;
	public ArrayList<String> systems = new ArrayList<String>();
	
	public Component(String name) {
		this.name = name;
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
	
	public void load(XmlPullParser xpp) {
		
	}
}
