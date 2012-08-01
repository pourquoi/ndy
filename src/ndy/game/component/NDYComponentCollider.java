package ndy.game.component;

import android.util.Log;
import ndy.game.collision.NDYCollider;
import ndy.game.collision.NDYColliderMap;
import ndy.game.collision.NDYCollision;
import ndy.game.math.NDYPolygon;
import ndy.game.math.NDYVector2;

public class NDYComponentCollider extends NDYComponent {
	private static final String TAG = "NDYComponentCollider";
	public NDYCollider collider;

	public NDYComponentCollider(NDYCollider collider) {
		super("collider");
		this.collider = collider;
	}
	
	public static boolean checkCollision(NDYComponentCollider a, NDYComponentCollider b, NDYCollision collision) {
		if(a.collider.structure == NDYCollider.STRUCT_MAP && b.collider.structure == NDYCollider.STRUCT_POLY )
			return checkMapPolyCollision(a, b, collision);
		if(b.collider.structure == NDYCollider.STRUCT_MAP && a.collider.structure == NDYCollider.STRUCT_POLY )
			return checkMapPolyCollision(b, a, collision);
		if(a.collider.structure == NDYCollider.STRUCT_POLY && b.collider.structure == NDYCollider.STRUCT_POLY )
			return checkPolyPolyCollision(a, b, collision);
		return false;
	}
	
	public static boolean checkPolyPolyCollision(NDYComponentCollider a, NDYComponentCollider b, NDYCollision collision) {
		return false;
	}
	
	public static boolean checkMapPolyCollision(NDYComponentCollider cmap, NDYComponentCollider cpoly, NDYCollision collision) {
		NDYComponentTransformation tpoly = (NDYComponentTransformation)cpoly.mParent.findComponent("transformation");
		NDYComponentTransformation tmap = (NDYComponentTransformation)cmap.mParent.findComponent("transformation");
		NDYColliderMap map = (NDYColliderMap)cmap.collider;
		NDYPolygon poly = cpoly.collider.getPolygon();
		for(NDYVector2 p : poly.points) {
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
				collision.a = cpoly.mParent;
				return true;
			}
		}
		
		return false;
	}

}
