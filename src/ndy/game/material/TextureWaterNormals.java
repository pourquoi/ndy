package ndy.game.material;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ndy.game.NDYGLSurfaceView;
import ndy.game.Ressource;
import ndy.game.math.NDYMath;
import ndy.game.math.Vector3;
import android.opengl.GLES20;

public class TextureWaterNormals extends Ressource {
	public WaterOptions mWater;
	public int mWaveNb;

	public TextureWaterNormals(String name, WaterOptions water, int waveNb) {
		super(name);
		mWater = water;
		mWaveNb = waveNb;
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
                GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT);
        
        int texw = 256, texh = 256;
        
        ByteBuffer texbuf = ByteBuffer.allocateDirect(texw*texh*4).order(ByteOrder.nativeOrder());
        byte [] col = new byte[4];
        
        Vector3 N = new Vector3();
        
        for(int i=0;i<texw;i++) {
        	for(int j=0;j<texh;j++) {
        		mWater.getWaveNormal(i * mWater.wavelength[mWaveNb] / (texw-1), j * mWater.wavelength[mWaveNb] / (texh-1), N, mWaveNb);
        		col[0] = (byte)(255*N.x);
        		col[1] = (byte)(255*N.y);
        		col[2] = (byte)(255*N.z);
        		col[3] = (byte)255;
        		texbuf.put(col);
        	}
        }

        texbuf.position(0);

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, texw, texh, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, texbuf);
        NDYGLSurfaceView.checkGLError("glTexImage2D");

        return true;
	}
}
