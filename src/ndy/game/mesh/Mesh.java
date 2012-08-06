package ndy.game.mesh;

import java.io.DataInputStream;
import java.io.EOFException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import ndy.game.NDYGLSurfaceView;
import ndy.game.Game;
import ndy.game.Ressource;
import ndy.game.math.Box;
import ndy.game.math.Vector3;
import android.opengl.GLES20;
import android.util.Log;

public class Mesh extends Ressource {
	private static String TAG = "Mesh";

	public static final int FLOAT_SIZE_BYTES = 4;
	public static final int SHORT_SIZE_BYTES = 2;

	public Hashtable<String, SubMesh> submeshes = new Hashtable<String, SubMesh>();
	public Hashtable<String, Animation> animations = new Hashtable<String, Animation>();

	public static Mesh factory(String name) {
		Mesh m = (Mesh) Ressource.getRessource(name);
		if (m == null) {
			if (name.contains("quad2d")) {
				m = Mesh.quad2d();
			} else if (name.contains("axis3d")) {
				m = Mesh.axis3d();
			} else {
				m = new Mesh(name);
				m.loadFile();
			}
			Ressource.addRessource(m);
		}
		return m;
	}

	public Mesh(String name) {
		super(name);
	}

	public void loadFile() {
		NDYMeshLoader loader = new NDYMeshLoader();
		try {
			loader.load(this);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public boolean load() {
		if (!super.load())
			return false;

		Iterator<Entry<String, SubMesh>> it = submeshes.entrySet().iterator();
		SubMesh sm = null;
		int[] buffers = new int[1];
		while (it.hasNext()) {
			sm = (SubMesh) it.next().getValue();
			GLES20.glGenBuffers(1, buffers, 0);
			NDYGLSurfaceView.checkGLError("glGenBuffers " + sm.name);
			
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
			NDYGLSurfaceView.checkGLError("glBindBuffer " + sm.name);
			
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, sm.vbuffer.capacity() * Mesh.FLOAT_SIZE_BYTES, sm.vbuffer, GLES20.GL_STATIC_DRAW);
			NDYGLSurfaceView.checkGLError("glBufferData " + sm.name);
			
			sm.vbo = buffers[0];
		}

		mId = 0;
		return true;
	}

	public static Mesh quad2d() {
		String name = "mesh/quad2d";
		Mesh mesh = (Mesh) Ressource.getRessource(name);
		if (mesh == null) {
			mesh = new Mesh(name);
			SubMesh submesh = new SubMesh();
			submesh.name = name;
			submesh.setDesc(SubMesh.VERTEX_DESC_POSITION_TEXCOORDS);

			float vertices[] = { 0.f, 0.f, 0.f, 0.f, 1.f, 1.f, 0.f, 0.f, 1.f, 1.f, 0.f, 1.f, 0.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 1.f, 1.f, 1.f, 0.f, 1.f, 0.f,
					0.f, 1.f, 0.f, 0.f, 0.f };
			submesh.setVertices(vertices);

			mesh.submeshes.put(submesh.name, submesh);
			Ressource.addRessource(mesh);
		}
		return mesh;
	}

	public static Mesh axis3d() {
		String name = "mesh/axis3d";
		Mesh mesh = (Mesh) Ressource.getRessource(name);
		if (mesh == null) {
			mesh = new Mesh(name);
			SubMesh submesh = new SubMesh();
			submesh.name = "axis3d";
			submesh.setDesc(SubMesh.VERTEX_DESC_POSITION_COLOR);
			submesh.drawMode = GLES20.GL_LINES;

			float vertices[] = { 0.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 1.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 0.f, 0.f, 0.f, 0.f, 1.f, 0.f, 1.f, 0.f, 1.f, 0.f, 0.f,
					1.f, 0.f, 1.f, 0.f, 0.f, 0.f, 0.f, 0.f, 1.f, 1.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 1.f };

			submesh.setVertices(vertices);

			mesh.submeshes.put(submesh.name, submesh);
			Ressource.addRessource(mesh);
		}

		return mesh;
	}

	// TODO make it work for odd chunk numbers
	public static Mesh plan(int cx, int cz) {
		String name = "mesh/plan_" + cx + "x" + cz;
		Mesh mesh = (Mesh) Ressource.getRessource(name);
		if (mesh == null) {
			mesh = new Mesh(name);
			SubMesh submesh = new SubMesh();
			submesh.name = name;
			submesh.setDesc(SubMesh.VERTEX_DESC_POSITION_TEXCOORDS);
			submesh.drawMode = GLES20.GL_TRIANGLE_STRIP;

			int oz = 0;
			int ox = 0;
			float dx = 1.f / (float) cx;
			float dz = 1.f / (float) cz;
			float x = 0.f;
			float z = 0.f;

			float[] v = new float[5 * (cx * cz * 2 + cx + 1)];
			int vi = 0;
			// (0,0,0)
			v[vi++] = 0.f;
			v[vi++] = 0.f;
			v[vi++] = 0.f;
			v[vi++] = 0.f;
			v[vi++] = 1.f;

			for (int i = 0; i < cx; i++) {
				ox = i % 2;
				x = (float) i / (float) cx;
				if (ox == 0) {
					// (x+dx,0,0)
					v[vi++] = x + dx;
					v[vi++] = 0.f;
					v[vi++] = 0.f;

					v[vi++] = 1.f;
					v[vi++] = 1.f;
				} else {
					// (x+dx,0,1)
					v[vi++] = x + dx;
					v[vi++] = 0.f;
					v[vi++] = 1.f;

					v[vi++] = 0.f;
					v[vi++] = 1.f;
				}

				for (int j = 0; j < cz; j++) {
					oz = j % 2;
					if (ox == 0) {
						z = (float) j / (float) cz;
						// (x,0,z+dz)
						v[vi++] = x;
						v[vi++] = 0.f;
						v[vi++] = z + dz;
						if (oz == 0) {
							v[vi++] = 0.f;
							v[vi++] = 0.f;
						} else {
							v[vi++] = 0.f;
							v[vi++] = 1.f;
						}

						// (x+dx,0,z+dz)
						v[vi++] = x + dx;
						v[vi++] = 0.f;
						v[vi++] = z + dz;
						if (oz == 0) {
							v[vi++] = 1.f;
							v[vi++] = 0.f;
						} else {
							v[vi++] = 1.f;
							v[vi++] = 1.f;
						}
					} else {
						z = (float) (cz - j - 1) / (float) cz;
						// (x,0,z)
						v[vi++] = x;
						v[vi++] = 0.f;
						v[vi++] = z;
						if (oz == 0) {
							v[vi++] = 1.f;
							v[vi++] = 0.f;
						} else {
							v[vi++] = 1.f;
							v[vi++] = 1.f;
						}

						// (x+dx,0,z)
						v[vi++] = x + dx;
						v[vi++] = 0.f;
						v[vi++] = z;

						if (oz == 0) {
							v[vi++] = 0.f;
							v[vi++] = 0.f;
						} else {
							v[vi++] = 0.f;
							v[vi++] = 1.f;
						}
					}
				}
			}
			submesh.setVertices(v);
			mesh.submeshes.put(submesh.name, submesh);

			Ressource.addRessource(mesh);
		}
		return mesh;
	}
}

class NDYMeshLoader {
	private static String TAG = "NDYMeshLoader";

	private String name = new String();
	private ArrayList<LoaderFace> faces = new ArrayList<LoaderFace>();
	private ArrayList<LoaderVertex> vertices = new ArrayList<LoaderVertex>();
	Vector3 min = null;
	Vector3 max = null;
	private boolean hasTexCoords = false;

	public void load(Mesh mesh) throws Exception {
		DataInputStream in = new DataInputStream(Game.instance.mContext.getAssets().open(mesh.toString()));

		Vector3 n = new Vector3();

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
					if (!vertices.isEmpty()) {
						addSubMesh(mesh);
					}
					hasTexCoords = false;
					faces.clear();
					vertices.clear();
					name = new String();
					min = null;
					max = null;
					byte c;
					while ((c = in.readByte()) != 0) {
						name += (char) c;
					}

					Log.d(TAG, "name:" + name);
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
						if (min == null || max == null) {
							min = new Vector3(v.x, v.y, v.z);
							max = new Vector3(v.x, v.y, v.z);
						} else {
							if (v.x < min.x)
								min.x = v.x;
							if (v.x > max.x)
								max.x = v.x;
							if (v.y < min.y)
								min.y = v.y;
							if (v.y > max.y)
								max.y = v.y;
							if (v.z < min.z)
								min.z = v.z;
							if (v.z > max.z)
								max.z = v.z;
						}

						vertices.add(v);
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
						LoaderVertex va = vertices.get(f.a);
						LoaderVertex vb = vertices.get(f.b);
						LoaderVertex vc = vertices.get(f.c);
						va.faces.add(f);
						vb.faces.add(f);
						vc.faces.add(f);

						// compute face normal
						Vector3 ab = new Vector3(vb.x - va.x, vb.y - va.y, vb.z - va.z);
						Vector3 bc = new Vector3(vc.x - vb.x, vc.y - vb.y, vc.z - vb.z);

						Vector3.crossProduct(bc, ab, n);
						n.normalize();
						f.nx = n.x;
						f.ny = n.y;
						f.nz = n.z;

						faces.add(f);
					}
					break;
				case 0x4140: // tex coords
					hasTexCoords = true;
					short coordCount = Short.reverseBytes(in.readShort());
					Log.d(TAG, "coord count:" + coordCount);

					for (int i = 0; i < coordCount; i++) {
						LoaderVertex v = vertices.get(i);
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
						faces.get(i).smoothgroups = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
					}
					break;
				default:
					in.skipBytes(chunkLength - bytesRead);
				}
			}
		} catch (EOFException e) {
			if (!vertices.isEmpty()) {
				addSubMesh(mesh);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}

		try {
			in.close();
		} catch (Exception e) {

		}
	}

	private void addSubMesh(Mesh mesh) {
		SubMesh submesh = new SubMesh();
		submesh.name = name;

		submesh.bbox = new Box(min, max.substract(min));

		int vsize = 6;
		if (hasTexCoords) {
			vsize += 2;
			submesh.setDesc(SubMesh.VERTEX_DESC_POSITION_NORMAL_TEXCOORDS);
		} else {
			submesh.setDesc(SubMesh.VERTEX_DESC_POSITION_NORMAL);
		}
		float[] svdata = new float[faces.size() * 3 * vsize];
		int i = 0;
		for (LoaderFace f : faces) {
			short[] vindexes = { f.a, f.b, f.c };
			for (short vi : vindexes) {
				svdata[i++] = vertices.get(vi).x;
				svdata[i++] = vertices.get(vi).y;
				svdata[i++] = vertices.get(vi).z;
				svdata[i++] = f.nx;
				svdata[i++] = f.ny;
				svdata[i++] = f.nz;
				if (hasTexCoords) {
					svdata[i++] = vertices.get(vi).u;
					svdata[i++] = vertices.get(vi).v;
				}
			}
		}

		submesh.setVertices(svdata);
		mesh.submeshes.put(name, submesh);
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
