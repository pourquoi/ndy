package ndy.game.component;

import java.util.Iterator;
import java.util.Map.Entry;

import ndy.game.NDYGLSurfaceView;
import ndy.game.mesh.NDYMesh;
import ndy.game.mesh.NDYSubMesh;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageRender;
import ndy.game.shader.NDYProgram;
import ndy.game.shader.NDYProgramBasic;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class NDYComponentMesh extends NDYComponent {
	public NDYMesh mesh;
	public NDYProgram program;

	protected float[] modelMatrix = new float[16]; // submesh transformation in world space
	protected float[] tmpMatrix = new float[32]; // allocate matrices for transformation computing

	public NDYComponentMesh(NDYMesh mesh, NDYProgram program) {
		super("mesh");
		this.mesh = mesh;
		this.program = program;
	}

	protected void computeModelMatrix(NDYSubMesh submesh) {
		NDYComponentTransformation tf = (NDYComponentTransformation) mParent.findComponent("transformation");

		Matrix.setIdentityM(tmpMatrix, 0);
		Matrix.translateM(tmpMatrix, 0, tf.pos.x, tf.pos.y, tf.pos.z);

		tf.rotQ.getMatrix(tmpMatrix, 16);

		Matrix.multiplyMM(modelMatrix, 0, tmpMatrix, 0, tmpMatrix, 16);
		Matrix.scaleM(modelMatrix, 0, tf.scale.x, tf.scale.y, tf.scale.z);
	}

	@Override
	public boolean processMessage(NDYMessage msg) {
		if (msg.getClass() == NDYMessageRender.class) {
			GLES20.glUseProgram(program.getId());
			NDYGLSurfaceView.checkGLError("glUseProgram");

			NDYProgramBasic p = (NDYProgramBasic) program;

			p.sendGameParams();

			p.sendActorMaterials(mParent);

			Iterator<Entry<String, NDYSubMesh>> it = mesh.submeshes.entrySet().iterator();
			while (it.hasNext()) {
				NDYSubMesh submesh = it.next().getValue();
				
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
