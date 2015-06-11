package ndy.game.message;

public class Message {
	public static final int T_UPDATE = 0;
	public static final int T_RENDER = 1;
	public static final int T_INPUT = 2;
	
	private int type;
	
	public Message(int type) {
		this.type = type;
	}
	
	public int getType() {return type;}
}
