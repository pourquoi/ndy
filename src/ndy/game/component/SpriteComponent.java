package ndy.game.component;

import ndy.game.NDYGLSurfaceView;
import ndy.game.Game;
import ndy.game.Ressource;
import ndy.game.actor.Actor;
import ndy.game.material.Texture;
import ndy.game.math.Vector3;
import ndy.game.mesh.Mesh;
import ndy.game.mesh.SubMesh;
import ndy.game.message.Message;
import ndy.game.message.RenderMessage;
import ndy.game.shader.Program;
import ndy.game.shader.ProgramBasic;
import ndy.game.system.RenderSystem;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class SpriteComponent extends Component {
	protected Program mProgram;
	protected Texture mTexture;
	protected Mesh mMesh;
	protected float[] mMatrix = new float[16];

	public SpriteComponent(String textureName, String programName) {
		super("sprite");

		mTexture = (Texture) Ressource.getRessource(textureName);
		if (mTexture == null) {
			mTexture = new Texture(textureName);
			Ressource.addRessource(mTexture);
		}

		mProgram = (Program) Ressource.getRessource(programName);
		if (mProgram == null) {
			mProgram = new ProgramBasic(programName);
			Ressource.addRessource(mProgram);
		}

		mMesh = Mesh.quad2d();
		
		systems.add(RenderSystem.name);
	}

	@Override
	public boolean processMessage(Message msg) {
		Actor r = parent;

		if (msg.getClass() == RenderMessage.class) {
			TransformationComponent trans = (TransformationComponent) r
					.findComponent("transformation");
			Game w = Game.instance;
			w.mCamera = w.mCameraOrtho;

			ProgramBasic p = (ProgramBasic) mProgram;

			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getId());

			GLES20.glUseProgram(p.getId());
			NDYGLSurfaceView.checkGLError("glUseProgram");

			p.sendGameParams();

			SubMesh quad = mMesh.submeshes.values().iterator().next();

			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getId());

			Matrix.setIdentityM(mMatrix, 0);
			Vector3 pos = trans.pos;
			Matrix.translateM(mMatrix, 0, pos.x, pos.y, pos.z);
			Vector3 scale = trans.scale;
			Matrix.scaleM(mMatrix, 0, scale.x, scale.y, 0);

			GLES20.glUniformMatrix4fv(p.mWorldMatrixHandle, 1, false, mMatrix,
					0);
			NDYGLSurfaceView.checkGLError("glUniformMatrix4fv world matrix");

			quad.vbuffer.position(quad.posOffset);
			GLES20.glVertexAttribPointer(p.mPositionHandle, 3, GLES20.GL_FLOAT,
					false, quad.vsize, quad.vbuffer);
			NDYGLSurfaceView.checkGLError("glVertexAttribPointer maPosition");

			GLES20.glEnableVertexAttribArray(mProgram.mPositionHandle);
			NDYGLSurfaceView
					.checkGLError("glEnableVertexAttribArray maPositionHandle");

			quad.vbuffer.position(quad.texcoordsOffset);
			GLES20.glVertexAttribPointer(p.mTextureHandle, 2, GLES20.GL_FLOAT,
					false, quad.vsize, quad.vbuffer);
			NDYGLSurfaceView
					.checkGLError("glVertexAttribPointer maTextureHandle");

			GLES20.glEnableVertexAttribArray(p.mTextureHandle);
			NDYGLSurfaceView
					.checkGLError("glEnableVertexAttribArray maTextureHandle");

			GLES20.glDrawArrays(quad.drawMode, 0, quad.vbuffer.capacity()
					* Mesh.FLOAT_SIZE_BYTES / quad.vsize);
			NDYGLSurfaceView.checkGLError("glDrawArrays");

			w.mCamera = w.mCameraPerspective;
		}

		return false;
	}

}
