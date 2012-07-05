package ndy.game.component;

import ndy.game.NDYActor;
import ndy.game.NDYCamera;
import ndy.game.NDYGLSurfaceView;
import ndy.game.NDYWorld;
import ndy.game.material.NDYMaterial;
import ndy.game.material.NDYTexture;
import ndy.game.math.Vector3;
import ndy.game.mesh.NDYMesh;
import ndy.game.mesh.NDYSubMesh;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageRender;
import ndy.game.shader.NDYProgram;
import ndy.game.shader.NDYProgramWater;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class NDYComponentWater extends NDYComponent {
	private static int LOD_COUNT = 2;
	protected NDYMesh [] mMeshes;
	protected NDYProgram mProgram;
	protected NDYMaterial mMaterial;
	protected boolean [] mGrid;
	protected int mGridWidth;
	protected int mGridLength;
	
	protected float [] mMatrix = new float[16];

	public NDYComponentWater(String program, String texture) {
		super("water");
		init(program, texture);
	}
	
	public void init(String program, String texture) {
		mMeshes = new NDYMesh[LOD_COUNT];
		mMeshes[0] = NDYMesh.plan(20, 20);
		mMeshes[1] = NDYMesh.plan(2, 2);
		
		mProgram = NDYProgram.factory(program);
		mMaterial = new NDYMaterial();
		mMaterial.texture = NDYTexture.factory(texture);
		
		mGridWidth = 10;
		mGridLength = 10;
		mGrid = new boolean[mGridWidth*mGridLength];
		
		for(int i=0;i<mGridWidth*mGridLength;i++) {
			mGrid[i] = true;
		}
	}
	
	public boolean processMessage(NDYMessage msg) {
		if( msg.getClass() == NDYMessageRender.class ) {
			 GLES20.glUseProgram(mProgram.getId());
			 NDYGLSurfaceView.checkGLError("glUseProgram");
			 
			 NDYProgramWater p = (NDYProgramWater)mProgram;
			 p.setWorldAttribs();
			 
			 if( mMaterial.texture != null ) {
				GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mMaterial.texture.getId());
				if( p.mSamplerHandles[0] != -1 )
					GLES20.glUniform1i(p.mSamplerHandles[0], 0);
			}

			NDYWorld w = NDYWorld.current;
			NDYCamera c = w.getCameraPerspective();
			Vector3 cpos = c.getPos();
			
			NDYActor r = mParent;
			NDYComponentTransformation trans = (NDYComponentTransformation)r.findComponent("transformation");
			
			Vector3 pos = trans.getPos();
			Vector3 scale = trans.getScale();
			
			Vector3 camdir = c.getTarget().substract(cpos);
			Vector3 camtopatch = new Vector3();
			
			NDYSubMesh submesh = mMeshes[1].submeshes.entrySet().iterator().next().getValue();
			NDYMaterial material = mMaterial;
			
			for(int i=0;i<mGridWidth;i++) {
				for(int j=0;j<mGridLength;j++) {
					if( mGrid[i*mGridLength+j] ) {						
						float x = pos.x+(float)i*scale.x;
						float z = pos.z+(float)j*scale.z;
						
						camtopatch.x = x - cpos.x + scale.x/2.f;
						camtopatch.z = z - cpos.z + scale.z/2.f;
						
						if( Vector3.dotProduct(camtopatch, camdir) < 0.f ) continue;
						
						Matrix.setIdentityM(mMatrix, 0);
						Matrix.translateM(mMatrix, 0, x, pos.y, z);
						Matrix.scaleM(mMatrix, 0, scale.x, scale.y, scale.z);
						
						GLES20.glUniformMatrix4fv(p.mWorldMatrixHandle, 1, false, mMatrix, 0);
				        NDYGLSurfaceView.checkGLError("glUniformMatrix4fv world matrix");
				        
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
			}
			
		}
		
		return false;
	}
}
