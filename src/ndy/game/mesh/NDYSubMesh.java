package ndy.game.mesh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import ndy.game.material.NDYMaterial;
import android.opengl.GLES20;

public class NDYSubMesh {
	private static String TAG = "SubMesh";
	
	public static int VERTEX_DESC_POSITION = 1;
	public static int VERTEX_DESC_POSITION_NORMAL = 2;
	public static int VERTEX_DESC_POSITION_TEXCOORDS = 3;
	public static int VERTEX_DESC_POSITION_NORMAL_TEXCOORDS = 4;
	
	public FloatBuffer vbuffer = null;
	public ShortBuffer ibuffer = null;
	
	public int vsize;
	public int posOffset = 0;
	public int normalOffset = 3;
	public int texcoordsOffset;
		
	public NDYMaterial material;

	public int numIndices;
	
	public String name = new String();
	public String animation = new String();
	
	public int drawMode = GLES20.GL_TRIANGLES;
	
	private int mDesc;
	
	public void setDesc(int desc) {
		mDesc = desc;
		if( desc == VERTEX_DESC_POSITION ) {
			vsize = 3 * NDYMesh.FLOAT_SIZE_BYTES;
		} else if( desc == VERTEX_DESC_POSITION_NORMAL ) {
			vsize = 6 * NDYMesh.FLOAT_SIZE_BYTES;
		} else if( desc == VERTEX_DESC_POSITION_TEXCOORDS ) {
			vsize = 5 * NDYMesh.FLOAT_SIZE_BYTES;
			texcoordsOffset = 3;
		} else if( desc == VERTEX_DESC_POSITION_NORMAL_TEXCOORDS ) {
			vsize = 8 * NDYMesh.FLOAT_SIZE_BYTES;
			texcoordsOffset = 6;
		}
	}
	
	public int getDesc() {
		return mDesc;
	}
	
	protected void setVertices(float [] vertices) {
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * NDYMesh.FLOAT_SIZE_BYTES);
		vbb.order(ByteOrder.nativeOrder());
		vbuffer = vbb.asFloatBuffer();
		vbuffer.put(vertices);
		vbuffer.position(0);
	}
	
	protected void setIndices(short [] indices) {
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * NDYMesh.SHORT_SIZE_BYTES);
		ibb.order(ByteOrder.nativeOrder());
		ibuffer = ibb.asShortBuffer();
		ibuffer.put(indices);
		ibuffer.position(0);
		
		numIndices = indices.length;
	}
}
