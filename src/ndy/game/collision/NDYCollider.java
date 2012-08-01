package ndy.game.collision;

import ndy.game.math.NDYPolygon;

public abstract class NDYCollider {
	public static final int T_STATIC = 1;
	public static final int T_DYNAMIC = 2;
	
	public static final int STRUCT_POLY = 1;
	public static final int STRUCT_MAP = 2;
	public int type;
	public int structure;
	
	public NDYPolygon getPolygon() {
		return null;
	}
}
