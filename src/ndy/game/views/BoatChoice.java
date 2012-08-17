package ndy.game.views;

import ndy.game.R;
import ndy.game.RaceOptions;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class BoatChoice extends Activity {
	private RaceOptions options;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.boatchoice);
		
		options = RaceOptions.fromBundle(getIntent().getBundleExtra("options"));
	}
	
	public void select(View view) {
		Intent i = new Intent();
		i.putExtra("options", options.toBundle());
		i.setAction(Intent.ACTION_MAIN);
		i.setClass(getApplicationContext(), WeatherChoice.class);
		this.startActivity(i);
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
