package ndy.game.component;

import ndy.game.Game;
import ndy.game.collision.NDYCollider;
import ndy.game.collision.NDYCollision;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import ndy.game.system.WorldSystem;

public class PhysicsComponent extends Component {
	private static final String TAG = "NDYComponentPhysics";
	public float speed = 0.f;
	public float rotspeed = 0.f;
	public float mass = 1.f;

	public PhysicsComponent() {
		super("physics");
		
		systems.add(WorldSystem.name);
	}
	
	public boolean processMessage(Message msg) {
		if (msg.getClass() == UpdateMessage.class) {
			TransformationComponent trans = (TransformationComponent) parent.findComponent("transformation");
			
		}

		return false;
	}
	
	public void computeSpeed() {
		
	}
	
	public void computePos() {
		
	}
	
	public void checkCollisions(ComponentCollider a) {
		if(a.collider.type == NDYCollider.T_STATIC) return;

		NDYCollision r = new NDYCollision();
		for(ComponentCollider b:Game.instance.colliders) {
			
			if( ComponentCollider.checkCollision(a, b, r) ) {
				handleCollision(r);
			}
		}
	}
	
	public void handleCollision(NDYCollision collision) {
		if( collision.action == NDYCollision.ACTION_SLOW_A && collision.a == parent ) {
			//speed = speed * (1.f-collision.slow);			
		}
	}
}
