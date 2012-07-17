package ndy.game.actor;

import java.util.ArrayList;

import ndy.game.NDYInput;
import ndy.game.NDYLakeMap;
import ndy.game.component.NDYComponentCinematicSailboat;
import ndy.game.component.NDYComponentFollow;
import ndy.game.component.NDYComponentMaterial;
import ndy.game.component.NDYComponentMesh;
import ndy.game.component.NDYComponentMeshSailboat;
import ndy.game.component.NDYComponentTransformation;
import ndy.game.component.NDYComponentWater;
import ndy.game.component.NDYWeather;
import ndy.game.material.NDYMaterial;
import ndy.game.material.NDYTexture;
import ndy.game.math.NDYVector3;
import ndy.game.mesh.NDYMesh;
import ndy.game.message.NDYMessageRender;
import ndy.game.message.NDYMessageUpdate;
import ndy.game.shader.NDYProgram;
import android.content.Context;

public class NDYGame extends NDYActor {
	private static String TAG = "NDYGame";

	public NDYInterface mInterface;
	public NDYInput mInput;

	public ArrayList<NDYActor> mActors;

	public NDYCamera mCamera;
	public NDYCamera mCameraPerspective;
	public NDYCamera mCameraOrtho;

	public NDYActor mRacer;
	public NDYComponentWater mWater;

	public Context mContext;

	public long mTime = 0;

	public NDYWeather mWeather;

	public static NDYGame instance = null;

	public NDYGame() {
		super("game");
	}

	public static void load(Context context) {
		if (NDYGame.instance == null) {
			NDYGame.instance = new NDYGame();
			NDYGame.instance.mContext = context;
			NDYGame.instance.init();
		} else {
			NDYGame.instance.mContext = context;
		}
	}

	public void init() {
		mActors = new ArrayList<NDYActor>();

		mWeather = new NDYWeather();
		mWeather.mLightDir = new NDYVector3(0.5f, -0.5f, 0.5f).normalize();
		this.addComponent(mWeather);

		// create the ui
		mInterface = new NDYInterface();
		mActors.add(mInterface);

		mInput = new NDYInput();

		// create the cameras
		mCameraPerspective = new NDYCamera();
		mCameraPerspective.setTarget(0f, 0f, 10.f);
		mCameraPerspective.setPos(0f, 10f, -10f);
		mActors.add(mCameraPerspective);
		mCamera = mCameraPerspective;

		mCameraOrtho = new NDYCamera();
		mCameraOrtho.mMode = NDYCamera.MODE_ORTHO;
		mActors.add(mCameraOrtho);

		// create the racer
		mRacer = new NDYActor("boat");
		// attach transformation component
		NDYComponentTransformation transformationComponent = new NDYComponentTransformation();
		transformationComponent.setPos(0f, 0f, 0.f);
		transformationComponent.setScale(0.5f, 0.5f, 0.5f);
		mRacer.addComponent(transformationComponent);
		NDYComponentMaterial materialComponent = new NDYComponentMaterial(NDYMaterial.Brass(), 0);
		mRacer.addComponent(materialComponent);
		// attach a mesh component
		NDYComponentMesh meshComponent = new NDYComponentMeshSailboat(NDYMesh.factory("models/ship.3ds"), NDYProgram.factory("shaders/basic"));
		mRacer.addComponent(meshComponent);
		// attach cinematic component
		NDYComponentCinematicSailboat cineComponent = new NDYComponentCinematicSailboat();
		mRacer.addComponent(cineComponent);
		mActors.add(mRacer);

		// make the camera follow the boat
		NDYComponentFollow followComponent = new NDYComponentFollow(mRacer, 30f);
		mCameraPerspective.addComponent(followComponent);

		NDYActor axis3D = new NDYActor("axis3D");
		transformationComponent = new NDYComponentTransformation();
		transformationComponent.setScale(100, 100, 100);
		axis3D.addComponent(transformationComponent);
		meshComponent = new NDYComponentMesh(NDYMesh.axis3d(), NDYProgram.factory("shaders/basic_colored"));
		axis3D.addComponent(meshComponent);
		mActors.add(axis3D);

		NDYActor arrow = new NDYActor("arrow");
		transformationComponent = new NDYComponentTransformation();
		transformationComponent.setScale(5.f, 5.f, 5.f);
		arrow.addComponent(transformationComponent);
		meshComponent = new NDYComponentMesh(NDYMesh.factory("models/arrow.3ds"), NDYProgram.factory("shaders/basic"));
		arrow.addComponent(meshComponent);
		mActors.add(arrow);
		transformationComponent.setRot(0, mWeather.mWindRot, 0);
		
		NDYLakeMap lake = new NDYLakeMap("textures/map_mazury.jpg");
		NDYMaterial material = new NDYMaterial();
		material.texture = NDYTexture.factory("textures/water.jpg");
		
		NDYMaterial env = new NDYMaterial();
		env.texture = NDYTexture.factory(mWeather.mEnvTex);
		
		NDYMaterial heightmap = new NDYMaterial();
		heightmap.texture = NDYTexture.factory("textures/map_mazury.jpg");
		
		NDYActor terrain = new NDYActor("water");
		transformationComponent = new NDYComponentTransformation();
		terrain.addComponent(transformationComponent);
		
		materialComponent = new NDYComponentMaterial(material,0);
		terrain.addComponent(materialComponent);
		materialComponent = new NDYComponentMaterial(env,1);
		terrain.addComponent(materialComponent);
		materialComponent = new NDYComponentMaterial(heightmap,2);
		terrain.addComponent(materialComponent);
		
		mWater = new NDYComponentWater(NDYProgram.factory("shaders/water"), lake, 1.f);
		terrain.addComponent(mWater);
		transformationComponent.setPos(-500.f, 0.f, -500.f);
		transformationComponent.setScale(1000.f, 1.f, 1000.f);
		mActors.add(terrain);
	}

	public void update(long dt) {
		mTime += dt;
		NDYMessageUpdate msg = new NDYMessageUpdate();
		msg.setInterval(dt);
		for (int i = 0, n = mActors.size(); i < n; i++) {
			mActors.get(i).dispatchMessage(msg);
		}
	}

	public void render() {
		NDYMessageRender msg = new NDYMessageRender();
		for (int i = 0, n = mActors.size(); i < n; i++) {
			mActors.get(i).dispatchMessage(msg);
		}
	}

	public void addActor(NDYActor a) {
		mActors.add(a);
	}
}
