package ndy.game;

import java.util.ArrayList;

import ndy.game.actor.NDYActor;
import ndy.game.actor.NDYCamera;
import ndy.game.actor.NDYTransformable;
import ndy.game.component.NDYComponentCinematicSailboat;
import ndy.game.component.NDYComponentFollow;
import ndy.game.component.NDYComponentMesh;
import ndy.game.component.NDYComponentMeshSailboat;
import ndy.game.math.Vector3;
import ndy.game.message.NDYMessageRender;
import ndy.game.message.NDYMessageUpdate;

import android.content.Context;


public class NDYWorld {
	private ArrayList<NDYActor> mActors;
	private NDYCamera mCamera;
	private NDYTransformable mRacer;
	private Context mContext;
	protected float [] mLightDir = new float[3];
	protected Vector3 mWindDir = new Vector3();
	private long mTime = 0;
	
	public static NDYWorld current;
			
	public NDYWorld() {
	}
	
	public static void load(Context context) {
		NDYWorld world = new NDYWorld();
		world.setContext(context);
		NDYWorld.current = world;
		world.init();
	}
	
	public void init() {
		mWindDir.x = 1;
		mWindDir.z = 1;
		mWindDir.normalize();
		
		// main directional light
		mLightDir[0] = 0.1f;
		mLightDir[1] = -0.5f;
		mLightDir[2] = 0.5f;
		
		mActors = new ArrayList<NDYActor>();
		
		// create the camera
		mCamera = new NDYCamera();
		mCamera.setTarget(0f, 0f, 10.f);
		mCamera.setPos(0f, 10f, -10f);
		mActors.add(mCamera);
		
		// create the racer
		mRacer = new NDYTransformable();
		mRacer.setPos(0f, 0f, 10.f);
		// attach a mesh component
		NDYComponentMesh meshComponent = new NDYComponentMeshSailboat("models/ship.3ds", "shaders/basic", null);		
		mRacer.addComponent(meshComponent);
		// attach cinematic component
		NDYComponentCinematicSailboat cineComponent = new NDYComponentCinematicSailboat();
		mRacer.addComponent(cineComponent);
		mActors.add(mRacer);
		
		NDYComponentFollow followComponent = new NDYComponentFollow(mRacer, 120f);
		mCamera.addComponent(followComponent);
		
		NDYTransformable plan = new NDYTransformable();
		meshComponent = new NDYComponentMesh("models/plan.3ds", "shaders/basic_textured", "textures/cube.png");
		plan.addComponent(meshComponent);
		mActors.add(plan);
		
		NDYTransformable arrow = new NDYTransformable();
		meshComponent = new NDYComponentMesh("models/arrow.3ds", "shaders/basic", null);
		arrow.addComponent(meshComponent);
		mActors.add(arrow);
		arrow.setRot(mWindDir.x, mWindDir.y, mWindDir.z);
	}
	
	public void update(long dt) {
		mTime += dt;
		NDYMessageUpdate msg = new NDYMessageUpdate();
		msg.setInterval(dt);
		for(int i=0, n=mActors.size(); i<n; i++) {
			mActors.get(i).dispatchMessage(msg);
		}
	}
	
	public void render() {
		NDYMessageRender msg = new NDYMessageRender();
		for(int i=0, n=mActors.size(); i<n; i++) {
			mActors.get(i).dispatchMessage(msg);
		}
	}
	
	public NDYCamera getCamera() {
		return mCamera;
	}
	
	public void setCamera(NDYCamera camera) {
		mCamera = camera;
	}
	
	public NDYTransformable getRacer() {
		return mRacer;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	/**
	 * Time elapsed in ms not including pauses.
	 * @return long
	 */
	public long getTime() {
		return mTime;
	}
	
	public float [] getLightDir() {
		return mLightDir;
	}
	
	public Vector3 getWindDir() {
		return mWindDir;
	}
}
