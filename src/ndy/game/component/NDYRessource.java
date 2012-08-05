package ndy.game.component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;

import ndy.game.NDYGame;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class NDYRessource {
	private static String TAG = "GLRessource";
	private static int mCounter = 0;
	protected int mId = -1;
	protected String mName;
	protected long mLastAccess;
	protected static HashMap<String, NDYRessource> ressourcePool = new HashMap<String, NDYRessource>();

	public NDYRessource() {
		mName = "unknown_res_" + (mCounter++);
	}

	public NDYRessource(String name) {
		mName = name;
	}

	public static NDYRessource getRessource(String name) {
		NDYRessource r = ressourcePool.get(name);
		if (r != null) {
			r.mLastAccess = NDYGame.instance.mTime;
		}
		return r;
	}

	public static void addRessource(NDYRessource ressource) {
		Log.i(TAG, "adding ressource " + ressource.toString());
		ressourcePool.put(ressource.mName, ressource);
	}

	public static void loadRessources() {
		Collection<NDYRessource> c = ressourcePool.values();
		for (NDYRessource r : c) {
			r.load();
		}
	}

	public static void unloadRessources() {
		Collection<NDYRessource> c = ressourcePool.values();
		for (NDYRessource r : c) {
			r.unload();
		}
	}

	public static String readTextFile(Context context, String filename) {
		String text = new String();
		AssetManager am = context.getAssets();
		try {
			InputStream is = am.open(filename);
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				text += line + "\n";
			}
			br.close();
			is.close();
		} catch (Exception e) {
			throw new RuntimeException("Error reading asset text file: " + filename);
		}

		return text;
	}

	public boolean load() {
		if (this.mId >= 0)
			return false;
		Log.i(TAG, "loading ressource " + mName);

		return true;
	}

	public void unload() {
		this.mId = -1;
	}

	public int getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	@Override
	public String toString() {
		return mName;
	}
}
