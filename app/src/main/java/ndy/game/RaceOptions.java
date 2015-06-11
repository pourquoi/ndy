package ndy.game;

import android.os.Bundle;

public class RaceOptions {
	public String lake;
	public String boat = "optimist.xml";
	
	public Bundle toBundle() {
		Bundle bundle = new Bundle();
		bundle.putString("lake", lake);
		bundle.putString("boat", boat);
		
		return bundle;
	}
	
	public static RaceOptions fromBundle(Bundle bundle) {
		RaceOptions options = new RaceOptions();
		
		if( bundle != null ) {
			options.lake = bundle.getString("lake");
			options.boat = bundle.getString("boat");
		}
		
		return options;
	}
}
