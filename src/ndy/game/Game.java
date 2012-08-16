package ndy.game;

import java.util.ArrayList;
import java.util.HashMap;

import ndy.game.actor.Actor;
import ndy.game.actor.GameWorld;
import ndy.game.component.WaterComponent;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import ndy.game.system.InputSystem;
import ndy.game.system.PhysicsSystem;
import ndy.game.system.RenderSystem;
import ndy.game.system.System;
import ndy.game.system.WorldSystem;
import android.content.Context;

public class Game {
	private static final String TAG = "NDYGame";
	
	public static final int STATE_LOADING = 0;
	public static final int STATE_RUNNING = 1;

	public HashMap<String, System> systems = new HashMap<String, System>();

	public ArrayList<Actor> actors = new ArrayList<Actor>();

	public Preferences prefs = new Preferences();
	public RaceOptions _options;
	
	public GameState state;
	
	public GameWorld world;
	public Actor mRacer;
	public WaterComponent mWater;

	public Context mContext;
	public long time = 0;

	public static Game instance = null;

	public Game(RaceOptions options) {
		state = new LoadingState();
		_options = options;
		systems.put(WorldSystem.name, new WorldSystem());
		systems.put(InputSystem.name, new InputSystem());
		systems.put(PhysicsSystem.name, new PhysicsSystem());
		systems.put(RenderSystem.name, new RenderSystem());
	}

	public void addActor(Actor a) {
		actors.add(a);
		a.inGame = true;
	}

	public void removeActor(Actor a) {
		actors.remove(a);
	}

	public boolean hasActor(Actor a) {
		return actors.contains(a);
	}
}
