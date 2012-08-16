package ndy.game;

import android.os.Bundle;

public class RaceOptions {
	public String lake;
	public String boat;
	
	public Bundle toBundle() {
		Bundle bundle = new Bundle();
		
		return bundle;
	}
	
	public static RaceOptions fromBundle(Bundle bundle) {
		RaceOptions options = new RaceOptions();
		
		return options;
	}
}
