package ndy.game.message;

public class NDYMessageInput extends NDYMessage {
	public static final int T_DOWN=1;
	public static final int T_UP=2;
	public static final int T_MOVE=3;
	public int type;
	public float dx;
	public float dy;
	public float x;
	public float y;

	public NDYMessageInput(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
