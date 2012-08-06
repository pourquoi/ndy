package ndy.game.material;

import ndy.game.Game;
import ndy.game.math.NDYMath;
import ndy.game.math.Vector3;
import android.util.FloatMath;

public class WaterOptions {
	public float [] amplitude;
	public float [] sharpness;
	public float [] phases;
	public float [] wavelength;
	public float [] wconst; //sqrt(2PIg/L), 2PI/L, Dirx*A, Diry*A
	
	public float [] wavedir;
	
	public void init(int numWaves) {
		amplitude = new float[numWaves];
		sharpness = new float[numWaves];
		phases = new float[numWaves];
		wavelength = new float[numWaves];
		wconst = new float[numWaves*4];
		wavedir = new float[numWaves*2];
	}
	
	public void calcConst() {
		for(int i=0;i<amplitude.length;i++) {
			wconst[i*4] = FloatMath.sqrt(NDYMath.TWOPI * Game.instance.mWeather.mGravity / wavelength[i]);
			wconst[i*4+1] = NDYMath.TWOPI / wavelength[i];
			wconst[i*4+2] = wavedir[i*2] * amplitude[i];
			wconst[i*4+3] = wavedir[i*2+1] * amplitude[i];
		}
	}
	
	public void getWaveNormal(float x, float y, Vector3 N, int i) {
		N.x = 0.f;
		N.y = 1.f;
		N.z = 0.f;

		float KdotP = wavedir[i*2]*wconst[i*4+1]*x + wavedir[i*2+1]*wconst[i*4+1]*y;
		float a = KdotP + phases[i];

		N.x -= wconst[i*4+2]*wconst[i*4+1]*FloatMath.cos(a);
		N.z -= wconst[i*4+3]*wconst[i*4+1]*FloatMath.cos(a);
		N.y -= sharpness[i]*amplitude[i]*FloatMath.sin(a);

		N.normalize();
	}
	
	public void getNormal(float x, float y, float t, Vector3 N) {
		N.x = 0.f;
		N.y = 1.f;
		N.z = 0.f;
		for(int i=0;i<amplitude.length;i++) {
			float KdotP = wavedir[i*2]*wconst[i*4+1]*x + wavedir[i*2+1]*wconst[i*4+1]*y;
			float a = KdotP - wconst[i*4] * t + phases[i];
			N.x -= wconst[i*4+2]*wconst[i*4+1]*FloatMath.cos(a);
			N.z -= wconst[i*4+3]*wconst[i*4+1]*FloatMath.cos(a);
			N.y -= sharpness[i]*amplitude[i]*FloatMath.sin(a);
		}
		
		N.normalize();
	}
}
