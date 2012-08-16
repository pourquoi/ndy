package ndy.game.mesh;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import ndy.game.NDYGLSurfaceView;
import ndy.game.Ressource;
import ndy.game.component.MeshComponent;
import ndy.game.shader.Program;
import ndy.game.shader.ProgramBasic;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import android.opengl.GLES20;

public class Mesh extends Ressource {
	private static String TAG = "Mesh";

	public static final int FLOAT_SIZE_BYTES = 4;
	public static final int SHORT_SIZE_BYTES = 2;

	public Hashtable<String, SubMesh> submeshes = new Hashtable<String, SubMesh>();

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
		LoaderMesh loader = new LoaderMesh(this);
		try {
			loader.load(this.getName());
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
	
	public static Mesh fromBody(String name, Body body) {
		Mesh mesh = (Mesh)Ressource.getRessource(name);
		if (mesh == null) {
			mesh = new Mesh(name);
			SubMesh submesh = new SubMesh();
			Fixture fixture = body.getFixtureList();
			PolygonShape shape = (PolygonShape) fixture.getShape();
			Vec2[] shapeVertices = shape.getVertices();
			float[] vertices = new float[shapeVertices.length * 3];
			int i = 0;
			for (Vec2 v : shapeVertices) {
				vertices[i++] = v.x;
				vertices[i++] = 0f;
				vertices[i++] = v.y;
			}
			submesh.setVertices(vertices);
			submesh.setDesc(SubMesh.VERTEX_DESC_POSITION);
			submesh.drawMode = GLES20.GL_LINE_LOOP;

			Ressource.addRessource(mesh);
		}
		
		return mesh;
	}
}
