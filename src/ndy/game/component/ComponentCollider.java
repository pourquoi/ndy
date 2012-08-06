package ndy.game.component;

import ndy.game.collision.NDYCollider;
import ndy.game.collision.NDYColliderMap;
import ndy.game.collision.NDYCollision;
import ndy.game.math.Polygon;
import ndy.game.math.Vector2;
import ndy.game.system.CollisionSystem;

public class ComponentCollider extends Component {
	private static final String TAG = "NDYComponentCollider";
	public NDYCollider collider;

	public ComponentCollider(NDYCollider collider) {
		super("collider");
		this.collider = collider;
		systems.add(CollisionSystem.name);
	}
	
	public static boolean checkCollision(ComponentCollider a, ComponentCollider b, NDYCollision collision) {
		if(a.collider.structure == NDYCollider.STRUCT_MAP && b.collider.structure == NDYCollider.STRUCT_POLY )
			return checkMapPolyCollision(a, b, collision);
		if(b.collider.structure == NDYCollider.STRUCT_MAP && a.collider.structure == NDYCollider.STRUCT_POLY )
			return checkMapPolyCollision(b, a, collision);
		if(a.collider.structure == NDYCollider.STRUCT_POLY && b.collider.structure == NDYCollider.STRUCT_POLY )
			return checkPolyPolyCollision(a, b, collision);
		return false;
	}
	
	public static boolean checkPolyPolyCollision(ComponentCollider a, ComponentCollider b, NDYCollision collision) {
		return false;
	}
	
	public static boolean checkMapPolyCollision(ComponentCollider cmap, ComponentCollider cpoly, NDYCollision collision) {
		TransformationComponent tpoly = (TransformationComponent)cpoly.parent.findComponent("transformation");
		TransformationComponent tmap = (TransformationComponent)cmap.parent.findComponent("transformation");
		NDYColliderMap map = (NDYColliderMap)cmap.collider;
		Polygon poly = cpoly.collider.getPolygon();
		for(Vector2 p : poly.points) {
			float x = p.x * tpoly.scale.x + tpoly.pos.x;
			float z = p.y * tpoly.scale.z + tpoly.pos.z;
			int u = (int) ((map.collisionMapWidth-1)*(x - tmap.pos.x)/map.width);  
			int v = (int) ((map.collisionMapHeight-1) - (map.collisionMapHeight-1)*(z - tmap.pos.z)/map.height);			
			float slow = map.getSlow(u, v);
			if(slow!=0.f) {
				collision.p.x = x;
				collision.p.z = z;
				collision.slow = slow;
				collision.action = NDYCollision.ACTION_SLOW_A;
				collision.a = cpoly.parent;
				return true;
			}
		}
		
		return false;
	}

}
