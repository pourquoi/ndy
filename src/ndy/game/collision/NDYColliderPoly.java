package ndy.game.collision;

import ndy.game.math.NDYPolygon;

public class NDYColliderPoly extends NDYCollider {
	NDYPolygon polygon;
	
	public NDYColliderPoly(NDYPolygon polygon) {
		this.polygon = polygon;
		this.structure = NDYCollider.STRUCT_POLY;
	}
	
	public NDYPolygon getPolygon() {
		return polygon;
	}
}
