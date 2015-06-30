package ndy.game.mesh;

import android.util.Log;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;

public class LoaderMesh extends Loader3DS {
	protected Mesh mesh;
	
	public LoaderMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	protected void processObject() {
		SubMesh submesh = new SubMesh();
		submesh.name = _name;
		submesh.cull = false;

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

	protected void finalize() {
		Vec2 min = null;
		Vec2 max = null;
		Log.i("ndy", "finalize " + mesh.submeshes.size());
		for(SubMesh sm : mesh.submeshes.values()) {
			if( sm.aabb == null ) continue;
			if( min == null ) {
				min = sm.aabb.lowerBound;
				max = sm.aabb.upperBound;
			}

			else {
				if( min.x > sm.aabb.lowerBound.x ) min.x = sm.aabb.lowerBound.x;
				if( min.y > sm.aabb.lowerBound.y ) min.y = sm.aabb.lowerBound.y;
				if( max.x < sm.aabb.upperBound.x ) max.x = sm.aabb.lowerBound.x;
				if( max.y < sm.aabb.upperBound.y ) max.y = sm.aabb.upperBound.y;
			}

			Log.i("ndy", "min/max");
		}

		if( min != null )
			mesh.aabb = new AABB(min, max);
	}
}
