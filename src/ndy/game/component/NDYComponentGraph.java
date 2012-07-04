package ndy.game.component;

import ndy.game.NDYActor;
import ndy.game.NDYGLSurfaceView;
import ndy.game.NDYRessource;
import ndy.game.NDYWorld;
import ndy.game.math.Vector3;
import ndy.game.mesh.NDYMesh;
import ndy.game.mesh.NDYSubMesh;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageRender;
import ndy.game.message.NDYMessageUpdate;
import ndy.game.shader.NDYProgram;
import ndy.game.shader.NDYProgramBasic;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class NDYComponentGraph extends NDYComponent {
	protected NDYProgram mProgram;
	protected NDYMesh mMesh;
	protected NDYSubMesh mSubmesh;
	protected float [] mMatrix = new float[16];
	protected float [] mData;
	
	protected float mNextVal = 0.f;
	protected float mMaxVal = 0.f;
	
	protected int mHeight;
	protected float [] mColor = new float[3];
	
	public NDYComponentGraph(int width, int height, String programName) {
		super("graph");
		
		mHeight = height;
		mColor[0] = mColor[1] = mColor[2] = 0.f;
		
		mProgram = (NDYProgram)NDYRessource.getRessource(programName);
		if( mProgram == null ) {
			mProgram = new NDYProgramBasic(programName);
			NDYRessource.addRessource(mProgram);
		}

		mMesh = new NDYMesh("mesh/graph");
		mSubmesh = new NDYSubMesh();
		mSubmesh.name = "points";
		mSubmesh.setDesc(NDYSubMesh.VERTEX_DESC_POSITION_COLOR);
		mSubmesh.drawMode = GLES20.GL_POINTS;
		
		mMesh.submeshes.put(mSubmesh.name, mSubmesh);
		
		mData = new float[width];
		for(int i=0;i<mData.length;i++) mData[i] = 0.f;
	}
	
	private void buildVertices() {
		float [] v = new float[mData.length*7];
		for(int i=0;i<mData.length; i++) {
			v[i*7] = i;
			v[i*7+1] = mData[i]*(float)mHeight/mMaxVal;
			v[i*7+2] = 0.f;
			v[i*7+3] = mColor[0];
			v[i*7+4] = mColor[1];
			v[i*7+5] = mColor[2];
			v[i*7+6] = 1.f;
		}
		
		mSubmesh.setVertices(v);
	}
	
	public boolean processMessage(NDYMessage msg) {
		NDYActor r = mParent;
		NDYWorld w = NDYWorld.current;
		
		if( msg.getClass() == NDYMessageUpdate.class ) {
			buildVertices();
			
			for(int i=0;i<mData.length-1; i++) {
				mData[i] = mData[i+1];
			}
			mData[mData.length-1] = mNextVal;
			if( mNextVal > mMaxVal ) mMaxVal = mNextVal;
						
			mNextVal = 0.f;
		}
		
		if( msg.getClass() == NDYMessageRender.class ) {
			NDYComponentTransformation trans = (NDYComponentTransformation)r.findComponent("transformation");
	        w.setCamera(w.getCameraOrtho());

			NDYProgramBasic p = (NDYProgramBasic)mProgram;
						
			GLES20.glUseProgram(p.getId());
	        NDYGLSurfaceView.checkGLError("glUseProgram");
	        
	        p.setWorldAttribs();
	        	        			
			Matrix.setIdentityM(mMatrix, 0);
			Vector3 pos = trans.getPos();
			Matrix.translateM(mMatrix, 0, pos.x, pos.y, pos.z);
			Vector3 scale = trans.getScale();
			Matrix.scaleM(mMatrix, 0, scale.x, scale.y, 0);
			
			GLES20.glUniformMatrix4fv(p.mWorldMatrixHandle, 1, false, mMatrix, 0);
	        NDYGLSurfaceView.checkGLError("glUniformMatrix4fv world matrix");
	        
	        mSubmesh.vbuffer.position(mSubmesh.posOffset);
	        GLES20.glVertexAttribPointer(p.mPositionHandle, 3, GLES20.GL_FLOAT, false, mSubmesh.vsize, mSubmesh.vbuffer);
	        NDYGLSurfaceView.checkGLError("glVertexAttribPointer maPosition");
	        
	        GLES20.glEnableVertexAttribArray(mProgram.mPositionHandle);
	        NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray maPositionHandle");
	        
	        mSubmesh.vbuffer.position(mSubmesh.colorOffset);
        	GLES20.glVertexAttribPointer(p.mColorHandle, 4, GLES20.GL_FLOAT, false, mSubmesh.vsize, mSubmesh.vbuffer);
        	NDYGLSurfaceView.checkGLError("glVertexAttribPointer maColorHandle");
        	
        	GLES20.glEnableVertexAttribArray(p.mColorHandle);
        	NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray maColorHandle");
        	
        	GLES20.glDrawArrays(mSubmesh.drawMode, 0, mSubmesh.vbuffer.capacity() * NDYMesh.FLOAT_SIZE_BYTES / mSubmesh.vsize);
        	NDYGLSurfaceView.checkGLError("glDrawArrays");
        	
        	w.setCamera(w.getCameraPerspective());
		}
		
		return false;
	}
	
	public void setNextVal(float v) {
		mNextVal = v;
	}
	
	public void setColor(float r, float g, float b) {
		mColor[0] = r;
		mColor[1] = g;
		mColor[2] = b;
	}
}
