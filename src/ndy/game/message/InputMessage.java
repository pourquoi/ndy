package ndy.game.message;

import ndy.game.actor.Actor;

public class InputMessage extends Message {
	public static final int CAMERA = 0;
	public static final int RUDDER = 1;
	public static final int MAINSAIL = 2;
	public static final int JIBSAIL = 3;

	public static final int T_DOWN=1;
	public static final int T_UP=2;
	public static final int T_MOVE=3;

	public int action;
	public float dx;
	public float dy;
	public float x;
	public float y;
	public Actor actor;

	public InputMessage() {
		super(Message.T_INPUT);
		// TODO Auto-generated constructor stub
	}

}
