package ndy.game.shader;

import ndy.game.NDYCamera;
import ndy.game.NDYGLSurfaceView;
import ndy.game.NDYRessource;
import ndy.game.NDYWorld;
import android.opengl.GLES20;

public class NDYProgramBasic extends NDYProgram {
	private static String TAG = "NDYProgramBasic";
	
	public static int MAX_TEXTURES = 4;
	
	public int mTimeHandle;
	
	public int mTextureHandle;
	public int mNormalHandle;
	public int mColorHandle;
	
	public int [] mSamplerHandles;

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
        
        mSamplerHandles = new int[MAX_TEXTURES];
        
        for(int i=0;i<MAX_TEXTURES;i++) {
        	mSamplerHandles[i] = GLES20.glGetAttribLocation(mId, "sTexture"+i);
        }
        
        mColorHandle = GLES20.glGetAttribLocation(mId, "aColor");
        
        mTextureHandle = GLES20.glGetAttribLocation(mId, "aTextureCoord");
        
        mNormalHandle = GLES20.glGetAttribLocation(mId, "aNormal");
        
        mTimeHandle = GLES20.glGetUniformLocation(mId, "uTime");
        
        mLightDirHandle = GLES20.glGetUniformLocation(mId, "uLightDir");
        
        mEyePosHandle = GLES20.glGetUniformLocation(mId, "uEyePos");
        
        mShininessHandle = GLES20.glGetUniformLocation(mId, "uShininess");
        
        mDiffuseHandle = GLES20.glGetUniformLocation(mId, "uDiffuse");
    
        mAmbientHandle = GLES20.glGetUniformLocation(mId, "uAmbient");
   
        mSpecularHandle = GLES20.glGetUniformLocation(mId, "uSpecular");
	}
	
	public void setWorldAttribs() {
		 NDYWorld w = NDYWorld.current;
		 
		 if( mTimeHandle != -1 ) {
			float t = w.getTime() / 1000.f;
	        GLES20.glUniform1f(mTimeHandle, t);
	        NDYGLSurfaceView.checkGLError("glUniform1f time");
        }

        NDYCamera c = w.getCamera();
        
        GLES20.glUniformMatrix4fv(mViewMatrixHandle, 1, false, c.getViewMatrix(), 0);
        NDYGLSurfaceView.checkGLError("glUniformMatrix4fv view matrix");

        GLES20.glUniformMatrix4fv(mProjectionHandle, 1, false, c.getProjectionMatrix(), 0);
        NDYGLSurfaceView.checkGLError("glUniformMatrix4fv projection matrix");
        
        if( mEyePosHandle != -1 ) {
	        GLES20.glUniform3f(mEyePosHandle, c.getPos().x, c.getPos().y, c.getPos().z);
	        NDYGLSurfaceView.checkGLError("glUniform3f eyepos");
        }

        if( mLightDirHandle != -1 ) {
        	GLES20.glUniform3fv(mLightDirHandle, 1, w.getLightDir(), 0);
        	NDYGLSurfaceView.checkGLError("glUniform3f lightdir");
        }
	}
}
