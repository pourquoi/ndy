package ndy.game.actor;

import ndy.game.Game;
import ndy.game.RaceOptions;
import ndy.game.component.CameraComponent;
import ndy.game.component.CameraFollowComponent;
import ndy.game.component.EnvironmentComponent;
import ndy.game.component.MaterialComponent;
import ndy.game.component.MeshComponent;
import ndy.game.component.TransformationComponent;
import ndy.game.component.WaterComponent;
import ndy.game.component.WorldInputComponent;
import ndy.game.material.Material;
import ndy.game.material.Texture;
import ndy.game.math.NDYMath;
import ndy.game.mesh.Mesh;
import ndy.game.shader.Program;
import ndy.game.shader.ProgramBasic;

public class GameWorld extends Actor {
	public CameraComponent camera;
	public CameraComponent cameraPerspective;
	public CameraComponent cameraOrtho;

	public EnvironmentComponent weather;
	
	public Player player;
	
	public WaterComponent water;

	public GameWorld() {
		super("world");
	}
	
	public void init() {
		cameraPerspective = new CameraComponent("perspective");
		cameraPerspective.mode = CameraComponent.MODE_PERSPECTIVE;
		
		cameraOrtho = new CameraComponent("ortho");
		cameraOrtho.mode = CameraComponent.MODE_ORTHO;
		
		camera = cameraPerspective;
		
		this.addComponent(cameraPerspective);
		this.addComponent(cameraOrtho);
	}
	
	public void load(RaceOptions options) {
		weather = new EnvironmentComponent();
		weather.mEnvTex = "textures/env_cloud_few_midmorning.png";
		weather.mLightDir.x = 0;
		weather.mLightDir.z = 0;
		weather.mLightDir.y = -1;
		weather.mLightDir.normalize();
		this.addComponent(weather);
		/*
		Actor w = new Actor();
		
		TransformationComponent t = new TransformationComponent();
		t.scale.x = 10;
		t.scale.z = 10;
		
		t.pos.x = -t.scale.x / 2;
		t.pos.z = -t.scale.z / 2;
		
		w.addComponent(t);
		
		Material texMat = Material.Glass();
		texMat.texture = Texture.factory("textures/water.jpg");

		Material envMat = new Material();
		envMat.texture = Texture.factory(weather.mEnvTex);
		
		Material hmMat = new Material();
		hmMat.texture = Texture.factory("textures/map_mazury.jpg");
		
		w.addComponent(new MaterialComponent(texMat,0));
		w.addComponent(new MaterialComponent(envMat,1));
		w.addComponent(new MaterialComponent(hmMat,2));
		
		water = new WaterComponent(Program.factory("shaders/water"), 5.1f);
		w.addComponent(water);
		
		Game.instance.addActor(w);*/
		
		player = new Player();

		TransformationComponent t = new TransformationComponent();
		t.rot.y = NDYMath.HALF_PI;
		t.pos.x = 0;
		t.pos.z = 0;
		/*
		t.scale.x = 10.f;
		t.scale.y = 10.f;
		t.scale.z = 10.f;
		*/
		player.addComponent(t);

		Mesh player_mesh = Mesh.factory("models/phenboat.3ds");
		MeshComponent player_mesh_component = new MeshComponent(player_mesh, Program.factory("shaders/basic"));
		player.addComponent(player_mesh_component);

		Material player_mat = Material.Glass();
		player.addComponent(new MaterialComponent(player_mat,0));

		float follow_dist = 0.3f;

		if( player_mesh.aabb != null ) {
			follow_dist = player_mesh.aabb.getPerimeter();
		}

		CameraFollowComponent c = new CameraFollowComponent("main", player, follow_dist);
		player.addComponent(c);

		this.camera = c;

		Game.instance.addActor(player);

		this.addComponent(new WorldInputComponent());
	}
}
