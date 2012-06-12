package ndy.game.mesh;

import java.io.DataInputStream;
import java.io.EOFException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Hashtable;

import ndy.game.NDYRessource;
import ndy.game.NDYWorld;
import ndy.game.math.Vector3;
import android.util.Log;

public class NDYMesh extends NDYRessource {
	private static String TAG = "Mesh";

	public static final int FLOAT_SIZE_BYTES = 4;
	public static final int SHORT_SIZE_BYTES = 2;

	public Hashtable<String, NDYSubMesh> submeshes = new Hashtable<String, NDYSubMesh>();
	public Hashtable<String, NDYAnimation> animations = new Hashtable<String, NDYAnimation>();

	public NDYMesh(String name) {
		super(name);
	}

	public void loadFile() {
		NDYMeshLoader loader = new NDYMeshLoader();
		try {
			loader.load(this);
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public boolean load() {
		if (!super.load())
			return false;
		mId = 0;
		return true;
	}
}

class NDYMeshLoader {
	private static String TAG = "NDYMeshLoader";

	private String name = new String();
	private ArrayList<LoaderFace> faces = new ArrayList<LoaderFace>();
	private ArrayList<LoaderVertex> vertices = new ArrayList<LoaderVertex>();
	private boolean hasTexCoords = false;
	
	public void load(NDYMesh mesh) throws Exception {
		DataInputStream in = new DataInputStream(NDYWorld.current.getContext().getAssets().open(mesh.toString()));

		try {
			while(true) {
				int bytesRead = 0;
				short chunkId = Short.reverseBytes(in.readShort());
				byte [] buffer = new byte[4];
				in.read(buffer);
				int chunkLength = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
				Log.d(TAG, "chunk ID:" + Integer.toHexString(chunkId) + " chunk length:" + chunkLength);
				bytesRead = 6;
				switch(chunkId) {
				case 0x4d4d: // main
					break;
				case 0x3d3d:
					break;
				case 0x4000: // object
					if( !vertices.isEmpty() ) {
						addSubMesh(mesh);
					}
					hasTexCoords = false;
					faces.clear();
					vertices.clear();
					name = new String();
					byte c;
					while((c=in.readByte()) != 0) {
						name += (char)c;
					}
					
					Log.d(TAG, "name:" + name);
					break;
				case 0x4100: // object trimesh
			         break;
				case 0x4110: // vertex list
					short vertexCount = Short.reverseBytes(in.readShort());
					Log.d(TAG, "vertex count: " + vertexCount);
		            
		            for(int i=0; i<vertexCount; i++)
		            {
		            	LoaderVertex v = new LoaderVertex();
		            	in.read(buffer);
		            	v.x = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		            	in.read(buffer);
		            	v.z = -ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		            	in.read(buffer);
		            	v.y = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		            	
		            	vertices.add(v);
		            }
					break;
				case 0x4120: // face list
					short faceCount = Short.reverseBytes(in.readShort());
					Log.d(TAG, "face count: " + faceCount);
		            		             
		            for(int i=0; i<faceCount; i++)
		            {
		            	LoaderFace f = new LoaderFace();
		            	f.a = Short.reverseBytes(in.readShort()); // vertex a
		            	f.b = Short.reverseBytes(in.readShort()); // vertex b
		            	f.c = Short.reverseBytes(in.readShort()); // vertex c
		            	//System.out.println(f.a+" "+f.b+" "+f.c);
		            	f.flags = in.readShort(); // face flags
		            	LoaderVertex va = vertices.get(f.a);
		            	LoaderVertex vb = vertices.get(f.b);
		            	LoaderVertex vc = vertices.get(f.c);
		            	va.faces.add(f);
		            	vb.faces.add(f);
		            	vc.faces.add(f);
		            	
		            	// compute face normal
		            	Vector3 ab = new Vector3(vb.x-va.x,vb.y-va.y,vb.z-va.z);
		            	Vector3 bc = new Vector3(vc.x-vb.x,vc.y-vb.y,vc.z-vb.z);
		            	
		            	Vector3 n = Vector3.crossProduct(ab, bc);
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
					
					for(int i=0;i<coordCount;i++) {
						LoaderVertex v = vertices.get(i);
						in.read(buffer);
						v.u = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
						in.read(buffer);
						v.v = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
						//System.out.println(u+" "+v);
					}
					break;
				case 0x4150: // smooth groups
					for(int i=0;i<(chunkLength-6);i++) {
						in.read(buffer);
						faces.get(i).smoothgroups = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
					}
					break;
				default:
					in.skipBytes(chunkLength-bytesRead);
				}
			}
		}
		catch( EOFException e ) {
			if( !vertices.isEmpty() ) {
				addSubMesh(mesh);
			}
		}
		catch( Exception e ) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		
		try {
			in.close();
		} catch( Exception e ) {
			
		}
	}
	
	private void addSubMesh(NDYMesh mesh) {
		NDYSubMesh submesh = new NDYSubMesh();
		submesh.name = name;
		
		int vsize = 6;
		if( hasTexCoords ) {
			vsize+=2;
			submesh.setDesc(NDYSubMesh.VERTEX_DESC_POSITION_NORMAL_TEXCOORDS);
		} else {
			submesh.setDesc(NDYSubMesh.VERTEX_DESC_POSITION_NORMAL);
		}
		float [] svdata = new float[faces.size()*3*vsize];
		int i = 0;
		for(LoaderFace f : faces) {
			short [] vindexes = {f.a,f.b,f.c};
			for(short vi : vindexes) {
				svdata[i++] = vertices.get(vi).x;
				svdata[i++] = vertices.get(vi).y;
				svdata[i++] = vertices.get(vi).z;
				svdata[i++] = f.nx;
				svdata[i++] = f.ny;
				svdata[i++] = f.nz;
				if( hasTexCoords ) {
					svdata[i++] = vertices.get(vi).u;
					svdata[i++] = vertices.get(vi).v;
				}
			}
		}
		
		submesh.setVertices(svdata);
		mesh.submeshes.put(name, submesh);
	}
}

class LoaderFace
{
	public short a, b, c;
	public float nx, ny, nz;
	public int smoothgroups;
	public short flags;
}

class LoaderVertex
{
	public float x, y, z;
	public float nx, ny, nz;
	public float u, v;
	public ArrayList<LoaderFace> faces = new ArrayList<LoaderFace>();
}
