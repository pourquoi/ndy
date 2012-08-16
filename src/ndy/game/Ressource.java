package ndy.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class Ressource {
	private static String TAG = "GLRessource";
	private static int mCounter = 0;
	protected int mId = -1;
	protected String mName;
	protected long mLastAccess;
	protected static HashMap<String, Ressource> ressourcePool = new HashMap<String, Ressource>();

	public Ressource() {
		mName = "unknown_res_" + (mCounter++);
	}

	public Ressource(String name) {
		mName = name;
	}

	public static Ressource getRessource(String name) {
		Ressource r = ressourcePool.get(name);
		if (r != null) {
			r.mLastAccess = Game.instance.time;
		}
		return r;
	}

	public static void addRessource(Ressource ressource) {
		Log.i(TAG, "adding ressource " + ressource.toString());
		ressourcePool.put(ressource.mName, ressource);
	}

	public static void loadRessources() {
		Collection<Ressource> c = ressourcePool.values();
		for (Ressource r : c) {
			r.load();
		}
	}

	public static void unloadRessources() {
		Collection<Ressource> c = ressourcePool.values();
		for (Ressource r : c) {
			r.unload();
		}
	}
	
	public static Element readXMLFile(Context context, String filename) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document document = null;

		try {
			AssetManager am = Game.instance.mContext.getAssets();
			document = builder.parse(am.open(filename));
		} catch (SAXException e) {
			throw new RuntimeException("Error parsing actor xml " + filename + ": " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("Error reading actor xml: " + filename + ": " + e.getMessage());
		}

		return document.getDocumentElement();
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
