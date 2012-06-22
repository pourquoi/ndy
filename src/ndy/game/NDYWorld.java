package ndy.game;

import java.util.ArrayList;

import ndy.game.actor.NDYActor;
import ndy.game.actor.NDYCamera;
import ndy.game.actor.NDYTransformable;
import ndy.game.component.NDYComponentCinematicSailboat;
import ndy.game.component.NDYComponentFollow;
import ndy.game.component.NDYComponentMesh;
import ndy.game.component.NDYComponentMeshSailboat;
import ndy.game.math.NDYMath;
import ndy.game.math.Vector3;
import ndy.game.mesh.NDYMesh;
import ndy.game.message.NDYMessageRender;
import ndy.game.message.NDYMessageUpdate;
import android.content.Context;


public class NDYWorld {
	private NDYUI mUI;
	private ArrayList<NDYActor> mActors;
	private NDYCamera mCamera;
	private NDYCamera mCameraPerspective;
	private NDYCamera mCameraOrtho;
	private NDYTransformable mRacer;
	private Context mContext;
	private long mTime = 0;
	protected float [] mLightDir = new float[3];
	protected float mWindRot = 45.f;
	protected float mWindSpeed = 5.f; // m/s
	
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
		// main directional light
		mLightDir[0] = 0.1f;
		mLightDir[1] = -0.5f;
		mLightDir[2] = 0.5f;
		
		mActors = new ArrayList<NDYActor>();
		
		// create the ui
		mUI = new NDYUI();
		mActors.add(mUI);
		
		// create the camera
		mCameraPerspective = new NDYCamera();
		mCameraPerspective.setTarget(0f, 0f, 10.f);
		mCameraPerspective.setPos(0f, 10f, -10f);
		mActors.add(mCameraPerspective);
		mCamera = mCameraPerspective;
		
		mCameraOrtho = new NDYCamera();
		mCameraOrtho.setMode(NDYCamera.MODE_ORTHO);
		mActors.add(mCameraOrtho);
		
		// create the racer
		mRacer = new NDYTransformable();
		mRacer.setPos(0f, 0f, 0.f);
		// attach a mesh component
		NDYComponentMesh meshComponent = new NDYComponentMeshSailboat("models/ship.3ds", "shaders/basic", null);		
		mRacer.addComponent(meshComponent);
		// attach cinematic component
		NDYComponentCinematicSailboat cineComponent = new NDYComponentCinematicSailboat();
		mRacer.addComponent(cineComponent);
		mActors.add(mRacer);
		
		NDYComponentFollow followComponent = new NDYComponentFollow(mRacer, 120f);
		mCamera.addComponent(followComponent);
		/*
		NDYTransformable plan = new NDYTransformable();
		meshComponent = new NDYComponentMesh("models/plan.3ds", "shaders/basic_textured", "textures/sand_2.jpg");
		plan.addComponent(meshComponent);
		mActors.add(plan);
		*/

		NDYTransformable axis3D = new NDYTransformable();
		axis3D.setScale(1000,1000,1000);
		NDYMesh mesh = NDYMesh.axis3d();
		meshComponent = new NDYComponentMesh(mesh.toString(),"shaders/basic_colored",null);
		axis3D.addComponent(meshComponent);
		mActors.add(axis3D);
		
		NDYTransformable arrow = new NDYTransformable();
		meshComponent = new NDYComponentMesh("models/arrow.3ds", "shaders/basic", null);
		arrow.addComponent(meshComponent);
		mActors.add(arrow);
		arrow.setRot(0, mWindRot, 0);
		
		NDYTransformable terrain = new NDYTransformable();
		mesh = NDYMesh.plan(4, 4);
		meshComponent = new NDYComponentMesh(mesh.toString(),"shaders/basic_textured","textures/sand_2.jpg");
		terrain.addComponent(meshComponent);
		terrain.setScale(100, 0, 100);
		mActors.add(terrain);
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
	
	public void addActor(NDYActor a) {
		mActors.add(a);
	}
	
	public NDYCamera getCamera() {
		return mCamera;
	}
	
	public void setCamera(NDYCamera camera) {
		mCamera = camera;
	}
	
	public NDYCamera getCameraPerspective() {
		return mCameraPerspective;
	}
	
	public NDYCamera getCameraOrtho() {
		return mCameraOrtho;
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
	
	public float getWindRot() {
		return mWindRot;
	}
	
	public float getWindSpeed() {
		return mWindSpeed;
	}
	
	public NDYUI getUI() {
		return mUI;
	}
}
