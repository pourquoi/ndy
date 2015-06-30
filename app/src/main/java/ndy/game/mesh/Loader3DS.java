package ndy.game.mesh;

import java.io.DataInputStream;
import java.io.EOFException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import ndy.game.Game;
import ndy.game.math.Vec3;

import org.jbox2d.common.Vec2;

import android.util.Log;

class Loader3DS {
	private static String TAG = "NDYMeshLoader";

	protected String _name = new String();
	protected ArrayList<LoaderFace> _faces = new ArrayList<LoaderFace>();
	protected ArrayList<LoaderVertex> _vertices = new ArrayList<LoaderVertex>();
	protected Vec2 _min = null;
	protected Vec2 _max = null;
	protected boolean _hasTexCoords = false;
	
	public Loader3DS() {
		
	}

	public void load(String path) throws Exception {
		DataInputStream in = new DataInputStream(Game.instance.mContext.getAssets().open(path));

		Vec3 n = new Vec3();

		try {
			while (true) {
				int bytesRead = 0;
				short chunkId = Short.reverseBytes(in.readShort());
				byte[] buffer = new byte[4];
				in.read(buffer);
				int chunkLength = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
				Log.d(TAG, "chunk ID:" + Integer.toHexString(chunkId) + " chunk length:" + chunkLength);
				bytesRead = 6;
				switch (chunkId) {
				case 0x4d4d: // main
					break;
				case 0x3d3d:
					break;
				case 0x4000: // object
					if (!_vertices.isEmpty()) {
						processObject();
					}
					_hasTexCoords = false;
					_faces.clear();
					_vertices.clear();
					_name = new String();
					_min = null;
					_max = null;
					byte c;
					while ((c = in.readByte()) != 0) {
						_name += (char) c;
					}

					Log.d(TAG, "name:" + _name);
					break;
				case 0x4100: // object trimesh
					break;
				case 0x4110: // vertex list
					short vertexCount = Short.reverseBytes(in.readShort());
					Log.d(TAG, "vertex count: " + vertexCount);

					for (int i = 0; i < vertexCount; i++) {
						LoaderVertex v = new LoaderVertex();
						in.read(buffer);
						v.x = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
						in.read(buffer);
						v.z = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
						in.read(buffer);
						v.y = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
						if (_min == null || _max == null) {
							_min = new Vec2(v.x,v.z);
							_max = new Vec2(v.x,v.z);
						} else {
							if (v.x < _min.x)
								_min.x = v.x;
							if (v.x > _max.x)
								_max.x = v.x;
							if (v.z < _min.y)
								_min.y = v.z;
							if (v.z > _max.y)
								_max.y = v.z;
						}

						_vertices.add(v);
					}
					break;
				case 0x4120: // face list
					short faceCount = Short.reverseBytes(in.readShort());
					Log.d(TAG, "face count: " + faceCount);

					for (int i = 0; i < faceCount; i++) {
						LoaderFace f = new LoaderFace();
						f.a = Short.reverseBytes(in.readShort()); // vertex a
						f.b = Short.reverseBytes(in.readShort()); // vertex b
						f.c = Short.reverseBytes(in.readShort()); // vertex c
						// System.out.println(f.a+" "+f.b+" "+f.c);
						f.flags = in.readShort(); // face flags
						LoaderVertex va = _vertices.get(f.a);
						LoaderVertex vb = _vertices.get(f.b);
						LoaderVertex vc = _vertices.get(f.c);
						va.faces.add(f);
						vb.faces.add(f);
						vc.faces.add(f);

						// compute face normal
						Vec3 ab = new Vec3(vb.x - va.x, vb.y - va.y, vb.z - va.z);
						Vec3 bc = new Vec3(vc.x - vb.x, vc.y - vb.y, vc.z - vb.z);
						Vec3.crossToOut(bc, ab, n);
						n.normalize();
						f.nx = n.x;
						f.ny = n.y;
						f.nz = n.z;

						_faces.add(f);
					}
					break;
				case 0x4140: // tex coords
					_hasTexCoords = true;
					short coordCount = Short.reverseBytes(in.readShort());
					Log.d(TAG, "coord count:" + coordCount);

					for (int i = 0; i < coordCount; i++) {
						LoaderVertex v = _vertices.get(i);
						in.read(buffer);
						v.u = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
						in.read(buffer);
						v.v = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
						// System.out.println(u+" "+v);
					}
					break;
				case 0x4150: // smooth groups
					for (int i = 0; i < (chunkLength - 6); i++) {
						in.read(buffer);
						_faces.get(i).smoothgroups = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
					}
					break;
				default:
					in.skipBytes(chunkLength - bytesRead);
				}
			}
		} catch (EOFException e) {
			if (!_vertices.isEmpty()) {
				processObject();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			in.close();
		} catch (Exception e) {

		}

		finalize();
	}
	
	protected void processObject() {
		
	}

	protected void finalize() {

	}
}

class LoaderFace {
	public short a, b, c;
	public float nx, ny, nz;
	public int smoothgroups;
	public short flags;
}

class LoaderVertex {
	public float x, y, z;
	public float nx, ny, nz;
	public float u, v;
	public ArrayList<LoaderFace> faces = new ArrayList<LoaderFace>();
}