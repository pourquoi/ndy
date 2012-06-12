package ndy.game.actor;

import java.util.ArrayList;
import java.util.Iterator;

import ndy.game.message.NDYMessage;


public class NDYTerrain extends NDYActor {
	private ArrayList<NDYTransformable> mPatches = new ArrayList<NDYTransformable>();
	
	@Override
	public boolean dispatchMessage(NDYMessage msg) {
		Iterator<NDYTransformable> i = mPatches.iterator();
		while(i.hasNext()) {
			NDYTransformable t = i.next();
			if( t.dispatchMessage(msg) ) {
				return true;
			}
		}
		return false;
	}
}
