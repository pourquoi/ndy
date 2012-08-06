package ndy.game;

import java.util.ArrayList;
import java.util.HashMap;

import ndy.game.actor.Actor;
import ndy.game.actor.Camera;
import ndy.game.actor.World;
import ndy.game.collision.NDYCollider;
import ndy.game.collision.NDYColliderMap;
import ndy.game.collision.NDYColliderPoly;
import ndy.game.component.CameraFocusComponent;
import ndy.game.component.Component;
import ndy.game.component.ComponentCollider;
import ndy.game.component.MaterialComponent;
import ndy.game.component.MeshComponent;
import ndy.game.component.SailboatMeshComponent;
import ndy.game.component.SailboatPhysicsComponent;
import ndy.game.component.TransformationComponent;
import ndy.game.component.WaterComponent;
import ndy.game.component.WeatherComponent;
import ndy.game.material.Material;
import ndy.game.material.Texture;
import ndy.game.math.Polygon;
import ndy.game.math.Vector2;
import ndy.game.math.Vector3;
import ndy.game.mesh.Mesh;
import ndy.game.message.Message;
import ndy.game.message.RenderMessage;
import ndy.game.message.UpdateMessage;
import ndy.game.shader.Program;
import ndy.game.system.CollisionSystem;
import ndy.game.system.InputSystem;
import ndy.game.system.RenderSystem;
import ndy.game.system.System;
import ndy.game.system.WorldSystem;
import android.content.Context;

public class Game {
	private static final String TAG = "NDYGame";

	public HashMap<String, System> systems = new HashMap<String, System>();

	public World world;

	private ArrayList<Actor> actors;
	public ArrayList<ComponentCollider> colliders;

	public Camera mCamera;
	public Camera mCameraPerspective;
	public Camera mCameraOrtho;

	public Actor mRacer;
	public WaterComponent mWater;

	public Context mContext;

	public long mTime = 0;

	public WeatherComponent mWeather;

	public static Game instance = null;

	public Game() {
		systems.put(WorldSystem.name, new WorldSystem());
		systems.put(InputSystem.name, new InputSystem());
		systems.put(RenderSystem.name, new RenderSystem());
		systems.put(CollisionSystem.name, new CollisionSystem());
	}

	public void dispatchMessage(Message m) {
		for (System s : systems.values()) {
			s.dispatchMessage(m);
		}
	}

	public static void load(Context context) {
		if (Game.instance == null) {
			Game.instance = new Game();
			Game.instance.mContext = context;
			Game.instance.init();
		} else {
			Game.instance.mContext = context;
		}
	}

	public void init() {
		actors = new ArrayList<Actor>();
		colliders = new ArrayList<ComponentCollider>();

		mWeather = new WeatherComponent();
		mWeather.mLightDir = new Vector3(0.5f, -0.5f, 0.5f).normalize();

		world = new World();
		addActor(world);

		world.addComponent(mWeather);

		// create the cameras
		mCameraPerspective = new Camera();
		mCameraPerspective.setTarget(0f, 0f, 10.f);
		mCameraPerspective.setPos(0f, 10f, -10f);
		world.addComponent(mCameraPerspective);
		mCamera = mCameraPerspective;

		mCameraOrtho = new Camera();
		mCameraOrtho.mMode = Camera.MODE_ORTHO;
		world.addComponent(mCameraOrtho);

		// create the racer
		mRacer = new Actor("boat");
		// attach transformation component
		TransformationComponent transformationComponent = new TransformationComponent();
		transformationComponent.setPos(0f, 0f, 0.f);
		// transformationComponent.setScale(0.5f, 0.5f, 0.5f);
		mRacer.addComponent(transformationComponent);
		// attach primary material
		MaterialComponent materialComponent = new MaterialComponent(Material.Brass(), 0);
		mRacer.addComponent(materialComponent);
		// attach a mesh component
		MeshComponent meshComponent = new SailboatMeshComponent(Mesh.factory("models/ship.3ds"), Program.factory("shaders/basic"));
		mRacer.addComponent(meshComponent);
		// attach collider
		Polygon boatpoly = new Polygon();
		boatpoly.points.add(new Vector2(0, 0));
		NDYCollider collider = new NDYColliderPoly(boatpoly);
		ComponentCollider colliderComponent = new ComponentCollider(collider);
		mRacer.addComponent(colliderComponent);

		// attach cinematic component
		SailboatPhysicsComponent cineComponent = new SailboatPhysicsComponent();
		mRacer.addComponent(cineComponent);
		addActor(mRacer);

		// make the camera follow the boat
		CameraFocusComponent followComponent = new CameraFocusComponent(30f);
		mRacer.addComponent(followComponent);

		Actor axis3D = new Actor("axis3D");
		transformationComponent = new TransformationComponent();
		transformationComponent.setScale(100, 100, 100);
		axis3D.addComponent(transformationComponent);
		meshComponent = new MeshComponent(Mesh.axis3d(), Program.factory("shaders/basic_colored"));
		axis3D.addComponent(meshComponent);
		addActor(axis3D);

		Actor arrow = new Actor("arrow");
		transformationComponent = new TransformationComponent();
		transformationComponent.setScale(5.f, 5.f, 5.f);
		arrow.addComponent(transformationComponent);
		meshComponent = new MeshComponent(Mesh.factory("models/arrow.3ds"), Program.factory("shaders/basic"));
		arrow.addComponent(meshComponent);
		addActor(arrow);
		transformationComponent.setRot(0, mWeather.mWindRot, 0);

		Material material = new Material();
		material.texture = Texture.factory("textures/water.jpg");

		Material env = new Material();
		env.texture = Texture.factory(mWeather.mEnvTex);

		Material heightmap = new Material();
		heightmap.texture = Texture.factory("textures/map_mazury.jpg");

		Actor terrain = new Actor("water");

		transformationComponent = new TransformationComponent();
		transformationComponent.setPos(-500.f, 0.f, -500.f);
		transformationComponent.setScale(1000.f, 1.f, 1000.f);
		terrain.addComponent(transformationComponent);

		materialComponent = new MaterialComponent(material, 0);
		terrain.addComponent(materialComponent);
		materialComponent = new MaterialComponent(env, 1);
		terrain.addComponent(materialComponent);
		materialComponent = new MaterialComponent(heightmap, 2);
		terrain.addComponent(materialComponent);

		collider = new NDYColliderMap("textures/map_mazury.jpg", 1000f, 1000f);
		colliderComponent = new ComponentCollider(collider);
		terrain.addComponent(colliderComponent);

		mWater = new WaterComponent(Program.factory("shaders/water"), 1.f);
		terrain.addComponent(mWater);

		addActor(terrain);
	}

	public void update(long dt) {
		mTime += dt;
		UpdateMessage msg = new UpdateMessage();
		msg.setInterval(dt);
		for (int i = 0, n = actors.size(); i < n; i++) {
			actors.get(i).dispatchMessage(msg);
		}
	}

	public void render() {
		RenderMessage msg = new RenderMessage();
		for (int i = 0, n = actors.size(); i < n; i++) {
			actors.get(i).dispatchMessage(msg);
		}
	}

	public void addActor(Actor a) {
		actors.add(a);
		a.inGame = true;
		Component c = a.findComponent("collider");
		if (c != null && !colliders.contains(c)) {
			colliders.add((ComponentCollider) c);
		}
	}

	public void removeActor(Actor a) {
		Component c = a.findComponent("collider");
		if (c != null && colliders.contains(c)) {
			colliders.remove(c);
		}
		actors.remove(a);
	}

	public boolean hasActor(Actor a) {
		return actors.contains(a);
	}
}
