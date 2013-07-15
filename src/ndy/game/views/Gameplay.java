package ndy.game.views;

import ndy.game.Game;
import ndy.game.R;
import ndy.game.RaceOptions;
import ndy.state.RunningState;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class Gameplay extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.gameplay);

		RaceOptions options = RaceOptions.fromBundle(getIntent().getBundleExtra("options"));

		Game.instance = new Game(options);
		Game.instance.load();
		Game.instance.state = new RunningState();
		Game.instance.mContext = this;
		
		Button b = (Button)findViewById(R.id.button1);
	}

	public void onPause() {
		super.onPause();
		GLSurfaceView glview = (GLSurfaceView) findViewById(R.id.nDYGLSurfaceView1);
		glview.onPause();
	}

	public void onResume() {
		super.onResume();
		GLSurfaceView glview = (GLSurfaceView) findViewById(R.id.nDYGLSurfaceView1);
		glview.onResume();
	}
	
	public void genWeather(View view) {
		Game.instance.world.water.genWaterFeatures();
		
		EditText et = (EditText)findViewById(R.id.editText1);
		et.getText().clear();
		et.getText().append(String.valueOf(Game.instance.world.water.wavelength));
	}
}
