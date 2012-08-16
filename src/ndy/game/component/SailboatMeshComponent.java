package ndy.game.component;

import ndy.game.mesh.Mesh;
import ndy.game.mesh.SubMesh;
import ndy.game.shader.Program;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec3;

import android.opengl.Matrix;

public class SailboatMeshComponent extends MeshComponent {
	public static String T_MAINSAIL = "mainsail";
	public static String T_JIBSAIL = "jibsail";

	public Vec3 sailAxe = new Vec3(0, 1, 0); // mainsail rotation axis
	public Vec3 jibAxe = new Vec3(0, 1, 0); // jibsail rotation axis

	protected float[] rotSail = new float[16];
	protected float[] quatBoat = new float[16];

	public SailboatMeshComponent(Mesh mesh, Program program) {
		super(mesh, program);
		SubMesh sail = mesh.submeshes.get(T_MAINSAIL);
		if (sail != null) {
			sail.cull = false;
		}
		sail = mesh.submeshes.get(T_JIBSAIL);
		if (sail != null) {
			sail.cull = false;
		}
	}

	@Override
	protected void computeModelMatrix(SubMesh submesh) {
		TransformationComponent tf = (TransformationComponent) parent.findComponent("transformation");
		SailboatPhysicsComponent cm = (SailboatPhysicsComponent) parent.findComponent("physics");

		Matrix.setIdentityM(tmpMatrix, 0);
		Matrix.translateM(tmpMatrix, 0, tf.pos.x, tf.pos.y, tf.pos.z);

		Matrix.setRotateEulerM(tmpMatrix, 16, tf.rot.x, tf.rot.y, tf.rot.z);

		if (cm != null) {
			if (submesh.name.equals(SailboatMeshComponent.T_MAINSAIL)) {
				Matrix.rotateM(tmpMatrix, 16, cm.mMainSailRot * MathUtils.RAD2DEG, 0f, 1f, 0f);
			} else if (submesh.name.equals(SailboatMeshComponent.T_JIBSAIL)) {
				Matrix.rotateM(tmpMatrix, 16, cm.mJibSailRot * MathUtils.RAD2DEG, 0f, 1f, 0f);
			}
		}

		Matrix.multiplyMM(modelMatrix, 0, tmpMatrix, 0, tmpMatrix, 16);
		Matrix.scaleM(modelMatrix, 0, tf.scale.x, tf.scale.y, tf.scale.z);
	}
}
