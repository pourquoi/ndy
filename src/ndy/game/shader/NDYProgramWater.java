package ndy.game.shader;

import ndy.game.NDYWorld;
import android.opengl.GLES20;

public class NDYProgramWater extends NDYProgramBasic {
	public int mWaveParams1Handle; // amplitude, phase, wavelength, sharpness
	public int mWaveParams2Handle;
	public int mWaveParams3Handle;
	public int mWaveVector1Handle;
	public int mWaveVector2Handle;
	public int mWaveVector3Handle;

	public NDYProgramWater(String name) {
		super(name);
	}
	
	protected void bindAttribs() {
		super.bindAttribs();
		
		mWaveParams1Handle = GLES20.glGetUniformLocation(mId, "uWaveParams1");
		mWaveVector1Handle = GLES20.glGetUniformLocation(mId, "uWaveVector1");
		mWaveParams2Handle = GLES20.glGetUniformLocation(mId, "uWaveParams2");
		mWaveVector2Handle = GLES20.glGetUniformLocation(mId, "uWaveVector2");
		mWaveParams3Handle = GLES20.glGetUniformLocation(mId, "uWaveParams3");
		mWaveVector3Handle = GLES20.glGetUniformLocation(mId, "uWaveVector3");
	}
	
	public void setWorldAttribs() {
		super.setWorldAttribs();
		NDYWorld w = NDYWorld.current;
		
		GLES20.glUniform4f(mWaveParams1Handle, 0.1f, 2.f, 10.f, 0.5f);
		GLES20.glUniform2f(mWaveVector1Handle, 0.5f, 0.5f);
		
		GLES20.glUniform4f(mWaveParams2Handle, 0.1f, 1.f, 15.f, 0.3f);
		GLES20.glUniform2f(mWaveVector2Handle, 0.5f, -0.5f);
		
		GLES20.glUniform4f(mWaveParams3Handle, 0.1f, 0.f, 5.f, 0.7f);
		GLES20.glUniform2f(mWaveVector3Handle, 0.5f, 0.5f);
	}
}
