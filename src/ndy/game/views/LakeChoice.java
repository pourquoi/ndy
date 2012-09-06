package ndy.game.views;

import ndy.game.R;
import ndy.game.RaceOptions;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class LakeChoice extends Activity {
	private static final int LAKES_ARRAY_STRIDE = 3;
	private RaceOptions options;
	private TypedArray _lakes;
	private int _lakeCount;
	private int _selectedLake;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.lakechoice);

		options = new RaceOptions();

		_lakes = this.getResources().obtainTypedArray(R.array.lakes);
		_lakeCount = _lakes.length() / LAKES_ARRAY_STRIDE;
		_selectedLake = 0;

		showSelectedLake();
	}

	public void showSelectedLake() {
		ImageView picture = (ImageView) this.findViewById(R.id.lake_picture);
		TextView name = (TextView) this.findViewById(R.id.lake_name);
		name.setText(_lakes.getString(_selectedLake * LAKES_ARRAY_STRIDE));
		picture.setImageDrawable(_lakes.getDrawable(_selectedLake * LAKES_ARRAY_STRIDE + 1));
		options.lake = _lakes.getString(_selectedLake * LAKES_ARRAY_STRIDE + 2);
	}

	public void select(View view) {
		Intent i = new Intent();
		i.putExtra("options", options.toBundle());
		i.setAction(Intent.ACTION_MAIN);
		i.setClass(getApplicationContext(), BoatChoice.class);
		this.startActivity(i);
	}

	public void next(View view) {
		_selectedLake++;
		if (_selectedLake >= _lakeCount)
			_selectedLake = 0;
		showSelectedLake();
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
