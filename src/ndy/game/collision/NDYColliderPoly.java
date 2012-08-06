package ndy.game.collision;

import ndy.game.math.Polygon;

public class NDYColliderPoly extends NDYCollider {
	Polygon polygon;
	
	public NDYColliderPoly(Polygon polygon) {
		this.polygon = polygon;
		this.structure = NDYCollider.STRUCT_POLY;
	}
	
	public Polygon getPolygon() {
		return polygon;
	}
}
