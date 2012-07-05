package ndy.game;

import java.util.ArrayList;

import ndy.game.component.NDYComponentCinematicSailboat;
import ndy.game.component.NDYComponentFollow;
import ndy.game.component.NDYComponentMesh;
import ndy.game.component.NDYComponentMeshSailboat;
import ndy.game.component.NDYComponentTransformation;
import ndy.game.component.NDYComponentWater;
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
	private NDYActor mRacer;
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
		mLightDir[1] = -0.6f;
		mLightDir[2] = 0.1f;
		
		mActors = new ArrayList<NDYActor>();
		
		// create the ui
		mUI = new NDYUI();
		mActors.add(mUI);
		
		// create the cameras
		mCameraPerspective = new NDYCamera();
		mCameraPerspective.setTarget(0f, 0f, 10.f);
		mCameraPerspective.setPos(0f, 10f, -10f);
		mActors.add(mCameraPerspective);
		mCamera = mCameraPerspective;
		
		mCameraOrtho = new NDYCamera();
		mCameraOrtho.setMode(NDYCamera.MODE_ORTHO);
		mActors.add(mCameraOrtho);
		
		// create the racer
		mRacer = new NDYActor();
		// attach transformation component
		NDYComponentTransformation transformationComponent = new NDYComponentTransformation();
		transformationComponent.setPos(0f, 0f, 0.f);
		mRacer.addComponent(transformationComponent);
		// attach a mesh component
		NDYComponentMesh meshComponent = new NDYComponentMeshSailboat("models/ship.3ds", "shaders/basic", null);		
		mRacer.addComponent(meshComponent);
		// attach cinematic component
		NDYComponentCinematicSailboat cineComponent = new NDYComponentCinematicSailboat();
		mRacer.addComponent(cineComponent);
		mActors.add(mRacer);
		
		// make the camera follow the boat
		NDYComponentFollow followComponent = new NDYComponentFollow(mRacer, 120f);
		mCameraPerspective.addComponent(followComponent);
		
		NDYActor axis3D = new NDYActor();
		transformationComponent = new NDYComponentTransformation();
		transformationComponent.setScale(1000,1000,1000);
		axis3D.addComponent(transformationComponent);
		NDYMesh mesh = NDYMesh.axis3d();
		meshComponent = new NDYComponentMesh(mesh.toString(),"shaders/basic_colored",null);
		axis3D.addComponent(meshComponent);
		mActors.add(axis3D);
		
		NDYActor arrow = new NDYActor();
		transformationComponent = new NDYComponentTransformation();
		arrow.addComponent(transformationComponent);
		meshComponent = new NDYComponentMesh("models/arrow.3ds", "shaders/basic", null);
		arrow.addComponent(meshComponent);
		mActors.add(arrow);
		transformationComponent.setRot(0, mWindRot, 0);
		
		NDYActor terrain = new NDYActor();
		transformationComponent = new NDYComponentTransformation();
		terrain.addComponent(transformationComponent);
		NDYComponentWater water = new NDYComponentWater("shaders/water","textures/SkyDome-Cloud-Few-MidMorning.png");
		terrain.addComponent(water);
		transformationComponent.setScale(100, 1, 100);
		transformationComponent.setPos(-500, 0, -500);
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
	
	public NDYActor getRacer() {
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
