package ndy.game.component;

import ndy.game.math.Vec3;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import ndy.game.system.WorldSystem;

import org.jbox2d.common.MathUtils;

public class EnvironmentComponent extends Component {
	public Vec3 mLightDir = new Vec3();
	public float mWindRot = MathUtils.QUARTER_PI;
	public float mWindSpeed = 5.f; // m/s
	public float mGravity = 9.81f;
	public String mEnvTex = null;

	public EnvironmentComponent() {
		super("weather");
		systems.add(WorldSystem.name);
	}
	
	@Override
	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {
		}
		
		return false;
	}
}
