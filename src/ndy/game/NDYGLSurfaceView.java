package ndy.game;

import ndy.game.math.NDYMath;
import ndy.game.message.InputMessage;
import ndy.game.system.InputSystem;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class NDYGLSurfaceView extends GLSurfaceView {
	private static String TAG = "NDYGLSurfaceView";
	private float lastTouchX, lastTouchY;
	private long mLastMoveTime = 0;
	private ndy.game.Renderer mRenderer;

	public NDYGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!this.isInEditMode()) {
			setEGLContextClientVersion(2);
			setEGLConfigChooser(true);

			mRenderer = new ndy.game.Renderer();
			setRenderer(mRenderer);
		}
	}

	public static void checkGLError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		final int action = event.getAction();
		long t = SystemClock.uptimeMillis();

		if (action == MotionEvent.ACTION_MOVE) {
			if (t - mLastMoveTime < 16) {
				return true;
			} else {
				mLastMoveTime = t;
			}
		}

		if (action == MotionEvent.ACTION_DOWN) {
			Log.d(TAG, "FPS: " + mRenderer.mFPS);
		}

		this.queueEvent(new Runnable() {
			@Override
			public void run() {
				InputSystem inputSystem = (InputSystem)Game.instance.systems.get(InputSystem.name);
				switch (action) {
				case MotionEvent.ACTION_UP:
					lastTouchX = event.getX();
					lastTouchY = event.getY();

					break;
				case MotionEvent.ACTION_DOWN:
					lastTouchX = event.getX();
					lastTouchY = event.getY();

					break;
				case MotionEvent.ACTION_MOVE:
					float dx = NDYMath.clamp(event.getX() - lastTouchX, -3.f, 3.f);
					float dy = NDYMath.clamp(event.getY() - lastTouchY, -3.f, 3.f);
					lastTouchX = event.getX();
					lastTouchY = event.getY();

					InputMessage msg = new InputMessage();
					msg.action = InputMessage.RUDDER;
					msg.dx = dx;
					inputSystem.inputBuffer.add(msg);
					
					msg = new InputMessage();
					msg.action = InputMessage.CAMERA;
					msg.dy = dy;
					inputSystem.inputBuffer.add(msg);
					
					break;
				default:
				}
			}
		});

		return true;
	}
}
