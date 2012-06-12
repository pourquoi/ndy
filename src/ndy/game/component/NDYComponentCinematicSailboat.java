package ndy.game.component;

import ndy.game.NDYWorld;
import ndy.game.actor.NDYTransformable;
import ndy.game.math.Vector3;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageUpdate;
import android.util.FloatMath;

public class NDYComponentCinematicSailboat extends NDYComponentCinematic {

	public NDYComponentCinematicSailboat() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean processMessage(NDYMessage msg) {
		super.processMessage(msg);
		
		if( msg.getClass() == NDYMessageUpdate.class ) {
			NDYTransformable r = (NDYTransformable)mParent;
			NDYComponentMeshSailboat b = (NDYComponentMeshSailboat)r.findComponent("mesh");
			if( b != null ) {
				float a = b.getMainSailRot();
				Vector3 v = new Vector3(mDir);
				v.x -= FloatMath.sin(a);
				v.z -= FloatMath.cos(a);
				Vector3 F = Vector3.dotProduct(v, NDYWorld.current.getWindDir());
				
				Vector3 delta = new Vector3(mDir);
				delta.scale(F.length()/10.f);
				r.getPos().add(delta);
			}
		}
		
		return false;
	}

}
