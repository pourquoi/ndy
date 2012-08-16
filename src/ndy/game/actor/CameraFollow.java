package ndy.game.actor;

import ndy.game.component.TransformationComponent;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

import android.opengl.Matrix;

public class CameraFollow extends Camera {
	private Actor _targetActor;
	private Vec2 _wantedPos = new Vec2();
	private Vec2 _targetDir = new Vec2();
	private float _distance;
	
	public CameraFollow(Actor target, float distance) {
		super();
		_targetActor = target;
		_distance = distance;
		
		mode = Camera.MODE_PERSPECTIVE;
	}
	
	@Override
	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {
			TransformationComponent T = (TransformationComponent) _targetActor.findComponent("transformation");
			_targetDir.x = MathUtils.cos(T.rot.y);
			_targetDir.y = MathUtils.sin(T.rot.y);

			_wantedPos.x = T.pos.x;
			_wantedPos.x = T.pos.z;

			_wantedPos.subLocal(_targetDir.mul(_distance));
			
			// TODO smooth
			
			pos.x = _wantedPos.x;
			pos.z = _wantedPos.y;
			
			target.set(T.pos);
			
			Matrix.setLookAtM(mViewMatrix, 0, pos.x, pos.y, pos.z, target.x, target.y, target.z, 0, 1, 0);
		}

		return false;
	}
}
