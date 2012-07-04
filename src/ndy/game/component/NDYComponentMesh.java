package ndy.game.component;

import java.util.Iterator;
import java.util.Map.Entry;

import ndy.game.NDYActor;
import ndy.game.NDYGLSurfaceView;
import ndy.game.NDYRessource;
import ndy.game.NDYWorld;
import ndy.game.material.NDYMaterial;
import ndy.game.material.NDYTexture;
import ndy.game.math.Quaternion;
import ndy.game.math.Vector3;
import ndy.game.mesh.NDYAnimation;
import ndy.game.mesh.NDYMesh;
import ndy.game.mesh.NDYSubMesh;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageRender;
import ndy.game.shader.NDYProgram;
import ndy.game.shader.NDYProgramBasic;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class NDYComponentMesh extends NDYComponent {
	protected NDYMesh mMesh;
	protected NDYMaterial mMaterial;
	protected NDYProgram mProgram;
	
	protected float [] mMatrix = new float[16];
	protected float [] mTranslationMatrix = new float[16];
	
	public NDYComponentMesh(String meshName, String programName, String textureName) {
		super("mesh");
		NDYMesh mesh = (NDYMesh)NDYRessource.getRessource(meshName);
		if( mesh == null ) {
			mesh = new NDYMesh(meshName);
			mesh.loadFile();
			NDYRessource.addRessource(mesh);
		}
		NDYProgram program = (NDYProgram)NDYRessource.getRessource(programName);
		if( program == null ) {
			program = NDYProgram.factory(programName);
			NDYRessource.addRessource(program);
		}
		
		NDYMaterial material = new NDYMaterial();
		if( textureName != null ) {
			NDYTexture texture = (NDYTexture)NDYRessource.getRessource(textureName);
			if( texture == null ) {
				texture = new NDYTexture(textureName);
				NDYRessource.addRessource(texture);
			}
			material.texture = texture;
		}
		
		mMesh = mesh;
		mMaterial = material;
		mProgram = program;
	}
	
	protected void computeModelMatrix(NDYSubMesh submesh) {
		NDYActor r = mParent;
		NDYComponentTransformation trans = (NDYComponentTransformation)r.findComponent("transformation");
		
		Vector3 pos = trans.getPos();
		Vector3 scale = trans.getScale();
		Quaternion q = trans.getRotQ();

		NDYAnimation anim = mMesh.animations.get(submesh.animation);
		if(anim!=null) {
			float animTime = (float)(NDYWorld.current.getTime() / 1000.f); 
			anim.computeTransform(q, animTime);
		}

		Matrix.setIdentityM(mTranslationMatrix, 0);
		Matrix.translateM(mTranslationMatrix, 0, pos.x, pos.y, pos.z);
					
		Matrix.multiplyMM(mMatrix, 0, mTranslationMatrix, 0, q.getMatrix(), 0);
		Matrix.scaleM(mMatrix, 0, scale.x, scale.y, scale.z);
	}
	
	@Override
	public boolean processMessage(NDYMessage msg) {
		if( msg.getClass() == NDYMessageRender.class ) {
			if( mMaterial.texture != null ) {
				GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mMaterial.texture.getId());
			}
			
	        GLES20.glUseProgram(mProgram.getId());
	        NDYGLSurfaceView.checkGLError("glUseProgram");
	        
	        NDYProgramBasic p = (NDYProgramBasic)mProgram;
	        
	        p.setWorldAttribs();
	        
	        Iterator<Entry<String, NDYSubMesh>> it = mMesh.submeshes.entrySet().iterator();
	        while(it.hasNext()) {
	        	NDYSubMesh submesh = it.next().getValue();

	        	computeModelMatrix(submesh);
	        	
	        	GLES20.glUniformMatrix4fv(p.mWorldMatrixHandle, 1, false, mMatrix, 0);
		        NDYGLSurfaceView.checkGLError("glUniformMatrix4fv world matrix");

	        	NDYMaterial material = mMaterial;
	        	if( submesh.material != null ) material = submesh.material;

	        	submesh.vbuffer.position(submesh.posOffset);
		        GLES20.glVertexAttribPointer(p.mPositionHandle, 3, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
		        NDYGLSurfaceView.checkGLError("glVertexAttribPointer maPosition");
		        GLES20.glEnableVertexAttribArray(mProgram.mPositionHandle);
		        NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray maPositionHandle");
		        
		        if( submesh.getDesc() == NDYSubMesh.VERTEX_DESC_POSITION_TEXCOORDS || submesh.getDesc() == NDYSubMesh.VERTEX_DESC_POSITION_NORMAL_TEXCOORDS ) {
		        	if( p.mTextureHandle != -1 ) {
		        		submesh.vbuffer.position(submesh.texcoordsOffset);
			        	GLES20.glVertexAttribPointer(p.mTextureHandle, 2, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
			        	NDYGLSurfaceView.checkGLError("glVertexAttribPointer maTextureHandle");
			        	GLES20.glEnableVertexAttribArray(p.mTextureHandle);
			        	NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray maTextureHandle");
		        	}
		        }
		        
		        if( submesh.getDesc() == NDYSubMesh.VERTEX_DESC_POSITION_NORMAL || submesh.getDesc() == NDYSubMesh.VERTEX_DESC_POSITION_NORMAL_TEXCOORDS ) {
		        	if( p.mNormalHandle != -1 ) {
				        submesh.vbuffer.position(submesh.normalOffset);
				        GLES20.glVertexAttribPointer(p.mNormalHandle, 3, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
				        GLES20.glEnableVertexAttribArray(p.mNormalHandle);
				        NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray mNormalHandle");
		        	}
		        }
		        
		        if( submesh.getDesc() == NDYSubMesh.VERTEX_DESC_POSITION_COLOR ) {
		        	if( p.mColorHandle != -1 ) {
		        		submesh.vbuffer.position(submesh.colorOffset);
		        		GLES20.glVertexAttribPointer(p.mColorHandle, 4, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
				        GLES20.glEnableVertexAttribArray(p.mColorHandle);
				        NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray mColorHandle");
		        	}
		        }
		        
		        if( p.mAmbientHandle != -1 ) {
			        GLES20.glUniform4fv(p.mAmbientHandle, 1, material.ambient, 0);
			        NDYGLSurfaceView.checkGLError("glUniform4fv ambient");
		        }
		        
		        if( p.mDiffuseHandle != -1 ) {
			        GLES20.glUniform4fv(p.mDiffuseHandle, 1, material.diffuse, 0);
			        NDYGLSurfaceView.checkGLError("glUniform4fv diffuse");
		        }
		        
		        if( p.mSpecularHandle != -1 ) {
			        GLES20.glUniform4fv(p.mSpecularHandle, 1, material.specular, 0);
			        NDYGLSurfaceView.checkGLError("glUniform4fv specular");
		        }
		        
		        if( p.mShininessHandle != -1 ) {
		        	GLES20.glUniform1f(p.mShininessHandle, material.shininess);
		        	NDYGLSurfaceView.checkGLError("glUniform1f shininess");
		        }
		        
		        if( submesh.ibuffer != null ) {
		        	GLES20.glDrawElements(submesh.drawMode, submesh.ibuffer.capacity(), GLES20.GL_UNSIGNED_SHORT, submesh.ibuffer);
		        	NDYGLSurfaceView.checkGLError("glDrawElements");
		        } else {
		        	GLES20.glDrawArrays(submesh.drawMode, 0, submesh.vbuffer.capacity() * NDYMesh.FLOAT_SIZE_BYTES / submesh.vsize);
		        	NDYGLSurfaceView.checkGLError("glDrawArrays");
		        }
	        }
		}
		return false;
	}

	public NDYMesh getMesh() {
		return mMesh;
	}
	
	public void setMesh(NDYMesh mesh) {
		mMesh = mesh;
	}
	
	public NDYMaterial getMaterial() {
		return mMaterial;
	}
	
	public void setMaterial(NDYMaterial material) {
		mMaterial = material;
	}
	
	public void setProgram(NDYProgram program) {
		mProgram = program;
	}
	
	public NDYProgram getProgram() {
		return mProgram;
	}
}
