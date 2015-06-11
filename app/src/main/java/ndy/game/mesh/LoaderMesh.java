package ndy.game.mesh;

import org.jbox2d.collision.AABB;

public class LoaderMesh extends Loader3DS {
	protected Mesh mesh;
	
	public LoaderMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	protected void processObject() {
		SubMesh submesh = new SubMesh();
		submesh.name = _name;

		submesh.aabb = new AABB(_min, _max);

		int vsize = 6;
		if (_hasTexCoords) {
			vsize += 2;
			submesh.setDesc(SubMesh.VERTEX_DESC_POSITION_NORMAL_TEXCOORDS);
		} else {
			submesh.setDesc(SubMesh.VERTEX_DESC_POSITION_NORMAL);
		}
		float[] svdata = new float[_faces.size() * 3 * vsize];
		int i = 0;
		for (LoaderFace f : _faces) {
			short[] vindexes = { f.a, f.b, f.c };
			for (short vi : vindexes) {
				svdata[i++] = _vertices.get(vi).x;
				svdata[i++] = _vertices.get(vi).y;
				svdata[i++] = _vertices.get(vi).z;
				svdata[i++] = f.nx;
				svdata[i++] = f.ny;
				svdata[i++] = f.nz;
				if (_hasTexCoords) {
					svdata[i++] = _vertices.get(vi).u;
					svdata[i++] = _vertices.get(vi).v;
				}
			}
		}

		submesh.setVertices(svdata);
		mesh.submeshes.put(_name, submesh);
	}
}
