package ndy.game.component;

import ndy.game.math.Quaternion;
import ndy.game.math.Vector3;
import ndy.game.mesh.Mesh;
import ndy.game.mesh.SubMesh;
import ndy.game.shader.Program;
import android.opengl.Matrix;

public class SailboatMeshComponent extends MeshComponent {
	public static String T_MAINSAIL = "mainsail";
	public static String T_JIBSAIL = "jibsail";

	public Vector3 sailAxe = new Vector3(0, 1, 0); // mainsail rotation axis
	public Vector3 jibAxe = new Vector3(0, 1, 0); // jibsail rotation axis

	protected Quaternion quatSail = new Quaternion();
	protected Quaternion quatBoat = new Quaternion();

	public SailboatMeshComponent(Mesh mesh, Program program) {
		super(mesh, program);
		SubMesh sail = mesh.submeshes.get(T_MAINSAIL);
		if( sail != null ) {
			sail.cull = false;
		}
		sail = mesh.submeshes.get(T_JIBSAIL);
		if( sail != null ) {
			sail.cull = false;
		}
	}

	@Override
	protected void computeModelMatrix(SubMesh submesh) {
		TransformationComponent tf = (TransformationComponent) parent.findComponent("transformation");
		SailboatPhysicsComponent cm = (SailboatPhysicsComponent) parent.findComponent("physics");

		quatBoat.x = tf.rotQ.x;
		quatBoat.y = tf.rotQ.y;
		quatBoat.z = tf.rotQ.z;
		quatBoat.w = tf.rotQ.w;

		if (cm != null) {
			if (submesh.name.equals(SailboatMeshComponent.T_MAINSAIL)) {				
				quatSail.fromAxis(sailAxe, cm.mMainSailRot);
				quatBoat.multiply(quatSail);
			} else if (submesh.name.equals(SailboatMeshComponent.T_JIBSAIL)) {
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
