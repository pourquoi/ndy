package ndy.game;

import ndy.game.actor.NDYTransformable;
import ndy.game.component.NDYComponentMeshSailboat;
import ndy.game.math.Vector3;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;


public class NDYGLSurfaceView extends GLSurfaceView {
	private static String TAG = "NDYGLSurfaceView";
	private float lastTouchX, lastTouchY;
	private NDYUI mUI = new NDYUI();

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
					mUI.setFocus(NDYUI.FOCUS_NONE);
					break;
				case MotionEvent.ACTION_DOWN:
					lastTouchX = event.getX();
					lastTouchY = event.getY();
					Log.d(TAG, "down ("+lastTouchX+","+lastTouchY+")");
					
					if( lastTouchY > height - height/8 ) {
						mUI.setFocus(NDYUI.FOCUS_RUDDER);
					} else {
						mUI.setFocus(NDYUI.FOCUS_MAINSAIL);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					float dx = event.getX() - lastTouchX;
					float dy = event.getY() - lastTouchY;
					lastTouchX = event.getX();
					lastTouchY = event.getY();
					Log.d(TAG, "move ("+dx+","+dy+")");
					NDYTransformable r = NDYWorld.current.getRacer();
					if( mUI.getFocus() == NDYUI.FOCUS_MAINSAIL ) {
						NDYComponentMeshSailboat b = (NDYComponentMeshSailboat)r.findComponent("mesh");
						if( b != null ) {
							b.setMainSailRot(b.getMainSailRot()+dx);
						}
					} else if( mUI.getFocus() == NDYUI.FOCUS_RUDDER ) {
						Vector3 rot = r.getRot();
						rot.y += dx;
					}
					
					NDYWorld.current.getCamera().getPos().y += dy;
					break;
				default:
				}
			}
		});
		return true;
	}
}
