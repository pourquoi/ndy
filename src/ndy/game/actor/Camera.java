package ndy.game.actor;

import ndy.game.component.Component;
import ndy.game.math.Vector3;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import android.opengl.Matrix;
import android.util.Log;

public class Camera extends Component {
	public static int MODE_ORTHO = 1;
	public static int MODE_PERSPECTIVE = 2;

	private static String TAG = "NDYCamera";
	public Vector3 mPos = new Vector3();
	public Vector3 mTarget = new Vector3();
	public float[] mViewMatrix = new float[16];
	public float[] mProjectionMatrix = new float[16];
	public int mWidth = 0, mHeight = 0;
	public int mMode = MODE_PERSPECTIVE;

	public Camera() {
		super("camera");
	}

	@Override
	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {
			if (mMode == MODE_PERSPECTIVE) {
				Matrix.setLookAtM(mViewMatrix, 0, mPos.x, mPos.y, mPos.z,
						mTarget.x, mTarget.y, mTarget.z, 0, 1, 0);
			} else if (mMode == MODE_ORTHO) {
				Matrix.setIdentityM(mViewMatrix, 0);
			}
		}

		return false;
	}

	public void resize(int w, int h) {
		Log.d(TAG, "surface changed (" + mWidth + "x" + mHeight + ") -> (" + w
				+ "x" + h + ")");
		mWidth = w;
		mHeight = h;
		if (mMode == MODE_PERSPECTIVE) {
			float ratio = (float) w / h;
			Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 1000);
		} else if (mMode == MODE_ORTHO) {
			Matrix.orthoM(mProjectionMatrix, 0, 0, w, 0, h, -1, 1);
		}
	}

	public void setPos(float x, float y, float z) {
		mPos.x = x;
		mPos.y = y;
		mPos.z = z;
	}

	public void setTarget(float x, float y, float z) {
		mTarget.x = x;
		mTarget.y = y;
		mTarget.z = z;
	}
}
