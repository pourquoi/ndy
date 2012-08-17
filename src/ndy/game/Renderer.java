package ndy.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ndy.game.component.CameraComponent;
import ndy.game.component.Component;
import ndy.game.message.RenderMessage;
import ndy.game.message.UpdateMessage;
import ndy.game.system.CameraSystem;
import ndy.game.system.InputSystem;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;

public class Renderer implements GLSurfaceView.Renderer {
	private static String TAG = "NDYRenderer";

	private long mAccumulator = 0;
	private long mCurrentTime = 0;
	private long mTimeStep = 10;

	public float mFPS = 0.f;

	public Renderer() {
		mCurrentTime = SystemClock.uptimeMillis();
	}

	static {
		System.loadLibrary("fix-GLES20");
	}

	native public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset);

	native public static void glDrawElements(int mode, int count, int type, int offset);

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.d("NDYRenderer", "surface created");
		// GLES20.glDisable(GLES20.GL_CULL_FACE);

		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glFrontFace(GLES20.GL_CW);
		GLES20.glCullFace(GLES20.GL_BACK);

		GLES20.glClearDepthf(1.f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);

		// [TODO] don't unload on first creation
		Ressource.unloadRessources();
		Ressource.loadRessources();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		GLES20.glViewport(0, 0, w, h);

		if (Game.instance != null) {
			CameraSystem cameraSystem = (CameraSystem) Game.instance.systems.get(CameraSystem.name);
			for (Component c : cameraSystem.components) {
				((CameraComponent) c).resize(w, h);
			}
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		long newTime = SystemClock.uptimeMillis();
		long frameTime = newTime - mCurrentTime;

		if (Game.instance != null) {
			InputSystem inputSystem = (InputSystem) Game.instance.systems.get(InputSystem.name);
			inputSystem.dispatchInputs();
		}

		mFPS = 1000.f / (float) frameTime;
		if (frameTime > mTimeStep * 5)
			frameTime = mTimeStep * 5;

		mCurrentTime = newTime;
		mAccumulator += frameTime;

		while (mAccumulator >= mTimeStep) {
			if (Game.instance != null) {
				UpdateMessage mupdate = new UpdateMessage();
				mupdate.setInterval(mTimeStep);
				Game.instance.state.dispatchMessage(mupdate);
			}
			mAccumulator -= mTimeStep;
		}

		mCurrentTime = newTime;

		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

		if (Game.instance != null) {
			RenderMessage mrender = new RenderMessage();
			Game.instance.state.dispatchMessage(mrender);
		}
	}
}
