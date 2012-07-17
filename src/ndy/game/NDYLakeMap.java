package ndy.game;

import java.io.IOException;
import java.io.InputStream;

import ndy.game.actor.NDYGame;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class NDYLakeMap {
	public static final byte COLLISION_SLOW = 1;
	public static final byte COLLISION_STOP = 2;
	public byte[] collisionMap;
	public int collisionMapWidth;
	public int collisionMapHeight;
	public String mName;

	public NDYLakeMap(String name) {
		InputStream is = null;

		try {
			is = NDYGame.instance.mContext.getAssets().open(name);
		} catch (IOException e) {
		} finally {
			Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(is);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
				}
			}

			collisionMap = new byte[bitmap.getWidth() * bitmap.getHeight()];
			int i = 0;
			collisionMapWidth = bitmap.getWidth();
			collisionMapHeight = bitmap.getHeight();
			for (int u = 0; u < collisionMapWidth; u++) {
				for (int v = 0; v < collisionMapHeight; v++) {
					int c = Color.blue(bitmap.getPixel(u, v));
					if( c < 127 && c > 10 ) collisionMap[i++] = COLLISION_SLOW;
					else if( c <= 10 ) collisionMap[i++] = COLLISION_STOP;
					else collisionMap[i++] = 0;
				}
			}

			bitmap.recycle();
		}
	}
}
