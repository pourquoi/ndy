package ndy.game;

import ndy.game.actor.NDYGame;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.view.Window;

public class NdyActivity extends Activity {
	private NDYGLSurfaceView mView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (info.reqGlEsVersion >= 0x20000) {
			NDYGame.load(this);

			mView = new NDYGLSurfaceView(this);
			setContentView(mView);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mView != null) {
			mView.onPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mView != null) {
			mView.onResume();
		}
	}
}