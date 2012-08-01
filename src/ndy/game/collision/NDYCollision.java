package ndy.game.collision;

import ndy.game.actor.NDYActor;
import ndy.game.math.NDYVector3;

public class NDYCollision {
	public static final int ACTION_SLOW_A = 8;
	public NDYVector3 p = new NDYVector3();
	public int action;
	public float slow;
	public NDYActor a, b;
}
