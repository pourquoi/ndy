package ndy.game.message;

public class UpdateMessage extends Message {
	protected long mInterval; // dt in ms

	public UpdateMessage() {
		super(Message.T_UPDATE);
	}
	
	public long getInterval() {
		return mInterval;
	}
	
	public void setInterval(long dt) {
		mInterval = dt;
	}
}
