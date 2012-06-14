package ndy.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;

public class NDYRenderer implements GLSurfaceView.Renderer {
	private long mAccumulator = 0;
	private long mCurrentTime = 0;
	private long mTimeStep = 10;
	
	public NDYRenderer() {
		mCurrentTime = SystemClock.uptimeMillis();
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.d("NDYRenderer", "surface created");
		//GLES20.glDisable(GLES20.GL_CULL_FACE);
		
//		GLES20.glEnable(GLES20.GL_CULL_FACE);
//		GLES20.glFrontFace(GLES20.GL_CCW);
//		GLES20.glCullFace(GLES20.GL_BACK);
		
		GLES20.glClearDepthf(1.f);
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);

		// [TODO] don't unload on first creation
		NDYRessource.unloadRessources();
		NDYRessource.loadRessources();
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		GLES20.glViewport(0, 0, w, h);
		NDYWorld.current.getCamera().resize(w, h);
		NDYWorld.current.getCameraOrtho().resize(w, h);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		//NDYRessource.loadRessources();

		long newTime = SystemClock.uptimeMillis();
		long frameTime = newTime - mCurrentTime;
		
		if( frameTime > mTimeStep*5 ) frameTime = mTimeStep*5;
		
		mCurrentTime = newTime;
		mAccumulator += frameTime;

		while( mAccumulator >= mTimeStep ) {
			NDYWorld.current.update(mTimeStep);
			mAccumulator -= mTimeStep;
		}
		
		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        NDYWorld.current.render();
	}
}
