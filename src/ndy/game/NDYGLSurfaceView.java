package ndy.game;

import ndy.game.math.NDYMath;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;


public class NDYGLSurfaceView extends GLSurfaceView {
	private static String TAG = "NDYGLSurfaceView";
	private float lastTouchX, lastTouchY;
	private NDYInput mInput = new NDYInput();

	public NDYGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		setEGLConfigChooser(true);

		NDYRenderer renderer = new NDYRenderer();
		setRenderer(renderer);
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
		final int width = this.getWidth();
		final int height = this.getHeight();

		this.queueEvent(new Runnable() {
			@Override
			public void run() {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					lastTouchX = event.getX();
					lastTouchY = event.getY();
					Log.d(TAG, "up ("+lastTouchX+","+lastTouchY+")");

					mInput.up(lastTouchX, lastTouchY);
					break;
				case MotionEvent.ACTION_DOWN:
					lastTouchX = event.getX();
					lastTouchY = event.getY();
					Log.d(TAG, "down ("+lastTouchX+","+lastTouchY+")");
					
					mInput.down(lastTouchX, lastTouchY);
					break;
				case MotionEvent.ACTION_MOVE:
					float dx = NDYMath.clamp(event.getX() - lastTouchX, -3.f, 3.f);
					float dy = NDYMath.clamp(event.getY() - lastTouchY, -3.f, 3.f);
					lastTouchX = event.getX();
					lastTouchY = event.getY();
					//Log.d(TAG, "move ("+dx+","+dy+")");

					mInput.move(dx, dy);
					break;
				default:
				}
			}
		});
		return true;
	}
}
