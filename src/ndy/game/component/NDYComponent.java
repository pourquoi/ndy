package ndy.game.component;

import ndy.game.actor.NDYActor;
import ndy.game.message.NDYMessage;


public class NDYComponent {
	protected String mName;
	protected NDYActor mParent;
	
	public NDYComponent(String name) {
		mName = name;
	}
	
	public boolean processMessage(NDYMessage msg) {
		return false;
	}
	
	public NDYActor getParent() {
		return mParent;
	}	
	
	public void setParent(NDYActor actor) {
		mParent = actor;
	}
	
	public String getName() {
		return mName;
	}
}
