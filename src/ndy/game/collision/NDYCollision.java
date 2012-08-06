package ndy.game.collision;

import ndy.game.actor.Actor;
import ndy.game.math.Vector3;

public class NDYCollision {
	public static final int ACTION_SLOW_A = 8;
	public Vector3 p = new Vector3();
	public int action;
	public float slow;
	public Actor a, b;
}
