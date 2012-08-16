package ndy.game.actor;

import ndy.game.component.Component;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;

import org.jbox2d.common.Vec3;

import android.opengl.Matrix;
import android.util.Log;

public class Camera extends Component {
	public static int MODE_ORTHO = 1;
	public static int MODE_PERSPECTIVE = 2;

	private static String TAG = "NDYCamera";
	public Vec3 pos = new Vec3();
	public Vec3 target = new Vec3();
	public float[] mViewMatrix = new float[16];
	public float[] mProjectionMatrix = new float[16];
	public int width = 0, height = 0;
	public int mode = MODE_PERSPECTIVE;

	public Camera() {
		super("camera");
	}

	@Override
	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {
			if (mode == MODE_PERSPECTIVE) {
				Matrix.setLookAtM(mViewMatrix, 0, pos.x, pos.y, pos.z, target.x, target.y, target.z, 0, 1, 0);
			} else if (mode == MODE_ORTHO) {
				Matrix.setIdentityM(mViewMatrix, 0);
			}
		}

		return false;
	}

	public void resize(int w, int h) {
		Log.d(TAG, "surface changed (" + width + "x" + height + ") -> (" + w + "x" + h + ")");
		width = w;
		height = h;
		if (mode == MODE_PERSPECTIVE) {
			float ratio = (float) w / h;
			Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 1000);
		} else if (mode == MODE_ORTHO) {
			Matrix.orthoM(mProjectionMatrix, 0, 0, w, 0, h, -1, 1);
		}
	}
}
