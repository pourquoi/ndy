package ndy.game.component;

import ndy.game.material.NDYTexture;
import ndy.game.math.NDYVector3;

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
}
