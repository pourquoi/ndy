package ndy.game.actor;

import ndy.game.RaceOptions;
import ndy.game.component.WeatherComponent;
import ndy.game.component.WaterComponent;

public class World extends Actor {
	public Camera camera;
	public Camera cameraPerspective;
	public Camera cameraOrtho;
	
	public WeatherComponent weather;
	
	public Player player;
	
	public WaterComponent water;
	
	public World() {
		super("world");
	}
	
	public void load(RaceOptions options) {
		
	}
}
