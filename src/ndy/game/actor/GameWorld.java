package ndy.game.actor;

import ndy.game.RaceOptions;
import ndy.game.component.EnvironmentComponent;
import ndy.game.component.WaterComponent;

public class GameWorld extends Actor {
	public Camera camera;
	public Camera cameraPerspective;
	public Camera cameraOrtho;
	
	public EnvironmentComponent weather;
	
	public Player player;
	
	public WaterComponent water;

	public GameWorld() {
		super("world");
	}
	
	public void init() {
		cameraPerspective = new Camera();
		cameraPerspective.mode = Camera.MODE_PERSPECTIVE;
		
		cameraOrtho = new Camera();
		cameraOrtho.mode = Camera.MODE_ORTHO;
		
		camera = cameraPerspective;
		
		this.addComponent(cameraPerspective);
		this.addComponent(cameraOrtho);
	}
	
	public void load(RaceOptions options) {
		weather = new EnvironmentComponent();
		weather.mEnvTex = "textures/env_cloud_few_midmorning.png";
		this.addComponent(weather);
		
	}
}
