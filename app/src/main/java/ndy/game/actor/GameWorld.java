package ndy.game.actor;

import ndy.game.Game;
import ndy.game.RaceOptions;
import ndy.game.component.CameraComponent;
import ndy.game.component.CameraFollowComponent;
import ndy.game.component.EnvironmentComponent;
import ndy.game.component.MaterialComponent;
import ndy.game.component.TransformationComponent;
import ndy.game.component.WaterComponent;
import ndy.game.component.WorldInputComponent;
import ndy.game.material.Material;
import ndy.game.material.Texture;
import ndy.game.math.NDYMath;
import ndy.game.shader.Program;

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
		
		Game.instance.addActor(w);
		
		player = new Player();
		t = new TransformationComponent();
		t.rot.y = NDYMath.HALF_PI;
		t.pos.x = 0;
		t.pos.z = 0;
		player.addComponent(t);
		
		CameraFollowComponent c = new CameraFollowComponent("main", player, 20);
		player.addComponent(c);
		
		this.camera = c;
		
		Game.instance.addActor(player);
		
		this.addComponent(new WorldInputComponent());
	}
}
