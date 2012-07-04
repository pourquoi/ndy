package ndy.game;

import java.util.Hashtable;

import ndy.game.component.NDYComponentGraph;
import ndy.game.component.NDYComponentSprite;
import ndy.game.component.NDYComponentTransformation;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageRender;
import ndy.game.message.NDYMessageUpdate;

public class NDYUI extends NDYActor {
	public static int FOCUS_NONE = 0;
	public static int FOCUS_MAINSAIL = 1;
	public static int FOCUS_RUDDER = 2;

	private int mFocus = FOCUS_NONE;
	
	private Hashtable<String, NDYComponentGraph> mGraphs = new Hashtable<String, NDYComponentGraph>();
		
	public NDYUI() {
		NDYActor t = new NDYActor();
		NDYComponentTransformation trans = new NDYComponentTransformation();
		trans.getScale().x = 100;
		trans.getScale().y = 100;
		t.addComponent(trans);
		NDYComponentSprite s = new NDYComponentSprite("textures/cube.png", "shaders/sprite");
		t.addComponent(s);
		//NDYWorld.current.addActor(t);
		
		//addGraph("default", 0.f, 0.f, 0.f);
	}
	
	@Override
	public boolean dispatchMessage(NDYMessage msg) {
		if( super.dispatchMessage(msg) ) return true;
		
		if( msg.getClass() == NDYMessageUpdate.class ) {
			
		}
		
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
	
	public void addGraph(String name, float r, float g, float b) {
		NDYComponentGraph graph = mGraphs.get(name);
		if(graph == null) {
			NDYActor t = new NDYActor();
			NDYComponentTransformation trans = new NDYComponentTransformation();
			t.addComponent(trans);
			graph = new NDYComponentGraph(300,100,"shaders/basic_colored");
			graph.setColor(r, g, b);
			t.addComponent(graph);
			NDYWorld.current.addActor(t);
			mGraphs.put(name, graph);
		}
	}
	
	public void addGraphPoint(String k, float v) {
		NDYComponentGraph g = mGraphs.get(k);
		if(g != null) {
			g.setNextVal(v);
		}
	}
}
