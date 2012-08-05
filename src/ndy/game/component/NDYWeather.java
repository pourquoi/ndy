package ndy.game.component;

import ndy.game.math.NDYVector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;

public class NDYWeather extends NDYComponent {
	public NDYVector3 mLightDir = new NDYVector3();
	public float mWindRot = 45.f; // degrees
	public float mWindSpeed = 5.f; // m/s
	public float mGravity = 9.81f;
	public String mEnvTex = null;

	public NDYWeather() {
		super("weather");
		mEnvTex = "textures/env_cloud_few_midmorning.png";
	}
	
	@Override
	public boolean processMessage(NDYMessage msg) {
		if (msg.getClass() == NDYMessageUpdate.class) {
		}
		
		return false;
	}
}
