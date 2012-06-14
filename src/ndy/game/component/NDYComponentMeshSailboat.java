package ndy.game.component;

import ndy.game.actor.NDYTransformable;
import ndy.game.math.Quaternion;
import ndy.game.math.Vector3;
import ndy.game.mesh.NDYSubMesh;
import android.opengl.Matrix;


public class NDYComponentMeshSailboat extends NDYComponentMesh {
	public static String T_MAINSAIL = "mainsail";
	public static String T_JIBSAIL = "jibsail";

	protected Vector3 mMainSailAxe = new Vector3(0,1,0); // mainsail rotation axis
	protected Vector3 mJibSailAxe = new Vector3(0,1,0); // jibsail rotation axis

	/**
	 * @param meshName
	 * @param programName
	 * @param textureName
	 */
	public NDYComponentMeshSailboat(String meshName, String programName, String textureName) {
		super(meshName, programName, textureName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void computeModelMatrix(NDYSubMesh submesh) {
		NDYTransformable r = (NDYTransformable)mParent;
		NDYComponentCinematicSailboat cinematic = (NDYComponentCinematicSailboat)r.findComponent("cinematic");
		
		Vector3 pos = r.getPos();
		Quaternion q = r.getRotQ();
		if( cinematic != null ) {
			if(submesh.name.equals(NDYComponentMeshSailboat.T_MAINSAIL)) {
				Quaternion q2 = new Quaternion();
				q2.fromAxis(mMainSailAxe, cinematic.getMainSailRot());
				q.multiply(q2);
			} else if(submesh.name.equals(NDYComponentMeshSailboat.T_JIBSAIL)) {
				Quaternion q2 = new Quaternion();
				q2.fromAxis(mJibSailAxe, cinematic.getJibSailRot());
				q.multiply(q2);
			}
		}

		Matrix.setIdentityM(mTranslationMatrix, 0);
		Matrix.translateM(mTranslationMatrix, 0, pos.x, pos.y, pos.z);
					
		Matrix.multiplyMM(mMatrix, 0, mTranslationMatrix, 0, q.getMatrix(), 0);
	}
	
	public Vector3 getMainSailAxe() {
		return mMainSailAxe;
	}
	
	public Vector3 getJibSailAxe() {
		return mJibSailAxe;
	}
}
