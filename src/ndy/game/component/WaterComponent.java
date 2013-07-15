package ndy.game.component;

import java.util.Random;

import ndy.game.Game;
import ndy.game.NDYGLSurfaceView;
import ndy.game.actor.Actor;
import ndy.game.material.WaterOptions;
import ndy.game.math.NDYMath;
import ndy.game.mesh.Mesh;
import ndy.game.mesh.SubMesh;
import ndy.game.message.Message;
import ndy.game.message.RenderMessage;
import ndy.game.message.UpdateMessage;
import ndy.game.shader.Program;
import ndy.game.shader.ProgramWater;
import ndy.game.system.RenderSystem;
import ndy.game.system.WorldSystem;

import org.xmlpull.v1.XmlPullParser;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.FloatMath;

public class WaterComponent extends Component {
	public Mesh[] meshes;
	public Program program;
	public WaterOptions water;
	public int numWaves;
	public float wavelength;

	public float[] modelMatrix = new float[16];

	public WaterComponent(Program program, float wavelength) {
		super("water");
		this.program = program;
		this.wavelength = wavelength;

		genWaterFeatures();

		genMeshes();
		
		systems.add(WorldSystem.name);
		systems.add(RenderSystem.name);
	}

	public void genMeshes() {
		meshes = new Mesh[2];
		meshes[0] = Mesh.plan(1, 1);
		meshes[1] = Mesh.plan(100, 100);
	}

	public void genWaterFeatures() {
		numWaves = 5;

		water = new WaterOptions();

		water.init(numWaves);

		Random rnd = new Random();
		
		this.wavelength = rnd.nextFloat() * 5.f;

		float wa = Game.instance.world.weather.mWindRot;

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

	public void renderPatch(ProgramWater p, SubMesh submesh, float x, float z, float w, float h) {
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, x, 0.f, z);
		Matrix.scaleM(modelMatrix, 0, w, 1.f, h);

		GLES20.glUniformMatrix4fv(p.mWorldMatrixHandle, 1, false, modelMatrix, 0);
		NDYGLSurfaceView.checkGLError("glUniformMatrix4fv world matrix");

		p.sendSubmesh(submesh);
	}

	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {
			TransformationComponent trans = (TransformationComponent) parent.findComponent("transformation");
			
			//trans.pos.z += 20 * ((UpdateMessage)msg).getInterval() / 1000.f;
			
		//	if( trans.pos.z < -trans.scale.z ) trans.pos.z = 0;
		}

		if (msg.getClass() == RenderMessage.class) {
			GLES20.glUseProgram(program.getId());
			NDYGLSurfaceView.checkGLError("glUseProgram");

			ProgramWater p = (ProgramWater) program;
			p.sendGameParams();
			p.sendActorMaterials(parent);

			TransformationComponent trans = (TransformationComponent) parent.findComponent("transformation");

			SubMesh submesh_low = meshes[0].submeshes.entrySet().iterator().next().getValue();
			SubMesh submesh_high = meshes[1].submeshes.entrySet().iterator().next().getValue();

			GLES20.glUniform1fv(p.mWaveAmplitudeHandle, numWaves, water.amplitude, 0);
			GLES20.glUniform4fv(p.mWaveConstHandle, numWaves, water.wconst, 0);
			GLES20.glUniform1fv(p.mWaveLengthHandle, numWaves, water.wavelength, 0);
			GLES20.glUniform1fv(p.mWavePhaseHandle, numWaves, water.phases, 0);
			GLES20.glUniform1fv(p.mWaveSharpnessHandle, numWaves, water.sharpness, 0);
			GLES20.glUniform2fv(p.mWaveVectorHandle, numWaves, water.wavedir, 0);
			
			//for(int i=-1;i<=2;i++) {
			int i=0;
			GLES20.glUniform2f(p.mWaterSize, trans.scale.x, trans.scale.z);
			GLES20.glUniform2f(p.mWaterPos, trans.pos.x, trans.pos.z + i*trans.scale.z);
			
			renderPatch(p, submesh_high, trans.pos.x, trans.pos.z + i*trans.scale.z, trans.scale.x, trans.scale.z);
			//}
		}

		return false;
	}
}
