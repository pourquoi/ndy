package ndy.game.component;

import ndy.game.actor.NDYGame;
import ndy.game.collision.NDYCollider;
import ndy.game.collision.NDYCollision;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;

public class NDYComponentPhysics extends NDYComponent {
	private static final String TAG = "NDYComponentPhysics";
	public float speed = 0.f;
	public float rotspeed = 0.f;
	public float mass = 1.f;

	public NDYComponentPhysics() {
		super("physics");
	}
	
	public boolean processMessage(NDYMessage msg) {
		if (msg.getClass() == NDYMessageUpdate.class) {
			NDYComponentTransformation trans = (NDYComponentTransformation) mParent.findComponent("transformation");
			
		}

		return false;
	}
	
	public void computeSpeed() {
		
	}
	
	public void computePos() {
		
	}
	
	public void checkCollisions(NDYComponentCollider a) {
		if(a.collider.type == NDYCollider.T_STATIC) return;

		NDYCollision r = new NDYCollision();
		for(NDYComponentCollider b:NDYGame.instance.colliders) {
			
			if( NDYComponentCollider.checkCollision(a, b, r) ) {
				handleCollision(r);
			}
		}
	}
	
	public void handleCollision(NDYCollision collision) {
		if( collision.action == NDYCollision.ACTION_SLOW_A && collision.a == mParent ) {
			//speed = speed * (1.f-collision.slow);			
		}
	}
}
