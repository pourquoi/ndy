package ndy.game.views;

import ndy.game.Game;
import ndy.game.R;
import ndy.game.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class Options extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.options);
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
