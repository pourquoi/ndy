package ndy.game;

import ndy.game.actor.NDYActor;
import ndy.game.actor.NDYTransformable;
import ndy.game.component.NDYComponentSprite;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageRender;

public class NDYUI extends NDYActor {
	public static int FOCUS_NONE = 0;
	public static int FOCUS_MAINSAIL = 1;
	public static int FOCUS_RUDDER = 2;

	private int mFocus = FOCUS_NONE;
	
	public NDYUI() {
		NDYTransformable t = new NDYTransformable();
		t.getScale().x = 100;
		t.getScale().y = 100;
		NDYComponentSprite s = new NDYComponentSprite("textures/cube.png", "shaders/sprite");
		t.addComponent(s);
		NDYWorld.current.addActor(t);
	}
	
	@Override
	public boolean dispatchMessage(NDYMessage msg) {
		if( super.dispatchMessage(msg) ) return true;
		
		if( msg.getClass() == NDYMessageRender.class ) {
			
		}

		return false;
	}
	
	public void setFocus(int focus) {
		mFocus = focus;
	}
	
	public int getFocus() {
		return mFocus;
	}
}
