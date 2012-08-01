package ndy.game.component;

import java.util.Random;

import ndy.game.NDYGLSurfaceView;
import ndy.game.actor.NDYActor;
import ndy.game.actor.NDYCamera;
import ndy.game.actor.NDYGame;
import ndy.game.material.NDYWaterDesc;
import ndy.game.math.NDYMath;
import ndy.game.math.NDYVector3;
import ndy.game.mesh.NDYMesh;
import ndy.game.mesh.NDYSubMesh;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageRender;
import ndy.game.message.NDYMessageUpdate;
import ndy.game.shader.NDYProgram;
import ndy.game.shader.NDYProgramWater;

import org.xmlpull.v1.XmlPullParser;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.FloatMath;

public class NDYComponentWater extends NDYComponent {
	public NDYMesh[] meshes;
	public NDYProgram program;
	public NDYWaterDesc water;
	public int numWaves;
	public float wavelength;

	public float[] modelMatrix = new float[16];

	public NDYComponentWater(NDYProgram program, float wavelength) {
		super("water");
		this.program = program;
		this.wavelength = wavelength;

		genWaterFeatures();

		genMeshes();
	}

	public void genMeshes() {
		meshes = new NDYMesh[2];
		meshes[0] = NDYMesh.plan(1, 1);
		meshes[1] = NDYMesh.plan(1, 1);
	}

	public void genWaterFeatures() {
		numWaves = 3;

		water = new NDYWaterDesc();

		water.init(numWaves);

		Random rnd = new Random();

		float wa = NDYGame.instance.mWeather.mWindRot;

		for (int i = 0; i < numWaves; i++) {
			water.wavelength[i] = wavelength + rnd.nextFloat() * wavelength;
			water.phases[i] = rnd.nextFloat() * NDYMath.TWOPI;
			water.sharpness[i] = rnd.nextFloat();
			water.amplitude[i] = water.wavelength[i] * 0.03f;
			float a = rnd.nextFloat() * NDYMath.TWOPI / 8.f;
			a = wa - a / 2.f + a;
			water.wavedir[i * 2] = FloatMath.cos(a);
			water.wavedir[i * 2 + 1] = FloatMath.sin(a);
			water.calcConst();
		}
	}

	public void load(XmlPullParser xpp) {

	}

	public void renderPatch(NDYProgramWater p, NDYSubMesh submesh, float x, float z, float w, float h) {
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, x, 0.f, z);
		Matrix.scaleM(modelMatrix, 0, w, 1.f, h);

		GLES20.glUniformMatrix4fv(p.mWorldMatrixHandle, 1, false, modelMatrix, 0);
		NDYGLSurfaceView.checkGLError("glUniformMatrix4fv world matrix");

		p.sendSubmesh(submesh);
	}

	public boolean processMessage(NDYMessage msg) {
		if (msg.getClass() == NDYMessageUpdate.class) {
			// TODO change water based on weather
		}

		if (msg.getClass() == NDYMessageRender.class) {
			GLES20.glUseProgram(program.getId());
			NDYGLSurfaceView.checkGLError("glUseProgram");

			NDYProgramWater p = (NDYProgramWater) program;
			p.sendGameParams();
			p.sendActorMaterials(mParent);

			NDYGame w = NDYGame.instance;
			NDYCamera c = w.mCameraPerspective;
			NDYVector3 cpos = c.mPos;

			NDYActor r = mParent;
			NDYComponentTransformation trans = (NDYComponentTransformation) r.findComponent("transformation");

			NDYSubMesh submesh_low = meshes[1].submeshes.entrySet().iterator().next().getValue();
			NDYSubMesh submesh_high = meshes[0].submeshes.entrySet().iterator().next().getValue();

			GLES20.glUniform1fv(p.mWaveAmplitudeHandle, numWaves, water.amplitude, 0);
			GLES20.glUniform4fv(p.mWaveConstHandle, numWaves, water.wconst, 0);
			GLES20.glUniform1fv(p.mWaveLengthHandle, numWaves, water.wavelength, 0);
			GLES20.glUniform1fv(p.mWavePhaseHandle, numWaves, water.phases, 0);
			GLES20.glUniform1fv(p.mWaveSharpnessHandle, numWaves, water.sharpness, 0);
			GLES20.glUniform2fv(p.mWaveVectorHandle, numWaves, water.wavedir, 0);
			GLES20.glUniform2f(p.mWaterSize, trans.scale.x, trans.scale.z);
			GLES20.glUniform2f(p.mWaterPos, trans.pos.x, trans.pos.z);

			renderPatch(p, submesh_high, trans.pos.x, trans.pos.z, trans.scale.x, trans.scale.z);
		}

		return false;
	}
}
