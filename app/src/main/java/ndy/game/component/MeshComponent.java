package ndy.game.component;

import java.util.Iterator;
import java.util.Map.Entry;

import ndy.game.NDYGLSurfaceView;
import ndy.game.mesh.Mesh;
import ndy.game.mesh.SubMesh;
import ndy.game.message.Message;
import ndy.game.message.RenderMessage;
import ndy.game.shader.Program;
import ndy.game.shader.ProgramBasic;
import ndy.game.system.RenderSystem;
import ndy.game.system.WorldSystem;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class MeshComponent extends Component {
	public Mesh mesh;
	public Program program;

	protected float[] modelMatrix = new float[16]; // submesh transformation in world space
	protected float[] tmpMatrix = new float[32]; // allocate matrices for transformation computing

	public MeshComponent(Mesh mesh, Program program) {
		super("mesh");
		this.mesh = mesh;
		this.program = program;
		
		systems.add(RenderSystem.name);
	}

	protected void computeModelMatrix(SubMesh submesh) {
		TransformationComponent tf = (TransformationComponent) parent.findComponent("transformation");

		Matrix.setIdentityM(tmpMatrix, 0);
		Matrix.translateM(tmpMatrix, 0, tf.pos.x, tf.pos.y, tf.pos.z);
		Matrix.setRotateEulerM(tmpMatrix, 16, tf.rot.x, tf.rot.y, tf.rot.z);

		Matrix.multiplyMM(modelMatrix, 0, tmpMatrix, 0, tmpMatrix, 16);
		Matrix.scaleM(modelMatrix, 0, tf.scale.x, tf.scale.y, tf.scale.z);
	}

	@Override
	public boolean processMessage(Message msg) {
		if (msg.getClass() == RenderMessage.class) {
			GLES20.glUseProgram(program.getId());
			NDYGLSurfaceView.checkGLError("glUseProgram");

			ProgramBasic p = (ProgramBasic) program;

			p.sendGameParams();

			p.sendActorMaterials(parent);

			Iterator<Entry<String, SubMesh>> it = mesh.submeshes.entrySet().iterator();
			while (it.hasNext()) {
				SubMesh submesh = it.next().getValue();
				
				if( !submesh.cull ) {
					GLES20.glDisable(GLES20.GL_CULL_FACE);
				}

				computeModelMatrix(submesh);

				GLES20.glUniformMatrix4fv(p.mWorldMatrixHandle, 1, false, modelMatrix, 0);
				NDYGLSurfaceView.checkGLError("glUniformMatrix4fv world matrix");

				p.sendSubmesh(submesh);
				
				if( !submesh.cull ) {
					GLES20.glEnable(GLES20.GL_CULL_FACE);
				}
			}
		}
		return false;
	}
}
