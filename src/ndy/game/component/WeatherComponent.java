package ndy.game.component;

import ndy.game.math.Vector3;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import ndy.game.system.WorldSystem;

public class WeatherComponent extends Component {
	public Vector3 mLightDir = new Vector3();
	public float mWindRot = 45.f; // degrees
	public float mWindSpeed = 5.f; // m/s
	public float mGravity = 9.81f;
	public String mEnvTex = null;

	public WeatherComponent() {
		super("weather");
		mEnvTex = "textures/env_cloud_few_midmorning.png";
		systems.add(WorldSystem.name);
	}
	
	@Override
	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {
		}
		
		return false;
	}
}
