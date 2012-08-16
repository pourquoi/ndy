package ndy.game.views;

import ndy.game.Game;
import ndy.game.R;
import ndy.game.RaceOptions;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class Gameplay extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.gameplay);
		
		RaceOptions options = RaceOptions.fromBundle(savedInstanceState.getBundle("options"));
		
		Game.instance = new Game(options);
	}
	
	public void onPause() {
		super.onPause();
		GLSurfaceView glview = (GLSurfaceView)findViewById(R.id.nDYGLSurfaceView1);
		glview.onPause();
	}
	
	public void onResume() {
		super.onResume();
		GLSurfaceView glview = (GLSurfaceView)findViewById(R.id.nDYGLSurfaceView1);
		glview.onResume();
	}
}
