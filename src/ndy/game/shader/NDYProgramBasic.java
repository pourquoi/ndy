package ndy.game.shader;

import ndy.game.NDYGLSurfaceView;
import ndy.game.NDYRessource;
import ndy.game.NDYWorld;
import android.opengl.GLES20;

public class NDYProgramBasic extends NDYProgram {
	private static String TAG = "NDYProgramBasic";
	
	public int mTimeHandle;
	
	public int mTextureHandle;
	public int mNormalHandle;

	public int mWorldMatrixHandle;
	public int mViewMatrixHandle;
	public int mProjectionHandle;
	
	public int mLightDirHandle;
	public int mEyePosHandle;
	
	public int mAmbientHandle;
	public int mDiffuseHandle;
	public int mSpecularHandle;
	public int mShininessHandle;

	public NDYProgramBasic(String name) {
		super(name);
	}

	@Override
	protected void initShaderSource() {
		mVertexSource = NDYRessource.readTextFile(NDYWorld.current.getContext(), mName+"_vs.glsl");
		mFragmentSource = NDYRessource.readTextFile(NDYWorld.current.getContext(), mName+"_ps.glsl");
	}
	
	@Override
	protected void bindAttribs() {
		super.bindAttribs();
				
        mTextureHandle = GLES20.glGetAttribLocation(mId, "aTextureCoord");
        NDYGLSurfaceView.checkGLError("glGetAttribLocation aTextureCoord");
        if( mTextureHandle == -1 ) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }
        
        mNormalHandle = GLES20.glGetAttribLocation(mId, "aNormal");
        NDYGLSurfaceView.checkGLError("glGetAttribLocation aNormal");
        if( mNormalHandle == -1 ) {
            throw new RuntimeException("Could not get attrib location for aNormal");
        }
        
        mTimeHandle = GLES20.glGetUniformLocation(mId, "uTime");
        NDYGLSurfaceView.checkGLError("glGetUniformLocation uTime");
        if( mTimeHandle == -1 ) {
            throw new RuntimeException("Could not get attrib location for uTime");
        }
        
        mWorldMatrixHandle = GLES20.glGetUniformLocation(mId, "uWorldMatrix");
        NDYGLSurfaceView.checkGLError("glGetUniformLocation uWorldMatrix");
        if( mWorldMatrixHandle == -1 ) {
            throw new RuntimeException("Could not get attrib location for uWorldMatrix");
        }

        mViewMatrixHandle = GLES20.glGetUniformLocation(mId, "uViewMatrix");
        NDYGLSurfaceView.checkGLError("glGetUniformLocation uViewMatrix");
        if( mViewMatrixHandle == -1 ) {
            throw new RuntimeException("Could not get attrib location for uViewMatrix");
        }
        
        mProjectionHandle = GLES20.glGetUniformLocation(mId, "uProjectionMatrix");
        NDYGLSurfaceView.checkGLError("glGetUniformLocation uProjectionMatrix");
        if( mProjectionHandle == -1 ) {
        	throw new RuntimeException("Could not get attrib location for uProjectionMatrix");
        }
        
        mLightDirHandle = GLES20.glGetUniformLocation(mId, "uLightDir");
        NDYGLSurfaceView.checkGLError("glGetUniformLocation uLightDir");
        if( mLightDirHandle == -1 ) {
        	throw new RuntimeException("Could not get attrib location for uLightDir");
        }
        
        mEyePosHandle = GLES20.glGetUniformLocation(mId, "uEyePos");
        NDYGLSurfaceView.checkGLError("glGetUniformLocation uEyePos");
        if( mProjectionHandle == -1 ) {
        	throw new RuntimeException("Could not get attrib location for uEyePos");
        }
        
        mShininessHandle = GLES20.glGetUniformLocation(mId, "uShininess");
        NDYGLSurfaceView.checkGLError("glGetUniformLocation uShininess");
        if( mShininessHandle == -1 ) {
        	throw new RuntimeException("Could not get attrib location for uShininess");
        }
        
        mDiffuseHandle = GLES20.glGetUniformLocation(mId, "uDiffuse");
        NDYGLSurfaceView.checkGLError("glGetUniformLocation uDiffuse");
        if( mDiffuseHandle == -1 ) {
        	throw new RuntimeException("Could not get attrib location for uDiffuse");
        }
        
        mAmbientHandle = GLES20.glGetUniformLocation(mId, "uAmbient");
        NDYGLSurfaceView.checkGLError("glGetUniformLocation uAmbient");
        if( mShininessHandle == -1 ) {
        	throw new RuntimeException("Could not get attrib location for uAmbient");
        }
        
        mSpecularHandle = GLES20.glGetUniformLocation(mId, "uSpecular");
        NDYGLSurfaceView.checkGLError("glGetUniformLocation uSpecular");
        if( mSpecularHandle == -1 ) {
        	throw new RuntimeException("Could not get attrib location for uSpecular");
        }
	}
}
