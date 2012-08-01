package ndy.game.collision;

import java.io.IOException;
import java.io.InputStream;

import ndy.game.actor.NDYGame;
import ndy.game.math.NDYMath;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class NDYColliderMap extends NDYCollider {
	public int[] collisionMap;
	public int collisionMapWidth;
	public int collisionMapHeight;
	public float width;
	public float height;
	public String mName;

	public NDYColliderMap(String name, float width, float height) {
		super();
		
		this.structure = NDYCollider.STRUCT_MAP;
		
		this.width = width;
		this.height = height;

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

			collisionMap = new int[bitmap.getWidth() * bitmap.getHeight()];
			int i = 0;
			collisionMapWidth = bitmap.getWidth();
			collisionMapHeight = bitmap.getHeight();
			for (int u = 0; u < collisionMapWidth; u++) {
				for (int v = 0; v < collisionMapHeight; v++) {
					int c = Color.blue(bitmap.getPixel(u, v));
					collisionMap[i++] = c;
				}
			}

			bitmap.recycle();
		}
	} 
	
	public float getSlow(int u, int v) {
		int mapindex = u * collisionMapHeight + v;
		if( mapindex > collisionMap.length || mapindex < 0 ) return 0.f;
		int c = collisionMap[mapindex];
		return (float)c/255.f;
	}
}
