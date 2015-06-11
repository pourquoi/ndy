package ndy.game.shader;

import android.opengl.GLES20;

public class ProgramWater extends ProgramBasic {
	public int mWaveAmplitudeHandle;
	public int mWaveVectorHandle;
	public int mWaveSharpnessHandle;
	public int mWaveConstHandle;
	public int mWavePhaseHandle;
	public int mWaveLengthHandle;
	public int mWaterSize;
	public int mWaterPos;

	public ProgramWater(String name) {
		super(name);
	}

	protected void bindAttribs() {
		super.bindAttribs();

		mWaveAmplitudeHandle = GLES20.glGetUniformLocation(mId, "uWaveAmplitude");
		mWaveVectorHandle = GLES20.glGetUniformLocation(mId, "uWaveVector");
		mWaveSharpnessHandle = GLES20.glGetUniformLocation(mId, "uWaveSharpness");
		mWavePhaseHandle = GLES20.glGetUniformLocation(mId, "uWavePhase");
		mWaveConstHandle = GLES20.glGetUniformLocation(mId, "uWaveConst");
		mWaveLengthHandle = GLES20.glGetUniformLocation(mId, "uWaveLength");
		mWaterSize = GLES20.glGetUniformLocation(mId, "uWaterSize");
		mWaterPos = GLES20.glGetUniformLocation(mId, "uWaterPos");
	}
}
