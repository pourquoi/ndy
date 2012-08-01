package ndy.game.component;

import ndy.game.math.NDYQuaternion;
import ndy.game.math.NDYVector3;
import ndy.game.mesh.NDYMesh;
import ndy.game.mesh.NDYSubMesh;
import ndy.game.shader.NDYProgram;
import android.opengl.Matrix;

public class NDYComponentMeshSailboat extends NDYComponentMesh {
	public static String T_MAINSAIL = "mainsail";
	public static String T_JIBSAIL = "jibsail";

	public NDYVector3 sailAxe = new NDYVector3(0, 1, 0); // mainsail rotation axis
	public NDYVector3 jibAxe = new NDYVector3(0, 1, 0); // jibsail rotation axis

	protected NDYQuaternion quatSail = new NDYQuaternion();
	protected NDYQuaternion quatBoat = new NDYQuaternion();

	public NDYComponentMeshSailboat(NDYMesh mesh, NDYProgram program) {
		super(mesh, program);
		NDYSubMesh sail = mesh.submeshes.get(T_MAINSAIL);
		if( sail != null ) {
			sail.cull = false;
		}
		sail = mesh.submeshes.get(T_JIBSAIL);
		if( sail != null ) {
			sail.cull = false;
		}
	}

	@Override
	protected void computeModelMatrix(NDYSubMesh submesh) {
		NDYComponentTransformation tf = (NDYComponentTransformation) mParent.findComponent("transformation");
		NDYComponentPhysicsSailboat cm = (NDYComponentPhysicsSailboat) mParent.findComponent("physics");

		quatBoat.x = tf.rotQ.x;
		quatBoat.y = tf.rotQ.y;
		quatBoat.z = tf.rotQ.z;
		quatBoat.w = tf.rotQ.w;

		if (cm != null) {
			if (submesh.name.equals(NDYComponentMeshSailboat.T_MAINSAIL)) {				
				quatSail.fromAxis(sailAxe, cm.mMainSailRot);
				quatBoat.multiply(quatSail);
			} else if (submesh.name.equals(NDYComponentMeshSailboat.T_JIBSAIL)) {
				quatSail.fromAxis(jibAxe, cm.mJibSailRot);
				quatBoat.multiply(quatSail);
			}
		}

		Matrix.setIdentityM(tmpMatrix, 0);
		Matrix.translateM(tmpMatrix, 0, tf.pos.x, tf.pos.y, tf.pos.z);

		quatBoat.getMatrix(tmpMatrix, 16);

		Matrix.multiplyMM(modelMatrix, 0, tmpMatrix, 0, tmpMatrix, 16);
		Matrix.scaleM(modelMatrix, 0, tf.scale.x, tf.scale.y, tf.scale.z);
	}
}
