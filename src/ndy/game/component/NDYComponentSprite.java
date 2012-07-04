package ndy.game.component;

import ndy.game.NDYActor;
import ndy.game.NDYGLSurfaceView;
import ndy.game.NDYRessource;
import ndy.game.NDYWorld;
import ndy.game.material.NDYTexture;
import ndy.game.math.Vector3;
import ndy.game.mesh.NDYMesh;
import ndy.game.mesh.NDYSubMesh;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageRender;
import ndy.game.shader.NDYProgram;
import ndy.game.shader.NDYProgramBasic;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class NDYComponentSprite extends NDYComponent {
	protected NDYProgram mProgram;
	protected NDYTexture mTexture;
	protected NDYMesh mMesh;
	protected float [] mMatrix = new float[16];

	public NDYComponentSprite(String textureName, String programName) {
		super("sprite");

		mTexture = (NDYTexture)NDYRessource.getRessource(textureName);
		if( mTexture == null ) {
			mTexture = new NDYTexture(textureName);
			NDYRessource.addRessource(mTexture);
		}

		mProgram = (NDYProgram)NDYRessource.getRessource(programName);
		if( mProgram == null ) {
			mProgram = new NDYProgramBasic(programName);
			NDYRessource.addRessource(mProgram);
		}

		mMesh = NDYMesh.quad2d();
	}
	
	@Override
	public boolean processMessage(NDYMessage msg) {
		NDYActor r = mParent;
		
		if( msg.getClass() == NDYMessageRender.class ) {
			NDYComponentTransformation trans = (NDYComponentTransformation)r.findComponent("transformation");
			NDYWorld w = NDYWorld.current;
	        w.setCamera(w.getCameraOrtho());

			NDYProgramBasic p = (NDYProgramBasic)mProgram;
			
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getId());
			
			GLES20.glUseProgram(p.getId());
	        NDYGLSurfaceView.checkGLError("glUseProgram");
	        
	        p.setWorldAttribs();
	        
	        NDYSubMesh quad = mMesh.submeshes.values().iterator().next();
	        
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getId());
			
			Matrix.setIdentityM(mMatrix, 0);
			Vector3 pos = trans.getPos();
			Matrix.translateM(mMatrix, 0, pos.x, pos.y, pos.z);
			Vector3 scale = trans.getScale();
			Matrix.scaleM(mMatrix, 0, scale.x, scale.y, 0);
			
			GLES20.glUniformMatrix4fv(p.mWorldMatrixHandle, 1, false, mMatrix, 0);
	        NDYGLSurfaceView.checkGLError("glUniformMatrix4fv world matrix");
	        
	        quad.vbuffer.position(quad.posOffset);
	        GLES20.glVertexAttribPointer(p.mPositionHandle, 3, GLES20.GL_FLOAT, false, quad.vsize, quad.vbuffer);
	        NDYGLSurfaceView.checkGLError("glVertexAttribPointer maPosition");
	        
	        GLES20.glEnableVertexAttribArray(mProgram.mPositionHandle);
	        NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray maPositionHandle");
	        
	        quad.vbuffer.position(quad.texcoordsOffset);
        	GLES20.glVertexAttribPointer(p.mTextureHandle, 2, GLES20.GL_FLOAT, false, quad.vsize, quad.vbuffer);
        	NDYGLSurfaceView.checkGLError("glVertexAttribPointer maTextureHandle");
        	
        	GLES20.glEnableVertexAttribArray(p.mTextureHandle);
        	NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray maTextureHandle");
        	
        	GLES20.glDrawArrays(quad.drawMode, 0, quad.vbuffer.capacity() * NDYMesh.FLOAT_SIZE_BYTES / quad.vsize);
        	NDYGLSurfaceView.checkGLError("glDrawArrays");
        	
        	w.setCamera(w.getCameraPerspective());
		}
		
		return false;
	}

}
