package ndy.game.views;

import ndy.game.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class LakeChoice extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.lakechoice);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}