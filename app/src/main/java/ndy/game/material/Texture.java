package ndy.game.material;

import java.io.IOException;
import java.io.InputStream;

import ndy.game.Game;
import ndy.game.Ressource;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class Texture extends Ressource {
	private static final String TAG = "NDYTexture";

	public static Texture factory(String name) {
		Texture t = (Texture)Ressource.getRessource(name);
		if( t == null ) {
			t = new Texture(name);
			Ressource.addRessource(t);
		}
		return t;
	}
	
	public Texture(String name) {
		super(name);
	}
	
	@Override
	public boolean load() {
		if( !super.load() ) return false;
		
		int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        mId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mId);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, 
        		GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        InputStream is = null;
        
		try {
			is = Game.instance.mContext.getAssets().open(mName);
		} catch (IOException e) {
			Log.e(TAG, "Could not open " + mName);
		} finally {
	        Bitmap bitmap;
	        try {
	            bitmap = BitmapFactory.decodeStream(is);
	        } finally {
	            try {
	                is.close();
	            } catch(IOException e) {
	            	Log.e(TAG, "Could not close " + mName);
	            }
	        }
	
	        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	        bitmap.recycle();
		}
        return true;
	}
}
